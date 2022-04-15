package dev.jorel.commandapi.annotations.parser;

import static dev.jorel.commandapi.annotations.Utils.hasAnnotation;

import javax.lang.model.element.Element;

import dev.jorel.commandapi.annotations.Utils;
import dev.jorel.commandapi.annotations.annotations.AnnotationEnum;
import dev.jorel.commandapi.annotations.annotations.Default;
import dev.jorel.commandapi.annotations.annotations.Subcommand;
import dev.jorel.commandapi.annotations.annotations.Suggestion;

public class MethodParser {

	/*
	 * Method parsing. Parses methods
	 */
	
	private Element classElement;
	
	public MethodParser(Element classElement) {
		this.classElement = classElement;
	}
	
	public void parseAllMethods() {
		for(Element methodElement : classElement.getEnclosedElements()) {
			
			for(AnnotationEnum annotation : Utils.unpackPresentAnnotations(classElement)) {
				switch(annotation) {
					case DEFAULT:
						// GOOOOOOOOOOOOO
						break;
					case SUBCOMMAND:
						break;
					case SUGGESTION:
						break;
					default:
						break;
				}
			}
			
			if(hasAnnotation(methodElement, Default.class) || hasAnnotation(methodElement, Subcommand.class)) {
				
			} else if(hasAnnotation(methodElement, Suggestion.class)) {
				
			}
			
		}
	}
	
	public void parse() {
		
	}
	
	public void parseSubcommandOrDefault() {
		
	}
	
	public void parseSuggestion() {
		
	}
	
}
