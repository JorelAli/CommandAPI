# Particle data (1.16.5 - 1.20.4)

The particle argument requires additional data for a particle depending on what the particle is. Information about this can be found [on the Argument types page on the MinecraftWiki](https://minecraft.wiki/w/Argument_types#particle). The following particles have additional data required to display them:

| Bukkit Particle         | Minecraft particle      | Arguments                                                             |
|-------------------------|-------------------------|-----------------------------------------------------------------------|
| `BLOCK_CRACK`           | `block`                 | `block block_id`<br><br>`block block_id[block_state=value]`               |
| `BLOCK_MARKER`          | `block_marker`          | `block_marker block_id`<br><br>`block_marker block_id[block_state=value]` |
| `REDSTONE`              | `dust`                  | `dust red green blue size`                                            |
| `DUST_COLOR_TRANSITION` | `dust_color_transition` | `dust_color_transition red1 green1 blue1 size red2 green2 blue2`      |
| `FALLING_DUST`          | `falling_dust`          | `falling_dust block_id`<br><br>`falling_dust block_id[block_state=value]` |
| `ITEM_CRACK`            | `item`                  | `item item_id`<br><br>`item item_id{NBT}`                                 |
| `SCULK_CHARGE`          | `sculk_charge`          | `sculk_charge angle`                                                  |
| `SHRIEK`                | `shriek`                | `shriek delay`                                                        |
| `VIBRATION`             | `vibration`             | `vibration x y z ticks`                                               |

## ParticleArgument examples

Because certain particles (in the table above) require additional data, it is not recommended to spawn a particle without its corresponding data. This can result in particles not showing due to missing requirements.

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

## Particle data implementation notes

The `vibration` particle will return a particle data of the Bukkit `Vibration` class. In the `Vibration` class, you can access the destination location using the `Vibration.getDestination()` method, which returns a `Vibration.Destination` instance. The CommandAPI will **always** return a `Vibration.Destination.BlockDestination` instance, and will never return a `Vibration.Destination.EntityDestination` instance. An example of accessing the location can be found below:

```java
ParticleData<Vibration> particleData; // The particle data you get from your argument
Location destination = ((BlockDestination) particleData.data().getDestination()).getLocation();
```
