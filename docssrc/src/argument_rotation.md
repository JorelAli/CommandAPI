# Rotation arguments

![An image of a rotation argument showing /rotationargument 90 180](./images/arguments/rotation.png)

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

```mccmd
/rotate <rotation> <target>
```

To do this, we'll use the rotation from the `RotationArgument` and select an entity using the `EntitySelectorArgument.OneEntity` class. We then check if our entity is an armor stand and if so, we set its head pose to the given rotation.

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentRotation1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentRotation1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentRotation1}}
```

</div>

Note how the head pose requires an `EulerAngle` as opposed to a pitch and yaw. To account for this, we convert our rotation (which is in degrees) into an `EulerAngle` in radians.

</div>
