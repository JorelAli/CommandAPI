package dev.jorel.commandapi.arguments;

import java.util.function.Function;

import org.bukkit.block.Biome;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit Biome object
 */
public class BiomeArgument extends SafeOverrideableArgument<Biome> implements ICustomProvidedArgument {
	
	public BiomeArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentMinecraftKeyRegistered(),((Function<Biome, String>) Biome::name).andThen(String::toLowerCase));
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
