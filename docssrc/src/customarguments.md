# Custom arguments

Custom arguments are arguably the most powerful argument that the CommandAPI offers. This argument is used to represent any String, or Minecraft key _(Something of the form `String:String`, such as `minecraft:diamond`)_. They basically represent `StringArgument` with overrideable suggestions and a built-in parser for any object of your choice. They are designed to be used for multiple commands - you define the argument once and can use it wherever you want when declaring commands.

-----

The `CustomArgument<T>` has two constructors, declared as follows:

```java
public CustomArgument(String nodeName, CustomArgumentFunction<T> parser);
public CustomArgument(String nodeName, CustomArgumentFunction<T> parser, boolean keyed);
```

The second argument is the `CustomArgumentFunction`, which is a lambda that takes in a String and returns some custom object of type `T`. The first constructor will construct a `CustomArgument` which uses the `StringArgument` as a base (thus, only simple strings). The second argument has the field `keyed`. When this field is set to `true`, the `CustomArgument` will use a `Minecraft key` as a base, allowing you to use Minecraft keys as input.

> **Developer's Note:**
>
> I may have complicated this too much, so let me clarify what I mean. The `CustomArgument` constructor is of the following forms:
>
> ```java
> CustomArgument(nodeName, (String) -> { ... return T; });
> CustomArgument(nodeName, (String) -> { ... return T; }, boolean keyed);
> ```
>
> Both constructors take in a **String** as input and return `T`. When enabling `keyed`, it allows the input to be of the form of a Minecraft key, but doesn't change the input type.

The custom argument requires the type of the target object that the custom argument will return when parsing the arguments for a command. For instance, if you have a `CustomArgument<Player>`, then when parsing the arguments for the command, you would cast it to a `Player` object.

<div class="example">

### Example - World argument

Say we want to create an argument to represents the list of available worlds on the server. We basically want to have an argument which always returns a Bukkit `World` object as the result. Here, we create a method `worldArgument()` that returns our custom argument that returns a `World`. First, we retrieve our `String[]` of world names to be used for our suggestions. We then write our custom argument that creates a `World` object from the input (in this case, we simply convert the String to a `World` using `Bukkit.getWorld(String)`). We perform error handling before returning our result:

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

By using a `CustomArgument` (as opposed to a simple `StringArgument` and overriding its suggestions), we are able to provide a much more powerful form of error handling (automatically handled inside the argument), and we can reuse this argument for other commands.

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