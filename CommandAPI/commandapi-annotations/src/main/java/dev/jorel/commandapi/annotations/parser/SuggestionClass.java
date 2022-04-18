package dev.jorel.commandapi.annotations.parser;

public class SuggestionClass {

	public enum SuggestionType {
		ARGUMENT_SUGGESTIONS, SAFE_SUGGESTIONS;
	}

	// ArgumentSuggestions or SafeSuggestions<>
	private final SuggestionType type;

	// In the case of SafeSuggestions, the class that it's parameterized over
	private final String primitive;
	
	public SuggestionClass(SuggestionType type, String primitive) {
		this.type = type;
		this.primitive = primitive;
	}
	
	

}
