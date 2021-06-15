# Resulting command executors

Resulting command executors are very similar to normal command executors, except they can return an integer _result value_.

```java
(sender, args) -> {
    //Code here
    return /*some integer here*/ ;
};
```

Similarly, these will return a _success value_ of 1 if it runs successfully, and a _success value_ of 0 if it runs unsuccessfully. **If a success value of 0 occurs, the _result value_ will be 0**. In short:

|                   |        Command Works        | Command Doesn't Work |
| :---------------: | :-------------------------: | :------------------: |
| **Success Value** |              1              |          0           |
| **Result Value**  | result defined in your code |          0           |

The concept of result values are better explained through examples:

<div class="example">

### Example - Random number result command

Say we want a command that returns a random number as a result. This can then be used by vanilla Minecraft's `/execute store result ...` command, which can be used for other command block chains.

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:resultingcommandexecutor}}
```

This returns a **success value of 1** _(Because no errors or `CommandAPI.fail(String)` was thrown)_ and a **result value of a random number**.

</div>

<div class="example">

### Example - Lootbox system with `/execute` command

We can store state using `/execute store` and we can perform conditional checks using `/execute if`. By combining these, we can create a system which can be used with commandblocks to say, give players random lootboxes and redeem them. The concept is to create a command that generates a random number from 1 to 100. If the number is 1 (thus, the chance of being chosen is \\(\frac{1}{100}\\)), then we award a player with some reward, say 64 diamonds.

To do this, we'll declare two commands:

```mccmd
/randomnumber        - returns a random number between 1 and 99 (inclusive)
/givereward <player> - gives a player 64 diamonds and broadcasts it in the chat
```

Since we're declaring commands that are to be used in `/execute`, we must ensure that these commands are registered in your plugin's `onLoad()` method. First, we write our implementation for `/randomnumber`. It is fairly straight forward using Java's `ThreadLocalRandom` to generate a random number:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:resultingcommandexecutor2}}
```

Now we write our implementation for `/givereward`. In this example, we use the `EntitySelectorArgument` to select a single player. We cast it to `Player` and then add the items to their inventory.

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:resultingcommandexecutor3}}
```

-----

Now that we've declared these commands, we can now use them in practice. We can use a command block to store a random number under the scoreboard score `randVal` for a player called `SomePlayer`, by executing the command `/randomnumber`. Since `/randomnumber` **returns** an integer, this value is stored in the scoreboard score:

```mccmd
/execute store result score SomePlayer randVal run randomnumber
```

To check if the random number is equal to 1, we can use the `/execute if` command. If their score stored in `randVal` matches 1, then we run the `/givereward` command.
```mccmd
/execute if score SomePlayer randVal matches 1 run givereward SomePlayer
```

</div>