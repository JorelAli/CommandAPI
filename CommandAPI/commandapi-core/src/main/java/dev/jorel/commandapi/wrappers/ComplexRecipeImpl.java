package dev.jorel.commandapi.wrappers;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ComplexRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

public record ComplexRecipeImpl(NamespacedKey key, Recipe recipe) implements ComplexRecipe {

	@Override
	public @NotNull ItemStack getResult() {
		return recipe.getResult();
	}

	@Override
	public @NotNull NamespacedKey getKey() {
		return key;
	}

}
