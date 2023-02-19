# MathOperation arguments

![An image of the math operation argument, with suggestions "%=", "*=", "+=", "-=", "/=", "<", "=", ">" and "><"](./images/arguments/mathop.png)

The CommandAPI's `MathOperationArgument` is used to represent the Minecraft scoreboard arithmetic operation to alter scoreboard scores. Since there is no default representation in the Bukkit API, the CommandAPI provides the `MathOperation` class to represent each operation:

| Symbol (in Minecraft) | MathOperation enum value |
| :-------------------: | ------------------------ |
|       \\(+=\\)        | `MathOperation.ADD`      |
|       \\(-=\\)        | `MathOperation.SUBTRACT` |
|       \\(*=\\)        | `MathOperation.MULTIPLY` |
|       \\(/=\\)        | `MathOperation.DIVIDE`   |
|      \\(\\%=\\)       | `MathOperation.MOD`      |
|        \\(=\\)        | `MathOperation.ASSIGN`   |
|        \\(<\\)        | `MathOperation.MIN`      |
|        \\(>\\)        | `MathOperation.MAX`      |
|       \\\(><\\)       | `MathOperation.SWAP`     |

-----

The `MathOperation` also has two methods:

```java
public int apply(int val1, int val2);
public float apply(float val1, float val2);
```

These methods are used to provide a basic implementation of these math operations on a given input. Given the values `val1` and `val2`, these are the operation that the `apply(val1, val2)` method performs:

| MathOperation enum value | Result                 |
| ------------------------ | ---------------------- |
| `MathOperation.ADD`      | `val1 + val2`          |
| `MathOperation.SUBTRACT` | `val1 - val2`          |
| `MathOperation.MULTIPLY` | `val1 * val2`          |
| `MathOperation.DIVIDE`   | `val1 / val2`          |
| `MathOperation.MOD`      | `val1 % val2`          |
| `MathOperation.ASSIGN`   | `val2`                 |
| `MathOperation.MIN`      | `Math.min(val1, val2)` |
| `MathOperation.MAX`      | `Math.max(val1, val2)` |
| `MathOperation.SWAP`     | `val2`                 |

-----

<div class="example">

### Example - Changing a player's level

Say we wanted to create a player's level. Typically, this is implemented in the following manner:

```mccmd
/xp set <player> <level>
/xp add <player> <levels>
```

Using the `MathOperationArgument`, we can extend the functionality of adding and setting a player's level by allowing the user to choose what operation they desire. To do this, we'll use the following syntax:

```mccmd
/changelevel <player> <operation> <value>
```

As with any command, we declare our arguments, cast them properly and then we write our main code. In this example, we use the `apply(int, int)` method from our `MathOperation` to calculate the player's new level.

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentMathOperation1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentMathOperation1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentMathOperation1}}
```

</div>

-----

There are various applications for the `changelevel` command based on what the user inputs. For example:

- To set the player _Notch_ to level 10:

  ```mccmd
  /changelevel Notch = 10
  ```

- To double the player _Notch's_ level:

  ```mccmd
  /changelevel Notch *= 2
  ```

- To set the player _Notch_'s level to 20, or keep it as their current level if it is higher than 20:

  ```mccmd
  /changelevel Notch > 20
  ```

</div>
