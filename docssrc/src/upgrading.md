# Upgrading guide

## From version 2.3 to 3.0

The CommandAPI's upgrade from version 2.3 to 3.0 is very intense and various refactoring operations took place, which means that plugins that implement the CommandAPI version 2.3 or below are likely not to work.

### Imports & Renaming

Various imports have been moved around for sake of readability and keeping the CommandAPI more organized. The following imports are all preceded with the CommandAPI package name `io.github.jorelali.commandapi.api`:

| Pre 3.0 import                            | 3.0 import                           |
| ----------------------------------------- | ------------------------------------ |
| `FunctionWrapper`                         | `wrappers.FunctionWrapper`           |
| `exceptions.GreedyStringException`        | `exceptions.GreedyArgumentException` |
| `arguments.LocationArgument.LocationType` | `arguments.LocationType`             |
| `CommandExecutor`                         | `executors.CommandExecutor`          |
| `ResultingCommandExecutor`                | `executors.ResultingCommandExecutor` |

For example, if you had the following code before:

```java
import io.github.jorelali.commandapi.api.FunctionWrapper;
import io.github.jorelali.commandapi.api.CommandAPI;
import io.github.jorelali.commandapi.api.arguments.Argument;

// ...

LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("function", new FunctionWrapper());

CommandAPI.getInstance().register("myCommand", arguments, (sender, args) -> {
    // ...
});

// ...
```

You would need to change the import according to the table above:

```java
import io.github.jorelali.commandapi.api.wrappers.FunctionWrapper;
import io.github.jorelali.commandapi.api.CommandAPI;
import io.github.jorelali.commandapi.api.arguments.Argument;

// ...

LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("function", new FunctionWrapper());

CommandAPI.getInstance().register("myCommand", arguments, (sender, args) -> {
    // ...
});

// ...
```

### Removed classes & Alternatives

To reduce redundancies, the CommandAPI removed a few classes:

| Removed class                           | Alternative                                                  |
| --------------------------------------- | ------------------------------------------------------------ |
| `SuggestedStringArgument`               | Use `.overrideSuggestions(String[])` for the relevant argument, as described [here](./arguments.html#arguments-with-overrideable-suggestions) |
| `DefinedCustomArguments` for Objectives | Use `ObjectiveArgument`                                      |
| `DefinedCustomArguments` for Teams      | Use `TeamArgument`                                           |

### Command registration

The way that commands are registered has been completely changed. It is highly recommended to switch to the new system, which is described [**here**](./commandregistration.html).

The following methods have been deprecated and will be removed in the next major release:

```java
CommandAPI.getInstance().register(String, LinkedHashMap, CommandExecutor);
CommandAPI.getInstance().register(String, String[], LinkedHashMap, CommandExecutor);
CommandAPI.getInstance().register(String, CommandPermission, LinkedHashMap, CommandExecutor);
CommandAPI.getInstance().register(String, CommandPermission, String[], LinkedHashMap, CommandExecutor);

CommandAPI.getInstance().register(String, LinkedHashMap, ResultingCommandExecutor);
CommandAPI.getInstance().register(String, String[], LinkedHashMap, ResultingCommandExecutor);
CommandAPI.getInstance().register(String, CommandPermission, LinkedHashMap, ResultingCommandExecutor);
CommandAPI.getInstance().register(String, CommandPermission, String[], LinkedHashMap, ResultingCommandExecutor);
```