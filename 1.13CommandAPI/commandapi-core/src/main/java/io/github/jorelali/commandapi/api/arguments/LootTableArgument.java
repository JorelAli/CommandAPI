package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.loot.LootTable;

import io.github.jorelali.commandapi.api.CommandAPIHandler;

public class LootTableArgument extends Argument implements CustomProvidedArgument {
	
	public LootTableArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentMinecraftKeyRegistered());
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return LootTable.class;
	}

	@Override
	public SuggestionProviders getSuggestionProvider() {
		return SuggestionProviders.LOOT_TABLES;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.LOOT_TABLE;
	}
}
