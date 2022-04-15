package dev.jorel.commandapi.annotations;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

import dev.jorel.commandapi.annotations.annotations.AnnotationEnum;
import dev.jorel.commandapi.annotations.annotations.AnnotationType;

public class Utils {

	public static <T extends Annotation> boolean hasAnnotation(Element element, Class<T> annotation) {
		return element.getAnnotation(annotation) != null;
	}
	
	public static AnnotationEnum[] unpackPresentAnnotations(Element element) {
		List<AnnotationEnum> annotations = new ArrayList<>();
		
		for(Class<? extends Annotation> annotation : Annotations.METHOD_ANNOTATIONS) {
			Annotation declaredAnnotation = element.getAnnotation(annotation);
			if(declaredAnnotation != null) {
				AnnotationType annotationType = declaredAnnotation.annotationType().getAnnotation(null);
				if(annotationType != null) {
					annotations.add(annotationType.value());
				}
			}
		}
		
		return annotations.toArray(new AnnotationEnum[0]);
	}
	
}
