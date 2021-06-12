# Internal CommandAPI

The CommandAPI does a lot of stuff "behind the scenes". This internal CommandAPI section will go into detail about what the CommandAPI does, how it's implemented and why it has been implemented like that.

-----

## Argument identifiers

The CommandAPI's arguments are basically representations of the different arguments that the Minecraft Command Data protocol handles. These are outlined in the table below:

| Identifier | CommandAPI argument |
|-|-|
| `brigadier:bool` | [`BooleanArgument`](./primitivearguments.md#boolean-arguments) |
| `brigadier:double` | [`DoubleArgument`](./primitivearguments.md#numerical-arguments) |
| `brigadier:float` | [`FloatArgument`](./primitivearguments.md#numerical-arguments) |
| `brigadier:integer` | [`IntegerArgument`](./primitivearguments.md#numerical-arguments) |
| `brigadier:long` | [`LongArgument`](./primitivearguments.md#numerical-arguments) |
| `brigadier:string` | [`StringArgument`](./stringarguments.md#string-argument)<br />[`TextArgument`](./stringarguments.md#text-argument)<br />[`GreedyStringArgument`](./stringarguments.md#greedy-string-argument)<br />[`CustomArgument<T>`](./customarguments.md) |
| `minecraft:angle` | [`AngleArgument`](./angleargument.md) |
| `minecraft:block_pos` | [`LocationArgument`](./locationargument.md#location-3d-space)<br />(`LocationType.BLOCK_POSITION`) |
| `minecraft:block_predicate` | [`BlockPredicateArgument`](./blockpredicateargs.md) |
| `minecraft:block_state` | [`BlockStateArgument`](./blockstatearguments.md) |
| `minecraft:color` | [`ChatColorArgument`](./chatarguments.md#chat-color-argument) |
| `minecraft:column_pos` | [`Location2DArgument`](./locationargument.md#location-2d-space) <br />(`LocationType.BLOCK_POSITION`) |
| `minecraft:component` | [`ChatComponentArgument`](./chatarguments.md#chat-component-argument) |
| `minecraft:dimension` | [`EnvironmentArgument`](./environmentargs.md) |
| `minecraft:entity` | [`EntitySelectorArgument`](./entityarguments.md#entity-selector-argument) |
| `minecraft:entity_anchor` | |
| `minecraft:entity_summon` | [`EntityTypeArgument`](./entityarguments.md#entity-type-argument) |
| `minecraft:float_range` | [`FloatRangeArgument`](./rangedarguments.md#the-integerrange--floatrange-class) |
| `minecraft:function` | [`FunctionArgument`](./functionwrapper.md) |
| `minecraft:game_profile` | [`PlayerArgument`](./entityarguments.md#player-argument) |
| `minecraft:game_profile` | [`OfflinePlayerArgument`](./entityarguments.md#offlineplayer-argument) |
| `minecraft:int_range` | [`IntegerRangeArgument`](./rangedarguments.md#the-integerrange--floatrange-class) |
| `minecraft:item_enchantment` | [`EnchantmentArgument`](./enchantmentargument.md) |
| `minecraft:item_predicate` | [`ItemStackPredicateArgument`](./itemstackpredicateargs.md) |
| `minecraft:item_slot` | |
| `minecraft:item_stack` | [`ItemStackArgument`](./itemstackarguments.md) |
| `minecraft:message` | [`ChatArgument`](./chatarguments.md#chat-argument) |
| `minecraft:mob_effect` | [`PotionEffectArgument`](./potionarguments.md) |
| `minecraft:nbt` | |
| `minecraft:nbt_compound_tag` | [`NBTCompoundArgument`](./nbtarguments.md) |
| `minecraft:nbt_path` | |
| `minecraft:nbt_tag` | |
| `minecraft:objective` | [`ObjectiveArgument`](./objectivearguments.md#objective-argument) |
| `minecraft:objective_criteria` | [`ObjectiveCriteriaArgument`](./objectivearguments.md#objective-criteria-argument) |
| `minecraft:operation` | [`MathOperationArgument`](./mathoperationarguments.md) |
| `minecraft:particle` | [`ParticleArgument`](./particlearguments.md) |
| `minecraft:resource_location` | [`AdvancementArgument`](./advancementargument.md)<br />[`BiomeArgument`](./biomeargument.md)<br />[`CustomArgument<T>`](./customarguments.md)<br />[`LootTableArgument`](./loottableargument.md)<br />[`RecipeArgument`](./recipeargument.md)<br />[`SoundArgument`](./soundargument.md) |
| `minecraft:rotation` | [`RotationArgument`](./rotationargs.md) |
| `minecraft:score_holder` | [`ScoreHolderArgument`](./scoreboardarguments.md#score-holder-argument) |
| `minecraft:scoreboard_slot` | [`ScoreboardSlotArgument`](./scoreboardarguments.md#scoreboard-slot-argument) |
| `minecraft:swizzle` | [`AxisArgument`](./axisarg.md) |
| `minecraft:team` | [`TeamArgument`](./teamarguments.md) |
| `minecraft:time` | [`TimeArgument`](./timeargs.md) |
| `minecraft:uuid` | [`UUIDArgument`](./uuidargs.md) |
| `minecraft:vec2` | [`Location2DArgument`](./locationargument.md#location-2d-space)<br />(`LocationType.PRECISE_POSITION`) |
| `minecraft:vec3` | [`LocationArgument`](./locationargument.md#location-3d-space)<br />(`LocationType.PRECISE_POSITION`) |

There are a few arguments that aren't implemented. Here's why:

- `minecraft:entity_anchor` - This argument only has two values: `eyes` and `feet`. It's incredibly unnecessary for any other purpose and is easier to implement with a `MultiLiteralArgument`.

- `minecraft:item_slot` - Bukkit's implementation of item slot numbers differs very wildly to Minecraft's implementation of item slot numbers. This difference makes it near-impossible to have a suitable middle-ground for item slot numbers that ensures that invalid numbers cannot be passed to the wrong inventory type. An implementation of this would require a rewrite of the current system to maintain proper inventory slot access safety.
- `minecraft:nbt`, `minecraft:nbt_path`, `minecraft:nbt_tag` - You've got the `NBTCompoundArgument`, that's good enough, right? ¯\\\_(ツ)\_/¯

-----

## Reloading datapacks

During the initialization of Minecraft 1.16+ servers, the CommandAPI uses a custom datapack reloading sequence as opposed to the normal Vanilla Minecraft datapack reloading method. The CommandAPI's method uses the server's current command dispatcher object as opposed to a new one, which allows datapacks to use commands registered by the CommandAPI. This can be invoked using the following method:

```java
CommandAPI.reloadDatapacks();
```

