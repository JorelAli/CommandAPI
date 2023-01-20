# Upgrading guide

## From 8.7.x to 9.0.0

CommandAPI 9.0.0 is arguably the biggest change in the CommandAPI's project structure and usage. This update was designed to allow the CommandAPI to be generalized for other platforms (e.g. Velocity, Fabric, Sponge), and as a result **this update is incompatible with previous versions of the CommandAPI**.

All deprecated methods from 8.7.x have been removed in this update. Please ensure that you use the relevant replacement methods (these are described in the JavaDocs for the various deprecated methods) before upgrading to 9.0.0.

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

```java,8.7.x
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

```java,8.7.x
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

The CommandAPI introduces a new `args.get(String)` method to access arguments using the argument node name. This method is significantly safer than using `args.get(int)` and makes your code much more compatible with optional arguments:

<div class="multi-pre">

```java,8.7.x
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

```java,8.7.x
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

```java,8.7.x
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

```java,8.7.x
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
