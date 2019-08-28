# Things to note in the next documentation:

- `LocationType.PRECISE_POSITION` Doesn't exist for Minecraft 1.13 for `Location2DArgument`, but does for 1.13.1+
- Commands can have prefixes such as `a:b`

-----

# Documentation plan

List of things to document

- New arguments:
  - [ ] ChatArgument
    - [ ] Note about spigot dependency
  - [ ] EnvironmentArgument
    - [ ] EnvironmentException - EnvironmentArgument only compatible with 1.13.1+
  - [ ] Ranged arguments:
    - [ ] FloatRangeArgument
    - [ ] IntegerRangeArgument
    - [ ] Mention how to use various Ranged classes
  - [ ] ItemSlotArgument
  - [x] LongArgument
  - [ ] NBTCompoundArgument
    - [ ] NBTNotFoundException, and the dependency on the NBTAPI
  - [ ] TimeArgument
    - [ ] TimeArgumentException - TimeArgument only compatible with 1.14+
  - [ ] Position based arguments:
    - [ ] Location2DArgument
      - [ ] Mention how to use Location2D, and how it extends Location so can be used wherever location is used
    - [ ] RotationArgument
      - [ ] Mention of Rotation class
    - [ ] AxisArgument
    - [ ] (Move LocationArgument section into "position based" section)
  - [ ] Scoreboard based arguments:
    - [ ] ScoreboardSlotArgument
      - [ ] Extra mention about how to use ScoreboardSlot class
    - [ ] ScoreHolderArgument
      - [ ] Mention ScoreHolderTypes and their use
    - [ ] TeamArgument
    - [ ] ObjectiveArgument
    - [ ] ObjectiveCriteriaArgument
- Things that weren't documented before but need to be:
  - [ ] InvalidRangeException, for int,long,float,double arguments with ranges
- Update afterword