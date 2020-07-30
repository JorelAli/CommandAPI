package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Objective;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the name of a scoreboard objective
 */
public class ObjectiveArgument extends Argument implements ISafeOverrideableSuggestions<Objective> {

	/**
	 * An Objective argument. Represents a scoreboard objective
	 */
	public ObjectiveArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentScoreboardObjective());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return String.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.OBJECTIVE;
	}

	@Override
	public Argument safeOverrideSuggestions(Objective... suggestions) {
		return super.overrideSuggestions(sMap0(Objective::getName, suggestions));
	}

	@Override
	public Argument safeOverrideSuggestions(Function<CommandSender, Objective[]> suggestions) {
		return super.overrideSuggestions(sMap1(Objective::getName, suggestions));
	}

	@Override
	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], Objective[]> suggestions) {
		return super.overrideSuggestions(sMap2(Objective::getName, suggestions));
	}
}
