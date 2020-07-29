# Potion effect arguments

![](./images/arguments/potion.png)

The `PotionEffectArgument` class represents Minecraft potion effects. When used, this argument is casted to Bukkit's `PotionEffectType` class.

<div class="example">

### Example - Giving a player a potion effect

Say we wanted to have a command that gives a player a potion effect. For this command, we'll use the following structure:

```
/potion <target> <potion> <duration> <strength>
```

In this example, we utilize some of the other arguments that we've described earlier, such as the `PlayerArgument` and `TimeArgument`. Since duration for the `PotionEffect` constructor is in ticks, this is perfectly fit for the `TimeArgument`, which is represented in ticks.

```java
//Declare our arguments
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("target", new PlayerArgument());
arguments.put("potion", new PotionEffectArgument());
arguments.put("duration", new TimeArgument());
arguments.put("strength", new IntegerArgument());

new CommandAPICommand("potion")
    .withArguments(arguments)
    .executes((sender, args) -> {
        Player target = (Player) args[0];
        PotionEffectType potion = (PotionEffectType) args[1];
        int duration = (int) args[2];
        int strength = (int) args[3];
        
        //Add the potion effect to the target player
        target.addPotionEffect(new PotionEffect(potion, duration, strength));
    })
    .register();
```

</div>