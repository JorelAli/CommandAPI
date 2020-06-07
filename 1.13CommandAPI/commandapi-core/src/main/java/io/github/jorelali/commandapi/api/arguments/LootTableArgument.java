package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.loot.LootTable;

import com.mojang.brigadier.arguments.ArgumentType;

import io.github.jorelali.commandapi.api.CommandAPIHandler;
import io.github.jorelali.commandapi.api.CommandPermission;

@SuppressWarnings("unchecked")
public class LootTableArgument implements Argument, CustomProvidedArgument {

	ArgumentType<?> rawType;
	
	public LootTableArgument() {
		rawType = CommandAPIHandler.getNMS()._ArgumentMinecraftKeyRegistered();
	}
	
	@Override
	public <T> ArgumentType<T> getRawType() {
		return (ArgumentType<T>) rawType;
	}

	@Override
	public Class<?> getPrimitiveType() {
		return LootTable.class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
	
	private CommandPermission permission = null;
	
	@Override
	public LootTableArgument withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}

	@Override
	public CommandPermission getArgumentPermission() {
		return permission;
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
