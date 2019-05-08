# The FunctionWrapper

The CommandAPI includes the `FunctionWrapper` class which is a wrapper for Minecraft's functions. It allows you to execute the commands that are represented by the respective `.mcfunction` file. There most important thing to note with the `FunctionWrapper`, and that it _does not "store" the functions inside it_, instead it just _stores what the function does_. In other words, you cannot "extract" the list of commands from a `FunctionWrapper` object.

## FunctionWrapper methods

The `FunctionWrapper` class consists of two methods:

| Method            | Result on execution                                  |
| ----------------- | ---------------------------------------------------- |
| `run()`           | Executes the Minecraft function                      |
| `runAs(Entity)`   | Executes the Minecraft function as a specific Entity |

The `FunctionWrapper` also implements the `Keyed` interface, allowing you to retrieve the NamespacedKey for this function using `getKey()`.
