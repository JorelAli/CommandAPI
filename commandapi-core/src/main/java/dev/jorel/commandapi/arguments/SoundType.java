package dev.jorel.commandapi.arguments;

import java.util.function.Function;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.arguments.SoundArgument.NamespacedKey;
import dev.jorel.commandapi.arguments.SoundArgument.Sound;

/**
 * An enum that represents the type that a {@link SoundArgument} should return
 * when used.
 * 
 * @deprecated Use {@code SoundArgument.}{@link Sound} or
 *             {@code SoundArgument.}{@link NamespacedKey}
 */
@Deprecated(forRemoval = true, since = "8.7.0")
public enum SoundType {

	/**
	 * Returns a Bukkit {@link Sound} object
	 */
	SOUND(soundOrNamespacedKey -> CommandAPIHandler.getInstance().getNMS().convert((org.bukkit.Sound) soundOrNamespacedKey)),

	/**
	 * Returns a Bukkit {@link org.bukkit.NamespacedKey} object. Can be used with
	 * {@link org.bukkit.NamespacedKey#asString} to convert it to a string to be used to play a
	 * sound.
	 */
	NAMESPACED_KEY(soundOrNamespacedKey -> ((org.bukkit.NamespacedKey) soundOrNamespacedKey).toString());

	private Function<Object, String> mapper;

	SoundType(Function<Object, String> mapper) {
		this.mapper = mapper;
	}

	Function<Object, String> getMapper() {
		return this.mapper;
	}
}
