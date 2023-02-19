# Biome arguments

![A biome argument suggesting a list of Minecraft biomes](./images/arguments/biome.png)

In Minecraft 1.16, they added the ability to refer to in-game biomes. The CommandAPI implements this using the `BiomeArgument`. As expected, this returns Bukkit's `Biome` enum when used.

<div class="example">

### Example - Setting the biome of a chunk

Say you want to set the biome of the current chunk that a player is in. We can do this using the `World.setBiome(x, y, z, biome)` method for a given world. We will use this command syntax to set the biome of our current chunk:

```mccmd
/setbiome <biome>
```

And we can set the biome of the current chunk as expected:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentBiome1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentBiome1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentBiome1}}
```

</div>

</div>

The `BiomeArgument` also supports returning a `NamespacedKey` for custom biomes. This can be done by using the `BiomeArgument.NamespacedKey` constructor instead of the normal `BiomeArgument` constructor:

```java
// Makes a BiomeArgument that returns a Biome
new BiomeArgument("biome");

// Makes a BiomeArgument that returns a NamespacedKey
new BiomeArgument.NamespacedKey("biome");
```

> **Developer's Note:**
>
> Spigot's support for custom biomes is really limited! If you have an example that lets you use custom biomes with namespaced keys, please open a GitHub issue, or reach out to us on Discord!
