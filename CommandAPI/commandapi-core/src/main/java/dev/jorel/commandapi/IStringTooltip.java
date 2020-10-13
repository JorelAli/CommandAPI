package dev.jorel.commandapi;

/**
 * A string-based tooltip interface that includes a string suggestion and a
 * string tooltip text to display when hovering over the suggestion
 */
public interface IStringTooltip {
	
	/**
	 * Returns the suggestion that this object contains
	 * @return the suggestion that this object contains
	 */
	public String getSuggestion();
	
	/**
	 * Returns the tooltip text that this object contains
	 * @return the tooltip text that this object contains
	 */
	public String getTooltip();
}
