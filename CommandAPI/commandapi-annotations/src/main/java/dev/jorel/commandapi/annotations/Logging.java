package dev.jorel.commandapi.annotations;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

public class Logging {

	ProcessingEnvironment processingEnv;

	public Logging(ProcessingEnvironment processor) {
		this.processingEnv = processor;
	}
	
	public void complain(Element element, String message) {
		processingEnv.getMessager().printMessage(Kind.ERROR, message, element);
	}

}
