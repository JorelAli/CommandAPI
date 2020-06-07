package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.loot.LootTable;

import io.github.jorelali.commandapi.api.CommandAPIHandler;

public class RecipeArgument extends Argument implements CustomProvidedArgument {

	public RecipeArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentMinecraftKeyRegistered());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return LootTable.class;
	}

	@Override
	public SuggestionProviders getSuggestionProvider() {
		return SuggestionProviders.RECIPES;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.RECIPE;
	}
}
