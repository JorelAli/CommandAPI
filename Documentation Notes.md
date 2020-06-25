# Things to note in the next documentation:

- `LocationType.PRECISE_POSITION` Doesn't exist for Minecraft 1.13 for `Location2DArgument`, but does for 1.13.1+
- Commands can have prefixes such as `a:b`

-----

# Documentation plan

List of things to document

- The deprecation of `.register(...)` and the new method `CommandAPICommand`

  - `.executesPlayer` and `.executesEntity`
- Update _all of the examples_ to fit the new registration system

- New arguments:
  - [x] ChatArgument
    - [x] Note about spigot dependency
    - [x] Note about "Raw JSON text"
  - [x] EnvironmentArgument
    - [x] EnvironmentException - EnvironmentArgument only compatible with 1.13.1+
  - [x] Ranged arguments:
    - [x] FloatRangeArgument
    - [x] IntegerRangeArgument
    - [x] Mention how to use various Ranged classes
  - [x] LongArgument
  - [x] Math operation argument
    - [x] All of the technicalities with math operation argument:	
      - [x] x < y -> min(x, y)
      - [x] x > y -> max(x, y)
      - [x] x >< y -> y
      - [x] x = y -> y
  - [x] NBTCompoundArgument
    - [x] NBTNotFoundException, and the dependency on the NBTAPI
  - [x] TimeArgument
    - [x] TimeArgumentException - TimeArgument only compatible with 1.14+
  - [ ] Position based arguments:
    - [x] Location2DArgument
      - [x] Mention how to use Location2D, and how it extends Location so can be used wherever location is used
    - [x] RotationArgument
      - [x] Mention of Rotation class
      - [ ] Test the rotation example + give it a name
    - [x] AxisArgument
    - [x] (Move LocationArgument section into "position based" section)
  - [x] Scoreboard based arguments:
    - [x] ScoreboardSlotArgument
      - [x] Extra mention about how to use ScoreboardSlot class
    - [x] ScoreHolderArgument
      - [x] Mention ScoreHolderTypes and their use
    - [x] TeamArgument
    - [x] ObjectiveArgument
    - [x] ObjectiveCriteriaArgument
- Things that weren't documented before but need to be:

  - [x] InvalidRangeException, for int,long,float,double arguments with ranges
  - [ ] Update documentation for `LiteralArgument`s, based off of the new research from SuperLiterals:
    - [ ] They can be "entered", but not accepted _(i.e. pressing the enter button still sends it to the server)_
    - [ ] Can consist of any non-whitespace character
    - [ ] **Highly recommend** just using a String argument instead, _(except can't use any non-whitespace character)_.
- [x] Update afterword
- Examples: https://github.com/JorelAli/1.13-Command-API/issues/69#issuecomment-525350311

- Gifs of each argument showcasing what its possibilities are like, similar to:

  ![better arguments](https://raw.githubusercontent.com/JorelAli/1.13-Command-API/master/images/explode.gif)