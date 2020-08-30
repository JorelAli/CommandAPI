package dev.jorel.commandapi;

import java.util.Arrays;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.Message;

public class NativeTooltip {

	private final String suggestion;
	private final Message tooltip;
	
	public static NativeTooltip empty(String suggestion) {
		return new NativeTooltip(suggestion, null);
	}
	
	public static NativeTooltip of(String suggestion, String tooltip) {
		return new NativeTooltip(suggestion, new LiteralMessage(tooltip));
	}
	
	private NativeTooltip(String suggestion, Message tooltip) {
		this.suggestion = suggestion;
		this.tooltip = tooltip;
	}
	
	
	public String getSuggestion() {
		return this.suggestion;
	}
	
	public Message getTooltip() {
		return this.tooltip;
	}
	
	public static NativeTooltip[] fromSuggestions(String[] suggestions) {
		return Arrays.stream(suggestions).map(NativeTooltip::empty).toArray(NativeTooltip[]::new);
	}
	
}
