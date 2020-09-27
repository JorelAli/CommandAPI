# The FunctionWrapper

The CommandAPI includes the `FunctionWrapper` class which is a wrapper for Minecraft's functions. It allows you to execute the commands that are represented by the respective `.mcfunction` file.

## FunctionWrapper methods

The `FunctionWrapper` class has the following methods:

```java
class FunctionWrapper implements Keyed {
    String[] getCommands();
	void run();
	void runAs(Entity e);
	NamespacedKey getKey();
}
```

These methods allow you to interact with the Minecraft function that this class wraps.

### `getCommands()`

The `getCommands()` method returns a `String[]` that contains the list of commands that the Minecraft function "holds". In other words, running this Minecraft function is basically as simple as iterating through its commands and running them in order. The commands that this `String[]` holds are the raw strings that this function represents - in other words, it can include things such as `@p` and `~ ~ ~` instead of "filled in" values.

### `run()`

The `run()` method basically does what it says on the tin - it runs the function. The command executor that runs this function is the command executor that was used to retrieve it. For example, if a player in-game populated this argument, then the player will be filled in for `@p` and the player's location would be used for things such as `~ ~ ~`:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:functionarguments}}
```

### `runAs(Entity)`

The `runAs(Entity)` is basically the same as the `run()` method, but it allows you to change the command executor to another entity. 