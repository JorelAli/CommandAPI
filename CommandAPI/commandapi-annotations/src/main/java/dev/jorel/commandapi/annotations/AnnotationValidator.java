package dev.jorel.commandapi.annotations;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import dev.jorel.commandapi.annotations.annotations.Command;
import dev.jorel.commandapi.annotations.annotations.NeedsOp;
import dev.jorel.commandapi.annotations.annotations.Permission;

public class AnnotationValidator {

	/**
	 * Return true if the @Command annotation is valid
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
		
		// TODO: Possibly have to check whether the command has invalid characters (e.g. whitespace)
		
		return true;
	}

	/**
	 * Return true if the variable element has suitable permissions (e.g. @Permission and @NeedsOp aren't both present).
	 * Also checks the value of @Permission to ensure it's a valid permission (e.g. no spaces?)
	 * @param element
	 * @return
	 */
	public static boolean validatePermissions(Element element, Logging logging) {
		
		NeedsOp needsOp = element.getAnnotation(NeedsOp.class);
		Permission permission = element.getAnnotation(Permission.class);
		
		if(needsOp != null ^ permission != null) {
			if(permission != null) {
				// TODO: Validate @Permission value
			}
		} else {
			logging.complain(element, "Cannot have both @NeedsOp and @Permission");
			return false;
		}
				
		return true;
	}

}
