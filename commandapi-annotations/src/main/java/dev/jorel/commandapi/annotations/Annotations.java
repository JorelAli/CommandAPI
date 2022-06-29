/*******************************************************************************
 * Copyright 2018, 2021 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi.annotations;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.google.auto.service.AutoService;

import dev.jorel.commandapi.annotations.annotations.ArgumentParser;
import dev.jorel.commandapi.annotations.annotations.Command;
import dev.jorel.commandapi.annotations.annotations.Help;
import dev.jorel.commandapi.annotations.annotations.NeedsOp;
import dev.jorel.commandapi.annotations.annotations.Permission;
import dev.jorel.commandapi.annotations.annotations.Subcommand;
import dev.jorel.commandapi.annotations.annotations.Suggestion;
import dev.jorel.commandapi.annotations.annotations.Suggests;
import dev.jorel.commandapi.annotations.arguments.AAdvancementArgument;
import dev.jorel.commandapi.annotations.arguments.AAdventureChatArgument;
import dev.jorel.commandapi.annotations.arguments.AAdventureChatComponentArgument;
import dev.jorel.commandapi.annotations.arguments.AAngleArgument;
import dev.jorel.commandapi.annotations.arguments.AAxisArgument;
import dev.jorel.commandapi.annotations.arguments.ABiomeArgument;
import dev.jorel.commandapi.annotations.arguments.ABlockPredicateArgument;
import dev.jorel.commandapi.annotations.arguments.ABlockStateArgument;
import dev.jorel.commandapi.annotations.arguments.ABooleanArgument;
import dev.jorel.commandapi.annotations.arguments.AChatArgument;
import dev.jorel.commandapi.annotations.arguments.AChatColorArgument;
import dev.jorel.commandapi.annotations.arguments.AChatComponentArgument;
import dev.jorel.commandapi.annotations.arguments.ADoubleArgument;
import dev.jorel.commandapi.annotations.arguments.AEnchantmentArgument;
import dev.jorel.commandapi.annotations.arguments.AEntitySelectorArgument;
import dev.jorel.commandapi.annotations.arguments.AEntityTypeArgument;
import dev.jorel.commandapi.annotations.arguments.AEnvironmentArgument;
import dev.jorel.commandapi.annotations.arguments.AFloatArgument;
import dev.jorel.commandapi.annotations.arguments.AFloatRangeArgument;
import dev.jorel.commandapi.annotations.arguments.AFunctionArgument;
import dev.jorel.commandapi.annotations.arguments.AGreedyStringArgument;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.jorel.commandapi.annotations.arguments.AIntegerRangeArgument;
import dev.jorel.commandapi.annotations.arguments.AItemStackArgument;
import dev.jorel.commandapi.annotations.arguments.AItemStackPredicateArgument;
import dev.jorel.commandapi.annotations.arguments.ALiteralArgument;
import dev.jorel.commandapi.annotations.arguments.ALocation2DArgument;
import dev.jorel.commandapi.annotations.arguments.ALocationArgument;
import dev.jorel.commandapi.annotations.arguments.ALongArgument;
import dev.jorel.commandapi.annotations.arguments.ALootTableArgument;
import dev.jorel.commandapi.annotations.arguments.AMathOperationArgument;
import dev.jorel.commandapi.annotations.arguments.AMultiLiteralArgument;
import dev.jorel.commandapi.annotations.arguments.ANBTCompoundArgument;
import dev.jorel.commandapi.annotations.arguments.ANamespacedKeyArgument;
import dev.jorel.commandapi.annotations.arguments.AObjectiveArgument;
import dev.jorel.commandapi.annotations.arguments.AObjectiveCriteriaArgument;
import dev.jorel.commandapi.annotations.arguments.AOfflinePlayerArgument;
import dev.jorel.commandapi.annotations.arguments.AParticleArgument;
import dev.jorel.commandapi.annotations.arguments.APlayerArgument;
import dev.jorel.commandapi.annotations.arguments.APotionEffectArgument;
import dev.jorel.commandapi.annotations.arguments.ARecipeArgument;
import dev.jorel.commandapi.annotations.arguments.ARotationArgument;
import dev.jorel.commandapi.annotations.arguments.AScoreHolderArgument;
import dev.jorel.commandapi.annotations.arguments.AScoreboardSlotArgument;
import dev.jorel.commandapi.annotations.arguments.ASoundArgument;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;
import dev.jorel.commandapi.annotations.arguments.ATeamArgument;
import dev.jorel.commandapi.annotations.arguments.ATextArgument;
import dev.jorel.commandapi.annotations.arguments.ATimeArgument;
import dev.jorel.commandapi.annotations.arguments.AUUIDArgument;
import dev.jorel.commandapi.annotations.parser.Parser;

/**
 * The main annotation processor for annotation-based arguments
 */
@AutoService(Processor.class)
public class Annotations extends AbstractProcessor {

	Logging logging;
	boolean testing = false;

	/**
	 * TODO: Use a LinkedHashSet to maintain order. We want to have primitives early
	 * on in the list so they are chosen in {@link Utils#predictAnnotation}
	 */
	public static final Set<Class<? extends Annotation>> ARGUMENT_ANNOTATIONS = Set.of(AAdvancementArgument.class,
			AAdventureChatArgument.class, AAdventureChatComponentArgument.class, AAngleArgument.class,
			AAxisArgument.class, ABiomeArgument.class, ABlockPredicateArgument.class, ABlockStateArgument.class,
			ABooleanArgument.class, AChatArgument.class, AChatColorArgument.class, AChatComponentArgument.class,
			ADoubleArgument.class, AEnchantmentArgument.class, AEntitySelectorArgument.class,
			AEntityTypeArgument.class, AEnvironmentArgument.class, AFloatArgument.class, AFloatRangeArgument.class,
			AFunctionArgument.class, AGreedyStringArgument.class, AIntegerArgument.class, AIntegerRangeArgument.class,
			AItemStackArgument.class, AItemStackPredicateArgument.class, ALiteralArgument.class,
			ALocation2DArgument.class, ALocationArgument.class, ALongArgument.class, ALootTableArgument.class,
			AMathOperationArgument.class, AMultiLiteralArgument.class, ANamespacedKeyArgument.class, ANBTCompoundArgument.class,
			AObjectiveArgument.class, AObjectiveCriteriaArgument.class, AOfflinePlayerArgument.class,
			AParticleArgument.class, APlayerArgument.class, APotionEffectArgument.class, ARecipeArgument.class,
			ARotationArgument.class, AScoreboardSlotArgument.class, AScoreHolderArgument.class, ASoundArgument.class,
			AStringArgument.class, ATeamArgument.class, ATextArgument.class, ATimeArgument.class, AUUIDArgument.class);

	public static final Set<Class<? extends Annotation>> OTHER_ANNOTATIONS = Set.of(Command.class, NeedsOp.class,
			Permission.class, Help.class, Suggestion.class, ArgumentParser.class, Subcommand.class, Suggests.class);

	/**
	 * Default constructor, doesn't do anything special
	 */
	public Annotations() {
		
	}
	
	/**
	 * Constructor used for commandapi-annotations tests. When provided, the name of
	 * the class which is generated will be randomized (to prevent conflicting class
	 * names which can break tests)
	 * 
	 * @param testing true to enable randomly generated class names
	 */
	public Annotations(boolean testing) {
		this.testing = testing;
	}
	
	// List of stuff we can deal with
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		List<Class<?>> annotations = new ArrayList<>();
		annotations.addAll(ARGUMENT_ANNOTATIONS);
		annotations.addAll(OTHER_ANNOTATIONS);

		return annotations.stream().map(Class::getCanonicalName).collect(Collectors.toSet());
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		logging = new Logging(processingEnv);
		
		// TODO: This check doesn't go here, but it's very important for testing. Move this to a test suite when ready
//		ARGUMENT_ANNOTATIONS.stream().forEach(annotation -> {
//			System.out.print(annotation.getSimpleName() + ": ");
//			Utils.getPrimitiveTypeMirror(annotation.getAnnotation(Primitive.class), processingEnv);
//			System.out.println();
//		});

		// We want things sorted :)
		SortedSet<TypeElement> commandClasses = new TreeSet<>((TypeElement elem1, TypeElement elem2) -> {
			return elem1.getQualifiedName().toString().compareTo(elem2.getQualifiedName().toString());
		});
		
		Set<? extends Element> commandElements = roundEnv.getElementsAnnotatedWith(Command.class);
		if(commandElements.isEmpty()) {
			return false;
		}
		
		for(Element element : commandElements) {
			commandClasses.add((TypeElement) element);
		}
		
		// Change the type of commandClasses - we're asserting it's a TypeElement (it literally can't be anything else)
		

		// We need to do multiple "phases". Firstly, we need to construct a context
		// for each @Command class, which outlines the list of suggestion methods, its
		// varying types, etc. You can think of this as a lexing/syntax analysis step.

		LinkedHashMap<TypeElement, Parser> context = Parser.generateContexts(commandClasses, processingEnv, logging);

		// We then perform out semantic analysis (checking that we've not got two
		// @Default
		// annotations, type checking of annotations to method parameter types, ensuring
		// suggestions map to what they should), ensuring we've not got two commands of the same name...

		new Semantics(logging).analyze(context);
		
		try {
			final String suffix = testing ? (String.valueOf(new Random().nextInt(Integer.MAX_VALUE))) : ""; 
			new ClassGenerator(processingEnv).generateClass("Commands" + suffix, context);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}
}
