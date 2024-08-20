/*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi.annotations.reloaded;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AnnotationUtils {

	/**
	 * Get the AnnotationMirror for a specific annotation on an element
	 */
	public Optional<? extends AnnotationMirror> getAnnotationMirror(Element element, Class<? extends Annotation> annotationClass) {
		var className = annotationClass.getCanonicalName();
		return element.getAnnotationMirrors().stream()
			.filter(mirror -> mirror.getAnnotationType().toString().equals(className))
			.findFirst();
	}

	/**
	 * Get the TypeMirror from an annotation
	 */
	public Optional<? extends AnnotationValue> getAnnotationValue(Element element, Class<? extends Annotation> annotationClass) {
		return getAnnotationMirror(element, annotationClass)
			.flatMap(annotationMirror -> annotationMirror.getElementValues().entrySet().stream()
				.filter(entry -> entry.getKey().getSimpleName().toString().equals("value"))
				.map(Map.Entry::getValue)
				.findFirst());
	}

	/**
	 * Get the TypeMirror from an annotation which has a value of type {@code Class<>}
	 */
	public Optional<TypeMirror> getAnnotationClassValue(Element element, Class<? extends Annotation> annotationClass) {
		return getAnnotationValue(element, annotationClass)
			.map(AnnotationValue::getValue)
			.map(TypeMirror.class::cast);
	}

	/**
	 * @return True, if the element contains the given annotation
	 */
	public boolean hasAnnotation(Element element, Class<? extends Annotation> annotationClass) {
		return element.getAnnotation(annotationClass) != null;
	}

	/**
	 * @return True, if the element contains any of the given annotations
	 */
	@SafeVarargs
	public final boolean hasAnyAnnotation(Element element, Class<? extends Annotation>... annotationClasses) {
		for (var annotationClass : annotationClasses) {
			if (hasAnnotation(element, annotationClass)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return True, if the element contains none of the given annotations
	 */
	@SafeVarargs
	public final boolean doesNotHaveAnnotations(Element element, Class<? extends Annotation>... annotationClasses) {
		return !hasAnyAnnotation(element, annotationClasses);
	}

	/**
	 * @param element The element to find enclosed elements within
	 * @param annotationClass The annotation to look for in enclosed elements
	 * @return A list of all enclosed elements with the given annotation
	 */
	public List<? extends Element> getEnclosedElementsWithAnnotation(Element element, Class<? extends Annotation> annotationClass) {
		return element.getEnclosedElements().stream()
			.filter(enclosed -> hasAnnotation(enclosed, annotationClass))
			.toList();
	}

	/**
	 * @param element The element to find enclosed methods within
	 * @param annotationClass The annotation to look for in enclosed methods
	 * @return A list of all enclosed methods with the given annotation
	 */
	public List<ExecutableElement> getEnclosedMethodsWithAnnotation(Element element, Class<? extends Annotation> annotationClass) {
		return getEnclosedElementsWithAnnotation(element, annotationClass).stream()
			.filter(enclosed -> enclosed.getKind() == ElementKind.METHOD)
			.map(ExecutableElement.class::cast)
			.toList();
	}

	/**
	 * @param element The element to find the top-level class of
	 * @return The top-level class of this element
	 */
	public TypeElement getTopLevelClass(Element element) {
		if (element instanceof TypeElement typeElement) {
			if (typeElement.getNestingKind() == NestingKind.TOP_LEVEL) {
				return typeElement;
			}
		}
		return getTopLevelClass(element.getEnclosingElement());
	}
}
