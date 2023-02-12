# Internal CommandAPI

The CommandAPI does a lot of stuff "behind the scenes". This internal CommandAPI section will go into detail about what the CommandAPI does, how it's implemented and why it has been implemented like that.

-----

## Argument identifiers

The CommandAPI's arguments are representations of the different arguments that the Minecraft Command Data protocol handles. These are outlined in the table below:

| Identifier | CommandAPI argument |
|-|-|
| `brigadier:bool` | [`BooleanArgument`](./argument_primitives.md#boolean-arguments) |
| `brigadier:double` | [`DoubleArgument`](./argument_primitives.md#numerical-arguments) |
| `brigadier:float` | [`FloatArgument`](./argument_primitives.md#numerical-arguments) |
| `brigadier:integer` | [`IntegerArgument`](./argument_primitives.md#numerical-arguments) |
| `brigadier:long` | [`LongArgument`](./argument_primitives.md#numerical-arguments) |
| `brigadier:string` | [`StringArgument`](./argument_strings.md#string-argument)<br />[`TextArgument`](./argument_strings.md#text-argument)<br />[`GreedyStringArgument`](./argument_strings.md#greedy-string-argument)<br />[`CustomArgument<T>`](./argument_custom.md) |
| `minecraft:angle` | [`AngleArgument`](./argument_angle.md) |
| `minecraft:block_pos` | [`LocationArgument`](./argument_locations.md#location-3d-space)<br />(`LocationType.BLOCK_POSITION`) |
| `minecraft:block_predicate` | [`BlockPredicateArgument`](./argument_blockpredicate.md) |
| `minecraft:block_state` | [`BlockStateArgument`](./argument_blockstate.md) |
| `minecraft:color` | [`ChatColorArgument`](./argument_chats.md#chat-color-argument) |
| `minecraft:column_pos` | [`Location2DArgument`](./argument_locations.md#location-2d-space) <br />(`LocationType.BLOCK_POSITION`) |
| `minecraft:component` | [`ChatComponentArgument`](./argument_chats.md#chat-component-argument) |
| `minecraft:dimension` | [`WorldArgument`](./argument_world.md) |
| `minecraft:entity` | [`EntitySelectorArgument`](./argument_entities.md#entity-selector-argument) |
| `minecraft:entity_anchor` | |
| `minecraft:entity_summon` | [`EntityTypeArgument`](./argument_entities.md#entity-type-argument) |
| `minecraft:float_range` | [`FloatRangeArgument`](./argument_range.md#the-integerrange--floatrange-class) |
| `minecraft:function` | [`FunctionArgument`](./functionwrapper.md) |
| `minecraft:game_profile` | [`PlayerArgument`](./argument_entities.md#player-argument) |
| `minecraft:game_profile` | [`OfflinePlayerArgument`](./argument_entities.md#offlineplayer-argument) |
| `minecraft:int_range` | [`IntegerRangeArgument`](./argument_range.md#the-integerrange--floatrange-class) |
| `minecraft:item_enchantment` | [`EnchantmentArgument`](./argument_enchantment.md) |
| `minecraft:item_predicate` | [`ItemStackPredicateArgument`](./argument_itemstackpredicate.md) |
| `minecraft:item_slot` | |
| `minecraft:item_stack` | [`ItemStackArgument`](./argument_itemstack.md) |
| `minecraft:message` | [`ChatArgument`](./argument_chats.md#chat-argument) |
| `minecraft:mob_effect` | [`PotionEffectArgument`](./argument_potion.md) |
| `minecraft:nbt` | |
| `minecraft:nbt_compound_tag` | [`NBTCompoundArgument`](./argument_nbt.md) |
| `minecraft:nbt_path` | |
| `minecraft:nbt_tag` | |
| `minecraft:objective` | [`ObjectiveArgument`](./argument_objectives.md#objective-argument) |
| `minecraft:objective_criteria` | [`ObjectiveCriteriaArgument`](./argument_objectives.md#objective-criteria-argument) |
| `minecraft:operation` | [`MathOperationArgument`](./argument_mathoperation.md) |
| `minecraft:particle` | [`ParticleArgument`](./argument_particle.md) |
| `minecraft:resource_location` | [`AdvancementArgument`](./advancementargument.md)<br />[`BiomeArgument`](./argument_biome.md)<br />[`CustomArgument<T>`](./argument_custom.md)<br />[`LootTableArgument`](./argument_loottable.md)<br />[`NamespacedKeyArgument`](./argument_namespacedkey.md)<br />[`RecipeArgument`](./argument_recipe.md)<br />[`SoundArgument`](./argument_sound.md) |
| `minecraft:rotation` | [`RotationArgument`](./argument_rotation.md) |
| `minecraft:score_holder` | [`ScoreHolderArgument`](./argument_scoreboards.md#score-holder-argument) |
| `minecraft:scoreboard_slot` | [`ScoreboardSlotArgument`](./argument_scoreboards.md#scoreboard-slot-argument) |
| `minecraft:swizzle` | [`AxisArgument`](./argument_axis.md) |
| `minecraft:team` | [`TeamArgument`](./argument_team.md) |
| `minecraft:time` | [`TimeArgument`](./argument_time.md) |
| `minecraft:uuid` | [`UUIDArgument`](./argument_uuid.md) |
| `minecraft:vec2` | [`Location2DArgument`](./argument_locations.md#location-2d-space)<br />(`LocationType.PRECISE_POSITION`) |
| `minecraft:vec3` | [`LocationArgument`](./argument_locations.md#location-3d-space)<br />(`LocationType.PRECISE_POSITION`) |

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

-----

## Getting a list of registered commands

The CommandAPI doesn't store the `CommandAPICommand` objects during the main running of the server because it simply doesn't need to. Instead, it stores a list of `RegisteredCommand` objects which are defined as the following, which should be fairly self-explanatory:

```java
public record RegisteredCommand {
    String commandName();
    List<String> argsAsStr();
    Optional<String> shortDescription();
    Optional<String> fullDescription();
    String[] aliases();
    CommandPermission permission();
}
```

The `argsAsStr()` method returns a list of arguments in a string format, of the form `argName:SimpleClassName`, where `argName` is the name of the argument (the argument's node name) and `SimpleClassName` is the name of the argument class that was used to construct it (such as `IntegerArgument`).

A `List<RegisteredCommand>` can be acquired using the following method:

```java
CommandAPI.getRegisteredCommands();
```

> Note that this list does not update when commands are _unregistered_, only when commands are registered.
