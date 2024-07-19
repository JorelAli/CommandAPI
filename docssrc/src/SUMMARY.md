# Summary

[Introduction](./intro.md)

-----

# Server Owner Usage

- [Installation for server owners](./installation.md)
- [Configuration for server owners](./config.md)
- [Command conversion](./conversionforowners.md)
  - [Single command conversion](./conversionforownerssingle.md)
  - [Single command conversion (with arguments)](./conversionforownerssingleargs.md)
  - [Entity selectors](./conversionentityselectors.md)
- [Skipping proxy senders](./skippingproxysenders.md)

-----

# CommandAPI Setup

- [Setting up your development environment](./setup_dev.md)
- [Shading the CommandAPI in your plugins](./setup_shading.md)
- [Using the annotation system](./setup_annotations.md)

# Creating Commands

- [Command registration](./commandregistration.md)
- [Command unregistration](./commandunregistration.md)
- [Command executors](./commandexecutors.md)
  - [Normal command executors](./normalexecutors.md)
  - [Proxied commandsenders](./proxysender.md)
  - [Native commandsenders](./native.md)
  - [Resulting command executors](./resultingcommandexecutors.md)
  - [Handling command failures](./commandfailures.md)
- [Command arguments](./arguments.md)
  - [CommandArguments](./commandarguments.md)
  - [Optional arguments](./optional_arguments.md)
  - [Listed arguments](./listed.md)
  - [Argument suggestions](./argumentsuggestions.md)
    - [The SuggestionsInfo record](./suggestionsinfo.md)
    - [String argument suggestions](./stringargumentsuggestions.md)
    - [Safely typed argument suggestions](./safeargumentsuggestions.md)
    - [Argument suggestions with tooltips](./tooltips.md)
    - [Asynchronous suggestions](./asyncsuggestions.md)
  - [Argument types](./argumenttypes.md)
    - [Primitive arguments](./argument_primitives.md)
    - [Ranged arguments](./argument_range.md)
    - [String arguments](./argument_strings.md)
    - [Positional arguments](./category_positional_arguments.md)
      - [Location arguments](./argument_locations.md)
      - [Rotation arguments](./argument_rotation.md)
      - [AxisArguments](./argument_axis.md)
    - [Chat arguments](./argument_chats.md)
      - [Spigot chat arguments](./argument_chat_spigot.md)
      - [Adventure chat arguments](./argument_chat_adventure.md)
      - [Chat preview](./chatpreview.md)
    - [Entity & player arguments](./argument_entities.md)
    - [Scoreboard arguments](./category_scoreboard_arguments.md)
      - [Scoreboard arguments](./argument_scoreboards.md)
      - [Objective arguments](./argument_objectives.md)
      - [Team arguments](./argument_team.md)
    - [Miscellaneous arguments](./category_miscellaneous_arguments.md)
      - [Angle arguments](./argument_angle.md)
      - [Advancement arguments](./advancementargument.md)
      - [Biome arguments](./argument_biome.md)
      - [BlockState arguments](./argument_blockstate.md)
      - [Enchantment arguments](./argument_enchantment.md)
      - [Itemstack arguments](./argument_itemstack.md)
      - [LootTable argument](./argument_loottable.md)
      - [MathOperation arguments](./argument_mathoperation.md)
      - [NamespacedKey arguments](./argument_namespacedkey.md)
      - [Particle arguments](./argument_particles.md)
        - [Particle data (before 1.20.5)](./argument_particle_old.md)
        - [Particle data (1.20.5+)](./argument_particle_new.md)
      - [Potion effect arguments](./argument_potion.md)
      - [Recipe arguments](./argument_recipe.md)
      - [Sound arguments](./argument_sound.md)
      - [Time arguments](./argument_time.md)
      - [UUID arguments](./argument_uuid.md)
      - [World arguments](./argument_world.md)
    - [Predicate arguments](./predicateargs.md)
      - [Block predicate arguments](./argument_blockpredicate.md)
      - [ItemStack predicate arguments](./argument_itemstackpredicate.md)
    - [NBT arguments](./argument_nbt.md)
    - [Literal arguments](./category_literal_arguments.md)
      - [Literal arguments](./argument_literal.md)
      - [Multi literal arguments](./argument_multiliteral.md)
    - [List arguments](./argument_list.md)
    - [Map arguments](./argument_map.md)
    - [Command arguments](./argument_command.md)
    - [Custom arguments](./argument_custom.md)
- [Functions & Tags](./functions.md)
  - [Setting up functions & tags](./functionsetup.md)
  - [The SimpleFunctionWrapper class](./simplefunctionwrapper.md)
  - [The FunctionWrapper class](./functionwrapper.md)
  - [Function arguments](./argument_function.md)
- [Permissions](./permissions.md)
- [Requirements](./requirements.md)
- [Aliases](./aliases.md)
- [Help](./help.md)
- [Subcommands](./subcommands.md)
- [Command trees](./commandtrees.md)

# Annotation-based Commands

- [Annotation-based commands](./annotationsintro.md)
- [Annotations](./annotations.md)
- [Registering annotation-based commands](./registeringannotations.md)

# Kotlin-based Commands

- [Kotlin-based commands](./kotlinintro.md)
- [Using the DSL](./kotlindsl.md)
- [Delegated properties](./delegated_properties.md)

# Testing Framework

- [Testing Commands](./test_intro.md)
- [Set Up](./test_setup.md)
- [Loading Test CommandAPI](./test_loadmockplugin.md)
- [Testing Utilities](./test_utilities.md)

-----

# Velocity

- [Velocity](./velocity_intro.md)

-----

# CommandAPI Utilities

- [Command conversion](./conversion.md)
- [Plugin reloading](./reloading.md)

# Internal CommandAPI

- [Internal CommandAPI](./internal.md)
- [Brigadier + CommandAPI](./brigadier.md)
- [Brigadier Suggestions](./brigadiersuggestions.md)

-----

# Java Tips

- [Predicate tips](./predicatetips.md)

-----

# CommandAPI Contribution

- [Introduction](./contributionintro.md)
- [Project Structure](./projectstructure.md)

-----

- [Upgrading guide](./upgrading.md)
  - [Upgrading guide (pre-9.0.0)](./upgrading_old.md)

[FAQ](./faq.md)

[Incompatible version information](./incompatibleversions.md)

[Troubleshooting](./troubleshooting.md)

-----

[Afterword](./afterword.md)

