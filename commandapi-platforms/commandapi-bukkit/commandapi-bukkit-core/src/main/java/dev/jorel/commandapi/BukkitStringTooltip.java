package dev.jorel.commandapi;

import com.mojang.brigadier.Message;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.Collection;
import java.util.function.Function;

public class BukkitStringTooltip extends StringTooltip {
	protected BukkitStringTooltip(String suggestion, Message tooltip) {
		super(suggestion, tooltip);
	}

	/**
	 * Constructs a StringTooltip with a suggestion and a formatted bungee text component tooltip
	 *
	 * @param suggestion the suggestion to provide to the user
	 * @param tooltip    the formatted tooltip to show to the user when they hover over the
	 *                   suggestion
	 * @return a StringTooltip representing this suggestion and tooltip
	 */
	public static StringTooltip ofBaseComponents(String suggestion, BaseComponent... tooltip) {
		return ofMessage(suggestion, BukkitTooltip.messageFromBaseComponents(tooltip));
	}

	/**
	 * Constructs a StringTooltip with a suggestion and a formatted adventure text component tooltip
	 *
	 * @param suggestion the suggestion to provide to the user
	 * @param tooltip    the formatted tooltip to show to the user when they hover over the
	 *                   suggestion
	 * @return a StringTooltip representing this suggestion and tooltip
	 */

	public static StringTooltip ofAdventureComponent(String suggestion, Component tooltip) {
		return ofMessage(suggestion, BukkitTooltip.messageFromAdventureComponent(tooltip));
	}
	/**
	 * Constructs a collection of {@link StringTooltip} objects from an array of suggestions, and a function which generates
	 * a formatted tooltip for each suggestion
	 *
	 * @param tooltipGenerator function which returns a formatted tooltip for the suggestion, an array of {@link BaseComponent}s
	 * @param suggestions array of suggestions to provide to the user
	 *
	 * @return a collection of {@link StringTooltip} objects from the provided suggestions, with the generated formatted
	 * 	tooltips
	 */
	public static Collection<StringTooltip> generateBaseComponents(Function<String, BaseComponent[]> tooltipGenerator, String... suggestions) {
		return generate(tooltipGenerator, BukkitStringTooltip::ofBaseComponents, suggestions);
	}

	/**
	 * Constructs a collection of {@link StringTooltip} objects from a collection of suggestions, and a function which generates
	 * a formatted tooltip for each suggestion
	 *
	 * @param tooltipGenerator function which returns a formatted tooltip for the suggestion, an array of {@link BaseComponent}s
	 * @param suggestions collection of suggestions to provide to the user
	 *
	 * @return a collection of {@link StringTooltip} objects from the provided suggestions, with the generated formatted
	 * 	tooltips
	 */
	public static Collection<StringTooltip> generateBaseComponents(Function<String, BaseComponent[]> tooltipGenerator, Collection<String> suggestions) {
		return generate(tooltipGenerator, BukkitStringTooltip::ofBaseComponents, suggestions);
	}

	/**
	 * Constructs a collection of {@link StringTooltip} objects from an array of suggestions, and a function which generates
	 * a tooltip formatted as an adventure {@link Component} for each suggestion
	 *
	 * @param tooltipGenerator function which returns a formatted tooltip for the suggestion, an adventure {@link Component}
	 * @param suggestions array of suggestions to provide to the user
	 *
	 * @return a collection of {@link StringTooltip} objects from the provided suggestions, with the generated formatted
	 * 	tooltips
	 */
	public static Collection<StringTooltip> generateAdventureComponents(Function<String, Component> tooltipGenerator, String... suggestions) {
		return generate(tooltipGenerator, BukkitStringTooltip::ofAdventureComponent, suggestions);
	}

	/**
	 * Constructs a collection of {@link StringTooltip} objects from a collection of suggestions, and a function which generates
	 * a tooltip formatted as an adventure {@link Component} for each suggestion
	 *
	 * @param tooltipGenerator function which returns a formatted tooltip for the suggestion, an adventure {@link Component}
	 * @param suggestions collection of suggestions to provide to the user
	 *
	 * @return a collection of {@link StringTooltip} objects from the provided suggestions, with the generated formatted
	 * 	tooltips
	 */
	public static Collection<StringTooltip> generateAdventureComponents(Function<String, Component> tooltipGenerator, Collection<String> suggestions) {
		return generate(tooltipGenerator, BukkitStringTooltip::ofAdventureComponent, suggestions);
	}
}
