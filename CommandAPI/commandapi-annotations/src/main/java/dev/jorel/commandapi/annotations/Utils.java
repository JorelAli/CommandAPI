package dev.jorel.commandapi.annotations;

import java.lang.annotation.Annotation;
import java.util.Map.Entry;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.annotations.arguments.Primitive;
import dev.jorel.commandapi.executors.ExecutorType;

public class Utils {
	
	public final static String COMMAND_VAR_NAME = "command";
	
	public static String quote(String str) {
		return "\"" + str + "\"";
	}
	
	public static String[] strCons(String str, String[] strArr) {
		String[] result = new String[1 + strArr.length];
		result[0] = str;
		System.arraycopy(strArr, 0, result, 1, strArr.length);
		return result;
	}
	
	public static ExecutorType[] executorTypeCons(ExecutorType type, ExecutorType[] types) {
		ExecutorType[] result = new ExecutorType[1 + types.length];
		result[0] = type;
		System.arraycopy(types, 0, result, 1, types.length);
		return result;
	}

	public static <T extends Annotation> boolean hasAnnotation(Element element, Class<T> annotation) {
		return element.getAnnotation(annotation) != null;
	}

	public static boolean isValidSender(TypeMirror typeMirror) throws ClassNotFoundException {
		return CommandSender.class.isAssignableFrom(Class.forName(typeMirror.toString()));
	}

	public static Annotation getArgumentAnnotation(Element element) {
		Annotation annotation = null;
		for (Class<? extends Annotation> annotationClass : Annotations.ARGUMENT_ANNOTATIONS) {
			annotation = element.getAnnotation(annotationClass);

			// We've found an annotation. We don't care about multiple argument annotations,
			// this is covered by semantics // TODO: Implement in semantics
			if (annotation != null) {
				break;
			}
		}
		return annotation;
	}
	
	public static TypeMirror[] getPrimitiveTypeMirror(Primitive primitive, ProcessingEnvironment processingEnv) {
		TypeMirror[] result = new TypeMirror[primitive.value().length];
		for(int i = 0; i < primitive.value().length; i++) {
			String p = primitive.value()[i];
			TypeElement element = processingEnv.getElementUtils().getTypeElement(p);
			
			// Check if it's a primitive (e.g. int, boolean etc.)?
			if(element == null) {
				for(TypeKind kind : TypeKind.values()) {
					if(kind.isPrimitive() && kind.name().equalsIgnoreCase(p)) {
						// TODO: Is Boxing really the right thing to do here?
						element = processingEnv.getTypeUtils().boxedClass(processingEnv.getTypeUtils().getPrimitiveType(kind));
						break;
					}
				}
			}
			
			// TODO: This is where things get a little messy. Until we figure out how to do this, we ignore arrays and generics
			if(element == null) {
				if(p.contains("<")) {
					p = p.substring(0, p.indexOf("<"));
				}
				if(p.contains("[")) {
					p = p.substring(0, p.indexOf("["));
				}
				element = processingEnv.getElementUtils().getTypeElement(p);
			}
			
			if(element == null) {
				// Oh no, we've got a missing type here!
				System.out.println(p);
			} else {
				result[i] = element.asType();
			}
			
		}
		return result;
	}

	/**
	 * Get the TypeMirror from an annotation which has a value of type {@code Class<>}
	 */
	public static TypeMirror getAnnotationClassValue(Element element, Class<? extends Annotation> annotationClass) {
		String className = annotationClass.getCanonicalName();
		AnnotationMirror annotationMirror = null;
		for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
			if (mirror.getAnnotationType().toString().equals(className)) {
				annotationMirror = mirror;
				break;
			}
		}

		if (annotationMirror != null) {
			AnnotationValue annotationValue = null;
			for (Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror
					.getElementValues().entrySet()) {
				if (entry.getKey().getSimpleName().toString().equals("value")) {
					annotationValue = entry.getValue();
				}
			}

			if (annotationValue != null) {
				return (TypeMirror) annotationValue.getValue();
			}
		}

		return null;
	}

	/**
	 * Somebody forgot to add an argument annotation to a method parameter! Let's
	 * help them out a bit. TODO: Even better, take inspiration from Elm and include
	 * a list of all possible annotation classes they could use that match their
	 * specified parameter
	 */
	public static String predictAnnotation(VariableElement element) {
		for (Class<? extends Annotation> annotation : Annotations.ARGUMENT_ANNOTATIONS) {
			for (String value : annotation.getAnnotation(Primitive.class).value()) {
				if (element.asType().toString().equals(value)) {
					return "Did you mean to add @" + annotation.getSimpleName();
				}
			}
		}
		return "";
	}

}
