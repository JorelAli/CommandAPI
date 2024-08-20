/*******************************************************************************
 * Copyright 2018, 2021 Jorel Ali (Skepter) - MIT License
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi.annotations.reloaded;

import com.google.auto.service.AutoService;
import dev.jorel.commandapi.annotations.reloaded.annotations.Command;
import dev.jorel.commandapi.annotations.reloaded.generators.IndentedWriter;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticRuleContextData;
import dev.jorel.commandapi.annotations.reloaded.parser.ParserUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * The main annotation processor for annotation-based arguments
 */
@AutoService(Processor.class)
public class Annotations extends AbstractProcessor {
	private final Configuration configuration;

	/**
	 * Default constructor, uses default configuration
	 */
	public Annotations() {
		this(new DefaultConfiguration());
	}

	/**
	 * Constructor used for commandapi-annotations tests. Allows an alternate
	 * configuration to be provided for testing purposes.
	 */
	public Annotations(Configuration configuration) {
		this.configuration = configuration;
	}

	// List of stuff we can deal with
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return configuration.getSupportedAnnotationTypes();
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		var logging = new Logging(processingEnv);
		var baseModule = configuration.getBaseModule();

		// We need to do multiple "phases".
		// Firstly, we perform semantic analysis (checking that we've not got two @Default
		// annotations, type checking of annotations to method parameter types, ensuring
		// suggestions map to what they should), ensuring we've not got two commands of
		// the same name...
		var semanticsContext = new SemanticRuleContextData(
			logging,
			processingEnv,
			roundEnv,
			configuration.getAnnotationUtils()
		);
		logging.info("Performing semantic checks");
		boolean semanticsPassed = baseModule.allPass(semanticsContext);
		logging.info("Semantic checks %s".formatted(semanticsPassed ? "passed" : "failed"));

		// We then need to construct a context
		// for each @Command class, which outlines the list of suggestion methods, its
		// varying types, etc. You can think of this as a lexing/syntax analysis step.

		var commandClasses = roundEnv.getElementsAnnotatedWith(Command.class).stream()
			.map(TypeElement.class::cast) // Change the type of commandClasses -
			// we're asserting it's a TypeElement (it literally can't be anything else)
			.collect(Collectors.toCollection(() -> // We want things sorted :)
				new TreeSet<>(Comparator.comparing(it -> it.getQualifiedName().toString())))
			);
		logging.info("Found %d @%s classes".formatted(commandClasses.size(), Command.class.getSimpleName()));
		if (commandClasses.isEmpty()) {
			logging.info("Nothing to do. Aborting.");
			return true;
		}

		logging.info("Parsing contexts");
		var parserUtils = new ParserUtils(
			logging,
			processingEnv,
			configuration.getImportsBuilder(),
			configuration.getAnnotationUtils()
		);
		var maybeContexts = baseModule.parseAllContexts(
			parserUtils,
			configuration.getCommandsClassName(),
			ZonedDateTime.now(),
			commandClasses
		);
		logging.info("Contexts parsed %s".formatted(maybeContexts.isPresent() ? "successfully" : "unsuccessfully"));

		if (!semanticsPassed || maybeContexts.isEmpty()) {
			logging.info("Aborting");
			return true;
		}
		var allContexts = maybeContexts.orElseThrow();
		try {
			logging.info("Generating %s class".formatted(configuration.getCommandsClassName()));
			JavaFileObject builderFile = processingEnv
				.getFiler()
				.createSourceFile(configuration.getCommandsClassName());
			try (var out = new PrintWriter(builderFile.openWriter())) {
				baseModule.generate(new IndentedWriter(out), allContexts);
			}
			logging.info("%s class generated".formatted(configuration.getCommandsClassName()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		logging.info("Done");
		return true;
	}
}
