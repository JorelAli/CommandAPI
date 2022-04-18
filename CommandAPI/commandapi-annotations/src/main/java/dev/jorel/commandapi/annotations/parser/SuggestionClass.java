package dev.jorel.commandapi.annotations.parser;

import javax.lang.model.element.TypeElement;

public record SuggestionClass(
		/**
		 * The type element
		 */
		TypeElement typeElement,

		/**
		 * Safe suggestions (SafeSuggestions<>) or normal suggestions
		 * (ArgumentSuggestions)?
		 */
		boolean isSafeSuggestions,

		/**
		 * In the case of SafeSuggestions, the class that it's parameterized over
		 */
		String primitive) {

}
