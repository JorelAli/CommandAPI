package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.advancement.Advancement;

import io.github.jorelali.commandapi.api.CommandAPIHandler;

public class AdvancementArgument extends Argument {
	
	public AdvancementArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentMinecraftKeyRegistered());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return Advancement.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.ADVANCEMENT;
	}
}
