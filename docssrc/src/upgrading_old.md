# Upgrading guide

## From 8.6.0 to 8.7.0

### Sound arguments

This update introduces a backwards-incompatible change to the `SoundArgument` which was introduced in 8.6.0. SoundArguments no longer need a generic type parameter:

<div class="multi-pre">

```java,8.6.0
new SoundArgument<Sound>("sound")
```

</div>

$$\downarrow$$

<div class="multi-pre">

```java,8.7.0
new SoundArgument("sound")
```

</div>

`NamespacedKey` SoundArguments no longer need a `SoundType` parameter, instead the `NamespacedKey` constructor should be used:

<div class="multi-pre">

```java,8.6.0
new SoundArgument<NamespacedKey>("sound", SoundType.NAMESPACED_KEY)
```

</div>

$$\downarrow$$

<div class="multi-pre">

```java,8.7.0
new SoundArgument.NamespacedKey("sound")
```

</div>

### Entity selector arguments

Entity selector arguments no longer need a generic type parameter or a `EntitySelector` parameter. Instead, the corresponding constructor should be used instead:

<div class="multi-pre">

```java,8.6.0
new EntitySelectorArgument<Player>("target", EntitySelector.ONE_PLAYER)
new EntitySelectorArgument<Collection<Player>>("target", EntitySelector.MANY_PLAYERS)

new EntitySelectorArgument<Entity>("target", EntitySelector.ONE_ENTITY)
new EntitySelectorArgument<Collection<Entity>>("target", EntitySelector.MANY_ENTITIES)
```

</div>

$$\downarrow$$

<div class="multi-pre">

```java,8.7.0
new EntitySelectorArgument.OnePlayer("target")
new EntitySelectorArgument.ManyPlayers("target")

new EntitySelectorArgument.OneEntity("target")
new EntitySelectorArgument.ManyEntities("target")
```

</div>

### Scoreholder arguments

The `ScoreHolderArgument` no longer needs a generic type parameter or a `ScoreHolderType` parameter. Instead, the corresponding constructor should be used instead:

<div class="multi-pre">

```java,8.6.0
new ScoreHolderArgument<String>(nodeName, ScoreHolderType.SINGLE);
new ScoreHolderArgument<Collection<String>>(nodeName, ScoreHolderType.MULTIPLE);
```

</div>

$$\downarrow$$

<div class="multi-pre">

```java,8.7.0
new ScoreHolderArgument.Single(nodeName);
new ScoreHolderArgument.Multiple(nodeName);
```

</div>

-----

## From 8.5.1 to 8.6.0

### Sound arguments

In 8.6.0, the `SoundArgument` now supports returning a `Sound` or `NamespacedKey` object. More information on how to use this can be found in the [Sound arguments](./argument_sound.md) page. `SoundArgument` objects now require a generic type parameter to specify what the return type will be (either `Sound` or `NamespacedKey`). For the default behaviour, use `Sound` as the generic type parameter:

```java
new SoundArgument("sound")
```

$$\downarrow$$

```java
new SoundArgument<Sound>("sound")
```

### Tooltips

In 8.6.0, tooltips have been reworked to provide support for Spigot's `BaseComponent[]`s, and Adventure's `Component`s. As a result, the default method `StringTooltip.of()` and `Tooltip.of()` have been deprecated in favour of the better named `StringTooltip.ofString()` and `Tooltip.ofString()` methods:

```java
StringTooltip.of("wave", "Waves at a player")

Tooltip.of(player.getWorld().getSpawnLocation(), "World spawn")
```

$$\downarrow$$

```java
StringTooltip.ofString("wave", "Waves at a player")

Tooltip.ofString(player.getWorld().getSpawnLocation(), "World spawn")
```

Additionally, the `IStringTooltip` interface's `getTooltip` method was changed to return a Brigadier `Message` object instead of a `String`. To use the `IStringTooltip` directly, you now have to add Brigadier to your project's dependencies (info on how to do that can be found [here](https://github.com/Mojang/brigadier#installation)).

You can use the `Tooltip.messageFromString(String)` to easily upgrade to the new `Message` return type:

```java
@Override
public String getTooltip() {
    return this.itemstack.getItemMeta().getLore().get(0);
}
```

$$\downarrow$$

```java
@Override
public Message getTooltip() {
    return Tooltip.messageFromString(this.itemstack.getItemMeta().getLore().get(0));
}
```

### Command failures

To support Spigot's `BaseComponent[]`s and Adventure's `Component`s, the `CommandAPI.fail()` method has now been deprecated in favour of the better named `CommandAPI.failWithString()` method:

```java
throw CommandAPI.fail("Error message");
```

$$\downarrow$$

```java
throw CommandAPI.failWithString("Error message");
```

### List arguments

List arguments can now be implemented using an underlying text argument, instead of requiring it to be a greedy string. This allows you to use multiple lists in a command, in any position. As such, the `ListArgumentBuilder.build()` method has been deprecated and replaced with `ListArgumentBuilder.buildGreedy()` instead:

```java
new ListArgumentBuilder<Material>("materials")
    .withList(List.of(Material.values()))
    .withMapper(material -> material.name().toLowerCase())
    .build();
```

$$\downarrow$$

```java
new ListArgumentBuilder<Material>("materials")
    .withList(List.of(Material.values()))
    .withMapper(material -> material.name().toLowerCase())
    .buildGreedy();
```

-----

## From 8.5.0 to 8.5.1

### Brigadier arguments

In 8.5.1, the methods `Brigadier.fromArgument` and `Brigadier.toSuggestions` were changed to use `Argument` based parameters instead of `String` based parameters. Instead of providing the node name, you now have to provide the whole argument:

```java
Argument<?> myArgument = new StringArgument("myargument");
List<Argument<?>> argumentList = List.of(myArgument);

Brigadier.fromArgument(argumentList, "myargument");
Brigadier.toSuggestions("myargument", argumentList);
```

$$\downarrow$$

```java
Argument<?> myArgument = new StringArgument("myargument");
List<Argument<?>> argumentList = List.of(myArgument);

Brigadier.fromArgument(argumentList, myArgument);
Brigadier.toSuggestions(myArgument, argumentList);
```

-----

## From 8.3.1 to 8.4.0

### Getting a list of registered commands

In 8.2.1, the CommandAPI exposed `CommandAPIHandler.getInstance().registeredCommands` to get a list of registered commands. This has now been changed and properly implemented as a getter method which can be accessed from `CommandAPI`:

```java
CommandAPIHandler.getInstance().registeredCommands
```

$$\downarrow$$

```java
CommandAPI.getRegisteredCommands()
```

### Entity selector arguments

The import for `EntitySelector` for the `EntitySelectorArgument` has moved to improve CommandAPI shading support with jar minimization:

```java
dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector
```

$$\downarrow$$

```java
dev.jorel.commandapi.arguments.EntitySelector
```

### Custom arguments

Custom arguments are no longer restricted to a string-based argument or a keyed-based argument and can now be implemented over any existing argument "base". This argument is now parameterized over two types: the first type being the return type of this custom argument and the second type being the return type of the "base" argument. Custom arguments should now use the new constructor that accepts an argument - more information on how to do that can be found on the [Custom arguments page](./argument_custom.md). It's recommended to review your implementation of custom arguments and upgrade them if you feel that you need a more powerful argument parser (for example, you might want to use a greedy string argument as the base argument instead of a string argument).

Custom arguments that are _not_ keyed can be drop-in replaced with a `StringArgument`:

```java
new CustomArgument<T>("nodename", inputInfo -> {
    // Code here
    return T;
});
```

$$\downarrow$$

```java
new CustomArgument<T, String>(new StringArgument("nodename"), inputInfo -> {
    // Code here
    return T;
});
```

Keyed custom arguments can be drop-in replaced with a `NamespacedKeyArgument`:

```java
new CustomArgument<T>("nodename", inputInfo -> {
    // Code here
    return T;
}, true);
```

$$\downarrow$$

```java
new CustomArgument<T, NamespacedKey>(new NamespacedKeyArgument("nodename"), inputInfo -> {
    // Code here
    return T;
});
```

### NBT arguments

NBT arguments now have a different implementation if you're using the plugin version of the CommandAPI or shading the CommandAPI.

NBTCompoundArguments are now parameterized over their implemented NBTCompound implementation. For the NBT API, this means:

```java
new NBTCompoundArgument("nbt");
```

$$\downarrow$$

```java
new NBTCompoundArgument<NBTContainer>("nbt");
```

#### If you're using the plugin version of the CommandAPI

You no longer have to include the NBT API separately, the CommandAPI comes with the NBT API built-in:

```java
de.tr7zw.nbtapi.NBTContainer
```

$$\downarrow$$

```java
dev.jorel.commandapi.nbtapi.NBTContainer
```

#### If you're shading the CommandAPI

You now need to shade the NBT API into your plugin (as well as the CommandAPI). So the CommandAPI knows how to use the underlying implementation of the NBT API, you have to configure it using the `CommandAPIConfig.initializeNBTAPI()` method in `CommandAPI.onLoad()`. More information on how to do that can be found on the [NBT arguments page, under Shading usage setup](./argument_nbt.md#shading-usage-setup).

-----

## From 8.0.0 or earlier to 8.1.0

Arguments are now parameterized over a generic type. This does very little in terms of the running of the CommandAPI, but does ensure type safety with its internals. Instances of the `Argument` type now have to be parameterized. In general, this basically means:

```java
Argument myArgument = new GreedyStringArgument("arg");
```

$$\downarrow$$

```java
Argument<?> myArgument = new GreedyStringArgument("arg");
```

Arguments that have multiple return types also need to be parameterized over their return type. This includes:

- `CustomArgument`
- `EntitySelectorArgument`
- `ScoreholderArgument`

For example:

```java
new EntitySelectorArgument("target", EntitySelector.ONE_PLAYER);
```

$$\downarrow$$

```java
new EntitySelectorArgument<Player>("target", EntitySelector.ONE_PLAYER);
```

-----

## From version 7.0.0 to 8.0.0

### Particle arguments

Particle arguments no longer return Bukkit's `org.bukkit.Particle` enum, but now return a wrapper `dev.jorel.commandapi.wrappers.ParticleData` instead. More information about this wrapper class and how to use it can be found on the [particle arguments page](./argument_particle.md). To update, change any `Particle` casts into a `ParticleData` cast instead:

```java
new CommandAPICommand("mycommand")
    .withArgument(new ParticleArgument("particle"))
    .executes((sender, args) -> {
        Particle particle = (Particle) args[0];
        // Do stuff with particle
    })
    .register();
```

$$\downarrow$$

```java
new CommandAPICommand("mycommand")
    .withArgument(new ParticleArgument("particle"))
    .executes((sender, args) -> {
        ParticleData particleData = (ParticleData) args[0];

        Particle particle = particleData.particle();
        Object data = particleData.data();

        // Do stuff with particle and data
    })
    .register();
```

-----

## From version 6.5.2 to 7.0.0

### Maven repository

The Maven repository used to serve the CommandAPI has changed from JitPack.io to Maven Central. For Maven projects, you no longer require wan explicit `<repository>` entry for the CommandAPI. for Gradle projects, you need to ensure `mavenCentral()` in present in your `repositories` section.

**The group ID has changed from `dev.jorel.CommandAPI` to `dev.jorel`**

More information about setting up your development environment can be found in [Setting up your development environment](./setup_dev.md).

### CommandAPI command failures

The `CommandAPI.fail()` no longer automatically throws the exception that it creates, and instead now requires you to manually throw the exception yourself. This improves upon invalid states in command executors and allows invalid states to be identified more easily at compile time. To update, simply add the `throw` keyword before you call `CommandAPI.fail()`:

```java
new CommandAPICommand("mycommand")
    .executes((sender, args) -> {
        if(!sender.hasPermission("some.permission")) {
            CommandAPI.fail("You don't have permission to run /mycommand!");
            return;
        }
        sender.sendMessage("Hello!");
    })
```

$$\downarrow$$

```java
new CommandAPICommand("mycommand")
    .executes((sender, args) -> {
        if(!sender.hasPermission("some.permission")) {
            throw CommandAPI.fail("You don't have permission to run /mycommand!");
        }
        sender.sendMessage("Hello!");
    })
```

### Suggestions

Suggestions have been overhauled and no longer take in a `Function<SuggestionsInfo, String[]>` anymore. Instead, they now take in a `ArgumentSuggestions` object which represents argument suggestions (and whether they are executed asynchronously or have tooltips).

#### Normal (string) suggestions

These normal suggestions methods have been replaced with an `ArgumentSuggestions` parameter instead of a function:

```java
Argument replaceSuggestions(Function<SuggestionInfo, String[]> suggestions);
Argument includeSuggestions(Function<SuggestionInfo, String[]> suggestions);
```

$$\downarrow$$

```java
Argument replaceSuggestions(ArgumentSuggestions suggestions);
Argument includeSuggestions(ArgumentSuggestions suggestions);
```

The same functionality can be reproduced by wrapping your existing functions in `ArgumentSuggestions.strings`:

```java
List<Argument> arguments = new ArrayList<>();
arguments.add(new StringArgument("world").replaceSuggestions(info -> 
    new String[] {"northland", "eastland", "southland", "westland" }
));
```

$$\downarrow$$

```java
List<Argument> arguments = new ArrayList<>();
arguments.add(new StringArgument("world").replaceSuggestions(ArgumentSuggestions.strings(info -> 
    new String[] {"northland", "eastland", "southland", "westland" }
)));
```

#### Normal (strings with tooltips) suggestions

The `...T()` methods have been replaced with the normal methods above, and can use the `ArgumentSuggestions.stringsWithTooltips` method:

```java
Argument replaceSuggestionsT(Function<SuggestionInfo, IStringTooltip[]> suggestions);
Argument includeSuggestionsT(Function<SuggestionInfo, IStringTooltip[]> suggestions);
```

$$\downarrow$$

```java
Argument replaceSuggestions(ArgumentSuggestions suggestions);
Argument includeSuggestions(ArgumentSuggestions suggestions);
```

For example:

```java
List<Argument> arguments = new ArrayList<>();
arguments.add(new StringArgument("emote")
    .replaceSuggestionsT( info -> new IStringTooltip[] {
            StringTooltip.of("wave", "Waves at a player"),
            StringTooltip.of("hug", "Gives a player a hug"),
            StringTooltip.of("glare", "Gives a player the death glare")
        }
    )
);
```

$$\downarrow$$

```java
List<Argument> arguments = new ArrayList<>();
arguments.add(new StringArgument("emote")
    .replaceSuggestions(ArgumentSuggestions.stringsWithTooltips(info -> new IStringTooltip[] {
            StringTooltip.of("wave", "Waves at a player"),
            StringTooltip.of("hug", "Gives a player a hug"),
            StringTooltip.of("glare", "Gives a player the death glare")
        }
    ))
);
```

#### Safe suggestions

Similar to above with normal suggestions, safe suggestions have been replaced with `replaceSafeSuggestions` and `includeSafeSuggestions` respectively:

```java
Argument replaceWithSafeSuggestions(Function<SuggestionInfo, S[]> suggestions);
Argument includeWithSafeSuggestions(Function<SuggestionInfo, S[]> suggestions);
```

$$\downarrow$$

```java
Argument replaceSafeSuggestions(SafeSuggestions<T> suggestions);
Argument includeSafeSuggestions(SafeSuggestions<T> suggestions);
```

These can be used with the `SafeSuggestions.suggest` and `SafeSuggestions.tooltips` methods to wrap existing functions. For example:

```java
List<Argument> arguments = new ArrayList<>();
arguments.add(new RecipeArgument("recipe").replaceWithSafeSuggestions(info -> 
    new Recipe[] { emeraldSwordRecipe, /* Other recipes here */ }
));
```

$$\downarrow$$

```java
List<Argument> arguments = new ArrayList<>();
arguments.add(new RecipeArgument("recipe").replaceSafeSuggestions(SafeSuggestions.suggest(info -> 
    new Recipe[] { emeraldSwordRecipe, /* Other recipes here */ }
)));
```

-----

## From version 6.2.0 or earlier to 6.3.0

Please refer to an older version of the documentation. This has been omitted to save space and reduce confusion in this upgrading section.
