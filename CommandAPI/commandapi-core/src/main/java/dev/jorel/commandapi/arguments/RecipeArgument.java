package dev.jorel.commandapi.arguments;

import org.bukkit.Keyed;
import org.bukkit.inventory.Recipe;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit Recipe object
 */
public class RecipeArgument extends SafeOverrideableArgument<Recipe> implements ICustomProvidedArgument {

	/**
	 * A Recipe argument. Represents a Bukkit Recipe or ComplexRecipe
	 * @param nodeName the name of the node for this argument
	 */
	public RecipeArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentMinecraftKeyRegistered(), fromKey((Recipe r) -> ((Keyed) r).getKey()));
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
