# Functions

The CommandAPI has support to use Minecraft's [functions](https://minecraft.gamepedia.com/Function_(Java_Edtion)) within your plugins. This is handled by using a class provided by the CommandAPI called `FunctionWrapper`, which allows you to execute functions. The CommandAPI also provides support to let you run your own commands within Minecraft function files.

## Using custom commands in functions

In order to use a command from your plugin in a `.mcfunction` file, you must register your command in your plugin's `onLoad()` method, instead of the `onEnable()` method. Failure to do so will not allow the command to be registered for Minecraft functions, causing the function file to fail to load during the server startup phase.

### Example - Registering command for use in a function

```java
public class Main extends JavaPlugin {

	@Override
	public void onLoad() {
		//Commands which will be used in Minecraft functions are registered here

		CommandAPI.getInstance().register("killall", new LinkedHashMap<>(), (sender, args) -> {
            //Kills all enemies in all worlds
        	Bukkit.getWorlds()
				.forEach(w -> w.getLivingEntities()
					.forEach(e -> e.setHealth(0))
				);
        });
	}
    
    @Override
    public void onEnable() {
        //Register all other commands here
    } 
}
```




## The function argument

By far, the most complicated argument that exists in the CommandAPI. The `FunctionArgument` class is used to execute Minecraft's [functions](https://minecraft.gamepedia.com/Function_(Java_Edition)). When a user passes a function argument, the CommandAPI will look up the function and return a `FunctionWrapper[]`, which is also included with the CommandAPI. This is a list of all functions which were retrieved by the user - either an array containing a single function or an array containing a list of functions (for example, from a [tag](https://minecraft.gamepedia.com/Function_(Java_Edition)#Tags))

### The `FunctionWrapper` class

The `FunctionWrapper` class has two methods:

| Method            | Result on execution                                  |
| ----------------- | ---------------------------------------------------- |
| `run()`           | Executes the Minecraft function(s)                   |
| `runAs(Entity)`   | Executes the Minecraft function as a specific Entity |

Running the function from a `FunctionWrapper` will execute the function as declared in the respective `.mcfunction` file.

### Function command registration

In order to allow a command from your plugin to be used in a `.mcfunction` file, you **must register your command in your plugin's `onLoad()` method**, instead of the `onEnable()` method. This is due to the loading order for Minecraft functions, which takes place after the `onLoad()` method, but before the `onEnable()` method.

### Functions

#### Registering commands for Minecraft function support

```
/killall - kills all players on the server
/fly - enables flight
```

`JavaPlugin` file:

```java
public class Main extends JavaPlugin {

    //Commands which will be used in Minecraft functions
	@Override
	public void onLoad() {
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		CommandAPI.getInstance().register("killall", arguments, (sender, args) -> {
            //Lambda to kill all enemies in all worlds
        	Bukkit.getWorlds().forEach(w -> w.getLivingEntities().forEach(e -> e.setHealth(0)));
        });
	}
    
    //All other commands
    @Override
    public void onEnable() {
        //Other commands
        LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		CommandAPI.getInstance().register("fly", arguments, (sender, args) -> {
			if(sender instanceof Player) {
				((Player) sender).setFlying(true);
			}
		});
    }
    
}
```

**Example function hierarchy:**

```
server/
├── world/
│   ├── advancements/
│   ├── data/
│   ├── datapacks/
│   │   └── bukkit/
│   │       ├── pack.mcmeta
│   │       └── data/
│   │           └── mycustomnamespace/
│   │               ├── functions/
│   │               │   ├── test.mcfunction
│   │               │   └── test2.mcfunction
│   │               └── tags/
│   │                   └── functions/
│   │                       └── mytag.json
│   └── ...
├── world_nether/
├── world_the_end/
├── ...
└── spigot.jar
```

**test.mcfunction**

Note how the _/killall_ command works here. Trying to register _/fly_ will cause an error as it was loaded in the `onEnable()` method instead of the `onLoad()` method.

```
killall
say Killed all living entities on the server
```

**mytag.json**

```json
{
    "values": [
    "mycustomnamespace:test",
    "mycustomnamespace:test2"
    ]
}
```

**In-game command usage**

```
/function mycustomnamespace:test
/function #mycustomnamespace:mytag
```

#### Using a pre-existing function in your code

```
/runfunction <function/tag>
```

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("functionarg", new FunctionArgument());
CommandAPI.getInstance().register("runfunction", arguments, (sender, args) -> {
    FunctionWrapper[] func = (FunctionWrapper[]) args[0];
    //Run all functions in the FunctionWrapper[]
    for(FunctionWrapper function : func) {
        function.run();
    }
});
```
