package dev.jorel.commandapi.preprocessor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.Diagnostic.Kind;

public class Preprocessor extends AbstractProcessor {

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return new HashSet<String>(Arrays.asList(
			RequireField.class.getCanonicalName(), 
			RequireFields.class.getCanonicalName()
		));
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		//Handle single annotations of AssertField
		for(Element element : roundEnv.getElementsAnnotatedWith(RequireField.class)) {
			processRequireField(element.getAnnotation(RequireField.class));
		}
		
		//Handle multiple annotations of AssertField
		for(Element element : roundEnv.getElementsAnnotatedWith(RequireFields.class)) {
			for(RequireField requireField : element.getAnnotation(RequireFields.class).value()) {
				processRequireField(requireField);
			}
		}
		return true;
	}
	
	private Class<?> fromMirror(MirroredTypeException e) {
		if(e.getTypeMirror().getKind().isPrimitive()) {
			switch(e.getTypeMirror().toString()) {
			case "void":
				return void.class;
			case "boolean":
				return boolean.class;
			case "char":
				return char.class;
			case "byte":
				return byte.class;
			case "short":
				return short.class;
			case "int":
				return int.class;
			case "long":
				return long.class;
			case "float":
				return float.class;
			case "double":
				return double.class;
			}
		}
		try {
			return Class.forName(e.getTypeMirror().toString());
		} catch (ClassNotFoundException e1) {
			return null;
		}
	}
	
	private void processRequireField(RequireField field) {
		Class<?> in;
		try {
			in = field.in();
		} catch(MirroredTypeException e) {
			in = fromMirror(e);
		}
		
		Class<?> type;
		try {
			type = field.ofType();
		} catch(MirroredTypeException e) {
			type = fromMirror(e);
		}
		
		Field classField;
		try {
			classField = in.getDeclaredField(field.name());
		} catch (NoSuchFieldException | SecurityException e) {
			String message = String.format("Field: %s %s.%s does not exist", type.getSimpleName(), in.getSimpleName(), field.name());
			super.processingEnv.getMessager().printMessage(Kind.ERROR, message);
			return;
		}
		
		if(!classField.getType().getCanonicalName().equals(type.getCanonicalName())) {
			String message = String.format("Field: %s %s.%s does not exist. Instead found field: %s %s.%s",
					type.getSimpleName(), 
					in.getSimpleName(),
					field.name(), 
					classField.getType().getSimpleName(),
					in.getSimpleName(),
					field.name());
			super.processingEnv.getMessager().printMessage(Kind.ERROR, message);
		}
		
	}
}
