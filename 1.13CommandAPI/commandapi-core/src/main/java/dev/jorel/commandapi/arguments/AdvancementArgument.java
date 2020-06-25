package dev.jorel.commandapi.arguments;

import org.bukkit.advancement.Advancement;

import dev.jorel.commandapi.CommandAPIHandler;

public class AdvancementArgument extends Argument implements ICustomProvidedArgument {
	
	public AdvancementArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentMinecraftKeyRegistered());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return Advancement.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.ADVANCEMENT;
	}

	@Override
	public SuggestionProviders getSuggestionProvider() {
		return SuggestionProviders.ADVANCEMENTS;
	}
}
