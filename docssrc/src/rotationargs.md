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

### Example: Rotate an armor stand head

Say we want to make an armor stand look in a certain direction. To do this, we'll use the following command:

```
/rotate <rotation> <target>
```

To do this, we'll use the rotation from the `RotationArgument` and select an entity using the `EntitySelectorArgument`, with `EntitySelector.ONE_ENTITY`. We then check if our entity is an armor stand and if so, we set its head pose to the given rotation.

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("rotation", new RotationArgument());
arguments.put("target", new EntitySelectorArgument(EntitySelector.ONE_ENTITY));

new CommandAPICommand("rotate")
    .withArguments(arguments)
    .executes((sender, args) -> {
        Rotation rotation = (Rotation) args[0];
        Entity target = (Entity) args[1];

        if(target instanceof ArmorStand) {
            ArmorStand a = (ArmorStand) target;
            a.setHeadPose(new EulerAngle(Math.toRadians(rotation.getPitch()), Math.toRadians(rotation.getYaw() - 90), 0));
        }
    })
    .register();
```

Note how the head pose requires an `EulerAngle` as opposed to a pitch and yaw. To account for this, we convert our rotation (which is in degrees) into an `EulerAngle` in radians.

</div>