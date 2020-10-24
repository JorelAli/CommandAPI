package dev.jorel.commandapi.arguments;

import org.bukkit.entity.Player;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit Player object
 */
public class PlayerArgument extends SafeOverrideableArgument<Player> {

	/**
	 * A Player argument. Produces a single player, regardless of whether
	 * <code>@a</code>, <code>@p</code>, <code>@r</code> or <code>@e</code> is used.
	 * 
	 * @param nodeName the name of the node for this argument
	 */
	public PlayerArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentProfile(), Player::getName);
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
