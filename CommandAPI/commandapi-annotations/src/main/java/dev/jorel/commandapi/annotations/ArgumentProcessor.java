package dev.jorel.commandapi.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import dev.jorel.commandapi.annotations.arguments.ArgumentMetadata;
import dev.jorel.commandapi.annotations.arguments.IntegerArgumentA;
import dev.jorel.commandapi.annotations.arguments.StringArgumentA;

public class ArgumentProcessor {
	
	final static Class<?>[] ARGUMENT_ANNOTATIONS = new Class<?>[] {
		IntegerArgumentA.class, StringArgumentA.class
	};
	
	public static boolean isArgument(AnnotationMirror mirror) {
		final String mirrorName = mirror.getAnnotationType().toString();
		return Arrays.stream(ARGUMENT_ANNOTATIONS).map(Class::getCanonicalName).anyMatch(mirrorName::equals);
	}
	
	public static ArgumentMetadata getArgumentMetadata(List<? extends AnnotationMirror> mirrors) {
		ArgumentMetadata meta = null;
		
		for(AnnotationMirror mirror : mirrors) {
			if(isArgument(mirror)) {
				Class<?>[] innerClasses = null;
				try {
					innerClasses = Class.forName(mirror.getAnnotationType().toString()).getDeclaredClasses();
				} catch (SecurityException | ClassNotFoundException e) {
					e.printStackTrace();
				}
				for(Class<?> innerClass : innerClasses) {
					if(ArgumentMetadata.class.isAssignableFrom(innerClass)) {
						try {
							meta = (ArgumentMetadata) innerClass.getDeclaredConstructor().newInstance();
						} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
								| InvocationTargetException | NoSuchMethodException | SecurityException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return meta;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Annotation> T getArgument(VariableElement tMirror) {
		for(AnnotationMirror mirror : tMirror.getAnnotationMirrors()) {
			if(isArgument(mirror)) {
				try {
					return tMirror.getAnnotation((Class<T>) Class.forName(mirror.getAnnotationType().toString()));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static List<Object> getArguments(ExecutableElement element) {
		
		for(int i = 1; i < element.getParameters().size(); i++) {
			
			VariableElement parameter = element.getParameters().get(i);
			
			if(parameter.getAnnotation(StringArgumentA.class) != null) {
				StringArgumentA argument = parameter.getAnnotation(StringArgumentA.class); 
			}
			
//			parameter.geta
			
			// Extract argument and sanity check the parameter
			long mirrors = parameter.getAnnotationMirrors().stream().map(ArgumentProcessor::isArgument).mapToInt(b -> b ? 1 : 0).count();
			if(mirrors != 1) {
				//Error
				System.out.println("Err" + parameter.getAnnotationMirrors() + "" + parameter);
			} else {
			System.out.println("Y");
				System.out.println(getArgument(parameter));
				System.out.println(getArgumentMetadata(parameter.getAnnotationMirrors()).getPrimitive().getCanonicalName());
			}
//			if(isArgument(parameter.getAnnotationMirrors())) {
//				
//			}
		}
//		element.getParameterTypes().get(0).getAnnotation(annotationType);
		
		List<Object> annotations = new ArrayList<>();
		for(AnnotationMirror mirror : element.getAnnotationMirrors()) {
			if(isArgument(mirror)) {
				System.out.println(mirror);
//				annotations.add(element.getAnnotation());
			}
		}
		return annotations;
	}
	
//	public static void processArguments(PrintWriter out) {
//		// @Arg/@Args handler
//		BiConsumer<Integer, Arg> argHandler = (indent_, arg) -> {
//			out.print(indent(indent_) + ".withArguments(new ");
//			String simpleClassName;
//			try {
//				simpleClassName = arg.type().getSimpleName();
//			} catch (MirroredTypeException e) {
//				simpleClassName = simpleFromQualified(fromTypeMirror(e));
//			}
//			out.print(simpleClassName);
//			out.print("(\"");
//			out.print(arg.name());
//			
//			if(simpleClassName.equals(LocationArgument.class.getSimpleName()) || simpleClassName.equals(Location2DArgument.class.getSimpleName())) {
//				out.print("\", ");
//				out.print(LocationType.class.getSimpleName() + "." + arg.locationType().name());
//			} else if(simpleClassName.equals(ScoreHolderArgument.class.getSimpleName())) {
//				out.print("\", ");
//				out.print(ScoreHolderType.class.getSimpleName() + "." + arg.scoreHolderType().name());
//			} else if(simpleClassName.equals(EntitySelectorArgument.class.getSimpleName())) {
//				out.print("\", ");
//				out.print(EntitySelector.class.getSimpleName() + "." + arg.entityType().name());
//			} else if (simpleClassName.equals(CustomArgument.class.getSimpleName())) {
//				processingEnv.getMessager().printMessage(Kind.ERROR, CustomArgument.class.getSimpleName() + " is not supported with annotations");
//			} else {
//				out.print("\"");
//			}
//			out.println("))");
//
//			//Construct the argument to get its primitive type
//			String expectedPrimitiveType = mapArgumentTypes(simpleClassName, arg);
//			
//			// Get the name of the parameter itself (e.g. Player p -> "p")
//			ExecutableElement executableMethodElement = (ExecutableElement) methodElement;
//			String paramName = executableMethodElement.getParameters().get(argIndex[0] + 1).getSimpleName().toString();
//			
//			// Get the type of the parameter
//			TypeMirror paramType = methodType.getParameterTypes().get(argIndex[0] + 1);
//			
//			if(paramType.toString().equals(expectedPrimitiveType)) {
//				argumentMapping.put(argIndex[0], expectedPrimitiveType);
//			} else {
//				processingEnv.getMessager().printMessage(Kind.ERROR,
//						"Invalid argument " + paramName + " in method " + methodElement.getSimpleName()
//								+ ". Expected type " + expectedPrimitiveType
//								+ " but instead got " + paramType.toString());
//			}
//			
//			argIndex[0]++;
//		};
//	}
	
}
