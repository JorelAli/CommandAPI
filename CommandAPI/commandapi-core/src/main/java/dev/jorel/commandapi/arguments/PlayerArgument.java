package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit Player object
 */
public class PlayerArgument extends Argument implements ISafeOverrideableSuggestions<Player> {

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
	
	public Argument safeOverrideSuggestions(Player... suggestions) {
		return super.overrideSuggestions(sMap0(Player::getName, suggestions));
	}

	public Argument safeOverrideSuggestions(Function<CommandSender, Player[]> suggestions) {
		return super.overrideSuggestions(sMap1(Player::getName, suggestions));
	}

	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], Player[]> suggestions) {
		return super.overrideSuggestions(sMap2(Player::getName, suggestions));
	}
}
