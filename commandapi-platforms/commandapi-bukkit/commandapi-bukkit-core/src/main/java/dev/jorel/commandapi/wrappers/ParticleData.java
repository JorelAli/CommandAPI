package dev.jorel.commandapi.wrappers;

import org.bukkit.Particle;

/**
 * A data structure that stores a particle and its corresponding data (or null
 * if no data is present)
 */
public record ParticleData<T> (
		/**
		 * The particle that this data structure is storing.
		 */
		Particle particle,
		/**
		 * The data that this particle contains, or null if no data is present. This can
		 * be passed to {@link org.bukkit.World#spawnParticle}
		 */
		T data) {
}
