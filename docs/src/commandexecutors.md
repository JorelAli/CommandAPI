# Command executors

> **Developer's Note:**
>
> This section can be a little bit difficult to follow. If you only want the bare basic features _(executes a command)_, read the section on _Normal command executors_ - this behaves very similar to the `onCommand` method in Bukkit.

-----

The CommandAPI provides various command executors which are lambdas which execute the code you want when a command is called. With a lot of simplification, there are two main types of command executors: 

- Ones that just runs the command (let's call it a _normal command executor_)
- Ones that returns an integer as a result (let's call it a _resulting command executor_)

> **Developer's Note:**
> 
> In general, you need not focus too much on what type of command executor to implement. If you know for certain that you're going to be using your command with command blocks, and specifically want to state whether a command returns a value, just ensure you return an integer at the end of your declared command executor. Java will infer the type _(whether it's a normal command executor or a resulting command executor)_ automatically, so feel free to return an integer or not. 

In addition to these two types of command executors, there are ways to restrict the execution of commands to certain `CommandSender` subclasses. In other words, you can make commands executable by players in game only for instance. These restrictions are covered in more detail in [Normal command executors](./normalexecutors.html).

