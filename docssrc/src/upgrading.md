# Upgrading guide

## From 9.?.? to 10.0.0

### Deprecated methods

For 10.0.0, all previously deprecated methods have been removed. Please make sure you use the replacement methods for the deprecated methods. The replacement methods should be described in the JavaDocs of deprecated methods.

## From 9.2.0 to 9.3.0

The `BukkitTooltip.generateAdvenureComponents` methods have now been deprecated in favour of the correctly named `BukkitTooltip.generateAdventureComponents` methods:

<div class="multi-pre">

```java,9.2.0
BukkitTooltip.generateAdvenureComponents()
```

</div>

$$\downarrow$$

<div class="multi-pre">

```java,9.3.0
BukkitTooltip.generateAdventureComponents()
```

</div>

-----

## From 9.0.3 to 9.1.0

### MultiLiteralArgument changes

All `MultiLiteralArgument` constructors have been deprecated in 9.1.0! Instead the new `MultiLiteralArgument` constructor should be used:

<div class="multi-pre">

```java,9.0.3
withArguments(new MultiLiteralArgument("gamemodes", List.of("survival", "creative", "adventure", "spectator")))
```

</div>

$$\downarrow$$

<div class="multi-pre">

```java,9.1.0
withArguments(new MultiLiteralArgument("gamemodes", "survival", "creative", "adventure", "spectator"))
```

</div>

### CommandArguments changes

For 9.1.0 all deprecated methods are no longer deprecated. To learn about all the methods now available, refer to the [CommandArguments](./commandarguments.md) page.

-----

## From 9.0.1 to 9.0.2

### MultiLiteralArgument and LiteralArgument changes

In previous versions, the ability has been introduced to access arguments by their node names. However, while this was possible for every other argument, it wasn't possible for `MultiLiteralArgument`s. This was now changed because the values from the `MultiLiteralArgument` are included in the [`CommandArguments`](./commandarguments.md) of a command.

Therefore, the current constructor has been deprecated and the new one should be used:

<div class="multi-pre">

```java,9.0.1
withArguments(new MultiLiteralArgument("survival", "creative", "adventure", "spectator"))
```

</div>

$$\downarrow$$

<div class="multi-pre">

```java,9.0.2
withArguments(new MultiLiteralArgument("gamemodes", List.of("survival", "creative", "adventure", "spectator")))
```

</div>

Because it is possible to list `LiteralArgument`s in the [`CommandArguments`](./commandarguments.md) of a command, there was also an additional constructor add to the `LiteralArgument` class. The other one is not deprecated.

Now, the `LiteralArgument` class contains two possible constructors:

```java
public LiteralArgument(String literal) // Recommended if the literal is not listed
public LiteralArgument(String nodeName, String literal)
```

-----

## From 9.0.0 to 9.0.1

### CustomArgumentException changes

For 9.0.1, the `CustomArgumentException` constructors have been deprecated and should no longer be used. Instead, use the `CustomArgumentException` static factory methods:

<div class="multi-pre">

```java,9.0.0
throw new CustomArgumentException(new MessageBuilder(...));
throw new CustomArgumentException("Error message");
```

</div>

$$\downarrow$$

<div class="multi-pre">

```java,9.0.1
throw CustomArgumentException.fromMessageBuilder(new MessageBuilder(...));
throw CustomArgumentException.fromString("Error message");
```

</div>

### CommandArguments changes

For 9.0.1 the various `CommandArguments#getOrDefault()` and `CommandArguments#getOrDefaultUnchecked()` have been deprecated and should no longer be used. Instead, use the `CommandArguments#getOptional()` and `CommandArguments#getOptionalUnchecked()` methods:

<div class="multi-pre">

```java,9.0.0_(Not_using_unchecked)
new CommandAPICommand("mycommand")
    .withOptionalArguments(new StringArgument("string"))
    .executes((sender, args) -> {
        String string = (String) args.getOrDefault("string", "Default Value");
    })
    .register();
```

```java,9.0.0_(Using_unchecked)
new CommandAPICommand("mycommand")
    .withOptionalArguments(new StringArgument("string"))
    .executes((sender, args) -> {
        String string = args.getOrDefaultUnchecked("string", "Default Value");
    })
    .register();
```

</div>

$$\downarrow$$

<div class="multi-pre">

```java,9.0.1_(Not_using_unchecked)
new CommandAPICommand("mycommand")
    .withOptionalArguments(new StringArgument("string"))
    .executes((sender, args) -> {
        String string = (String) args.getOptional("string").orElse("Default Value");
    })
    .register();
```

```java,9.0.1_(Using_unchecked)
new CommandAPICommand("mycommand")
    .withOptionalArguments(new StringArgument("string"))
    .executes((sender, args) -> {
        String string = args.getOptionalUnchecked("string").orElse("Default Value");
    })
    .register();
```

</div>

-----

## From 8.8.x to 9.0.0

CommandAPI 9.0.0 is arguably the biggest change in the CommandAPI's project structure and usage. This update was designed to allow the CommandAPI to be generalized for other platforms (e.g. Velocity, Fabric, Sponge), and as a result **this update is incompatible with previous versions of the CommandAPI**.

All deprecated methods from 8.8.x have been removed in this update. Please ensure that you use the relevant replacement methods (these are described in the JavaDocs for the various deprecated methods) before upgrading to 9.0.0.

-----

### Project dependencies

For Bukkit/Spigot/Paper plugins, the `commandapi-core` and `commandapi-shade` modules should no longer be used. Instead, use the new `commandapi-bukkit-core` and `commandapi-bukkit-shade` modules:

<div class="linked-multi-pre">

```xml,Maven
<dependencies>
    <dependency>
        <groupId>dev.jorel</groupId>
        <artifactId>commandapi-core</artifactId>
        <version>9.0.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

```xml,Maven_(shaded)
<dependencies>
    <dependency>
        <groupId>dev.jorel</groupId>
        <artifactId>commandapi-shade</artifactId>
        <version>9.0.0</version>
    </dependency>
</dependencies>
```

```gradle,Gradle
dependencies {
    compileOnly "dev.jorel:commandapi-core:9.0.0"
}
```

```kotlin,Kotlin_Gradle
dependencies {
    compileOnly("dev.jorel:commandapi-core:9.0.0")
}
```

```gradle,Gradle_(shaded)
dependencies {
    implementation "dev.jorel:commandapi-shade:9.0.0"
}
```

```kotlin,Kotlin_Gradle_(shaded)
dependencies {
    implementation("dev.jorel:commandapi-shade:9.0.0")
}
```

$$\downarrow$$

```xml,Maven
<dependencies>
    <dependency>
        <groupId>dev.jorel</groupId>
        <artifactId>commandapi-bukkit-core</artifactId>
        <version>9.0.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

```xml,Maven_(shaded)
<dependencies>
    <dependency>
        <groupId>dev.jorel</groupId>
        <artifactId>commandapi-bukkit-shade</artifactId>
        <version>9.0.0</version>
    </dependency>
</dependencies>
```

```gradle,Gradle
dependencies {
    compileOnly "dev.jorel:commandapi-bukkit-core:9.0.0"
}
```

```kotlin,Kotlin_Gradle
dependencies {
    compileOnly("dev.jorel:commandapi-bukkit-core:9.0.0")
}
```

```gradle,Gradle_(shaded)
dependencies {
    implementation "dev.jorel:commandapi-bukkit-shade:9.0.0"
}
```

```kotlin,Kotlin_Gradle_(shaded)
dependencies {
    implementation("dev.jorel:commandapi-bukkit-shade:9.0.0")
}
```

</div>

Additionally, when using the Kotlin DSL for Bukkit, instead of using `commandapi-kotlin`, use `commandapi-bukkit-kotlin`:

<div class="linked-multi-pre">

```xml,Maven
<dependencies>
    <dependency>
        <groupId>dev.jorel</groupId>
        <artifactId>commandapi-kotlin</artifactId>
        <version>9.0.0</version>
    </dependency>
</dependencies>
```

```gradle,Gradle
dependencies {
    implementation "dev.jorel:commandapi-kotlin:9.0.0"
}
```

```kotlin,Kotlin_Gradle
dependencies {
    implementation("dev.jorel:commandapi-kotlin:9.0.0")
}
```

$$\downarrow$$

```xml,Maven
<dependencies>
    <dependency>
        <groupId>dev.jorel</groupId>
        <artifactId>commandapi-bukkit-kotlin</artifactId>
        <version>9.0.0</version>
    </dependency>
</dependencies>
```

```gradle,Gradle
dependencies {
    implementation "dev.jorel:commandapi-bukkit-kotlin:9.0.0"
}
```

```kotlin,Kotlin_Gradle
dependencies {
    implementation("dev.jorel:commandapi-bukkit-kotlin:9.0.0")
}
```

</div>

-----

### Loading and enabling the CommandAPI when shading

The `CommandAPI.onLoad()` method has changed in this update. Instead of using the `CommandAPIConfig` object, use the `CommandAPIBukkitConfig` and pass in the current plugin reference (`this`).

and `CommandAPI.onEnable()` method has also changed, and now no longer requires the plugin reference (`this`), as it is now included in `CommandAPI.onLoad()` instead.:

<div class="multi-pre">

```java,8.8.x
public void onLoad() {
    CommandAPI.onLoad(new CommandAPIConfig());
}

public void onEnable() {
    CommandAPI.onEnable(this);
}
```

</div>

$$\downarrow$$

<div class="multi-pre">

```java,9.0.0
public void onLoad() {
    CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
}

public void onEnable() {
    CommandAPI.onEnable();
}
```

</div>

-----

### Accessing arguments

Arguments for commands are no longer an `Object[]` and have now been replaced with a more powerful `CommandArguments` object. This object now lets you access arguments in a number of ways:

#### Using the `args.get(int)` method

If you're in a rush and just want to upgrade quickly, call the `.get(int)` method instead of accessing the arguments using the array access notation:

<div class="multi-pre">

```java,8.8.x
new CommandAPICommand("cmd")
    .withArguments(new StringArgument("mystring"))
    .withArguments(new PotionEffectArgument("mypotion"))
    .withArguments(new LocationArgument("mylocation"))
    .executes((sender, args) -> {
        String stringArg = (String) args[0];
        PotionEffectType potionArg = (PotionEffectType) args[1];
        Location locationArg = (Location) args[2];
    })
    .register();
```

</div>

$$\downarrow$$

<div class="multi-pre">

```java,9.0.0
new CommandAPICommand("cmd")
    .withArguments(new StringArgument("mystring"))
    .withArguments(new PotionEffectArgument("mypotion"))
    .withArguments(new LocationArgument("mylocation"))
    .executes((sender, args) -> {
        String stringArg = (String) args.get(0);
        PotionEffectType potionArg = (PotionEffectType) args.get(1);
        Location locationArg = (Location) args.get(2);
    })
    .register();
```

</div>

#### Using the `args.get(String)` method _(recommended)_

The CommandAPI introduces a new `args.get(String)` method to access arguments using the argument node name. This method also makes your code much more compatible with optional arguments:

<div class="multi-pre">

```java,8.8.x
new CommandAPICommand("cmd")
    .withArguments(new StringArgument("mystring"))
    .withArguments(new PotionEffectArgument("mypotion"))
    .withArguments(new LocationArgument("mylocation"))
    .executes((sender, args) -> {
        String stringArg = (String) args[0];
        PotionEffectType potionArg = (PotionEffectType) args[1];
        Location locationArg = (Location) args[2];
    })
    .register();
```

</div>

$$\downarrow$$

<div class="multi-pre">

```java,9.0.0
new CommandAPICommand("cmd")
    .withArguments(new StringArgument("mystring"))
    .withArguments(new PotionEffectArgument("mypotion"))
    .withArguments(new LocationArgument("mylocation"))
    .executes((sender, args) -> {
        String stringArg = (String) args.get("mystring");
        PotionEffectType potionArg = (PotionEffectType) args.get("mypotion");
        Location locationArg = (Location) args.get("mylocation");
    })
    .register();
```

</div>

-----

### `CommandAPI` helper methods

The `CommandAPI.failWithBaseComponents(message)` and `CommandAPI.failWithAdventureComponent(message)` methods have now been moved from `CommandAPI` to `CommandAPIBukkit`, because these methods are Bukkit/Spigot/Paper specific and don't exist for other platforms (e.g. Velocity, Fabric, Sponge):

<div class="multi-pre">

```java,8.8.x
CommandAPI.failWithBaseComponents(...);
CommandAPI.failWithAdventureComponent(...);
```

</div>

$$\downarrow$$

<div class="multi-pre">

```java,9.0.0
CommandAPIBukkit.failWithBaseComponents(...);
CommandAPIBukkit.failWithAdventureComponent(...);
```

</div>

-----

### Removal of the `EnvironmentArgument`

The `EnvironmentArgument` has been removed in this update, as it was implemented incorrectly and is not fit for purpose. Instead, the CommandAPI has the more accurate `WorldArgument`.

-----

### Changes to the `TeamArgument`

The `TeamArgument` has been updated to no longer use a `String` as its return type. Instead, you can now just use a `Team` object directly:

<div class="multi-pre">

```java,8.8.x
new CommandAPICommand("team")
    .withArguments(new TeamArgument("team"))
    .executes((sender, args) -> {
        String teamName = (String) args.get("team");
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
    })
    .register();
```

</div>

$$\downarrow$$

<div class="multi-pre">

```java,9.0.0
new CommandAPICommand("team")
    .withArguments(new TeamArgument("team"))
    .executes((sender, args) -> {
        Team team = (Team) args.get("team");
    })
    .register();
```

</div>

-----

### Changes to the `ObjectiveArgument`

The `ObjectiveArgument` has been updated to no longer use a `String` as its return type. Instead, you can now just use an `Objective` object directly:

<div class="multi-pre">

```java,8.8.x
new CommandAPICommand("objective")
    .withArguments(new ObjectiveArgument("objective"))
    .executes((sender, args) -> {
        String objectiveName = (String) args.get("objective");
        Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(objectiveName);
    })
    .register();
```

</div>

$$\downarrow$$

<div class="multi-pre">

```java,9.0.0
new CommandAPICommand("objective")
    .withArguments(new ObjectiveArgument("objective"))
    .executes((sender, args) -> {
        Objective objective = (Objective) args.get("objective");
    })
    .register();
```

</div>

-----

### Changes to the `ListArgumentBuilder`

The `ListArgumentBuilder` no longer has `withList(Function<CommandSender, Collection<T>> list)` and instead uses `SuggestionInfo` to have `withList(Function<SuggestionInfo<CommandSender>, Collection<T>> list)`.

This now allows you to access more information when generating a list dynamically instead of just the command sender. To access the original command sender, you can use the `sender()` method from `SuggestionInfo`:

<div class="multi-pre">

```java,8.8.x
ListArgument<?> arg = new ListArgumentBuilder<>("values", ", ")
    .withList(sender -> List.of("cat", "wolf", "axolotl", sender.getName()))
    .withStringMapper()
    .buildGreedy();
```

</div>

$$\downarrow$$

<div class="multi-pre">

```java,9.0.0
ListArgument<?> arg = new ListArgumentBuilder<>("values", ", ")
    .withList(info -> List.of("cat", "wolf", "axolotl", info.sender().getName()))
    .withStringMapper()
    .buildGreedy();
```

</div>

-----

### Changes to the `Rotation` wrapper

The `Rotation` class now uses a constructor which has the **yaw first, and the pitch second**, instead of the pitch first and the yaw second.

<div class="multi-pre">

```java,8.8.x
new Rotation(20, 80); // Yaw = 80, Pitch = 20
```

</div>

$$\downarrow$$

<div class="multi-pre">

```java,9.0.0
new Rotation(20, 80); // Yaw = 20, Pitch = 80
```

</div>

-----

### Changes to the `ScoreboardSlot` wrapper

The `ScoreboardSlot` wrapper is now an enum that has direct support for sidebar team colors, via the `SIDEBAR_TEAM_###` enum values, for example `SIDEBAR_TEAM_RED`;

<div class="multi-pre">

```java,8.8.x
ScoreboardSlot slot = // Some ScoreboardSlot
DisplaySlot displaySlot = slot.getDisplaySlot(); // Returns PLAYER_LIST, SIDEBAR or BELOW_NAME

// Extract the color if necessary
if (slot.hasTeamColor()) {
    ChatColor color = slot.getTeamColor();
}
```

</div>

$$\downarrow$$

<div class="multi-pre">

```java,9.0.0
ScoreboardSlot slot = // Some ScoreboardSlot
DisplaySlot displaySlot = slot.getDisplaySlot(); // Returns PLAYER_LIST, BELOW_NAME or SIDEBAR_TEAM_###
```

</div>
