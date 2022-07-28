# Chat preview

Chat preview is a feature introduced in Minecraft 1.19 that allows the server to display a preview of a chat message to the client before the client sends their message to the server. This chat preview feature is also compatible with `/say` and `/msg`, as well as the `ChatArgument` and `AdventureChatArgument` classes.

-----

## Enabling chat preview

To use chat preview, your server must have `previews-chat` set to `true` in the `server.properties` file:

```properties
...
previews-chat=true
...
```

For players that want to use chat preview, they must have `Chat Preview: ON` enabled in `Options > Chat Settings...`

-----

## Using chat preview

The `ChatArgument` and `AdventureChatArgument` classes includes the method `withPreview(PreviewableFunction preview)` that lets you generate a preview to send to the client. This method takes in the `PreviewableFunction` functional interface, which is a function that takes in a `PreviewInfo` and returns either a `BaseComponent[]` (for `ChatArgument`) or a `Component` (for `AdventureChatArgument`):

```java
public T generatePreview(PreviewInfo info) throws WrapperCommandSyntaxException;
```

The `PreviewInfo` class is a record containing the following:

```java
public record PreviewInfo<T> {
    Player player();
    String input();
    String fullInput();
    T parsedInput();
}
```

The following methods are as follows:

```java
Player player();
```

`player()` is the player that is currently typing a chat preview.

-----

```java
String input();
```

`input()` is the current input for the current `ChatArgument` or `AdventureChatArgument`. If a user is typing `/mycommand hellowor¦` and the command syntax is `/mycommand <ChatArgument>`, the result of `input()` would be `"hellowor"`.

-----

```java
String fullInput();
```

`fullInput()` is the full input that the player has typed, including the leading `/` symbol which is required to start a command. If a user is typing `/mycommand hellowor¦`, the result of `fullInput()` would be `"/mycommand hellowor"`.

-----

```java
T parsedInput();
```

`parsedInput()` is similar to `input()`, except it has been parsed by the CommandAPI's argument parser. This is a representation of what the argument in the executor would look like. For a `ChatArgument` the return type is `BaseComponent[]`, and for `AdventureChatArgument` the return type is `Component`.

-----

## Chat preview examples

<div class="example">

Say we wanted to make our own `/broadcast` command that allowed the user to use `&` chat colors. We can use chat preview to show users what the result of their `/broadcast` command would look like before running the command.

```mccmd
/broadcast <message>
```

Because the `ChatArgument` and `AdventureChatArgument` can support entity selectors (such as `@p`), it's best to use the `info.parsedInput()` method to handle parsed entity selectors. In our code, we use the `.withPreview()` method and take the parsed input and convert it to plain text. We then convert the plain text with `&` characters into component text to be displayed to the user.

For execution, we do the same procedure, because the text that the user enters still has `&` characters

<div class="multi-pre">

```java,Spigot
{{#include ../../commandapi-core/src/test/java/Examples.java:chatpreviewspigot}}
```

```java,Paper
{{#include ../../commandapi-core/src/test/java/Examples.java:chatpreviewadventure}}
```

</div>

</div>
