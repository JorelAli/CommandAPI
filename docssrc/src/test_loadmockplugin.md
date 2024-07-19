# Loading The CommandAPI in Tests

## Plugin Dependency

If your plugin depends on the CommandAPI plugin to load, when running tests you should load the `JavaPlugin` `MockCommandAPIPlugin` before you use MockBukkit to [load your plugin](https://mockbukkit.readthedocs.io/en/latest/first_tests.html#creating-the-test-class). You can either load this class directly with `MockBukkit.load(MockCommandAPIPlugin.class)`, or use one of the static `MockCommandAPIPlugin#load` methods:

```java
MockCommandAPIPlugin load()
```

Loads the CommandAPI Plugin in the test environment. Works exactly the same as `MockBukkit.load(MockCommandAPIPlugin.class)`.

```java
MockCommandAPIPlugin load(Consumer<CommandAPIBukkitConfig> configureSettings)
```

Loads the CommandAPI Plugin after applying the given consumer. This allows configuring any setting from the [config.yml](./config.md#configuration-settings) using the methods provided by [CommandAPIBukkitConfig](./setup_shading.md#loading).

<div class="example">

### Example - Loading test CommandAPI with settings

To change, for example, the `missing-executor-implementation` message while running tests, you can use the method `CommandAPIBukkitConfig#missingExecutorImplementationMessage` when the `configureSettings` callback is run:

```java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:testLoadMockPlugin1}}
```

</div>

## Shaded Dependency

If your plugin shades the CommandAPI, the CommandAPI will automatically load as usual when you use MockBukkit to load your plugin. Just note that you **must** call `CommandAPI.onDisable()` in your plugin's `onDisable` method in order for the test environment to reset properly after each test.
