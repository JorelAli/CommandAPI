package dev.jorel.commandapi.arguments;

import org.bukkit.potion.PotionEffectType;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit PotionEffectType object
 */
public class PotionEffectArgument extends SafeOverrideableArgument<PotionEffectType> {

	/**
	 * A PotionEffect argument. Represents status/potion effects
	 * @param nodeName the name of the node for this argument
	 */
	public PotionEffectArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentMobEffect(), CommandAPIHandler.getInstance().getNMS()::convert);
	}

	@Override
	public Class<?> getPrimitiveType() {
		return PotionEffectType.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.POTION_EFFECT;
	}
}
