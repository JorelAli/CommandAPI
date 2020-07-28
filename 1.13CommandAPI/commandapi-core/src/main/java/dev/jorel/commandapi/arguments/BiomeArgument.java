package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit Biome object
 */
public class BiomeArgument extends Argument implements ICustomProvidedArgument, ISafeOverrideableSuggestions<Biome> {
	
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

	@Override
	public Argument safeOverrideSuggestions(Biome... suggestions) {
		super.suggestions = sMap0(((Function<Biome, String>) Biome::name).andThen(String::toLowerCase), suggestions);
		return this;
	}

	@Override
	public Argument safeOverrideSuggestions(Function<CommandSender, Biome[]> suggestions) {
		super.suggestions = sMap1(((Function<Biome, String>) Biome::name).andThen(String::toLowerCase), suggestions);
		return this;
	}

	@Override
	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], Biome[]> suggestions) {
		super.suggestions = sMap2(((Function<Biome, String>) Biome::name).andThen(String::toLowerCase), suggestions);
		return this;
	}
}
