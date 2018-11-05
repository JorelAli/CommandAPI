package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.Particle;

import io.github.jorelali.commandapi.api.SemiReflector;

@SuppressWarnings("unchecked")
public class ParticleArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * A Particle argument. Represents Minecraft particles
	 */
	public ParticleArgument() {
		rawType = SemiReflector.getNMSArgumentInstance("ArgumentParticle");
	}
	
	@Override
	public <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType() {
		return (com.mojang.brigadier.arguments.ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) Particle.class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
}
