# Single command conversion (with arguments)

For even finer control when converting a single command, you can provide the list of arguments that are required to run the command! This lets you use the command UI in converted commands as you see fit. Before we explain how to do this in detail, let's first take a look at an example of this in action.

<div class="example">

### Example - Converting EssentialsX's /speed command

EssentialsX includes a command `/speed` which lets you change the current speed that a player can move at. The command format is the following:

```mccmd
/speed <speed>
/speed <speed> <target>
/speed <walk/fly> <speed>
/speed <walk/fly> <speed> <target>
```

Which means you can run any of the following commands:

```mccmd
/speed 5
/speed 2 Notch
/speed fly 6
/speed walk 3 Notch
```

By looking at this, we can see that:

- `<speed>` is a number. By using the command, we can determine that the range of values is between 0 and 10 (inclusive).
- `<target>` is a player
- `<walk/fly>` don't change - these are "fixed" values.

We can represent this using the following `config.yml` file:

```yaml
verbose-outputs: false
create-dispatcher-json: false
plugins-to-convert:
  - Essentials:
    - speed <speed>[0..10]
    - speed <target>[minecraft:game_profile]
    - speed (walk|fly) <speed>[0..10]
    - speed (walk|fly) <speed>[0..10] <target>[minecraft:game_profile]
```

Using this, we can display options, such as "fly" and "walk", as well as optional targets ("Skepter"):

![A command "/execute run speed" with argument suggestions for entity selectors, as well as "fly" and "walk"](./images/speedoptions.png)

Additionally, we can apply limits to the numbers that can be provided. For example, here we limit the number to a value between 0 to 10. If a value is outside of that range, and error is shown to the user:

![A command "/execute run speed walk 15" with an error displaying `"Long must not be more than 10, found 15 at position 24: ...peed walk <--[HERE]`](./images/speedlimit.png)

</div>

-----

## Command argument syntax

The argument syntax is a little tricky to get the hang of at the beginning, but it should be fairly straight forward. There are two main types of arguments that you can have:

### Literal arguments

Literal arguments are arguments with "fixed" values, such as `walk` or `fly` from our example above. To declare a literal value, place brackets around the value. For example:

```mccmd
(walk)
```

To have multiple different literals, place a pipe symbol `|` between each entry within the brackets. For example:

```mccmd
(walk|fly)
```

### Named arguments

Named arguments must have a name, declared in angled brackets `<name>`, followed by the type of the argument in square brackets `[type]`. In the example above, we had a named argument `<target>`, with the argument type as a player: `[minecraft:game_profile]`.

The name in the argument can be whatever you want, but it is recommended to keep it as a lowercase value consisting only of letters.

**The following argument types are highly recommended** and are very likely to be compatible with every plugin command that you may want to convert:

| Type                  | Description                                                  |
| --------------------- | ------------------------------------------------------------ |
| `api:entity`          | An single entity (e.g. `@e[limit=1]`)                        |
| `api:entities`        | Many entities (e.g. `@e`)                                    |
| `api:player`          | A single player (e.g. `Notch` or `@r`)                       |
| `api:players`         | Many players (e.g. `@a`)                                     |
| `api:greedy_string`   | An unlimited amount of text. This can only be used as the last entry of a list of arguments |
| `brigadier:bool`      | A Boolean value `true` or `false`                            |
| `brigadier:double`    | A decimal number                                             |
| `brigadier:float`     | A decimal number                                             |
| `brigadier:integer`   | A whole number                                               |
| `brigadier:long`      | A whole number                                               |
| `brigadier:string`    | A single word                                                |
| `minecraft:block_pos` | A location of x, y and z coordinates (whole numbers)         |

In the example above, we used the a "range type" in the form `[0..10]`. This is a special argument type that will conform to `brigader:long` or `brigader:double` and apply a limit to the values that can be entered.

<div class="example">

### Example - Declaring"range type" arguments

To declare the range \\(10 \le x \le 50\\) (a value must be between 10 and 50 (inclusive)):

```mccmd
<name>[10..50]
```

To declare the range \\(10 \le x\\) (a value must be bigger than or equal to 10):

```mccmd
<name>[10..]
```

To declare the range \\(x \le 50\\) (a value must be less than or equal to 50):

```mccmd
<name>[..50]
```

To declare the range \\(0 \le x \le 1\\), where \\(x\\) is a decimal value:

```mccmd
<name>[0.0..1.0]
```

To declare a value \\(x\\) that can take any range of values and is a decimal number:

```mccmd
<name>[brigadier:double]
```

</div>

-----

## List of all supported argument types

The list of types are based on [the list of argument types from the Minecraft Wiki](https://minecraft.gamepedia.com/Argument_types), with a few changes. The complete list that the CommandAPI supports is as follows:

| Type                           | Description                                                  |
| ------------------------------ | ------------------------------------------------------------ |
| `api:advancement`              | An advancement                                               |
| `api:biome`                    | A biome                                                      |
| `api:entity`                   | An single entity (e.g. `@e[limit=1]`)                        |
| `api:entities`                 | Many entities (e.g. `@e`)                                    |
| `api:greedy_string`            | An unlimited amount of text. This can only be used as the last entry of a list of arguments |
| `api:loot_table`               | A loot table                                                 |
| `api:player`                   | A single player (e.g. `Notch` or `@r`)                       |
| `api:players`                  | Many players (e.g. `@a`)                                     |
| `api:recipe`                   | A recipe                                                     |
| `api:sound`                    | A sound effect                                               |
| `api:text`                     | Text encased in quotes: `"text with spaces"`                 |
| `brigadier:bool`               | A Boolean value `true` or `false`                            |
| `brigadier:double`             | A decimal number                                             |
| `brigadier:float`              | A decimal number                                             |
| `brigadier:integer`            | A whole number                                               |
| `brigadier:long`               | A whole number                                               |
| `brigadier:string`             | A single word                                                |
| `minecraft:angle`              | A yaw angle in degrees (from -180.0 to 179.9)                |
| `minecraft:block_pos`          | A location of x, y and z coordinates (whole numbers)         |
| `minecraft:block_predicate`    | A block predicate                                            |
| `minecraft:block_state`        | A block type (e.g. `stone`)                                  |
| `minecraft:color`              | A chat color (e.g. `red`, `green`)                           |
| `minecraft:column_pos`         | A location of x and z coordinates (whole numbers)            |
| `minecraft:component`          | Raw JSON text                                                |
| `minecraft:dimension`          | A dimension/world, (e.g. `minecraft:overworld`)              |
| `minecraft:entity`             | An entity (e.g. `Notch`)                                     |
| `minecraft:entity_summon`      | An entity type (e.g. `cow`, `wither`)                        |
| `minecraft:float_range`        | A range of decimal numbers                                   |
| `minecraft:function`           | A datapack function                                          |
| `minecraft:game_profile`       | A player (e.g. `Notch`)                                      |
| `minecraft:int_range`          | A range of whole numbers                                     |
| `minecraft:item_enchantment`   | An enchantment (e.g. `unbreaking`)                           |
| `minecraft:item_predicate`     | An item predicate                                            |
| `minecraft:item_stack`         | An item (e.g. `stick`)                                       |
| `minecraft:message`            | A plain text message which can have target selectors (e.g. `Hello @p`). This can only be used as the last entry of a list of arguments |
| `minecraft:mob_effect`         | A potion effect (e.g. `speed`, `jump_boost`)                 |
| `minecraft:nbt_compound_tag`   | Raw compound NBT in SNBT format                              |
| `minecraft:objective`          | An objective name (e.g. `temperature`)                       |
| `minecraft:objective_criteria` | An objective criteria (e.g. `deaths`)                        |
| `minecraft:operation`          | An operation symbol (e.g. `+=`, `*=`)                        |
| `minecraft:particle`           | A particle (e.g. `crit`, `flame`)                            |
| `minecraft:rotation`           | A rotation of yaw and pitch values (e.g. `~ ~`)              |
| `minecraft:score_holder`       | A score holder (e.g. `Notch`)                                |
| `minecraft:scoreboard_slot`    | A scoreboard slot (e.g. `sidebar`)                           |
| `minecraft:swizzle`            | A collection of axes (e.g. `xyz`, `xz`)                      |
| `minecraft:team`               | A team name (e.g. `hunters`)                                 |
| `minecraft:time`               | A duration of time (e.g. `2d`)                               |
| `minecraft:uuid`               | A UUID (e.g. `dd12be42-52a9-4a91-a8a1-11c01849e498`)         |
| `minecraft:vec2`               | A location of x and z coordinates (decimal numbers)          |
| `minecraft:vec3`               | A location of x, y and z coordinates (decimal numbers)       |

<!-- api:environment exists, but is deprecated and shouldn't be used anymore! -->
