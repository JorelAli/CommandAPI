# Literal arguments

Literal arguments are used to represent "forced options" for a command. For instance, take Minecraft's `/gamemode` command. The syntax consists of the following:

```mccmd
/gamemode <mode>
/gamemode <mode> <player>
```

It consists of a gamemode, followed by an optional player argument. The list of gamemodes are as follows:

```mccmd
/gamemode survival 
/gamemode creative
/gamemode adventure
/gamemode spectator
```

Unlike regular commands (as those implemented by Bukkit for example), these four options are "hardcoded" - they're not "suggestions". The user can _only_ enter one of these four examples, no other values are allowed.

<div class="warning">

**Developer's Note:**

There is a simpler alternative to the `LiteralArgument` class! Instead of having to deal with arguments not being present in the array of arguments, consider using the much more intuitive `MultiLiteralArgument`, which is described in more detail [here](./argument_multiliteral.md)!

</div>

-----

## Literal arguments vs regular arguments

Unlike regular arguments that are shown in this chapter, the literal argument is _technically_ not an argument. Due to this fact, literal arguments are [unlisted](./listed.md) by default. In other words, **the literal argument is not present in the `args[]` for the command declaration.**

<div class="example">

### Example - Literal arguments and regular arguments

To illustrate the behavior of literal arguments, we create a command of the following form:

```mccmd
/mycommand <literal> <text>
```

As an example, let's declare the literal "hello" as a valid literal for this command. When we retrieve the result from `args[0]`, it returns the value of the `TextArgument`, as opposed to the literal "hello":

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentLiteral1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentLiteral1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentLiteral1}}
```

</div>

The `LiteralArgument` class also provides the `LiteralArgument.of()` and `LiteralArgument.literal()` helper methods which can be used as an alternative way to declare literal arguments:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentLiteral2}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentLiteral2}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentLiteral2}}
```

</div>

If I were to run the following command:

```mccmd
/mycommand hello goodbye
```

The value of `text` in the code above would be "goodbye".

</div>

<div class="example">

### Example - Gamemode command using literal arguments

This is a demonstration of how you could create a command similar to Minecraft's `/gamemode` command by using literal arguments. To do this, we are effectively registering 4 separate commands, each called `/gamemode`, but with different literal arguments.

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentLiteral3}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentLiteral3}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentLiteral3}}
```

</div>

Note how, since we don't have access to the literal from `args`, we must access the provided gamemode from elsewhere.

</div>

-----

## Literal argument warnings

Literal arguments require a string in the constructor. If the literal is an empty String or is null, the CommandAPI will throw a `BadLiteralException`.

Because literal arguments are _"hardcoded"_, each literal is effectively mapped to a single command. This is shown when using the configuration option `create-dispatcher-json: true` which shows the JSON result of registered commands. For instance, take the `/defaultgamemode` command:

```json
"defaultgamemode": {
    "type": "literal",
    "children": {
        "adventure": {
            "type": "literal",
            "executable": true
        },
        "creative": {
            "type": "literal",
            "executable": true
        },
        "spectator": {
            "type": "literal",
            "executable": true
        },
        "survival": {
            "type": "literal",
            "executable": true
        }
    }
},
```

Each option produces a new "command" in the tree of commands. This means that having exceptionally large lists of literals, or nested literals (e.g. `/command <literal1> <literal2>`) can cause very large trees which cannot be sent to the clients _(it can cause clients to crash)_.

> **Developer's Note:**
>
> Take care when using literal arguments. If your list of arguments is exceptionally large, or contains many nested arguments, the server may be unable to send the command information to the client. If many command argument choices are required, consider using a `StringArgument` and using `.replaceSuggestions()` to create your own list of required arguments.
