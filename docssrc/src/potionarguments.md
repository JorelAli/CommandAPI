# Potion effect arguments

![An image of a potion argument with a list of potion effect suggestions](./images/arguments/potion.png)

The `PotionEffectArgument` class represents Minecraft potion effects. When used, this argument is casted to Bukkit's `PotionEffectType` class.

<div class="example">

### Example - Giving a player a potion effect

Say we wanted to have a command that gives a player a potion effect. For this command, we'll use the following syntax:

```mccmd
/potion <target> <potion> <duration> <strength>
```

In this example, we utilize some of the other arguments that we've described earlier, such as the `PlayerArgument` and `TimeArgument`. Since duration for the `PotionEffect` constructor is in ticks, this is perfectly fit for the `TimeArgument`, which is represented in ticks.

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-core/src/test/java/Examples.java:potioneffectarguments}}
```

```kotlin,Kotlin
{{#include ../../commandapi-core/src/test/kotlin/Examples.kt:potioneffectarguments}}
```

</div>

</div>
