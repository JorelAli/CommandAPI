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

The particle argument requires additional data for a particle depending on what the particle is. Information about this can be found [on the Argument types page on the MinecraftWiki](https://minecraft.fandom.com/wiki/Argument_types#particle). The following particles have additional data required to display them:

| Bukkit Particle       | Arguments                                                             |
|-----------------------|-----------------------------------------------------------------------|
| BLOCK_CRACK           | `block block_id`<br>`block block_id[block_state=value]`               |
| BLOCK_MARKER          | `block_marker block_id`<br>`block_marker block_id[block_state=value]` |
| DUST                  | `dust red green blue size`                                            |
| DUST_COLOR_TRANSITION | `dust_color_transition red1 green1 blue1 size red2 green2 blue2`      |
| FALLING_DUST          | `falling_dust block_id`<br>`falling_dust block_id[block_state=value]` |
| ITEM_CRACK            | `item item_id`<br>`item item_id{NBT}`                                 |
| SCULK_CHARGE          | `sculk_charge angle`                                                  |
| SHRIEK                | `shriek delay`                                                        |
| VIBRATION             | `vibration x y z ticks`                                               |

## ParticleArgument examples

Because certain particles (in the table above) require additional data, it is no longer recommended to spawn a particle without its corresponding data. This can result in particles not showing due to missing requirements.

<div class="warning">

### Example - Show particles at a player's location (without data)

Say we wanted to have a command that displayed particles at a player's location. We will use the following command syntax:

```mccmd
/showparticle <particle>
```

With this, we can simply spawn the particle using the `World.spawnParticle(Particle, Location, int)` method:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentParticle1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentParticle1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentParticle1}}
```

</div>

Running this can result in errors due to missing requirements. If you provide a particle that has additional requirements, Bukkit will throw an error and the particle will not be displayed. Instead, the example below should be used.

</div>

<div class="example">

### Example - Show particles  at a player's location (with data)

We can fix the issues with the example above by providing the data of the argument using the `ParticleData` record:

```mccmd
/showparticle <particle>
```

In this case, we'll use the `World.spawnParticle(Particle particle, Location location, int count, T data)` method which accepts some particle data:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentParticle2}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentParticle2}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentParticle2}}
```

</div>

This can be used with commands such as:

```mccmd
/showparticle minecraft:dust_color_transition 0 0 0 20 1 0 0
/showparticle minecraft:block_marker diamond_block
```

</div>
