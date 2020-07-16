# Functions

The CommandAPI has support to use Minecraft's [functions](https://minecraft.gamepedia.com/Function_(Java_Edtion)) within your plugins. This is handled by using a class provided by the CommandAPI called `FunctionWrapper`, which allows you to execute functions. The CommandAPI also provides support to let you run your own commands within Minecraft function files.

-----

## Using custom commands in functions

In order to use a command from your plugin in a `.mcfunction` file, you must register your command in your plugin's `onLoad()` method, instead of the `onEnable()` method. Failure to do so will not allow the command to be registered for Minecraft functions, causing the function file to fail to load during the server startup phase.

> **Developer's Note:**
>
> In short, if you want to register a command which can be used in Minecraft functions, register it in your plugin's `onLoad()` method.

<div class="example">

### Example - Registering command for use in a function

Say we have a command `/killall` that simply kills all entities in all worlds on the server. If we were to register this in our `onLoad()` method, this would allow us to use the `/killall` command in Minecraft functions and tags.

```java
public class Main extends JavaPlugin {

	@Override
	public void onLoad() {
		//Commands which will be used in Minecraft functions are registered here

        new CommandAPICommand("killall")
            .executes((sender, args) -> {
                //Kills all enemies in all worlds
                Bukkit.getWorlds().forEach(w -> w.getLivingEntities().forEach(e -> e.setHealth(0)));
        	})
            .register();
	}
    
    @Override
    public void onEnable() {
        //Register all other commands here
    } 
}
```

</div>