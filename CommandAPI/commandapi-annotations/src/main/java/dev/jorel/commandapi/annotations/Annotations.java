package dev.jorel.commandapi.annotations;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.sql.rowset.Predicate;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.Location2DArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.ScoreHolderArgument;
import dev.jorel.commandapi.arguments.ScoreHolderArgument.ScoreHolderType;
import dev.jorel.commandapi.wrappers.FloatRange;
import dev.jorel.commandapi.wrappers.FunctionWrapper;
import dev.jorel.commandapi.wrappers.IntegerRange;
import dev.jorel.commandapi.wrappers.Location2D;
import dev.jorel.commandapi.wrappers.MathOperation;
import dev.jorel.commandapi.wrappers.Rotation;
import dev.jorel.commandapi.wrappers.ScoreboardSlot;

/* The main processor */
public class Annotations extends AbstractProcessor {

	// List of stuff we can deal with
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return new HashSet<String>(Arrays.asList(Alias.class.getCanonicalName(), Arg.class.getCanonicalName(),
				Command.class.getCanonicalName(), Default.class.getCanonicalName(),
				NeedsOp.class.getCanonicalName(),
				Permission.class.getCanonicalName(), Subcommand.class.getCanonicalName()));
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
	
	/*
	 * So you're probably wondering, why do I have to do all of this
	 * instead of just create an instance of the Argument class and
	 * then everything's all good?
	 * 
	 * Well, that's because you can't instantiate an Argument
	 * class without having Brigadier as a dependency in the plugin you're
	 * trying to compile and that's completely against what the CommandAPI
	 * is trying to achieve
	 */
	private String mapArgumentTypes(String argumentClass, Arg arg) {
		switch (argumentClass) {
		case "AdvancementArgument":
			return "org.bukkit.advancement.Advancement";
		case "AngleArgument":
			return float.class.getCanonicalName();
		case "AxisArgument":
			return EnumSet.class.getCanonicalName() + "<org.bukkit.Axis>";
		case "BiomeArgument":
			return "org.bukkit.block.Biome";
		case "BlockPredicateArgument":
			return Predicate.class.getCanonicalName() + "<org.bukkit.block.Block>";
		case "BlockStateArgument":
			return "org.bukkit.block.data.BlockData";
		case "BooleanArgument":
			return boolean.class.getCanonicalName();
		case "ChatArgument":
			return "net.md_5.bungee.api.chat.BaseComponent[]";
		case "ChatColorArgument":
			return "org.bukkit.ChatColor";
		case "ChatComponentArgument":
			return "net.md_5.bungee.api.chat.BaseComponent[]";
		case "DoubleArgument":
			return double.class.getCanonicalName();
		case "EnchantmentArgument":
			return "org.bukkit.enchantments.Enchantment";
		case "EntitySelectorArgument":
			switch(arg.entityType()) {
			case MANY_ENTITIES:
				return Collection.class.getCanonicalName() + "<org.bukkit.entity.Entity>";
			case MANY_PLAYERS:
				return Collection.class.getCanonicalName() + "<org.bukkit.entity.Player>";
			case ONE_ENTITY:
				return "org.bukkit.entity.Entity";
			case ONE_PLAYER:
				return "org.bukkit.entity.Player";
			}
			return null;
		case "EntityTypeArgument":
			return "org.bukkit.entity.EntityType";
		case "EnvironmentArgument":
			return "org.bukkit.World.Environment";
		case "FloatArgument":
			return float.class.getCanonicalName();
		case "FloatRangeArgument":
			return FloatRange.class.getCanonicalName();
		case "FunctionArgument":
			return FunctionWrapper[].class.getCanonicalName();
		case "IntegerArgument":
			return int.class.getCanonicalName();
		case "IntegerRangeArgument":
			return IntegerRange.class.getCanonicalName();
		case "ItemStackArgument":
			return "org.bukkit.inventory.ItemStack";
		case "ItemStackPredicateArgument":
			return Predicate.class.getCanonicalName() + "<org.bukkit.inventory.ItemStack>";
		case "Location2DArgument":
			return Location2D.class.getCanonicalName();
		case "LocationArgument":
			return "org.bukkit.Location";
		case "LongArgument":
			return long.class.getCanonicalName();
		case "LootTableArgument":
			return "org.bukkit.loot.LootTable";
		case "MathOperationArgument":
			return MathOperation.class.getCanonicalName();
		case "NBTCompoundArgument":
			return "de.tr7zw.nbtapi.NBTContainer";
		case "ParticleArgument":
			return "org.bukkit.Particle";
		case "PlayerArgument":
			return "org.bukkit.entity.Player";
		case "PotionEffectArgument":
			return "org.bukkit.potion.PotionEffectType";
		case "RecipeArgument":
			return "org.bukkit.inventory.Recipe";
		case "RotationArgument":
			return Rotation.class.getCanonicalName();
		case "ScoreboardSlotArgument":
			return ScoreboardSlot.class.getCanonicalName();
		case "ScoreHolderArgument":
			switch(arg.scoreHolderType()) {
			case MULTIPLE:
				return Collection.class.getCanonicalName() + "<String>";
			case SINGLE:
				return String.class.getCanonicalName();
			}
			return null;
		case "SoundArgument":
			return "org.bukkit.Sound";
		case "LiteralArgument":
		case "GreedyStringArgument":
		case "TextArgument":
		case "StringArgument":
		case "TeamArgument":
		case "ObjectiveArgument":
		case "ObjectiveCriteriaArgument":
			return String.class.getCanonicalName();
		case "TimeArgument":
			return int.class.getCanonicalName();
		case "UUIDArgument":
			return UUID.class.getCanonicalName();
		}
		return null;
	}

	// Indentation, because half of this file is actually just making stuff look nice
	private String indent(int indent) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < indent; i++) {
			builder.append("    ");
		}
		return builder.toString();
	}

	// Get fully qualified name from type mirror
	private String fromTypeMirror(MirroredTypeException e) {
		return e.getTypeMirror().toString();
	}
	
	private String simpleFromQualified(String name) {
		if(name == null) {
			return null;
		}
		return name.split("\\.")[name.split("\\.").length - 1];
	}

	private void processCommand(RoundEnvironment roundEnv, Element classElement) throws IOException {
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
				
				// @Arg/@Args handler
				BiConsumer<Integer, Arg> argHandler = (indent_, arg) -> {
					String className;
					String simpleClassName;
					try {
						className = arg.type().getCanonicalName();
						simpleClassName = arg.type().getSimpleName();
					} catch (MirroredTypeException e) {
						className = fromTypeMirror(e);
						simpleClassName = simpleFromQualified(className);
					}
					imports.add(className);
					if(simpleClassName.equals(LocationArgument.class.getSimpleName()) || simpleClassName.equals(Location2DArgument.class.getSimpleName())) {
						imports.add(LocationType.class.getCanonicalName());
					} else if(simpleClassName.equals(ScoreHolderArgument.class.getSimpleName())) {
						imports.add(ScoreHolderType.class.getCanonicalName());
					} else if(simpleClassName.equals(EntitySelectorArgument.class.getSimpleName())) {
						imports.add(EntitySelector.class.getCanonicalName());
					}
					
					String import_ = mapArgumentTypes(simpleClassName, arg);
					imports.add(import_.replaceAll("<.+>", ""));
					if(import_.contains("<")) {
						System.out.println(import_);
						imports.add(import_.substring(import_.indexOf("<") + 1, import_.indexOf(">")));
					}
				};
				
				// @Arg
				if (methodElement.getAnnotation(Arg.class) != null) {
					argHandler.accept(indent, methodElement.getAnnotation(Arg.class));
				}
				// @Args
				if (methodElement.getAnnotation(Args.class) != null) {
					for (Arg arg : methodElement.getAnnotation(Args.class).value()) {
						argHandler.accept(indent, arg);
					}
				}
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
					final int[] argIndex = new int[] {0};
					
					// @Arg/@Args handler
					BiConsumer<Integer, Arg> argHandler = (indent_, arg) -> {
						out.print(indent(indent_) + ".withArguments(new ");
						String simpleClassName;
						try {
							simpleClassName = arg.type().getSimpleName();
						} catch (MirroredTypeException e) {
							simpleClassName = simpleFromQualified(fromTypeMirror(e));
						}
						out.print(simpleClassName);
						out.print("(\"");
						out.print(arg.name());
						
						if(simpleClassName.equals(LocationArgument.class.getSimpleName()) || simpleClassName.equals(Location2DArgument.class.getSimpleName())) {
							out.print("\", ");
							out.print(LocationType.class.getSimpleName() + "." + arg.locationType().name());
						} else if(simpleClassName.equals(ScoreHolderArgument.class.getSimpleName())) {
							out.print("\", ");
							out.print(ScoreHolderType.class.getSimpleName() + "." + arg.scoreHolderType().name());
						} else if(simpleClassName.equals(EntitySelectorArgument.class.getSimpleName())) {
							out.print("\", ");
							out.print(EntitySelector.class.getSimpleName() + "." + arg.entityType().name());
						} else if (simpleClassName.equals(CustomArgument.class.getSimpleName())) {
							processingEnv.getMessager().printMessage(Kind.ERROR, CustomArgument.class.getSimpleName() + " is not supported with annotations");
						} else {
							out.print("\"");
						}
						out.println("))");

						//Construct the argument to get its primitive type
						String expectedPrimitiveType = mapArgumentTypes(simpleClassName, arg);
						
						// Get the name of the parameter itself (e.g. Player p -> "p")
						ExecutableElement executableMethodElement = (ExecutableElement) methodElement;
						String paramName = executableMethodElement.getParameters().get(argIndex[0] + 1).getSimpleName().toString();
						
						// Get the type of the parameter
						TypeMirror paramType = methodType.getParameterTypes().get(argIndex[0] + 1);
						
						if(paramType.toString().equals(expectedPrimitiveType)) {
							argumentMapping.put(argIndex[0], expectedPrimitiveType);
						} else {
							processingEnv.getMessager().printMessage(Kind.ERROR,
									"Invalid argument " + paramName + " in method " + methodElement.getSimpleName()
											+ ". Expected type " + expectedPrimitiveType
											+ " but instead got " + paramType.toString());
						}
						
						argIndex[0]++;
					};

					// @Arg
					if (methodElement.getAnnotation(Arg.class) != null) {
						argHandler.accept(indent, methodElement.getAnnotation(Arg.class));
					}

					// @Args
					if (methodElement.getAnnotation(Args.class) != null) {
						for (Arg arg : methodElement.getAnnotation(Args.class).value()) {
							argHandler.accept(indent, arg);
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
					
					for(int i = 0; i < argIndex[0]; i++) {
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
