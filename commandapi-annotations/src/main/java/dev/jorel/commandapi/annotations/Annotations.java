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
import java.util.*;
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
		ADoubleArgument.class, AEnchantmentArgument.class,

		AEntitySelectorArgument.ManyPlayers.class,
		AEntitySelectorArgument.OnePlayer.class,
		AEntitySelectorArgument.ManyEntities.class,
		AEntitySelectorArgument.OneEntity.class,

		AEntityTypeArgument.class, AFloatArgument.class, AFloatRangeArgument.class,
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

		// TODO: This check doesn't go here, but it's very important for testing. Move
		// this to a test suite when ready
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
		if (commandElements.isEmpty()) {
			return false;
		}

		for (Element element : commandElements) {
			commandClasses.add((TypeElement) element);
		}

		// Change the type of commandClasses - we're asserting it's a TypeElement (it
		// literally can't be anything else)

		// We need to do multiple "phases". Firstly, we need to construct a context
		// for each @Command class, which outlines the list of suggestion methods, its
		// varying types, etc. You can think of this as a lexing/syntax analysis step.

		LinkedHashMap<TypeElement, Parser> context = Parser.generateContexts(commandClasses, processingEnv, logging);

		// We then perform out semantic analysis (checking that we've not got two
		// @Default
		// annotations, type checking of annotations to method parameter types, ensuring
		// suggestions map to what they should), ensuring we've not got two commands of
		// the same name...

		new Semantics(logging).analyze(context);

		try {
			final String suffix = testing ? (String.valueOf(new Random().nextInt(Integer.MAX_VALUE))) : "";
			new ClassGenerator(processingEnv).generateClass("Commands" + suffix, context);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	// Indentation, because half of this file is actually just making stuff look nice
	private String indent(int indent) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < indent; i++) {
			builder.append("    ");
		}
		return builder.toString();
	}
	
	private String simpleFromQualified(String name) {
		if(name == null) {
			return null;
		}
		return name.split("\\.")[name.split("\\.").length - 1];
	}
	
	private SortedSet<String> calculateImports(Element classElement) {
		SortedSet<String> imports = new TreeSet<>();
		
		imports.add(CommandAPICommand.class.getCanonicalName());
		if(classElement.getAnnotation(NeedsOp.class) != null) {
			imports.add(CommandPermission.class.getCanonicalName());
		}
		for (Element methodElement : classElement.getEnclosedElements()) {
			if (methodElement.getAnnotation(Subcommand.class) != null) {
				imports.add(MultiLiteralArgument.class.getCanonicalName());
			}
			if (methodElement.getAnnotation(NeedsOp.class) != null) {
				imports.add(CommandPermission.class.getCanonicalName());
			}
			
			if(methodElement instanceof ExecutableElement method) {
				for(VariableElement parameter : method.getParameters()) {
					Annotation argument = getArgument(parameter);
					if(argument != null) {
						
						if (argument instanceof ANBTCompoundArgument) {
							// Get NBTCompoundArgument type from type mirror
							imports.add(parameter.asType().toString());
						} else {
							imports.addAll(Arrays.asList(getPrimitive(argument).value()));
						}

						if(argument.annotationType().getEnclosingClass() == null) {
							// Normal arguments
							imports.add("dev.jorel.commandapi.arguments." + argument.annotationType().getSimpleName().substring(1));
						} else {
							// Nested arguments, like EntitySelectorArgument
							imports.add("dev.jorel.commandapi.arguments." + argument.annotationType().getEnclosingClass().getSimpleName().substring(1));
						}
						
						if(argument instanceof ALocationArgument || argument instanceof ALocation2DArgument) {
							imports.add(LocationType.class.getCanonicalName());
						}
					}
					
				}
			}
		}
		
		for(String import_ : new TreeSet<>(imports)) {
			if(import_.contains("<")) {
				imports.add(import_.substring(0, import_.indexOf("<")));
				imports.add(import_.substring(import_.indexOf("<") + 1, import_.indexOf(">")));
			}
		}
		
		return imports;
	}
	
	private void emitImports(PrintWriter out, Element classElement) {
		String previousImport = "";
		for(String import_ : calculateImports(classElement)) {
			// Separate different packages
			if(previousImport.contains(".") && import_.contains(".") &&
				!previousImport.substring(0, previousImport.indexOf(".")).equals(import_.substring(0, import_.indexOf(".")))) {
					out.println();
			}
			// Don't import stuff like "String"
			if(!import_.contains(".") || import_.contains("<")) {
				continue;
			}
			
			out.print("import ");
			out.print(import_);
			out.println(";");
			previousImport = import_;
		}
		out.println();
	}
	
	// (https://www.baeldung.com/java-annotation-processing-builder)
	private void emitPackage(PrintWriter out, TypeElement commandClass) {
		int lastDot = commandClass.getQualifiedName().toString().lastIndexOf('.');
	    if (lastDot > 0) {
	    	out.print("package ");
			out.print(commandClass.getQualifiedName().toString().substring(0, lastDot));
			out.println(";");
			out.println();
	    }
	}
	
	private int emitSubcommand(PrintWriter out, Element methodElement, int indent) {
		if (methodElement.getAnnotation(Subcommand.class) != null) {
			out.println(indent(indent) + ".withArguments(");
			indent++;
			out.print(indent(indent) + "new MultiLiteralArgument(\"subcommand\", ");
			
			if(methodElement.getAnnotation(Subcommand.class).value().length == 0) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "Invalid @Subcommand on " + methodElement.getSimpleName() + " - no subcommand name was found");
			}
			
			// @Subcommand (name)
			out.print(Arrays.stream(methodElement.getAnnotation(Subcommand.class).value())
				.map(x -> "\"" + x + "\"").collect(Collectors.joining(", ")));
			
			out.println(")");
			indent++;
			out.println(indent(indent) + ".setListed(false)");
			
			// @NeedsOp
			if (methodElement.getAnnotation(NeedsOp.class) != null) {
				out.println(indent(indent) + ".withPermission(CommandPermission.OP)");
			}
			
			// @Permission
			if (methodElement.getAnnotation(Permission.class) != null) {
				out.print(indent(indent) + ".withPermission(\"");
				out.print(methodElement.getAnnotation(Permission.class).value());
				out.println("\")");
			}
			indent--;
			indent--;
			out.println(indent(indent) + ")");
		}
		return indent;
	}
	
	private <T extends Annotation> Map<Integer, String> emitArgumentsAndGenerateArgumentMapping(PrintWriter out, Element methodElement, int indent) throws IllegalArgumentException {
		Map<Integer, String> argumentMapping = new HashMap<>();
		
		ExecutableElement executableMethodElement = (ExecutableElement) methodElement;
		for(int i = 1; i < executableMethodElement.getParameters().size(); i++) {
			VariableElement parameter = executableMethodElement.getParameters().get(i);
			T argumentAnnotation = getArgument(parameter);
			if (argumentAnnotation == null) {
				processingEnv.getMessager().printMessage(Kind.ERROR,
						"Parameter " + parameter.getSimpleName() + " in method "
								+ methodElement.getSimpleName()
								+ " does not have an argument annotation on it! ");
				throw new IllegalArgumentException();
			}
			
			emitArgument(out, argumentAnnotation, parameter, indent);
			
			// Handle return types
			Primitive primitive = getPrimitive(argumentAnnotation);
			if (argumentAnnotation instanceof ANBTCompoundArgument) {
				argumentMapping.put(i - 1, parameter.asType().toString());
			} else {
				if(primitive.value().length == 1) {
					argumentMapping.put(i - 1, primitive.value()[0]);
				}
			}
		}
		
		return argumentMapping;
	}
	
	private int emitExecutes(PrintWriter out, Map<Integer, String> argumentMapping, ExecutableType methodType, TypeElement commandClass, Element methodElement, int indent) {
		String[] firstParam = methodType.getParameterTypes().get(0).toString().split("\\.");
		out.print(indent(indent));
		switch (firstParam[firstParam.length - 1]) {
		case "Player" -> out.print(".executesPlayer");
		case "ConsoleCommandSender" -> out.print(".executesConsole");
		case "BlockCommandSender" -> out.print(".executesCommandBlock");
		case "ProxiedCommandSender" -> out.print(".executesProxy");
		case "NativeProxyCommandSender" -> out.print(".executesNative");
		case "Entity" -> out.print(".executesEntity");
		case "CommandSender" -> out.print(".executes");
		default -> out.print(".executes");
		}

		out.println("((sender, args) -> {");
		indent++;
		out.print(indent(indent));

		// Return int or void?
		if (methodType.getReturnType().toString().equals("int")) {
			out.print("return ");
		}

		out.print(commandClass.getSimpleName());
		out.print(".");
		out.print(methodElement.getSimpleName());
		out.print("(sender");
		
		for(int i = 0; i < argumentMapping.size(); i++) {
			String fromArgumentMap = argumentMapping.get(i);
			out.print(", (");

			if(fromArgumentMap.contains("<")) {
				out.print(simpleFromQualified(fromArgumentMap.substring(0, fromArgumentMap.indexOf("<"))));
				out.print("<");
				out.print(simpleFromQualified(fromArgumentMap.substring(fromArgumentMap.indexOf("<") + 1, fromArgumentMap.indexOf(">"))));
				out.print(">");
			} else {
				out.print(simpleFromQualified(fromArgumentMap));
			}
			out.print(") args.get(");
			out.print(i);
			out.print(")");
		}
		//populate stuff here
		
		out.println(");");
		indent--;
		out.println(indent(indent) + "})");
		
		return indent;
	}
	
	private int emitClassDeclarationStart(PrintWriter out, TypeElement commandClass, int indent) {
		out.println("// This class was automatically generated by the CommandAPI");
		out.print("public class ");
		out.print(commandClass.getSimpleName() + "$Command");
		out.println(" {");
		out.println();
		indent++;
		return indent;
	}
	
	private void emitPermission(PrintWriter out, Element classElement, int indent) {
		if (classElement.getAnnotation(Permission.class) != null) {
			out.print(indent(indent) + ".withPermission(\"");
			out.print(classElement.getAnnotation(Permission.class).value());
			out.println("\")");
		}
	}
	
	private void emitAlias(PrintWriter out, Element classElement, int indent) {
		if (classElement.getAnnotation(Alias.class) != null) {
			out.print(indent(indent) + ".withAliases(");
			out.print(Arrays.stream(classElement.getAnnotation(Alias.class).value())
					.map(x -> "\"" + x + "\"").collect(Collectors.joining(", ")));
			out.println(")");
		}
	}
	
	private void emitNeedsOp(PrintWriter out, Element classElement, int indent) {
		if (classElement.getAnnotation(NeedsOp.class) != null) {
			out.println(indent(indent) + ".withPermission(CommandPermission.OP)");
		}
	}
	
	private void emitHelp(PrintWriter out, Element classElement, int indent) {
		if (classElement.getAnnotation(Help.class) != null) {
			Help helpAnnotation = classElement.getAnnotation(Help.class);
			
			if(helpAnnotation.shortDescription().isEmpty()) {
				out.print(indent(indent) + ".withFullDescription(\"");
				out.print(helpAnnotation.value());
				out.println("\")");
			} else {
				out.print(indent(indent) + ".withHelp(\"");
				out.print(helpAnnotation.shortDescription());
				out.print("\", \"");
				out.print(helpAnnotation.value());
				out.println("\")");
			}
		}
	}

	private void processCommand(Element classElement) throws IOException {
		TypeElement commandClass = (TypeElement) classElement;
		JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(commandClass.getQualifiedName() + "$Command");
		int indent = 0;

		try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
			emitPackage(out, commandClass); // Package name
			emitImports(out, classElement); // Imports	
			emitClassDeclarationStart(out, commandClass, indent); // Class declaration

			// Main registration method
			out.println(indent(indent) + "@SuppressWarnings(\"unchecked\")");
			out.println(indent(indent) + "public static void register() {");
			out.println();
			indent++;

			for (Element methodElement : classElement.getEnclosedElements()) {
				if (methodElement.getAnnotation(Default.class) != null
						|| methodElement.getAnnotation(Subcommand.class) != null) {
					
					ExecutableType methodType = (ExecutableType) methodElement.asType();
					if(!methodElement.getModifiers().contains(Modifier.STATIC)) {
						processingEnv.getMessager().printMessage(Kind.ERROR, "Method " + methodElement.getSimpleName() + " must be static to be used as a command");
					}

					out.print(indent(indent) + "new CommandAPICommand(\"");
					out.print(commandClass.getAnnotation(Command.class).value());
					out.println("\")");
					indent++;

					indent = emitSubcommand(out, methodElement, indent); // @Subcommand (Also handle @Alias for @Subcommand)
					emitNeedsOp(out, classElement, indent);    // @NeedsOp
					emitPermission(out, classElement, indent); // @Permission
					emitAlias(out, classElement, indent);      // @Alias
					emitHelp(out, classElement, indent);       // @Help
					
					//Maps parameter index to argument's primitive type
					Map<Integer, String> argumentMapping = null;
					try {
						argumentMapping = emitArgumentsAndGenerateArgumentMapping(out, methodElement, indent);
					} catch (IllegalArgumentException e) {
						return;
					}
					
					// .executes
					indent = emitExecutes(out, argumentMapping, methodType, commandClass, methodElement, indent);

					// Register command
					out.println(indent(indent) + ".register();");
					out.println();
					indent--;
				}
			}
			out.println(indent(indent) + "}"); // register()
			indent--;
			out.println();
			out.println("}"); // $Command class
		}
	}

	private <T extends Annotation> void emitArgument(PrintWriter out, T argumentAnnotation, VariableElement parameter, int indent) {
		out.print(indent(indent) + ".withArguments(new ");
		// We're assuming that the name of the argument MUST be "A" + the same name
		if(argumentAnnotation.annotationType().getEnclosingClass() == null) {
			// Normal arguments
			out.print(argumentAnnotation.annotationType().getSimpleName().substring(1));
		} else {
			// Nested arguments, like EntitySelectorArgument
			out.print(argumentAnnotation.annotationType().getEnclosingClass().getSimpleName().substring(1));
			out.print(".");
			out.print(argumentAnnotation.annotationType().getSimpleName());
		}
		
		// Node name
		out.print("(\"");
		out.print(parameter.getSimpleName());
		out.print("\"");
		
		// Handle parameters
		// Number arguments
		if(argumentAnnotation instanceof AIntegerArgument argument) {
			out.print(", " + argument.min() + ", " + argument.max());
		} else if(argumentAnnotation instanceof ALongArgument argument) {
			out.print(", " + argument.min() + "L, " + argument.max() + "L");
		} else if(argumentAnnotation instanceof AFloatArgument argument) {
			out.print(", " + argument.min() + "F, " + argument.max() + "F");
		} else if(argumentAnnotation instanceof ADoubleArgument argument) {
			out.print(", " + argument.min() + "D, " + argument.max() + "D");
		}
		
		// Non-number arguments
		else if(argumentAnnotation instanceof ALocation2DArgument argument) {
			out.print(", " + LocationType.class.getSimpleName() + "." + argument.value().toString());
		} else if(argumentAnnotation instanceof ALocationArgument argument) {
			out.print(", " + LocationType.class.getSimpleName() + "." + argument.value().toString());
		} else if(argumentAnnotation instanceof AMultiLiteralArgument argument) {
			out.print(", " + Arrays.stream(argument.value()).map(s -> "\"" + s + "\"").collect(Collectors.joining(", ")));
		} else if(argumentAnnotation instanceof ALiteralArgument argument) {
			out.print(", \"");
			out.print(argument.value());
			out.print("\"");
		}
		
		out.print(")");
		
		if(argumentAnnotation instanceof ALiteralArgument) {
			out.print(".setListed(true)");
		}
		
		out.println(")");
	}

	// Checks if an annotation mirror is an argument annotation
	private boolean isArgument(AnnotationMirror mirror) {
		final String mirrorName = mirror.getAnnotationType().toString();
		return Arrays.stream(ARGUMENT_ANNOTATIONS).map(Class::getCanonicalName).anyMatch(mirrorName::equals);
	}

	// Get the Primitive annotation from an annotation
	private <T extends Annotation> Primitive getPrimitive(T annotation) {
		return annotation.annotationType().getDeclaredAnnotation(Primitive.class);
	}

	@SuppressWarnings("unchecked")
	private <T extends Annotation> T getArgument(VariableElement tMirror) {
		for(AnnotationMirror mirror : tMirror.getAnnotationMirrors()) {
			if(isArgument(mirror)) {
				T argumentAnnotation = null;
				String mirrorCanonicalName = mirror.getAnnotationType().toString();
				try {
					argumentAnnotation = tMirror.getAnnotationsByType((Class<T>) Class.forName(mirrorCanonicalName))[0];
				} catch (ClassNotFoundException e) {
					// We might be in a nested class. Let's try accessing that
					try {
						// Replace final . with $
						mirrorCanonicalName = mirrorCanonicalName.substring(0, mirrorCanonicalName.lastIndexOf(".")) + "$" + mirrorCanonicalName.substring(mirrorCanonicalName.lastIndexOf(".") + 1);
						argumentAnnotation = tMirror.getAnnotationsByType((Class<T>) Class.forName(mirrorCanonicalName))[0];
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					}
				}
				
				return argumentAnnotation;
			}
		}
		return null;
	}

}
