# Upgrading guide

## From version 3.x to 4.0

The maven repository url has changed:

Instead of being:

```
https://raw.githubusercontent.com/JorelAli/CommandAPI/mvn-repo/1.13-Command-API/
```

You must now use:

```xml
https://raw.githubusercontent.com/JorelAli/CommandAPI/mvn-repo/CommandAPI/
```

This information can be viewed in section [3. Setting up your development environment](./quickstart.md)

-----

## From version 2.3 to 3.0

The CommandAPI's upgrade from version 2.3 to 3.0 is very intense and various refactoring operations took place, which means that plugins that implement the CommandAPI version 2.3 or will not to work with the CommandAPI version 3.0. This page outlines the few major changes and points you to the various pages in the documentation that covers how to use version 3.0.

-----

### Imports & Renaming

The default package name has been changed. Instead of being registered under the `io.github.jorelali` package, the CommandAPI has been moved to the `dev.jorel` package:

\\[\texttt{io.github.jorelali.commandapi.api}\rightarrow\texttt{dev.jorel.commandapi}\\]

To organise classes with other classes of similar functions, new packages have been introduced. These can be fully explored using the [new JavaDocs](https://www.jorel.dev/CommandAPI/javadocs/html/annotated.html)

-----

### Removed classes & Alternatives

To reduce redundancies, the CommandAPI removed a few classes:

| Removed class                           | Alternative                                                  |
| --------------------------------------- | ------------------------------------------------------------ |
| `SuggestedStringArgument`               | Use `.overrideSuggestions(String[])` for the relevant argument, as described [here](./arguments.html#overriding-argument-suggestions) |
| `DefinedCustomArguments` for Objectives | Use [`ObjectiveArgument`](./objectivearguments.md)           |
| `DefinedCustomArguments` for Teams      | Use [`TeamArgument`](./teamarguments.md)                     |

-----

### Command registration

The way that commands are registered has been completely changed. It is highly recommended to switch to the new system, which is described [**here**](./commandregistration.html).

The following methods have been removed:

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

Additionally, the CommandAPI is no longer accessed by using `CommandAPI.getInstance()`. This has been replaced with static methods that can be accessed without an instance of the CommandAPI, so you can use the following:

```java
CommandAPI.fail(String command);
CommandAPI.canRegister();
CommandAPI.unregister(String command);
CommandAPI.unregister(String command, boolean force);
```

