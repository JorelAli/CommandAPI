package dev.jorel.commandapi.annotations;

import java.lang.annotation.Annotation;
import java.util.Map.Entry;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.annotations.arguments.Primitive;

public class Utils {

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

	/**
	 * Get the TypeMirror from an annotation which has a value of type class
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
