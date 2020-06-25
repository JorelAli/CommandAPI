# MathOperation arguments

The CommandAPI's `MathOperationArgument` is used to represent the Minecraft scoreboard arithmetic operation to alter scoreboard scores. Since there is no default representation in the Bukkit API, the CommandAPI provides the `MathOperation` class to represent each operation:

| Symbol (in Minecraft) | MathOperation enum value |
| :-------------------: | ------------------------ |
|       \\(+=\\)        | `MathOperation.ADD`      |
|       \\(-=\\)        | `MathOperation.SUBTRACT` |
|       \\(*=\\)        | `MathOperation.MULTIPLY` |
|       \\(/=\\)        | `MathOperation.DIVIDE`   |
|      \\(\\%=\\)       | `MathOperation.MOD`      |
|        \\(=\\)        | `MathOperation.ASSIGN`   |
|        \\(<\\)        | `MathOperation.MIN`      |
|        \\(>\\)        | `MathOperation.MAX`      |
|       \\\(><\\)       | `MathOperation.SWAP`     |

-----

### Example - Changing a player's level

