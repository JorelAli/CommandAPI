# Potion effect arguments

![](./images/arguments/potion.png)

The `PotionEffectArgument` class represents Minecraft potion effects. When used, this argument is casted to Bukkit's `PotionEffectType` class.

<div class="example">

### Example - Giving a player a potion effect

Say we wanted to have a command that gives a player a potion effect. For this command, we'll use the following syntax:

```mccmd
/potion <target> <potion> <duration> <strength>
```

In this example, we utilize some of the other arguments that we've described earlier, such as the `PlayerArgument` and `TimeArgument`. Since duration for the `PotionEffect` constructor is in ticks, this is perfectly fit for the `TimeArgument`, which is represented in ticks.

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:potioneffectarguments}}
```

</div>