# Maven-shaded-tests

An example showcasing using MockBukkit and the CommandAPI testing suite to test commands registered using the CommandAPI.

> **Dev note:**
>
> Testing using the CommandAPI is still in its early stage and is only set up for Minecraft 1.19.2, so the following is subject to change!

-----

## Set up

### Set up (pom.xml)

In your `pom.xml`, you need the following:

- Mojang's Brigadier dependency
- The CommandAPI dependencies (in this order):
  - `commandapi-bukkit-test-tests` (rewrites CommandAPIVersionHandler to hook into testing NMS implementations)
  - `commandapi-bukkit-shade` (as normal for shading)
  - `commandapi-bukkit-test-impl` (Test implementation with utility methods and initial test setups for version-specific implementations)
  - `commandapi-bukkit-test-impl-1.19.2` (Test implementation for Minecraft 1.19.2)
- A full copy of Spigot (not using Mojang mappings)

For testing, you'll need:

- JUnit (e.g. JUnit 5)

> ⚠️ You do _not_ need `MockBukkit`, this is automatically included when you include `commandapi-bukkit-test-impl-1.19.2`.

For building and setup, you'll need:

- Maven Surefire plugin

A sample `pom.xml` can be found in this folder.

### Set up (test directory)

In your test directory, you'll need:

- A copy of your `plugin.yml` file from your main source directory. This needs to go in a `test/resources` directory
- Your tests in `test/java/...`

```txt
.
├── README.md
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── io
│   │   │       └── github
│   │   │           └── jorelali
│   │   │               ├── MyCommands.java
│   │   │               └── MyMain.java
│   │   └── resources
│   │       └── plugin.yml
│   └── test
│       ├── java
│       │   └── io
│       │       └── github
│       │           └── jorelali
│       │               └── MyCommandTests.java
│       └── resources
│           └── plugin.yml
```

### Set up (main code)

- Add the additional `JavaPlugin` constructors to your main plugin class. For example, for a main class callead `MyMain`:

```java
// Additional constructors required for MockBukkit
public MyMain() {
    super();
}

public MyMain(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
    super(loader, description, dataFolder, file);
}
```

More information about this can be found at [MockBukkit](https://github.com/MockBukkit/MockBukkit#using-mockbukkit)

### Troubleshooting

- **Problem:** No tests were found

  **Answer:** Read [Why Maven Doesn’t Find JUnit Tests to Run](https://www.baeldung.com/maven-cant-find-junit-tests)

-----

## Writing tests

Assuming you're using JUnit 5 or greater:

- Create a class that extends `dev.jorel.commandapi.test.TestBase`
- Add a `@BeforeEach` method that calls `super.setUp()` with your main plugin class file
- Add a `@AfterEach` method that calls `super.tearDown()`
- Call commands using `server.dispatchCommand()`

```java
class MyCommandTests extends TestBase {

    @BeforeEach
    public void setUp() {
        super.setUp(MyMain.class);
    }

    @AfterEach
    public void tearDown() {
        super.tearDown();
    }

    @Test
    void testMyEffectCommand() {
        PlayerMock player = server.addPlayer();

        server.dispatchCommand(player, "myeffect " + player.getName() + " speed");
        assertNotNull(player.getPotionEffect(PotionEffectType.SPEED));
    }
}

```

Other useful methods that you may want to use:

- `server.dispatchCommand`

  Runs a command that was registered via the CommandAPI. If this command fails, this automatically fails the test. This should not have the leading `/` character

- `server.dispatchThrowableCommand`

  This throws a Brigadier `CommandSyntaxException` and does not automatically fail the test. This should not have the leading `/` character

- `server.getSuggestions`

  Lists the suggestions that was displayed to the user if they typed a certain command string. This should not have the leading `/` character

- `server.addAdvancement`

  Adds an advancement to the current running server instance. This method registers advancements using NMS, making them compatible with CommandAPI advancement-related arguments
