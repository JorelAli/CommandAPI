package dev.jorel.commandapi;

import java.util.function.Function;

public class Tooltip<S> {

	private final S object;
	private final String tooltip;
	
	private Tooltip(S object, String tooltip) {
		this.object = object;
		this.tooltip = tooltip;
	}
	
	public static <S> Tooltip<S> of(S object, String tooltip) {
		return new Tooltip<S>(object, tooltip);
	}
	
	public static <S> Tooltip<S> none(S object) {
		return new Tooltip<S>(object, null);
	}
	
	@SafeVarargs
	public static <S> Tooltip<S>[] arrayOf(Tooltip<S>... tooltips) {
		return tooltips;
	}
	
	public static <S> Function<Tooltip<S>, StringTooltip> build(Function<S, String> mapper) {
		return t -> StringTooltip.of(mapper.apply(t.object), t.tooltip);
	}
	
}
