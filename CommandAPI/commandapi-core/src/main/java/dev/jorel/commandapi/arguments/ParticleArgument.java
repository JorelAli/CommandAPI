package dev.jorel.commandapi.arguments;

import org.bukkit.Particle;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit Particle object
 */
public class ParticleArgument extends SafeOverrideableArgument<Particle> {

	/**
	 * A Particle argument. Represents Minecraft particles
	 * @param nodeName the name of the node for this argument
	 */
	public ParticleArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentParticle(), CommandAPIHandler.getInstance().getNMS()::convert);
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
