# Testing Utilities

The class `CommandAPITestUtilities` provides many static methods to interact with commands registered in the test environment. The standard way to access these methods is by using the class name.

```java
import dev.jorel.commandapi.CommandAPITestUtilities;

class Tests {
    @Test
    void test() {
        CommandAPITestUtilities.assertCommandSucceeds(...);
    }
}
```

However, you can also call these methods with just the method name if you add a static import:

```java
import static dev.jorel.commandapi.CommandAPITestUtilities.assertCommandSucceeds;

class Tests {
    @Test
    void test() {
        assertCommandSucceeds(...);
    }
}
```

or make your test class extend `CommandAPITestUtilities`:

```java
import dev.jorel.commandapi.CommandAPITestUtilities;

class Tests extends CommandAPITestUtilties {
    @Test
    void test() {
        assertCommandSucceeds(...);
    }
}
```

This is similar to importing static methods from the [`org.junit.jupiter.api.Assertions`](https://junit.org/junit5/docs/current/user-guide/#writing-tests-assertions) class, and may help make your tests easier to read.

## Running commands

There are 3 methods to test basic command execution:

```java
void dispatchCommand(CommandSender sender, String command) throws CommandSyntaxException
void assertCommandSucceeds(CommandSender sender, String command)
CommandSyntaxException assertCommandFails(CommandSender sender, String command, String expectedMessage)
```

`dispatchCommand` will simply attempt to execute a command. It may throw a `CommandSyntaxException` if the command fails, either because it wasn't a valid command or because your command executor threw a [`WrapperCommandSyntaxException`](./commandfailures.md#handling-command-failures).

`assertCommandSucceeeds` will run `dispatchCommand` and cause the test to fail if the command fails for any reason.

`assertCommandFails` will run `dispatchCommand` and cause the test to fail if the command does _not_ throw a `CommandSyntaxException`. This method will also fail if the command throws an exception but with a different error message than the `expectedMessage` String. It returns the `CommandSyntaxException` that was caught in case you want to inspect any other features of the exception.

## Verifying arguments

When a command is executed, you may want to assert that the Objects provided as arguments match your expectations. There are 4 basic methods for doing this:

```java
void assertCommandSucceedsWithArguments(CommandSender sender, String command, Object... argumentsArray)
void assertCommandSucceedsWithArguments(CommandSender sender, String command, Map<String, Object> argumentsMap)

void assertCommandFailsWithArguments(CommandSender sender, String command, String expectedFailureMessage, Object... argumentsArray)
void assertCommandFailsWithArguments(CommandSender sender, String command, String expectedFailureMessage, Map<String, Object> argumentsMap)
```

If you expect the command to succeed, use `assertCommandSucceedsWithArguments`. If you expect the command's executor to throw a [`WrapperCommandSyntaxException`](./commandfailures.md#handling-command-failures), use `assertCommandFailsWithArguments`. You can give these methods either an array or a Map holding all arguments you expect to be present for the command.

Note that if the command input cannot be parsed, the command will fail, but a CommandAPI executor will never be run. In this case, a CommandAPI executor will have never been run, so `assertCommandFailsWithArguments` will not have any arguments to inspect, and the test will fail. You can only successfully use `assertCommandFails` in this situation.

## Verifying suggestions

### Suggestion texts

There are 4 basic methods that may be used to verify the text of a command's suggestions:

```java
void assertCommandSuggests(CommandSender sender, String command, String... expectedSuggestions)
void assertCommandSuggests(CommandSender sender, String command, List<String> expectedSuggestions)

void assertCommandSuggests(CommandSender sender, String command, int startingAt, String... expectedSuggestions)
void assertCommandSuggests(CommandSender sender, String command, int startingAt, List<String> expectedSuggestions)
```

You can either provide the expected suggestions as an array or a List. Note that the suggestions will be provided in alphabetical order.

You can optionally provide a `startingAt` index. This is the place in the command String where you expect the first character of the suggestions will be placed.

### Suggestion tooltips

If the suggestions you want to check include [tooltips](./tooltips.md), you may use these 5 methods:

```java
Suggestion makeTooltip(String text, String tooltip);

void assertCommandSuggestsTooltips(CommandSender sender, String command, Suggestion... expectedSuggestions)
void assertCommandSuggestsTooltips(CommandSender sender, String command, List<Suggestion> expectedSuggestions)

void assertCommandSuggestsTooltips(CommandSender sender, String command, int startingAt, Suggestion... expectedSuggestions)
void assertCommandSuggestsTooltips(CommandSender sender, String command, int startingAt, List<Suggestion> expectedSuggestions)
```

The 4 `assertCommandSuggestsTooltips` methods work the same as the `assertCommandSuggests` methods, but you provide `Suggestion` objects containing both the text and a tooltip. The `makeTooltip` method allows you to easily create these `Suggestion` objects.
