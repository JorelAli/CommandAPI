# Things to note in the next documentation:

- Refactored LocationArgument's LocationType to `io.github.jorelali.commandapi.api.arguments.LocationType` instead of `io.github.jorelali.commandapi.api.arguments.LocationArgument.LocationType`.
  This would be a change that would require developers to change the imports and make code incompatible with previous versions of the CommandAPI.
- `LocationType.PRECISE_POSITION` Doesn't exist for Minecraft 1.13 for `Location2DArgument`, but does for 1.13.1+
- Commands can have prefixes such as `a:b`

