package dev.jorel.commandapi;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.Message;

/**
 * Represents a suggestion for an argument with a hover tooltip text for that
 * suggestion. This class is to be used with normal suggestion overrides, via
 * the <code>Argument.overrideSuggestionsT</code> method.
 */
public class StringTooltip {

	private final String suggestion;
	private final Message tooltip;
	
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
		return tooltip == null ? none(suggestion) : new StringTooltip(suggestion, new LiteralMessage(tooltip));
	}
	
	private StringTooltip(String suggestion, Message tooltip) {
		this.suggestion = suggestion;
		this.tooltip = tooltip;
	}
	
	/**
	 * Constructs a StringTooltip[] from an array of StringTooltips. This method
	 * only exists for consistency between this class and the Tooltip class.
	 * 
	 * @param tooltips the tooltips to convert to a StringTooltip[]
	 * @return a StringTooltip[] from the provided StringTooltips
	 */
	public static StringTooltip[] arrayOf(StringTooltip... tooltips) {
		return tooltips;
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
	public Message getTooltip() {
		return this.tooltip;
	}
	
}
