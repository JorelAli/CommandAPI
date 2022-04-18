package dev.jorel.commandapi.annotations;

import java.lang.annotation.Annotation;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

import org.bukkit.command.CommandSender;

public class Utils {

	public static <T extends Annotation> boolean hasAnnotation(Element element, Class<T> annotation) {
		return element.getAnnotation(annotation) != null;
	}
	
	public static boolean isValidSender(TypeMirror typeMirror) throws ClassNotFoundException {
		return CommandSender.class.isAssignableFrom(Class.forName(typeMirror.toString()));
	}
	
	public static Annotation getArgumentAnnotation(Element element) {
		Annotation annotation = null;
		for(Class<? extends Annotation> annotationClass : Annotations.ARGUMENT_ANNOTATIONS) {
			annotation = element.getAnnotation(annotationClass);
			
			// We've found an annotation. We don't care about multiple argument annotations,
			// this is covered by semantics // TODO: Implement in semantics
			if(annotation != null) {
				break;
			}
		}
		return annotation;
	}
	
}
