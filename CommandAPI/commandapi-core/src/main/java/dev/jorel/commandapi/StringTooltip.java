package dev.jorel.commandapi;

/**
 * Represents a suggestion for an argument with a hover tooltip text for that
 * suggestion. This class is to be used with normal suggestion overrides, via
 * the <code>Argument.overrideSuggestionsT</code> method.
 */
public class StringTooltip implements IStringTooltip {

	private final String suggestion;
	private final String tooltip;
	
	/**
	 * Constructs a StringTooltip with a suggestion and no tooltip
	 * 
	 * @param suggestion the suggestion to provide to the user
	 * @return a StringTooltip representing this suggestion
	 */
	public static StringTooltip none(String suggestion) {
		return new StringTooltip(suggestion, null);
	}
	
	/**
	 * Constructs a StringTooltip with a suggestion and a tooltip
	 * 
	 * @param suggestion the suggestion to provide to the user
	 * @param tooltip    the tooltip to show to the user when they hover over the
	 *                   suggestion
	 * @return a StringTooltip representing this suggestion and tooltip
	 */
	public static StringTooltip of(String suggestion, String tooltip) {
		return tooltip == null ? none(suggestion) : new StringTooltip(suggestion, tooltip);
	}
	
	private StringTooltip(String suggestion, String tooltip) {
		this.suggestion = suggestion;
		this.tooltip = tooltip;
	}
	
	/**
	 * Returns the current suggestion that this class holds
	 * @return the current suggestion that this class holds
	 */
	public String getSuggestion() {
		return this.suggestion;
	}
	
	/**
	 * Returns the current tooltip text that this class holds
	 * @return the current tooltip text that this class holds
	 */
	public String getTooltip() {
		return this.tooltip;
	}
	
}
