package dev.jorel.commandapi.wrappers;

import org.bukkit.Particle;

public record ParticleData<T>(Particle particle, T data) {
}
