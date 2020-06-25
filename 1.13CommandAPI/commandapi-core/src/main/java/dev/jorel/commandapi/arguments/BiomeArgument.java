package dev.jorel.commandapi.arguments;

import org.bukkit.block.Biome;

import dev.jorel.commandapi.CommandAPIHandler;

public class BiomeArgument extends Argument implements ICustomProvidedArgument {
	
	public BiomeArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentMinecraftKeyRegistered());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return Biome.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.BIOME;
	}

	@Override
	public SuggestionProviders getSuggestionProvider() {
		return SuggestionProviders.BIOMES;
	}
}
