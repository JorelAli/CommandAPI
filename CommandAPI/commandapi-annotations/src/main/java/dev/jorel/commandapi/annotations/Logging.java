package dev.jorel.commandapi.annotations;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

public class Logging {

	boolean errorsLogged;
	ProcessingEnvironment processingEnv;

	public Logging(ProcessingEnvironment processor) {
		this.processingEnv = processor;
		this.errorsLogged = false;
	}
	
	public void complain(Element element, String message) {
		errorsLogged = true;
		processingEnv.getMessager().printMessage(Kind.ERROR, message, element);
	}
	
	public void info(Element element, String message) {
		processingEnv.getMessager().printMessage(Kind.MANDATORY_WARNING, message, element);
	}
	
	public void info(String message) {
		processingEnv.getMessager().printMessage(Kind.MANDATORY_WARNING, message);
	}
	
	public Messager getMessager() {
		return processingEnv.getMessager();	
	}
	
	/*
		typeElement.getAnnotationMirrors().forEach(mirror -> {
			mirror.getElementValues();
			logging.getMessager().printMessage(Kind.MANDATORY_WARNING, mirror.getElementValues().toString());
			logging.getMessager().printMessage(Kind.MANDATORY_WARNING, mirror.getElementValues().toString(), typeElement, (AnnotationMirror)mirror);
		});
	 */

}
