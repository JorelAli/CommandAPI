package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.wrappers.ScoreboardSlot;

/**
 * An argument that represents the Bukkit ScoreboardSlot object
 */
public class ScoreboardSlotArgument extends Argument implements ISafeOverrideableSuggestions<ScoreboardSlot> {

	/**
	 * A Display slot argument. Represents scoreboard slots
	 */
	public ScoreboardSlotArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentScoreboardSlot());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return ScoreboardSlot.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SCOREBOARD_SLOT;
	}

	@Override
	public Argument safeOverrideSuggestions(ScoreboardSlot... suggestions) {
		return super.overrideSuggestions(sMap0(ScoreboardSlot::toString, suggestions));
	}

	@Override
	public Argument safeOverrideSuggestions(Function<CommandSender, ScoreboardSlot[]> suggestions) {
		return super.overrideSuggestions(sMap1(ScoreboardSlot::toString, suggestions));
	}

	@Override
	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], ScoreboardSlot[]> suggestions) {
		return super.overrideSuggestions(sMap2(ScoreboardSlot::toString, suggestions));
	}
}
