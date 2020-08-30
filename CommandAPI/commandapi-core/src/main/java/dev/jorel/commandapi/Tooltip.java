package dev.jorel.commandapi;

import java.util.function.Function;

public class Tooltip<T> {

	private final T object;
	private final String tooltip;
	
	private Tooltip(T object, String tooltip) {
		this.object = object;
		this.tooltip = tooltip;
	}
	
	public static <T> Tooltip<T> of(T object, String tooltip) {
		return new Tooltip<T>(object, tooltip);
	}
	
	public static <T> Tooltip<T> none(T object) {
		return new Tooltip<T>(object, null);
	}
	
	public NativeTooltip build(Function<T, String> mapper) {
		return NativeTooltip.of(mapper.apply(object), tooltip);
	}
	
}
