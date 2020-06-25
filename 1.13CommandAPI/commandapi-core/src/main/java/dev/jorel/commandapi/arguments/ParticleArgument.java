package dev.jorel.commandapi.arguments;

import org.bukkit.Particle;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit Particle object
 */
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
