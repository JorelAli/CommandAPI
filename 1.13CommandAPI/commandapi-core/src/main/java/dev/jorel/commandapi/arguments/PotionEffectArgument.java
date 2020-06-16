package dev.jorel.commandapi.arguments;

import org.bukkit.potion.PotionEffectType;

import dev.jorel.commandapi.CommandAPIHandler;

public class PotionEffectArgument extends Argument {

	/**
	 * A PotionEffect argument. Represents status/potion effects
	 */
	public PotionEffectArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentMobEffect());
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
