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
		return new HashSet<String>(Arrays.asList(
			Alias.class.getCanonicalName(), 
			Arg.class.getCanonicalName(),
			Command.class.getCanonicalName(),
			Default.class.getCanonicalName(),
			Description.class.getCanonicalName(),
			Executor.class.getCanonicalName(),
			NeedsOp.class.getCanonicalName(),
			Permission.class.getCanonicalName(),
			Subcommand.class.getCanonicalName()
		));
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for(Element element : roundEnv.getElementsAnnotatedWith(Command.class)) {
			try {
				processCommand(roundEnv, element);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	
	private void processCommand(RoundEnvironment roundEnv, Element element) throws IOException {
		TypeElement commandClass = (TypeElement) element;
		
		for(Element e : element.getEnclosedElements()) {
			if(e.getAnnotation(Default.class) != null) {
				ExecutableType type = (ExecutableType) e.asType();
				//MethodName
				String methodName = e.getSimpleName().toString();
				if(e.getAnnotation(NeedsOp.class) != null) {
					//needs op
				}
				
				//executes
				String executeType = "";
				String executor = executeType + "((sender, args) -> " + element.getSimpleName() + "." + methodName + "(sender, args))";
				String register = ".register();";
			}
		}
		
		
		
		JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(commandClass.getQualifiedName());
		
		String packageName = null;
		String simpleName = null;
		packageName = "";
		//figure out what the package name is...
		
		try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
			if (packageName != null) {
	            out.print("package ");
	            out.print(packageName);
	            out.println(";");
	            out.println();
	        }
	 
	        out.print("public class ");
	        out.print(simpleName);
	        out.println(" {");
	        out.println();
	        out.println("  public static void register() {");
	        out.println("  new CommandAPICommand(");
		}
	}
	
}
