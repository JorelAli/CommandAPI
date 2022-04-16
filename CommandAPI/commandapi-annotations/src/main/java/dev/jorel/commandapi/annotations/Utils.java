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
	
//	public static AnnotationEnum[] unpackMethodAnnotations(Element element) {
//		List<AnnotationEnum> annotations = new ArrayList<>();
//		
//		for(Class<? extends Annotation> annotation : Annotations.METHOD_ANNOTATIONS) {
//			Annotation declaredAnnotation = element.getAnnotation(annotation);
//			if(declaredAnnotation != null) {
//				AnnotationType annotationType = declaredAnnotation.annotationType().getAnnotation(null);
//				if(annotationType != null) {
//					annotations.add(annotationType.value());
//				}
//			}
//		}
//		
//		return annotations.toArray(new AnnotationEnum[0]);
//	}
	
}
