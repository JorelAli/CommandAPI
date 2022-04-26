# Particle arguments

![](./images/arguments/particle.png)

The `ParticleArgument` class represents Minecraft particles. As expected, this is casted to the CommandAPI's `ParticleData` class.

## The `ParticleData` class

The `ParticleData` class is a record that contains two values:

- `Particle particle`, which is the Bukkit enum `Particle` representation of what particle was provided
- `T data`, which represents any additional particle data which was provided.

```java
public record ParticleData<T>(Particle particle, T data);
```

The `T data` can be used in Bukkit's `World.spawnParticle(Particle particle, Location location, int count, T data)` method.

## Particle data

The particle argument requires additional data for a particle depending on what the particle is. Information about this can be found [on the Argument types page on the MinecraftWiki](https://minecraft.fandom.com/wiki/Argument_types#particle). The following particles have additional data required to display them:

| Bukkit Particle       | Arguments                                                             |
|-----------------------|-----------------------------------------------------------------------|
| BLOCK_CRACK           | `block block_id`<br>`block block_id[block_state=value]`               |
| BLOCK_MARKER          | `block_marker block_id`<br>`block_marker block_id[block_state=value]` |
| FALLING_DUST          | `falling_dust block_id`<br>`falling_dust block_id[block_state=value]` |
| REDSTONE              | `dust red green blue size`                                            |
| DUST_COLOR_TRANSITION | `dust_color_transition red1 green1 blue1 size red2 green2 blue2`      |
| ITEM_CRACK            | `item item_id`<br>`item item_id{NBT}`                                 |
| VIBRATION             | `vibration x y z ticks`                                               |

## ParticleArgument examples

<div class="example">

### Example - Show particles at a player's location

Say we wanted to have a command that displayed particles at a player's location. We will use the following command syntax:

```mccmd
/showparticle <particle>
```

With this, we can simply spawn the particle using the `World.spawnParticle(Particle, Location, int)` method:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:particlearguments}}
```

</div>

<div class="example">

### Example - Show particles with data

We can expand the example above by providing the data of the argument using the `ParticleData` record:

```mccmd
/showparticle <particle>
```

In this case, we'll use the `World.spawnParticle(Particle particle, Location location, int count, T data)` method which accepts some particle data:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:particlearguments2}}
```

</div>