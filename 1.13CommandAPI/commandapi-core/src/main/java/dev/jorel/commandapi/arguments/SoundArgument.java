package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit Sound object
 */
public class SoundArgument extends Argument implements ICustomProvidedArgument, ISafeOverrideableSuggestions<Sound> {
	
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

	@Override
	public Argument safeOverrideSuggestions(Sound... suggestions) {
		return super.overrideSuggestions(sMap0(CommandAPIHandler.getNMS()::convert, suggestions));
	}

	@Override
	public Argument safeOverrideSuggestions(Function<CommandSender, Sound[]> suggestions) {
		return super.overrideSuggestions(sMap1(CommandAPIHandler.getNMS()::convert, suggestions));
	}

	@Override
	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], Sound[]> suggestions) {
		return super.overrideSuggestions(sMap2(CommandAPIHandler.getNMS()::convert, suggestions));
	}
}
