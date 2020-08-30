package dev.jorel.commandapi;

import java.util.function.Function;

public class Tooltip<T> {

	private final T object;
	private final String tooltip;
	
	public Tooltip(T object, String tooltip) {
		this.object = object;
		this.tooltip = tooltip;
	}
	
	public NativeTooltip build(Function<T, String> mapper) {
		return NativeTooltip.of(mapper.apply(object), tooltip);
	}
	
}
