package dev.jorel.commandapi.arguments;

import org.bukkit.Keyed;
import org.bukkit.loot.LootTable;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit LootTable object
 */
public class LootTableArgument extends SafeOverrideableArgument<LootTable> implements ICustomProvidedArgument {
	
	public LootTableArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getNMS()._ArgumentMinecraftKeyRegistered(), fromKey(Keyed::getKey));
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
