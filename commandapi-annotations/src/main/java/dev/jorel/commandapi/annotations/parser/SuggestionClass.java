package dev.jorel.commandapi.annotations.parser;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

public record SuggestionClass(
		/**
		 * The type element
		 */
		TypeElement typeElement,

		/**
		 * In the case of SafeSuggestions, the class that it's parameterized over
		 * TODO: Why is primitive null?
		 */
		String primitive, 
		
		/**
		 * Being passed here because we need it to emit suggestions
		 */
		ProcessingEnvironment processingEnv) {

	public SuggestionClass(TypeElement typeElement, ProcessingEnvironment processingEnv) {
		this(typeElement, null, processingEnv);
	}
	
	/**
	 * Safe suggestions (SafeSuggestions) or normal suggestions
	 * (ArgumentSuggestions)?
	 */
	public boolean isSafeSuggestions() {
		return primitive != null;
	}
	
}
