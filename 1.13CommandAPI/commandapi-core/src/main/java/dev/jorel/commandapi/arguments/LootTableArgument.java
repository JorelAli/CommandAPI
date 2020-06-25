package dev.jorel.commandapi.arguments;

import org.bukkit.loot.LootTable;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit LootTable object
 */
public class LootTableArgument extends Argument implements ICustomProvidedArgument {
	
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
