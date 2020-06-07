package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.enchantments.Enchantment;

import io.github.jorelali.commandapi.api.CommandAPIHandler;

public class EnchantmentArgument extends Argument {
	
	/**
	 * An Enchantment argument. Represents an enchantment for items 
	 */
	public EnchantmentArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentEnchantment());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return Enchantment.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.ENCHANTMENT;
	}
}
