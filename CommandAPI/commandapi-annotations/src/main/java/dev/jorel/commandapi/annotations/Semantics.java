package dev.jorel.commandapi.annotations;

import java.util.Map;
import java.util.Map.Entry;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

public class Semantics {

	Logging logging;

	public Semantics(Logging logging) {
		this.logging = logging;
	}

	public void analyze(Map<Element, Context> allContext) {

		/*
		 * Rules that must be met - Subcommands can only go on inner classes
		 */

		for (Entry<Element, Context> contextEntry : allContext.entrySet()) {
			Element classElement = contextEntry.getKey();
			Context context = contextEntry.getValue();

			if (classElement.getKind() == ElementKind.CLASS) {
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
