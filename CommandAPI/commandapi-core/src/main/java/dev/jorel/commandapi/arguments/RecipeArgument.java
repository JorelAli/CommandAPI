package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.Keyed;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Recipe;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit Recipe object
 */
public class RecipeArgument extends Argument implements ICustomProvidedArgument, ISafeOverrideableSuggestions<Recipe> {

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

	@Override
	public Argument safeOverrideSuggestions(Recipe... suggestions) {
		return super.overrideSuggestions(sMap0(fromKey((Recipe r) -> ((Keyed) r).getKey()), suggestions));
	}

	@Override
	public Argument safeOverrideSuggestions(Function<CommandSender, Recipe[]> suggestions) {
		return super.overrideSuggestions(sMap1(fromKey((Recipe r) -> ((Keyed) r).getKey()), suggestions));
	}

	@Override
	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], Recipe[]> suggestions) {
		return super.overrideSuggestions(sMap2(fromKey((Recipe r) -> ((Keyed) r).getKey()), suggestions));
	}
	
	
}
