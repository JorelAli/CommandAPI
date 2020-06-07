package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.Sound;

import com.mojang.brigadier.arguments.ArgumentType;

import io.github.jorelali.commandapi.api.CommandAPIHandler;
import io.github.jorelali.commandapi.api.CommandPermission;

@SuppressWarnings("unchecked")
public class SoundArgument implements Argument, CustomProvidedArgument {

	ArgumentType<?> rawType;
	
	public SoundArgument() {
		rawType = CommandAPIHandler.getNMS()._ArgumentMinecraftKeyRegistered();
	}
	
	@Override
	public <T> ArgumentType<T> getRawType() {
		return (ArgumentType<T>) rawType;
	}

	@Override
	public Class<?> getPrimitiveType() {
		return Sound.class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
	
	private CommandPermission permission = null;
	
	@Override
	public SoundArgument withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}

	@Override
	public CommandPermission getArgumentPermission() {
		return permission;
	}

	@Override
	public SuggestionProviders getSuggestionProvider() {
		return SuggestionProviders.SOUNDS;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SOUND;
	}
}
