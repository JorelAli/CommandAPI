package dev.jorel.commandapi.annotations;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.JavaFileObject;
import javax.tools.Diagnostic.Kind;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.ScoreHolderArgument.ScoreHolderType;

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
			Set<String> imports = new HashSet<>();
			
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
						simpleClassName = className.split("\\.")[className.split("\\.").length - 1];
					}
					imports.add(className);
					if(simpleClassName.equals("LocationArgument") || simpleClassName.equals("Location2DArgument")) {
						imports.add(LocationType.class.getCanonicalName());
					} else if(simpleClassName.equals("ScoreHolderArgument")) {
						imports.add(ScoreHolderType.class.getCanonicalName());
					} else if(simpleClassName.equals("EntitySelectorArgument")) {
						imports.add(EntitySelector.class.getCanonicalName());
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
			
			for(String import_ : imports) {
				out.print("import ");
				out.print(import_);
				out.println(";");
			}
			out.println();

			// Class declaration
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
					
					// @Arg/@Args handler
					BiConsumer<Integer, Arg> argHandler = (indent_, arg) -> {
						out.print(indent(indent_) + ".withArguments(new ");
						String simpleClassName;
						try {
							simpleClassName = arg.type().getSimpleName();
						} catch (MirroredTypeException e) {
							simpleClassName = fromTypeMirror(e).split("\\.")[fromTypeMirror(e).split("\\.").length - 1];
						}
						out.print(simpleClassName);
						out.print("(\"");
						out.print(arg.name());
						
						if(simpleClassName.equals("LocationArgument") || simpleClassName.equals("Location2DArgument")) {
							out.print("\", ");
							out.print("LocationType." + arg.locationType().name());
						} else if(simpleClassName.equals("ScoreHolderArgument")) {
							out.print("\", ");
							out.print("ScoreHolderType." + arg.scoreHolderType().name());
						} else if(simpleClassName.equals("EntitySelectorArgument")) {
							out.print("\", ");
							out.print("EntitySelector." + arg.entityType().name());
						} else {
							out.print("\"");
						}
						
						out.println("))");
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
					out.println("(sender, args);");
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
