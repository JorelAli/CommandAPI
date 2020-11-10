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

/* The main processor */
public class Annotations extends AbstractProcessor {

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

	private String indent(int indent) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < indent; i++) {
			builder.append("  ");
		}
		return builder.toString();
	}

	private String fromTypeMirror(MirroredTypeException e) {
		return e.getTypeMirror().toString();
	}

	@SuppressWarnings("unused")
	private void processCommand(RoundEnvironment roundEnv, Element element) throws IOException {
		TypeElement commandClass = (TypeElement) element;
		JavaFileObject builderFile = processingEnv.getFiler()
				.createSourceFile(commandClass.getQualifiedName() + "$Command");

		try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {

			int indent = 0;
			String packageName = null;

			if (packageName != null) {
				out.print("package ");
				out.print(packageName);
				out.println(";");
				out.println();
			}

			// Imports
			out.println("import dev.jorel.commandapi.CommandAPICommand;");
			out.println("import dev.jorel.commandapi.CommandPermission;");
			out.println("import dev.jorel.commandapi.arguments.MultiLiteralArgument;");
			out.println("import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;");
			out.println("import dev.jorel.commandapi.arguments.LocationType;");
			out.println("import dev.jorel.commandapi.arguments.ScoreHolderArgument.ScoreHolderType;");
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

			for (Element methodElement : element.getEnclosedElements()) {
				if (methodElement.getAnnotation(Default.class) != null
						|| methodElement.getAnnotation(Subcommand.class) != null) {
					
					ExecutableType methodType = (ExecutableType) methodElement.asType();

					out.print(indent(indent) + "new CommandAPICommand(\"");
					out.print(commandClass.getAnnotation(Command.class).value());
					out.println("\")");
					indent++;

					// @Subcommand (Also handle @Alias for @Subcommand)
					if (methodElement.getAnnotation(Subcommand.class) != null) {
						out.print(indent(indent) + ".withArguments(new MultiLiteralArgument(\"");
						out.print(methodElement.getAnnotation(Subcommand.class).value());
						out.print("\"");
						
						if (methodElement.getAnnotation(Alias.class) != null) {
							out.print(", ");
							out.print(Arrays.stream(methodElement.getAnnotation(Alias.class).value())
									.map(x -> "\"" + x + "\"").collect(Collectors.joining(", ")));
						}
						
						out.println(").setListed(false))");
					}

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

					// @Alias (@Default only)
					if (methodElement.getAnnotation(Alias.class) != null & methodElement.getAnnotation(Default.class) != null) {
						out.print(indent(indent) + ".withAliases(");
						out.print(Arrays.stream(methodElement.getAnnotation(Alias.class).value())
								.map(x -> "\"" + x + "\"").collect(Collectors.joining(", ")));
						out.println(")");
					}
					
					// @Arg/@Args handler
					BiConsumer<Integer, Arg> argHandler = (indent_, arg) -> {
						out.print(indent(indent_) + ".withArguments(new ");
						String className;
						String simpleClassName;
						try {
							className = arg.type().getCanonicalName();
							simpleClassName = arg.type().getSimpleName();
						} catch (MirroredTypeException e) {
							className = fromTypeMirror(e);
							simpleClassName = className.split("\\.")[className.split("\\.").length - 1];
						}
						out.print(className);
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
