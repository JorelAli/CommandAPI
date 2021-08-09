# Custom arguments

Custom arguments are an experimental feature which the CommandAPI offers, which allows you to represent any String, or Minecraft key _(Something of the form `String:String`, such as `minecraft:diamond`)_ with a custom parser. They basically represent `StringArgument` with replaced suggestions and a built-in parser for any object of your choice. They are designed to be used for multiple commands - you can define the argument once and can use it wherever you want when declaring commands.

-----

The `CustomArgument<T>` has the following two constructors:

```java
public CustomArgument(String nodeName, CustomArgumentInfoParser<T> parser);
public CustomArgument(String nodeName, CustomArgumentInfoParser<T> parser, boolean keyed);
```

There are effectively two forms that this can take:

**A custom argument with a string-based parser**

The simplest form requires the node name as per any other argument, and a parser which takes in as input a record of info and returns a custom object of your choice. For example, if you wanted to create a custom argument that represents a World, you can use this to return a Bukkit `World` object.

```java
new CustomArgument(nodeName, inputInfo -> { 
    // code here
    return T; 
});
```

The CommandAPI will use an underlying `StringArgument` to parse this custom argument, so the limitations of string arguments will apply to this argument (it can only contain alphanumeric characters (A-Z, a-z and 0-9), and the underscore character (_)).

**A custom argument with a parser that takes in a Minecraft Key**

With the second constructor, if you provide `true` to the `keyed` field, the input can be of the form of a Minecraft key (so it can have `:` in the name).

### Type params

The custom argument requires the type of the target object that the custom argument will return when parsing the arguments for a command. For instance, if you have a `CustomArgument<Player>`, then when parsing the arguments for the command, you would cast it to a `Player` object.

-----

## The CustomArgumentInfoParser class

To create a parser for a `CustomArgument`, you need to provide a `CustomArgumentInfoParser` function to the constructor. The `CustomArgumentInfoParser` class is a functional interface which accepts `CustomArgumentInfo` and returns `T`, an object of your choosing:

```java
@FunctionalInterface
public static interface CustomArgumentInfoParser<T> {

    public T apply(CustomArgumentInfo info) throws CustomArgumentException;

}
```

The `CustomArgumentInfo` record is very similar to the `SuggestionInfo` record for declaring argument suggestions. This record contains the following methods:

```java
public record CustomArgumentInfo {
    CommandSender sender();
    Object[] previousArgs(); 
    String input();
}
```

These fields are as follows:

```java
CommandSender sender();
```

`sender()` represents the command sender that is typing the command. This is normally a `Player`, but can also be a console command sender if using a Paper server.

```java
Object[] previousArgs();
```

`previousArgs()` represents a list of previously declared arguments, which are parsed and interpreted as if they were being used to execute the command.

```java
String input();
```

`input()` represents the current input _for the custom argument_ that the user has typed. For example, if a user is typing `/mycommand hello` and the first argument is a CustomArgument, the `input()` would return `"hello"`.

-----

<div class="example">

### Example - World argument

Say we want to create an argument to represents the list of available worlds on the server. We basically want to have an argument which always returns a Bukkit `World` object as the result. Here, we create a method `worldArgument()` that returns our custom argument that returns a `World`. First, we retrieve our `String[]` of world names to be used for our suggestions. We then write our custom argument that creates a `World` object from the input (in this case, we simply convert the input to a `World` using `Bukkit.getWorld(String)`). We perform error handling before returning our result:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:customarguments2}}
```

In our error handling step, we check if the world is equal to null (since the `Bukkit.getWorld(String)` is `@Nullable`). To handle this case, we throw a `CustomArgumentException` with an error from a `MessageBuilder`. The `CustomArgumentException` has two constructors, so a message builder isn't required each time:

```java
new CustomArgumentException(String message);
new CustomArgumentException(MessageBuilder message);
```

-----

We can use our custom argument like any other argument. Say we wanted to write a command to teleport to a specific world. We will create a command of the following syntax:

```
/tpworld <world>
```

Since we have defined the method `worldArgument()` which automatically generates our argument, we can use it as follows:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:customarguments}}
```

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