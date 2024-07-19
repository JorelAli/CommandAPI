# Testing Commands

When developing large projects, it is good practice to add automated tests for your code. This section of the documentation describes how to use the `commandapi-bukkit-test-toolkit` dependency along with [MockBukkit](https://github.com/MockBukkit/MockBukkit) and [JUnit](https://junit.org/junit5/) to test the usage of commands registered with the CommandAPI.

For a big-picture view, you can find example projects that include automated tests in the [CommandAPI GitHub repository](https://github.com/JorelAli/CommandAPI/tree/master/examples).

<div class="warning">

**Developer's Note:**

Many methods have not yet been implemented in the test toolkit. Most notably, only [primitive arguments](./argument_primitives.md), [String arguments](./argument_strings.md), [literal arguments](./category_literal_arguments.md), and the [`IntegerRangeArgument`](./argument_range.md) are fully implemented. The [`EntitySelectorArgument`, `PlayerArgument`, and `OfflinePlayerArgument`](./argument_entities.md) should mostly work, though [target selector arguments](https://minecraft.wiki/w/Target_selectors#Target_selector_arguments) (e.g. `@e[type=pig]`) are not yet implemented.

If a test ends up calling a method that has not yet been implemented, an `UnimplementedMethodException` will be thrown, causing the test to fail. If you see an `UnimplementedMethodException`, please tell us about it with a [GitHub Issue](https://github.com/JorelAli/CommandAPI/issues) or a message in the CommandAPI Discord. Pull requests are also always welcome!

</div>
