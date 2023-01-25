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
package dev.jorel.commandapi.preprocessor;

import java.lang.reflect.Field;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.Diagnostic.Kind;

/**
 * The main entry point for the internal CommandAPI annotation preprocessor.
 * This has nothing to do with registering commmands using annotations!
 */
public class Preprocessor extends AbstractProcessor {

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Set.of(
			RequireField.class.getCanonicalName(),
			RequireFields.class.getCanonicalName(),
			NMSMeta.class.getCanonicalName(),
			Differs.class.getCanonicalName());
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		// Handle single annotations of AssertField
		for (Element element : roundEnv.getElementsAnnotatedWith(RequireField.class)) {
			processRequireField(element.getAnnotation(RequireField.class));
		}

		// Handle multiple annotations of AssertField
		for (Element element : roundEnv.getElementsAnnotatedWith(RequireFields.class)) {
			for (RequireField requireField : element.getAnnotation(RequireFields.class).value()) {
				processRequireField(requireField);
			}
		}
		return true;
	}
	
	private Class<?> fromMirror(MirroredTypeException e) {
		if(e.getTypeMirror().getKind().isPrimitive()) {
			return switch(e.getTypeMirror().toString()) {
			case "void"     -> void.class;
			case "boolean" -> boolean.class;
			case "char"    -> char.class;
			case "byte"    -> byte.class;
			case "short"   -> short.class;
			case "int"     -> int.class;
			case "long"    -> long.class;
			case "float"   -> float.class;
			case "double"  -> double.class;
			default        -> throw new IllegalArgumentException("Unexpected value: " + e.getTypeMirror().toString());
			};
		}
		try {
			return Class.forName(e.getTypeMirror().toString(), false, this.getClass().getClassLoader());
		} catch (ClassNotFoundException e1) {
			return null;
		}
	}
	
	private void processRequireField(RequireField field) {
		Class<?> in;
		try {
			in = field.in();
		} catch (MirroredTypeException e) {
			in = fromMirror(e);
		}

		Class<?> type;
		try {
			type = field.ofType();
		} catch (MirroredTypeException e) {
			type = fromMirror(e);
		}
		
		if(in == null || type == null) {
			String message = String.format("Failed to initialize field %s %s.%s for @RequireField check. Somehow one of these is null? ",
				field.ofType(),
				field.in(),
				field.name());
			super.processingEnv.getMessager().printMessage(Kind.ERROR, message);
			return;
		}

		Field classField;
		try {
			classField = in.getDeclaredField(field.name());
		} catch (NullPointerException e) {
			String message = String.format("Field: %s ??.%s does not exist. The class for this field is also missing!",
				type.getSimpleName(),
				field.name());
			super.processingEnv.getMessager().printMessage(Kind.ERROR, message);
			return;
		} catch (NoSuchFieldException | SecurityException e) {
			String message = String.format("Field: %s %s.%s does not exist",
				type.getSimpleName(),
				in.getSimpleName(),
				field.name());
			super.processingEnv.getMessager().printMessage(Kind.ERROR, message);
			return;
		}

		if (!classField.getType().getCanonicalName().equals(type.getCanonicalName())) {
			String message = String.format("Field: %s %s.%s does not exist. Instead found field: %s %s.%s",
				type.getCanonicalName(),
				in.getSimpleName(),
				field.name(),
				classField.getType().getCanonicalName(),
				in.getSimpleName(),
				field.name());
			super.processingEnv.getMessager().printMessage(Kind.ERROR, message);
		}

	}
}
