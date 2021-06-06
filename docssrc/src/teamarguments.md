# Team arguments

The `TeamArgument` class interacts with the Minecraft scoreboard and represents a team. Similar to the `ObjectiveArgument` class, the `TeamArgument` class must be casted to a String.

<div class="example">

### Example - Toggling friendly fire in a team

Let's say we want to create a command to toggle the state of friendly fire in a team. We want a command of the following form

```mccmd
/togglepvp <team>
```

To do this, given a team we want to use the `setAllowFriendlyFire(boolean)` function. As with the `ObjectiveArgument`, we must convert the `String` into a `Team` object.

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:teamarguments}}
```

</div>