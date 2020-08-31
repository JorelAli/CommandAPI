package dev.jorel.commandapi;

import java.util.Arrays;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.Message;

public class StringTooltip {

	private final String suggestion;
	private final Message tooltip;
	
	public static StringTooltip none(String suggestion) {
		return new StringTooltip(suggestion, null);
	}
	
	public static StringTooltip of(String suggestion, String tooltip) {
		return tooltip == null ? none(suggestion) : new StringTooltip(suggestion, new LiteralMessage(tooltip));
	}
	
	private StringTooltip(String suggestion, Message tooltip) {
		this.suggestion = suggestion;
		this.tooltip = tooltip;
	}
	
	public static StringTooltip[] arrayOf(StringTooltip... tooltips) {
		return tooltips;
	}
	
	public String getSuggestion() {
		return this.suggestion;
	}
	
	public Message getTooltip() {
		return this.tooltip;
	}
	
	public static StringTooltip[] fromSuggestions(String[] suggestions) {
		return Arrays.stream(suggestions).map(StringTooltip::none).toArray(StringTooltip[]::new);
	}
	
}
