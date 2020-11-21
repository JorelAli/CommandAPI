package dev.jorel.commandapi.annotations;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.VariableElement;

import dev.jorel.commandapi.annotations.arguments.IntegerArgumentA;
import dev.jorel.commandapi.annotations.arguments.Primitive;
import dev.jorel.commandapi.annotations.arguments.StringArgumentA;

public class ArgumentProcessor {
	
	final static Class<?>[] ARGUMENT_ANNOTATIONS = new Class<?>[] {
		IntegerArgumentA.class, StringArgumentA.class
	};
	
	public static boolean isArgument(AnnotationMirror mirror) {
		final String mirrorName = mirror.getAnnotationType().toString();
		return Arrays.stream(ARGUMENT_ANNOTATIONS).map(Class::getCanonicalName).anyMatch(mirrorName::equals);
	}
	
	public static <T extends Annotation> Primitive getPrimitive(T annotation) {
		return annotation.annotationType().getDeclaredAnnotation(Primitive.class);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Annotation> T getArgument(VariableElement tMirror) {
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
