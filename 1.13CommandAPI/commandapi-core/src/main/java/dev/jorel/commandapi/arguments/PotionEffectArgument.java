package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;
import org.bukkit.potion.PotionEffectType;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit PotionEffectType object
 */
public class PotionEffectArgument extends Argument implements ISafeOverrideableSuggestions<PotionEffectType> {

	/**
	 * A PotionEffect argument. Represents status/potion effects
	 */
	public PotionEffectArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentMobEffect());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return PotionEffectType.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.POTION_EFFECT;
	}

	@Override
	public Argument safeOverrideSuggestions(PotionEffectType... suggestions) {
		return super.overrideSuggestions(sMap0(CommandAPIHandler.getNMS()::convert, suggestions));
	}

	@Override
	public Argument safeOverrideSuggestions(Function<CommandSender, PotionEffectType[]> suggestions) {
		return super.overrideSuggestions(sMap1(CommandAPIHandler.getNMS()::convert, suggestions));
	}

	@Override
	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], PotionEffectType[]> suggestions) {
		return super.overrideSuggestions(sMap2(CommandAPIHandler.getNMS()::convert, suggestions));
	}
}
