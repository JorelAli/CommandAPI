package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.potion.PotionEffectType;

import io.github.jorelali.commandapi.api.CommandAPIHandler;

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
