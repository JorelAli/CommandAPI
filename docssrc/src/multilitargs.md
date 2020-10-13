# Multi literal arguments

So far, we've described normal arguments and literal arguments. We've described the nuances with literal arguments and how they're not really "arguments", so they don't appear in the `args[]` for commands.

Now forget all of that. Multi literal arguments are the same as literal arguments but they _do_ appear in the `args[]` for commands (i.e. they are [listed](./listed.md)). Multi literal arguments are just a way better alternative to literal arguments. The multi literal argument constructor allows you to provide a `String[]` of possible values which you can use for your command declaration.

The multi literal argument has all of the same benefits of a regular literal argument - they are hardcoded options that the user must enter - they don't allow other values.

> **Developer's Note:**
>
> The only reason that `LiteralArgument` still exists is for legacy purposes. `MultiLiteralArgument` is much more recommended because it's easier to understand and implement. The `LiteralArgument` has a very slight performance improvement over the `MultiLiteralArgument`, but it's basically unnoticeable.

<div class="example">

### Example - Using multi literals to make the gamemode command

In this example, we'll show how to use multi literals to declare Minecraft's `/gamemode` command. As you can see from the example code below, the argument declaration and command declaration is the same as if you were declaring any normal argument or command.

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:multiliteralarguments}}
```

</div>

