package dev.jorel.commandapi.arguments;

import org.bukkit.loot.LootTable;

import dev.jorel.commandapi.CommandAPIHandler;

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
