# Location arguments

![](./images/arguments/loc.png)

In the CommandAPI, there are two arguments used to represent location. The `LocationArgument` argument, which represents a 3D location \\( (x, y, z) \\) and the `Location2DArgument`, which represents 2D location \\( (x, z) \\). 

-----

## Location (3D space)

The `LocationArgument` class is used to specify a location in the **command sender's current world**, returning a Bukkit `Location` object. It allows the user to enter three numbers as coordinates, or use relative coordinates (i.e. the `~` and `^` operators).

The `LocationArgument` constructor requires a `LocationType`, which specifies the type of location that is accepted by the command. The `LocationType` enum consists of two values:

### `LocationType.BLOCK_POSITION`
`BLOCK_POSITION` refers to integer block coordinates. When in-game as a player, the suggested location is the coordinates of block you are looking at when you type the command.

![BLOCK_POSITION](https://user-images.githubusercontent.com/42968178/50574833-92ef9e00-0df0-11e9-9ae5-59235b1bb8cb.png)

### `LocationType.PRECISE_POSITION`
`PRECISE_PRECISION` uses exact coordinates, using the `double` primitive type. When in-game as a player, the suggested location is the exact coordinates of where your cursor is pointing at when you type the command.

 ![PRECISE_POSITION](https://user-images.githubusercontent.com/42968178/50574841-b6b2e400-0df0-11e9-9003-481b139d190f.png)

If no `LocationType` is provided, **the `LocationArgument` will use `PRECISE_POSITION` by default**.

-----

<div class="example">

### Example - Break block using coordinates

We can declare a simple command to break a block:

```mccmd
/break <location>
```

Simply put, given the coordinates provided to the command, "break" the block by setting it's type to `Material.AIR`. For this example, we're referring to block specific coordinates, so we want to use `LocationType.BLOCK_POSITION`:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:locationarguments}}
```

</div>

-----

## Location (2D space)

![](./images/arguments/loc2d.png)

The `Location2DArgument` is pretty much identical in use to the `LocationArgument` for 3D coordinates, except instead of returning a `Location` object, it instead returns a `Location2D` object that extends `Location` (thus, being compatible anywhere you would normally be able to use `Location`).