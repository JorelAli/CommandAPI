package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.Particle;
import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit Particle object
 */
public class ParticleArgument extends Argument implements ISafeOverrideableSuggestions<Particle> {

	/**
	 * A Particle argument. Represents Minecraft particles
	 */
	public ParticleArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentParticle());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return Particle.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.PARTICLE;
	}

	@Override
	public Argument safeOverrideSuggestions(Particle... suggestions) {
		super.suggestions = sMap0(CommandAPIHandler.getNMS()::convert, suggestions);
		return this;
	}

	@Override
	public Argument safeOverrideSuggestions(Function<CommandSender, Particle[]> suggestions) {
		super.suggestions = sMap1(CommandAPIHandler.getNMS()::convert, suggestions);
		return this;
	}

	@Override
	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], Particle[]> suggestions) {
		super.suggestions = sMap2(CommandAPIHandler.getNMS()::convert, suggestions);
		return this;
	}
}
