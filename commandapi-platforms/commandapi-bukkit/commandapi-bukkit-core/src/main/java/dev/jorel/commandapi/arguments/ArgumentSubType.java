package dev.jorel.commandapi.arguments;

import java.util.Collection;

import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * An enum that represents argument subtypes. For example, the
 * EntitySelectorArgument can be split into four arguments:
 * <ul>
 * <li>One entity</li>
 * <li>One player</li>
 * <li>Many entities</li>
 * <li>Many players</li>
 * </ul>
 * 
 * This class is used internally by the CommandAPI, you shouldn't need to use
 * this class yourself.
 */
public enum ArgumentSubType {
	/**
	 * A single entity. Returns a {@link Entity}
	 */
	ENTITYSELECTOR_ONE_ENTITY,

	/**
	 * A single player. Returns a {@link Player}
	 */
	ENTITYSELECTOR_ONE_PLAYER,

	/**
	 * Many entities. Returns a {@link Collection}{@code <}{@link Entity}{@code >}
	 */
	ENTITYSELECTOR_MANY_ENTITIES,

	/**
	 * Many players. Returns a {@link Collection}{@code <}{@link Player}{@code >}
	 */
	ENTITYSELECTOR_MANY_PLAYERS,

	/**
	 * A Biome. Returns a {@link Biome}
	 */
	BIOME_BIOME,

	/**
	 * A Biome. Returns a {@link NamespacedKey}
	 */
	BIOME_NAMESPACEDKEY,

	/**
	 * A Sound. Returns a {@link Sound}
	 */
	SOUND_SOUND,

	/**
	 * A Sound. Returns a {@link NamespacedKey}
	 */
	SOUND_NAMESPACEDKEY,

	/**
	 * A ScoreHolder. Returns a {@link String}
	 */
	SCOREHOLDER_SINGLE,

	/**
	 * Multiple scoreholders. Returns a
	 * {@link Collection}{@code <}{@link String}{@code >}
	 */
	SCOREHOLDER_MULTIPLE,

	/**
	 * A PotionEffectType. Returns a {@link org.bukkit.potion.PotionEffectType}
	 */
	POTION_EFFECT_POTION_EFFECT,

	/**
	 * A PotionEffectType. Returns a {@link org.bukkit.NamespacedKey}
	 */
	POTION_EFFECT_NAMESPACEDKEY;
}