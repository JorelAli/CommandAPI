package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.advancement.Advancement;
import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit Advancement object
 */
public class AdvancementArgument extends Argument implements ICustomProvidedArgument, ISafeOverrideableSuggestions<Advancement> {
	
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

	@Override
	public SuggestionProviders getSuggestionProvider() {
		return SuggestionProviders.ADVANCEMENTS;
	}

	@Override
	public Argument safeOverrideSuggestions(Advancement... suggestions) {
		return super.overrideSuggestions(sMap0(fromKey(Advancement::getKey), suggestions));
	}

	@Override
	public Argument safeOverrideSuggestions(Function<CommandSender, Advancement[]> suggestions) {
		return super.overrideSuggestions(sMap1(fromKey(Advancement::getKey), suggestions));
	}

	@Override
	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], Advancement[]> suggestions) {
		return super.overrideSuggestions(sMap2(fromKey(Advancement::getKey), suggestions));
	}
}
