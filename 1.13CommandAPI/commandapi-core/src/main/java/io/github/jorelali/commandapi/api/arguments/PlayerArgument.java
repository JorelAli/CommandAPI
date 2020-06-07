package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.entity.Player;

import io.github.jorelali.commandapi.api.CommandAPIHandler;

public class PlayerArgument extends Argument {

	/**
	 * A Player argument. Produces a single player, regardless of whether @a, @p, @r or @e is used.
	 */
	public PlayerArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentProfile());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return Player.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.PLAYER;
	}
}
