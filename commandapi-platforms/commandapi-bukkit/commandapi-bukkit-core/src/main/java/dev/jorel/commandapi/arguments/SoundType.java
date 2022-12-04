package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPIBukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;

import java.util.function.Function;

/**
 * An enum that represents the type that a {@link SoundArgument} should return
 * when used.
 */
public enum SoundType {

	/**
	 * Returns a Bukkit {@link Sound} object
	 */
	SOUND(soundOrNamespacedKey -> CommandAPIBukkit.get().convert((Sound) soundOrNamespacedKey)),

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
