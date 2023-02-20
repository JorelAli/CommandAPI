# Custom arguments

Custom arguments are a quality-of-life feature that the CommandAPI offers which allows you to perform pre-processing on an argument in the argument instance rather than in your `executes()` method for a command. They are designed to be used for multiple commands - you can define the argument once and can use it wherever you want when declaring commands.

-----

The `CustomArgument<T, B>` has the following constructor:

```java
public CustomArgument(Argument<B> base, CustomArgumentInfoParser<T, B> parser);
```

This constructor takes in two parameters:

- A "base argument", which is the argument that it'll use as the underlying parser. For example, if this is a `StringArgument`, it'll use the StringArgument's parsing rules ( alphanumeric characters (A-Z, a-z and 0-9), and the underscore character) and if this is a `LocationArgument`, it'll take three numerical values.

- A "parser", which lets you process the argument based on its input. This is described in more detail below.

### Type params

The custom argument requires two type parameters, `<T>` and `<B>`:

- `<T>` refers to the type that this argument will return when parsing the arguments for a command. For instance, if you have a `CustomArgument<Player, ...>`, then when parsing the arguments for the command, you would cast it to a `Player` object.

- `<B>` refers to the type that the base argument will return. This can be found in the [Argument Casting](./arguments.md#argument-casting) section. For example, if the base argument is a `StringArgument`, you'd have `CustomArgument<..., String>`.

-----

## The CustomArgumentInfoParser class

To create a parser for a `CustomArgument`, you need to provide a `CustomArgumentInfoParser` function to the constructor. The `CustomArgumentInfoParser` class is a functional interface which accepts `CustomArgumentInfo` and returns `T`, an object of your choosing:

```java
@FunctionalInterface
public interface CustomArgumentInfoParser<T, B> {

    public T apply(CustomArgumentInfo<B> info) throws CustomArgumentException;

}
```

The `CustomArgumentInfo` record is very similar to the `SuggestionInfo` record for declaring argument suggestions. This record contains the following methods:

```java
public record CustomArgumentInfo<B> {
    CommandSender sender();
    Object[] previousArgs(); 
    String input();
    B currentInput();
}
```

These fields are as follows:

- ```java
  CommandSender sender();
  ```

  `sender()` represents the command sender that is typing the command. This is normally a `Player`, but can also be a console command sender if using a Paper server.

- ```java
  Object[] previousArgs();
  ```

  `previousArgs()` represents a list of previously declared arguments, which are parsed and interpreted as if they were being used to execute the command.

- ```java
  String input();
  ```

  `input()` represents the current input _for the custom argument_ that the user has typed. For example, if a user is typing `/mycommand hello` and the first argument is a CustomArgument, the `input()` would return `"hello"`.

- ```java
  B currentInput();
  ```

  `currentInput()` represents the current input, as parsed by the base argument. For example, if your base argument was an `IntegerArgument`, the return type of `currentInput()` would be an `int`.

-----

<div class="example">

### Example - World argument

Say we want to create an argument to represents the list of available worlds on the server. We want to have an argument which always returns a Bukkit `World` object as the result. Here, we create a method `worldArgument()` that returns our custom argument that returns a `World`. First, we retrieve our `String[]` of world names to be used for our suggestions. We then write our custom argument that creates a `World` object from the input (in this case, we simply convert the input to a `World` using `Bukkit.getWorld(String)`). We perform error handling before returning our result:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentCustom1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentCustom1}}
```

</div>

In our error handling step, we check if the world is equal to null (since the `Bukkit.getWorld(String)` is `@Nullable`). To handle this case, we throw a `CustomArgumentException` with an error from a `MessageBuilder`. The `CustomArgumentException` has two constructors, so a message builder isn't required each time:

```java
new CustomArgumentException(String message);
new CustomArgumentException(MessageBuilder message);
```

-----

We can use our custom argument like any other argument. Say we wanted to write a command to teleport to a specific world. We will create a command of the following syntax:

```mccmd
/tpworld <world>
```

Since we have defined the method `worldArgument()` which automatically generates our argument, we can use it as follows:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentCustom2}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentCustom2}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentCustom2}}
```

</div>

-----

By using a `CustomArgument` (as opposed to a simple `StringArgument` and replacing its suggestions), we are able to provide a much more powerful form of error handling (automatically handled inside the argument), and we can reuse this argument for other commands.

</div>

-----

## Message Builders

The `MessageBuilder` class is a class to easily create messages to describe errors when a sender sends a command which does not meet the expected syntax for an argument. It acts in a similar way to a `StringBuilder`, where you can append content to the end of a String.

The following methods are as follows:

| Method | Description |
| ------ | ----------- |
| `appendArgInput()` | Appends the argument that failed that the sender submitted to the end of the builder. E.g. `/foo bar` will append `bar` |
| `appendFullInput()` | Appends the full command that a sender submitted to the end of the builder. E.g. `/foo bar` will append `foo bar` |
| `appendHere()` | Appends the text `<--[HERE]` to the end of the builder |
| `append(Object)`| Appends an object to the end of the builder |

<div class="example">

### Example - Message builder for invalid objective argument

To create a `MessageBuilder`, simply call its constructor and use whatever methods as you see fit. Unlike a `StringBuilder`, you don't have to "build" it when you're done - the CommandAPI does that automatically:

```java
new MessageBuilder("Unknown world: /").appendFullInput().appendHere();
```

</div>
