package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.Particle;

import io.github.jorelali.commandapi.api.CommandAPIHandler;

public class ParticleArgument extends Argument {

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
}
