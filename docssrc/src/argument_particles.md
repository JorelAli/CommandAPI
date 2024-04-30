# Particle arguments

![A particle argument suggesting a list of Minecraft particle effects](./images/arguments/particle.png)

The `ParticleArgument` class represents Minecraft particles. This is casted to the CommandAPI's `ParticleData` class.

## The `ParticleData` class

The `ParticleData` class is a record that contains two values:

- `Particle particle`, which is the Bukkit enum `Particle` representation of what particle was provided
- `T data`, which represents any additional particle data which was provided.

```java
public record ParticleData<T>(Particle particle, T data);
```

The `T data` can be used in Bukkit's `World.spawnParticle(Particle particle, Location location, int count, T data)` method.

## Particle data

Particle data depends on your version of Minecraft. In 1.20.5, Minecraft and Spigot updated their particle API and they are no longer compatible with each other. Information about how the CommandAPI uses particle data can be found using the links below:

- [Particle data for Minecraft 1.16.5 - 1.20.4](./argument_particle_old.md)
- [Particle data for Minecraft 1.20.5+](./argument_particle_new.md)
