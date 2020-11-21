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

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
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
import dev.jorel.commandapi.annotations.arguments.EntitySelectorArgumentA;
import dev.jorel.commandapi.annotations.arguments.IntegerArgumentA;
import dev.jorel.commandapi.annotations.arguments.LongArgumentA;
import dev.jorel.commandapi.annotations.arguments.Primitive;
import dev.jorel.commandapi.annotations.arguments.StringArgumentA;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;

/**
 * The main annotation processor for annotation-based arguments
 */
public class Annotations extends AbstractProcessor {

	// List of stuff we can deal with
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Arrays.stream(new Class<?>[] {
			Alias.class,
			Command.class,
			Default.class,
			NeedsOp.class,
			Permission.class,
			Subcommand.class,
			IntegerArgumentA.class,
			StringArgumentA.class,
			LongArgumentA.class
		}).map(Class::getCanonicalName).collect(Collectors.toSet());
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
	
//		case "EntitySelectorArgument":
//			switch(arg.entityType()) {
//			case MANY_ENTITIES:
//				return Collection.class.getCanonicalName() + "<org.bukkit.entity.Entity>";
//			case MANY_PLAYERS:
//				return Collection.class.getCanonicalName() + "<org.bukkit.entity.Player>";
//			case ONE_ENTITY:
//				return "org.bukkit.entity.Entity";
//			case ONE_PLAYER:
//				return "org.bukkit.entity.Player";
//			}
//			return null;
//		case "EntityTypeArgument":
//			return "org.bukkit.entity.EntityType";
//		case "EnvironmentArgument":
//			return "org.bukkit.World.Environment";
//		case "FloatArgument":
//			return float.class.getCanonicalName();
//		case "FloatRangeArgument":
//			return FloatRange.class.getCanonicalName();
//		case "FunctionArgument":
//			return FunctionWrapper[].class.getCanonicalName();
//		case "IntegerArgument":
//			return int.class.getCanonicalName();
//		case "IntegerRangeArgument":
//			return IntegerRange.class.getCanonicalName();
//		case "ItemStackArgument":
//			return "org.bukkit.inventory.ItemStack";
//		case "ItemStackPredicateArgument":
//			return Predicate.class.getCanonicalName() + "<org.bukkit.inventory.ItemStack>";
//		case "Location2DArgument":
//			return Location2D.class.getCanonicalName();
//		case "LocationArgument":
//			return "org.bukkit.Location";
//		case "LongArgument":
//			return long.class.getCanonicalName();
//		case "LootTableArgument":
//			return "org.bukkit.loot.LootTable";
//		case "MathOperationArgument":
//			return MathOperation.class.getCanonicalName();
//		case "NBTCompoundArgument":
//			return "de.tr7zw.nbtapi.NBTContainer";
//		case "ParticleArgument":
//			return "org.bukkit.Particle";
//		case "PlayerArgument":
//			return "org.bukkit.entity.Player";
//		case "PotionEffectArgument":
//			return "org.bukkit.potion.PotionEffectType";
//		case "RecipeArgument":
//			return "org.bukkit.inventory.Recipe";
//		case "RotationArgument":
//			return Rotation.class.getCanonicalName();
//		case "ScoreboardSlotArgument":
//			return ScoreboardSlot.class.getCanonicalName();
//		case "ScoreHolderArgument":
//			switch(arg.scoreHolderType()) {
////			case MULTIPLE:
////				return Collection.class.getCanonicalName() + "<String>";
////			case SINGLE:
////				return String.class.getCanonicalName();
////			}
//			return null;
//		case "SoundArgument":
//			return "org.bukkit.Sound";
//		case "LiteralArgument":
//		case "GreedyStringArgument":
//		case "TextArgument":
//		case "StringArgument":
//		case "TeamArgument":
//		case "ObjectiveArgument":
//		case "ObjectiveCriteriaArgument":
//			return String.class.getCanonicalName();
//		case "TimeArgument":
//			return int.class.getCanonicalName();
//		case "UUIDArgument":
//			return UUID.class.getCanonicalName();
//		}
//		return null;
//	}

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
			for (Element methodElement : classElement.getEnclosedElements()) {
				if (methodElement.getAnnotation(Subcommand.class) != null) {
					imports.add(MultiLiteralArgument.class.getCanonicalName());
				}
				if (methodElement.getAnnotation(NeedsOp.class) != null) {
					imports.add(CommandPermission.class.getCanonicalName());
				}
				
				// Frankly, CBA to do it for each argument, it's so not worth it
				imports.add("dev.jorel.commandapi.arguments.*");
			}
			
			String previousImport = "";
			for(String import_ : imports) {
				if(previousImport.contains(".") && import_.contains(".")) {
					if(!previousImport.substring(0, previousImport.indexOf(".")).equals(import_.substring(0, import_.indexOf(".")))) {
						out.println();
					}
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
						T argumentAnnotation = ArgumentProcessor.getArgument(parameter);
						if (argumentAnnotation == null) {
							processingEnv.getMessager().printMessage(Kind.ERROR,
									"Parameter " + parameter.getSimpleName() + " in method "
											+ methodElement.getSimpleName()
											+ " does not have an argument annotation on it! ");
							return;
						}
						
						
						out.print(indent(indent) + ".withArguments(new ");
						// We're assuming that the name of the argument MUST be the same name + "A"
						out.print(argumentAnnotation.annotationType().getSimpleName().substring(0, argumentAnnotation.annotationType().getSimpleName().length() - 1));
						
						// Node name
						out.print("(\"");
						out.print(parameter.getSimpleName());
						out.print("\"");
						
						//Complex processing goes here...
						if(argumentAnnotation instanceof IntegerArgumentA) {
							IntegerArgumentA argument = (IntegerArgumentA) argumentAnnotation;
							out.print(", " + argument.min() + ", " + argument.max());
						} else if(argumentAnnotation instanceof LongArgumentA) {
							LongArgumentA argument = (LongArgumentA) argumentAnnotation;
							out.print(", " + argument.min() + ", " + argument.max());
						}
						
						out.println("))");
						
						Primitive primitive = ArgumentProcessor.getPrimitive(argumentAnnotation);
						if(primitive.value().length == 1) {
							argumentMapping.put(i - 1, primitive.value()[0]);
						} else {
							if(argumentAnnotation instanceof EntitySelectorArgumentA) {
								EntitySelectorArgumentA argument = (EntitySelectorArgumentA) argumentAnnotation;
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
							}
							//TODO
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
						out.print(", (");
						String fromArgumentMap = argumentMapping.get(i);
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

}
