package dev.jorel.commandapi.arguments;

import java.util.function.Function;

import org.bukkit.block.Biome;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit Biome object
 */
public class BiomeArgument extends SafeOverrideableArgument<Biome> implements ICustomProvidedArgument {
	
	/**
	 * Constructs a BiomeArgument with a given node name.
	 * @param nodeName the name of the node for argument
	 */
	public BiomeArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentMinecraftKeyRegistered(),((Function<Biome, String>) Biome::name).andThen(String::toLowerCase));
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
