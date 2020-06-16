package dev.jorel.commandapi.arguments;

import org.bukkit.Sound;

import dev.jorel.commandapi.CommandAPIHandler;

public class SoundArgument extends Argument implements CustomProvidedArgument {
	
	public SoundArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentMinecraftKeyRegistered());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return Sound.class;
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
