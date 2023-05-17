package dev.jorel.commandapi.annotations;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;

import dev.jorel.commandapi.annotations.parser.Parser;

public class Semantics {

	Logging logging;

	public Semantics(Logging logging) {
		this.logging = logging;
	}

	public void analyze(LinkedHashMap<TypeElement, Parser> allContext) {

		/*
		 * Rules that must be met - Subcommands can only go on inner classes
		 */

		for (Entry<TypeElement, Parser> contextEntry : allContext.entrySet()) {
			TypeElement classElement = contextEntry.getKey();
			Parser context = contextEntry.getValue();

			if (classElement.getKind() == ElementKind.CLASS) {
				if(classElement.getNestingKind() == NestingKind.TOP_LEVEL) {
					
				}
				
				if(classElement.getEnclosingElement().getKind() == ElementKind.PACKAGE) {
					
				} else {
					logging.complain(classElement, "@Command can only go on a top level class");
					// AAAAAAAAAAAAAAAAAAA
				}
			} else {
				
			}
		}
	}

}
