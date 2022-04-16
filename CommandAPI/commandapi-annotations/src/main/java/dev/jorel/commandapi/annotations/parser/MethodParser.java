package dev.jorel.commandapi.annotations.parser;

import javax.lang.model.element.Element;

import dev.jorel.commandapi.annotations.Utils;
import dev.jorel.commandapi.annotations.annotations.AnnotationEnum;

public class MethodParser {

	/*
	 * Method parsing. Parses methods
	 */
	
	private Element classElement;
	
	public MethodParser(Element classElement) {
		this.classElement = classElement;
	}
	
	public void parseAllMethods() {
//		for(Element methodElement : classElement.getEnclosedElements()) {
//			
//			for(AnnotationEnum annotation : Utils.unpackMethodAnnotations(classElement)) {
//				switch(annotation) {
//					case SUBCOMMAND:
//						break;
//					case SUGGESTION:
//						break;
//					default:
//						break;
//				}
//			}			
//		}
	}
	
	public void parse() {
		
	}
	
	public void parseSubcommandOrDefault() {
		
	}
	
	public void parseSuggestion() {
		
	}
	
}
