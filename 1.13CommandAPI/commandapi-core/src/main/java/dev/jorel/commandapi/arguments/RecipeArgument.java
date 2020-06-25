package dev.jorel.commandapi.arguments;

import org.bukkit.inventory.Recipe;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit Recipe object
 */
public class RecipeArgument extends Argument implements ICustomProvidedArgument {

	public RecipeArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentMinecraftKeyRegistered());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return Recipe.class;
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
