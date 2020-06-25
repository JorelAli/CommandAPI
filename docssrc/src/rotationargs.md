# Rotation arguments

The `RotationArgument` allows users to specify a pair of pitch and yaw coordinates. By default (using the `~` symbol), this refers to the player's current pitch and yaw of where they are looking at.

The `RotationArgument` class returns a `Rotation` object, which consists of the following methods:

| Method name                  | What it does                                        |
| ---------------------------- | --------------------------------------------------- |
| `float getPitch()`           | Returns a player's pitch (up and down rotation)     |
| `float getYaw()`             | Returns a player's yaw (left and right rotation)    |
| `float getNormalizedPitch()` | Returns a player's pitch between -90 and 90 degrees |
| `float getNormalizedYaw()`   | Returns a player's yaw between -180 and 180 degrees |

<div class="example">

### Example: ???

Say we want to make an armor stand look in a certain direction.

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("rotation", new RotationArgument());
arguments.put("target", new PlayerArgument());

new CommandAPICommand("rotate")
    .withArguments(arguments)
    .executes((sender, args) -> {
        Rotation rotation = (Rotation) args[0];
        Player target = (Player) args[1];

        Location newLocation = target.getLocation();
        float newPitch = Location.normalizePitch(newLocation.getPitch() + rotation.getPitch());
        float newYaw = Location.normalizeYaw(newLocation.getYaw() + rotation.getYaw());

        newLocation.setPitch(newPitch);
        newLocation.setYaw(newYaw);

        target.teleport(newLocation);
    })
    .register();
```

</div>