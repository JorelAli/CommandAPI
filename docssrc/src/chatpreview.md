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
public record PreviewInfo {
    Player player();
    String input();
    String fullInput();
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

## Chat preview examples

Say we wanted to make our own `/broadcast` command that allowed the user to use `&` chat colors. As such, we also want the users to see what their command would look like...
