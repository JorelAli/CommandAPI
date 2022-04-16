package dev.jorel.commandapi.annotations;

import javax.lang.model.element.TypeElement;

import dev.jorel.commandapi.annotations.annotations.Command;

public class AnnotationValidator {

	/**
	 * Return true if everything is all good. False if anything is bad
	 * @param commandAnnotation
	 * @param logging
	 * @return
	 */
	public static boolean validateCommand(TypeElement typeElement, Command commandAnnotation, Logging logging) {
		if(commandAnnotation.value().length == 0) {
			logging.complain(typeElement, "@Command annotation must have at least one value");
			return false;
		}
		
		for(String value : commandAnnotation.value()) {
			if(value.isBlank()) {
				logging.complain(typeElement, "@Command annotation value cannot be blank");
				return false;
			}
		}
		
		return true;
	}

}
