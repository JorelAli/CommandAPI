# Primitive arguments

Primitive arguments are arguments that represent Java primitive types, such as `int`, `float`, `double`, `boolean` and `long`. These arguments are defined in their respective classes:

| Primitive type | CommandAPI class  |
| -------------- | ----------------- |
| `int`          | `IntegerArgument` |
| `float`        | `FloatArgument`   |
| `double`       | `DoubleArgument`  |
| `long`         | `LongArgument`    |
| `boolean`      | `BooleanArgument` |

These arguments simply cast to their primitive type and don't need any extra work.

-----

## Boolean arguments

![A boolean argument showing the suggestions 'false' and 'true'](./images/arguments/boolean.png)

The `BooleanArgument` class represents the Boolean values `true` and `false`.

<div class="example">

### Example - Config editing plugin

Say we want to create a plugin that lets you edit its own `config.yml` file using a command. To do this, let's create a command with the following syntax:

```mccmd
/editconfig <config-key> <value>
```

We first retrieve the keys from the configuration file using the typical Bukkit API. We construct our `List` to hold our arguments, with the first parameter being a String key (in the form of a `TextArgument`, [overridden with an array of suggestions](./argumentsuggestions.md)). Finally, we register our command and update the config, ensuring that we cast the `BooleanArgument` to `boolean`:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentPrimitives1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentPrimitives1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentPrimitives1}}
```

</div>

</div>

-----

## Numerical arguments

Numbers are represented using the designated number classes:

| Class | Description |
| ----- | ----------- |
| `IntegerArgument` | Whole numbers between `Integer.MIN_VALUE` and `Integer.MAX_VALUE` |
| `LongArgument` | Whole numbers between `Long.MIN_VALUE` and `Long.MAX_VALUE` |
| `DoubleArgument` | Double precision floating point numbers |
| `FloatArgument` | Single precision floating point numbers |

Each numerical argument can have ranges applied to them, which restricts the user to only entering numbers from within a certain range. This is done using the constructor, and the range specified:

| Constructor | Description |
| ----------- | ----------- |
| `new IntegerArgument()` | Any range |
| `new IntegerArgument(min)` | Values greater than _or equal to_ `min` |
| `new IntegerArgument(min, max)` | Values greater than or equal to `min` and less than or equal to `max` |

Each range is _inclusive_, so it includes the number given to it. If the minimum value provided is larger than the maximum value, an `InvalidRangeException` is thrown.
