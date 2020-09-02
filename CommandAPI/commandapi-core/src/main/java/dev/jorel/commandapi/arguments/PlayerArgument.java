package dev.jorel.commandapi.arguments;

import org.bukkit.entity.Player;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit Player object
 */
public class PlayerArgument extends SafeOverrideableArgument<Player> {

	/**
	 * A Player argument. Produces a single player, regardless of whether @a, @p, @r or @e is used.
	 */
	public PlayerArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentProfile(), Player::getName);
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
