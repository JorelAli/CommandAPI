package dev.jorel.commandapi.annotations.parser;

import javax.lang.model.element.TypeElement;

public record SuggestionClass(
		/**
		 * The type element
		 */
		TypeElement typeElement,

		/**
		 * In the case of SafeSuggestions, the class that it's parameterized over
		 */
		String primitive) {

	public SuggestionClass(TypeElement typeElement) {
		this(typeElement, null);
	}
	
	/**
	 * Safe suggestions (SafeSuggestions) or normal suggestions
	 * (ArgumentSuggestions)?
	 */
	public boolean isSafeSuggestions() {
		return primitive != null;
	}
	
}
