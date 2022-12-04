package dev.jorel.commandapi.arguments;

import java.util.function.Function;

import org.bukkit.NamespacedKey;
import org.bukkit.Sound;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An enum that represents the type that a {@link SoundArgument} should return
 * when used.
 */
public enum SoundType {

	/**
	 * Returns a Bukkit {@link Sound} object
	 */
	SOUND(soundOrNamespacedKey -> CommandAPIHandler.getInstance().getNMS().convert((Sound) soundOrNamespacedKey)),

	/**
	 * Returns a Bukkit {@link NamespacedKey} object. Can be used with
	 * {@link NamespacedKey#asString} to convert it to a string to be used to play a
	 * sound.
	 */
	NAMESPACED_KEY(soundOrNamespacedKey -> ((NamespacedKey) soundOrNamespacedKey).toString());

	private Function<Object, String> mapper;
	
	SoundType(Function<Object, String> mapper) {
		this.mapper = mapper;
	}

	Function<Object, String> getMapper() {
		return this.mapper;
	}
}
