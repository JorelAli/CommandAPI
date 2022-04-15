package dev.jorel.commandapi.annotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

public class Context {

	public static Map<Element, Context> generateContexts(Set<? extends Element> commandClasses,
			RoundEnvironment roundEnv) {
		
		Map<Element, Context> contextMap = new HashMap<>();
		for(Element classElement : commandClasses) {
			contextMap.put(classElement, new Context(classElement, roundEnv));
		}
		
		return contextMap;
	}
	
	public Context(Element classElement, RoundEnvironment roundEnv) {
		if(classElement.getKind() != ElementKind.CLASS) {
			// complain
		}
	}

}
