# The `SuggestionsInfo` record

Argument suggestion methods can accept a function which takes in a `SuggestionsInfo` object and returns a suitable format for suggestions. The `SuggestionInfo` class is a record which contains the following methods:

```java
public record SuggestionInfo {
    CommandSender sender();
    CommandArguments previousArgs();
    String currentInput();
    String currentArg();
}
```

These methods can be used to aid with providing context-aware suggestions for users. The fields are as follows:

```java
CommandSender sender();
```

`sender()` represents the command sender which is typing this command and requesting these suggestions. This is normally a `Player`, but can also be a console command sender if using a Paper server.

-----

```java
CommandArguments previousArgs();
```

`previousArgs()` represents the previously declared arguments, which are parsed and interpreted as if they were being used to execute the command. See [this example on the string argument suggestions page](./stringargumentsuggestions.md#suggestions-depending-on-previous-arguments) for an idea of how to utilize this field.

-----

```java
String currentInput();
```

`currentInput()` represents the current input that the command sender has entered. This is effectively everything that they have typed, including the leading `/` symbol which is required to start a command. If a user is typing `/mycommand hellowor¦`, the result of `currentInput()` would be `"/mycommand hellowor"`.

-----

```java
String currentArg();
```

`currentArg()` represents the current text which the command sender has entered for the argument which you're trying to provide suggestions for. If a user is typing `/mycommand hellowor¦`, the result of `currentArg()` would be `"hellowor"`.
