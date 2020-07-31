package dev.jorel.commandapi.arguments;

import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents a duration of time in ticks
 */
public class UUIDArgument extends Argument implements ISafeOverrideableSuggestions<UUID> {
	
	/**
	 * A Time argument. Represents the number of in game ticks 
	 */
	public UUIDArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentUUID());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return UUID.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.UUID;
	}

	public Argument safeOverrideSuggestions(UUID... suggestions) {
		return super.overrideSuggestions(sMap0(UUID::toString, suggestions));
	}

	public Argument safeOverrideSuggestions(Function<CommandSender, UUID[]> suggestions) {
		return super.overrideSuggestions(sMap1(UUID::toString, suggestions));
	}

	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], UUID[]> suggestions) {
		return super.overrideSuggestions(sMap2(UUID::toString, suggestions));
	}
}
