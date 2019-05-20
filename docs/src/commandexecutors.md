# Command executors

> **Developer's Note:**
>
> This section can be a little bit difficult to follow. If you only want the bare basic features _(executes a command)_, read the section below on _Normal command executors_ - this behaves almost identical to regular command registration in Bukkit.

-----

The CommandAPI provides two separate command executors which are lambdas which execute the code you want when a command is called. These are the classes `CommandExecutor` _(Not to be confused with Bukkit's `CommandExecutor` class)_, which just runs the contents of a command, and `ResultingCommandExecutor` that returns an integral _(whole number)_ result.

> **Developer's Note:**
> 
> In general, you need not focus too much on what type of command executor to implement. If you know for certain that you're going to be using your command with command blocks, just ensure you return an integer at the end of your declared command executor. Java will infer the type _(whether it's a CommandExecutor or ResultingCommandExecutor)_ automatically, so feel free to return an integer or not. 

## Normal command executors

Command executors are of the following format, where `sender` is a [`CommandSender`](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/command/CommandSender.html), and `args` is an `Object[]`, which represents arguments which are parsed by the CommandAPI.

```java
(sender, args) -> {
  //Code here  
};
```

With normal command executors, these do not need to return anything. By default, this will return a _success value_ of 1 if it runs successfully, and a _success value_ of 0 if it runs unsuccessfully, either by throwing an exception _(RuntimeException)_ or by forcing the command to fail.

## Forcing commands to fail

