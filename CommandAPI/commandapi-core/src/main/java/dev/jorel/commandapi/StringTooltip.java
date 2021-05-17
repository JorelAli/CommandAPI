/*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
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
