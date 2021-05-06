package dev.jorel.commandapi.annotations;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.annotations.arguments.*;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.ScoreHolderArgument.ScoreHolderType;

/**
 * The main annotation processor for annotation-based arguments
 */
public class Annotations extends AbstractProcessor {

	private final Class<?>[] ARGUMENT_ANNOTATIONS = new Class<?>[] { AAdvancementArgument.class,
			AAdventureChatArgument.class, AAdventureChatComponentArgument.class, AAngleArgument.class,
			AAxisArgument.class, ABiomeArgument.class, ABlockPredicateArgument.class, ABlockStateArgument.class,
			ABooleanArgument.class, AChatArgument.class, AChatColorArgument.class, AChatComponentArgument.class,
			ADoubleArgument.class, AEnchantmentArgument.class, AEntitySelectorArgument.class, AEntityType.class,
			AEnvironmentArgument.class, AFloatArgument.class, AFloatRangeArgument.class, AFunctionArgument.class,
			AGreedyStringArgument.class, AIntegerArgument.class, AIntegerRangeArgument.class, AItemStackArgument.class,
			AItemStackPredicateArgument.class, ALiteralArgument.class, ALocation2DArgument.class,
			ALocationArgument.class, ALongArgument.class, ALootTableArgument.class, AMathOperationArgument.class,
			AMultiLiteralArgument.class, ANBTCompoundArgument.class, AObjectiveArgument.class,
			AObjectiveCriteriaArgument.class, AParticleArgument.class, APlayerArgument.class,
			APotionEffectArgument.class, ARecipeArgument.class, ARotationArgument.class, AScoreboardSlotArgument.class,
			AScoreHolderArgument.class, ASoundArgument.class, AStringArgument.class, ATeamArgument.class,
			ATextArgument.class, ATimeArgument.class, AUUIDArgument.class };

	// List of stuff we can deal with
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Stream
				.concat(Arrays.stream(new Class<?>[] { Alias.class, Command.class, Default.class, NeedsOp.class,
						Permission.class, Subcommand.class }), Arrays.stream(ARGUMENT_ANNOTATIONS))
				.map(Class::getCanonicalName).collect(Collectors.toSet());
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (Element element : roundEnv.getElementsAnnotatedWith(Command.class)) {
			try {
				processCommand(roundEnv, element);
			} catch (IOException e) {
				e.printStackTrace();
			}
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

	private <T extends Annotation> void processCommand(RoundEnvironment roundEnv, Element classElement) throws IOException {
		TypeElement commandClass = (TypeElement) classElement;
		JavaFileObject builderFile = processingEnv.getFiler()
				.createSourceFile(commandClass.getQualifiedName() + "$Command");

		try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {

			// Package name (https://www.baeldung.com/java-annotation-processing-builder)
			int indent = 0;
			int lastDot = commandClass.getQualifiedName().toString().lastIndexOf('.');
		    if (lastDot > 0) {
		    	out.print("package ");
				out.print(commandClass.getQualifiedName().toString().substring(0, lastDot));
				out.println(";");
				out.println();
		    }

			// Imports
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
				
				if(methodElement instanceof ExecutableElement) {
					ExecutableElement method = (ExecutableElement) methodElement;
					for(VariableElement parameter : method.getParameters()) {
						if(getArgument(parameter) != null) {
							imports.addAll(Arrays.asList(getPrimitive(getArgument(parameter)).value()));
							imports.add("dev.jorel.commandapi.arguments." + getArgument(parameter).annotationType().getSimpleName().substring(1));
							if(getArgument(parameter) instanceof ALocationArgument || getArgument(parameter) instanceof ALocation2DArgument) {
								imports.add(LocationType.class.getCanonicalName());
							} else if(getArgument(parameter) instanceof AScoreHolderArgument) {
								imports.add(ScoreHolderType.class.getCanonicalName());
							} else if(getArgument(parameter) instanceof AEntitySelectorArgument) {
								imports.add(EntitySelector.class.getCanonicalName());
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
			
			String previousImport = "";
			for(String import_ : imports) {
				// Separate different packages
				if(previousImport.contains(".") && import_.contains(".")) {
					if(!previousImport.substring(0, previousImport.indexOf(".")).equals(import_.substring(0, import_.indexOf(".")))) {
						out.println();
					}
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

			// Class declaration
			out.println("// This class was automatically generated by the CommandAPI");
			out.print("public class ");
			out.print(commandClass.getSimpleName() + "$Command");
			out.println(" {");
			out.println();
			indent++;

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

					// @Subcommand (Also handle @Alias for @Subcommand)
					if (methodElement.getAnnotation(Subcommand.class) != null) {
						out.println(indent(indent) + ".withArguments(");
						indent++;
						out.print(indent(indent) + "new MultiLiteralArgument(");
						
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
					
					// @NeedsOp
					if (classElement.getAnnotation(NeedsOp.class) != null) {
						out.println(indent(indent) + ".withPermission(CommandPermission.OP)");
					}

					// @Permission
					if (classElement.getAnnotation(Permission.class) != null) {
						out.print(indent(indent) + ".withPermission(\"");
						out.print(classElement.getAnnotation(Permission.class).value());
						out.println("\")");
					}

					// @Alias
					if (classElement.getAnnotation(Alias.class) != null) {
						out.print(indent(indent) + ".withAliases(");
						out.print(Arrays.stream(classElement.getAnnotation(Alias.class).value())
								.map(x -> "\"" + x + "\"").collect(Collectors.joining(", ")));
						out.println(")");
					}
					

					//Maps parameter index to argument's primitive type
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
							return;
						}
						
						out.print(indent(indent) + ".withArguments(new ");
						// We're assuming that the name of the argument MUST be "A" + the same name
						out.print(argumentAnnotation.annotationType().getSimpleName().substring(1));
						
						// Node name
						if(argumentAnnotation instanceof AMultiLiteralArgument || argumentAnnotation instanceof ALiteralArgument) {
							// Ignore node name for MultiLiteralArgument and LiteralArgument
							out.print("(");
						} else {
							out.print("(\"");
							out.print(parameter.getSimpleName());
							out.print("\"");
						}
						
						// Handle parameters
						// Number arguments
						if(argumentAnnotation instanceof AIntegerArgument) {
							AIntegerArgument argument = (AIntegerArgument) argumentAnnotation;
							out.print(", " + argument.min() + ", " + argument.max());
						} else if(argumentAnnotation instanceof ALongArgument) {
							ALongArgument argument = (ALongArgument) argumentAnnotation;
							out.print(", " + argument.min() + "L, " + argument.max() + "L");
						} else if(argumentAnnotation instanceof AFloatArgument) {
							AFloatArgument argument = (AFloatArgument) argumentAnnotation;
							out.print(", " + argument.min() + "F, " + argument.max() + "F");
						} else if(argumentAnnotation instanceof ADoubleArgument) {
							ADoubleArgument argument = (ADoubleArgument) argumentAnnotation;
							out.print(", " + argument.min() + "D, " + argument.max() + "D");
						}
						
						// Non-number arguments
						else if(argumentAnnotation instanceof ALocation2DArgument) {
							ALocation2DArgument argument = (ALocation2DArgument) argumentAnnotation;
							out.print(", " + LocationType.class.getSimpleName() + "." + argument.value().toString());
						} else if(argumentAnnotation instanceof ALocationArgument) {
							ALocationArgument argument = (ALocationArgument) argumentAnnotation;
							out.print(", " + LocationType.class.getSimpleName() + "." + argument.value().toString());
						} else if(argumentAnnotation instanceof AEntitySelectorArgument) {
							AEntitySelectorArgument argument = (AEntitySelectorArgument) argumentAnnotation;
							out.print(", " + EntitySelector.class.getSimpleName() + "." + argument.value().toString());
						} else if(argumentAnnotation instanceof AScoreHolderArgument) {
							AScoreHolderArgument argument = (AScoreHolderArgument) argumentAnnotation;
							out.print(", " + ScoreHolderType.class.getSimpleName() + "." + argument.value().toString());
						} else if(argumentAnnotation instanceof AMultiLiteralArgument) {
							AMultiLiteralArgument argument = (AMultiLiteralArgument) argumentAnnotation;
							out.print(Arrays.stream(argument.value()).map(s -> "\"" + s + "\"").collect(Collectors.joining(", ")));
						} else if(argumentAnnotation instanceof ALiteralArgument) {
							ALiteralArgument argument = (ALiteralArgument) argumentAnnotation;
							out.print("\"");
							out.print(argument.value());
							out.print("\"");
						}
						
						out.print(")");
						
						if(argumentAnnotation instanceof ALiteralArgument) {
							out.print(".setListed(true)");
						}
						
						out.println(")");
						
						// Handle return types
						Primitive primitive = getPrimitive(argumentAnnotation);
						if(primitive.value().length == 1) {
							argumentMapping.put(i - 1, primitive.value()[0]);
						} else {
							if(argumentAnnotation instanceof AEntitySelectorArgument) {
								AEntitySelectorArgument argument = (AEntitySelectorArgument) argumentAnnotation;
								switch(argument.value()) {
								case MANY_ENTITIES:
									argumentMapping.put(i - 1, primitive.value()[0]);
									break;
								case MANY_PLAYERS:
									argumentMapping.put(i - 1, primitive.value()[1]);
									break;
								case ONE_ENTITY:
									argumentMapping.put(i - 1, primitive.value()[2]);
									break;
								case ONE_PLAYER:
									argumentMapping.put(i - 1, primitive.value()[3]);
									break;
								}
							} else if (argumentAnnotation instanceof AScoreHolderArgument) {
								AScoreHolderArgument argument = (AScoreHolderArgument) argumentAnnotation;
								switch(argument.value()) {
								case MULTIPLE:
									argumentMapping.put(i - 1, primitive.value()[0]);
									break;
								case SINGLE:
									argumentMapping.put(i - 1, primitive.value()[1]);
									break;
								}
							}
						}
					}
					// .executes
					String[] firstParam = methodType.getParameterTypes().get(0).toString().split("\\.");
					switch(firstParam[firstParam.length - 1]) {
					case "Player":
						out.print(indent(indent) + ".executesPlayer");
						break;
					case "ConsoleCommandSender":
						out.print(indent(indent) + ".executesConsole");
						break;
					case "BlockCommandSender":
						out.print(indent(indent) + ".executesCommandBlock");
						break;
					case "ProxiedCommandSender":
						out.print(indent(indent) + ".executesProxy");
						break;
					case "NativeProxyCommandSender":
						out.print(indent(indent) + ".executesNative");
						break;
					case "Entity":
						out.print(indent(indent) + ".executesEntity");
						break;
					case "CommandSender":
					default:
						out.print(indent(indent) + ".executes");
						break;
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
						out.print(") args[");
						out.print(i);
						out.print("]");
					}
					//populate stuff here
					
					out.println(");");
					indent--;
					out.println(indent(indent) + "})");

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
				try {
					return tMirror.getAnnotationsByType((Class<T>) Class.forName(mirror.getAnnotationType().toString()))[0];
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
