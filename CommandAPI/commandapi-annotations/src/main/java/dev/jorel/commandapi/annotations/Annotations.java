package dev.jorel.commandapi.annotations;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.tools.JavaFileObject;

/* The main processor */
public class Annotations extends AbstractProcessor {

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return new HashSet<String>(Arrays.asList(Alias.class.getCanonicalName(), Arg.class.getCanonicalName(),
				Command.class.getCanonicalName(), Default.class.getCanonicalName(),
				Description.class.getCanonicalName(), Executor.class.getCanonicalName(),
				NeedsOp.class.getCanonicalName(), Permission.class.getCanonicalName(),
				Subcommand.class.getCanonicalName()));
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
		for(int i = 0; i < indent; i++) {
			builder.append("  ");
		}
		return builder.toString();
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
			out.println();

			// Class declaration
			out.print("public class ");
			out.print(commandClass.getSimpleName() + "$Command");
			out.println(" {");
			out.println();
			indent++;
			
			// Main registration method
			out.println(indent(indent) + "public static void register() {");
			indent++;
			out.print(indent(indent) + "new CommandAPICommand(\"");
			out.print(commandClass.getAnnotation(Command.class).value());
			out.println("\")");
			indent++;

			for (Element e : element.getEnclosedElements()) {
				if (e.getAnnotation(Default.class) != null) {
					ExecutableType type = (ExecutableType) e.asType();
					
					// @NeedsOp
					if (e.getAnnotation(NeedsOp.class) != null) {
						out.println(indent(indent) + ".withPermission(CommandPermission.OP)");
					}
					
					// @NeedsOp
					if (e.getAnnotation(Permission.class) != null) {
						out.print(indent(indent) + ".withPermission(\"");
						out.print(indent(indent) + e.getAnnotation(Permission.class).value());
						out.println(indent(indent) + "\")");
					}

					// @Executor
					if(e.getAnnotation(Executor.class) != null) {
						switch(e.getAnnotation(Executor.class).value()) {
						case ALL:
							out.print(indent(indent) + ".executes");
							break;
						case BLOCK:
							out.print(indent(indent) + ".executesCommandBlock");
							break;
						case CONSOLE:
							out.print(indent(indent) + ".executesConsole");
							break;
						case ENTITY:
							out.print(indent(indent) + ".executesEntity");
							break;
						case NATIVE:
							out.print(indent(indent) + ".executesNative");
							break;
						case PLAYER:
							out.print(indent(indent) + ".executesPlayer");
							break;
						case PROXY:
							out.print(indent(indent) + ".executesProxy");
							break;
						default:
							out.print(indent(indent) + ".executes");
							break;
						}
						
					} else {
						out.print(indent(indent) + ".executes");
					}
					
					out.println("((sender, args) -> {");
					indent++;
					out.print(indent(indent));
					out.print(commandClass.getSimpleName());
					out.print(".");
					out.print(e.getSimpleName());
					out.println("(sender, args);");
					indent--;
					out.println(indent(indent) + "})");
					
					// Register command
					out.println(indent(indent) + ".register();");
					indent--;
				} else if (e.getAnnotation(Subcommand.class) != null) {

				}
			}
			out.println();
			out.println(indent(indent) + "}"); // register()
			indent--;
			out.println();
			out.println("}"); // $Command class
		}
	}

}
