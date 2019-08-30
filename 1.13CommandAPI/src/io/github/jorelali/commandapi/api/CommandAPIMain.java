package io.github.jorelali.commandapi.api;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.function.IntBinaryOperator;
import java.util.logging.Logger;

import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import de.tr7zw.nbtapi.NBTContainer;
import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.AxisArgument;
import io.github.jorelali.commandapi.api.arguments.ChatArgument;
import io.github.jorelali.commandapi.api.arguments.ChatComponentArgument;
import io.github.jorelali.commandapi.api.arguments.EnvironmentArgument;
import io.github.jorelali.commandapi.api.arguments.FloatRangeArgument;
import io.github.jorelali.commandapi.api.arguments.GreedyStringArgument;
import io.github.jorelali.commandapi.api.arguments.IntegerArgument;
import io.github.jorelali.commandapi.api.arguments.IntegerRangeArgument;
import io.github.jorelali.commandapi.api.arguments.ItemSlotArgument;
import io.github.jorelali.commandapi.api.arguments.Location2DArgument;
import io.github.jorelali.commandapi.api.arguments.LocationType;
import io.github.jorelali.commandapi.api.arguments.MathOperationArgument;
import io.github.jorelali.commandapi.api.arguments.NBTCompoundArgument;
import io.github.jorelali.commandapi.api.arguments.RotationArgument;
import io.github.jorelali.commandapi.api.arguments.ScoreHolderArgument;
import io.github.jorelali.commandapi.api.arguments.ScoreHolderArgument.ScoreHolderType;
import io.github.jorelali.commandapi.api.arguments.ScoreboardSlotArgument;
import io.github.jorelali.commandapi.api.arguments.TimeArgument;
import io.github.jorelali.commandapi.api.wrappers.FloatRange;
import io.github.jorelali.commandapi.api.wrappers.IntegerRange;
import io.github.jorelali.commandapi.api.wrappers.Rotation;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_14_R1.ArgumentInventorySlot;

public class CommandAPIMain extends JavaPlugin implements Listener {
	
	private static Logger logger;
	
	public static Logger getLog() {
		return logger;
	}
	
	/** 
	 * Configuration wrapper class.
	 * The config.yml file used by the CommandAPI is only ever read from,
	 * nothing is ever written to it. That's why there's only getter methods.
	 */
	public class Config {
		
		//Output registering and unregistering of commands
		private final boolean verboseOutput;
		
		//Create a command_registration.json file
		private final boolean createDispatcherFile;
				
		public Config(FileConfiguration fileConfig) {
			verboseOutput = fileConfig.getBoolean("verbose-outputs");
			createDispatcherFile = fileConfig.getBoolean("create-dispatcher-json");
		}
		
		public boolean hasVerboseOutput() {
			return verboseOutput;
		}
		
		public boolean willCreateDispatcherFile() {
			return createDispatcherFile;
		}
		
	}

	private static Config config;
	private static File dispatcherFile;

	//Gets the instance of Config
	protected static Config getConfiguration() {
		return config;
	}
	
	protected static File getDispatcherFile() {
		return dispatcherFile;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent e) {
		CommandAPIHandler.getNMS().resendPackets(e.getPlayer());
	}
	
	@Override
	public void onLoad() {
		saveDefaultConfig();
		CommandAPIMain.config = new Config(getConfig());
		CommandAPIMain.dispatcherFile = new File(getDataFolder(), "command_registration.json");
		logger = getLogger();
		
		//Instantiate CommandAPI
		CommandAPI.getInstance();
	}
	
	@Override
	public void onEnable() {
		//Prevent command registration after server has loaded
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
			CommandAPI.canRegister = false;
			
			//Sort out permissions after the server has finished registering them all
			CommandAPI.fixPermissions();
		}, 0L);
        
        getServer().getPluginManager().registerEvents(this, this);
        
        //Testing all happens below here
        
        LinkedHashMap<String, Argument> args1 = new LinkedHashMap<>();
        args1.put("time", new TimeArgument());
        
        CommandAPI.getInstance().register("tim", args1, (s, a) -> {
        	System.out.println(a[0]);
        });
        
        args1.clear();
        args1.put("2d", new Location2DArgument(LocationType.BLOCK_POSITION));
        
        CommandAPI.getInstance().register("2dblock", args1, (s, a) -> {
        	System.out.println(a[0]);
        });
        
        args1.clear();
        args1.put("2d", new Location2DArgument(LocationType.PRECISE_POSITION));
        
        CommandAPI.getInstance().register("2dprecise", args1, (s, a) -> {
        	System.out.println(a[0]);
        });
        
        args1.clear();
        args1.put("range", new IntegerRangeArgument());
        
        CommandAPI.getInstance().register("range", args1, (s, a) -> {
        	IntegerRange r = (IntegerRange) a[0];
        	System.out.println(r.getLowerBound());
        	System.out.println(r.getUpperBound());
        });
        
        args1.clear();
        args1.put("frange", new FloatRangeArgument());
        
        CommandAPI.getInstance().register("frange", args1, (s, a) -> {
        	FloatRange r = (FloatRange) a[0];
        	System.out.println(r.getLowerBound());
        	System.out.println(r.getUpperBound());
        });
        
        args1.clear();
        args1.put("dim", new EnvironmentArgument());
        
        CommandAPI.getInstance().register("dim", args1, (s, a) -> {
        	Environment r = (Environment) a[0];
        	System.out.println(r);
        });
        
        args1.clear();
        args1.put("rot", new RotationArgument());
        
        CommandAPI.getInstance().register("rot", args1, (s, a) -> {
        	Rotation r = (Rotation) a[0];
        	System.out.println(r.getPitch() + ", " + r.getYaw());
        });
        
        args1.clear();
        args1.put("axes", new AxisArgument());
        
        CommandAPI.getInstance().register("axes", args1, (s, a) -> {
        	@SuppressWarnings("unchecked")
			EnumSet<Axis> r = (EnumSet<Axis>) a[0];
        	System.out.println(r);
        });
        
        args1.clear();
        args1.put("slot", new ItemSlotArgument());
        
        CommandAPI.getInstance().register("slot", args1, (s, a) -> {
        	int slot = (int) a[0];
        	Player player = (Player) s;
        	player.getInventory().setItem(slot, new ItemStack(Material.DIRT));
        	
        	try {
        		Field f = ArgumentInventorySlot.class.getDeclaredField("c");
        		f.setAccessible(true);
        		System.out.println(f.get(null));
        	} catch(Exception e) {}
        });
        
        args1.clear();
        args1.put("displaySlot", new ScoreboardSlotArgument());
        
        CommandAPI.getInstance().register("displaySlot", args1, (s, a) -> {
        	System.out.println(a[0]);
        });
        
        args1.clear();
        args1.put("comp", new ChatComponentArgument());
        
        CommandAPI.getInstance().register("comp", args1, (s, a) -> {
        	BaseComponent[] aa = (BaseComponent[]) a[0];
        	System.out.println(Arrays.deepToString(aa));
        });
        
        args1.clear();
        args1.put("chat", new ChatArgument());
        
        CommandAPI.getInstance().register("chat", args1, (s, a) -> {
        	BaseComponent[] aa = (BaseComponent[]) a[0];
        	System.out.println(Arrays.deepToString(aa));
        	try {
        		s.spigot().sendMessage(aa);
        	} catch(Exception e) {
        		System.out.println("Whoopsy");
        	}
        });
        
        args1.clear();
        args1.put("holdm", new ScoreHolderArgument(ScoreHolderType.MULTIPLE));
        
        CommandAPI.getInstance().register("holdm", args1, (s, a) -> {
        	@SuppressWarnings("unchecked")
			Collection<String> strs = (Collection<String>) a[0];
        	System.out.println(strs);
        });
        
        args1.clear();
        args1.put("holds", new ScoreHolderArgument(ScoreHolderType.SINGLE));
        
        CommandAPI.getInstance().register("holds", args1, (s, a) -> {
        	String strs = (String) a[0];
        	System.out.println(strs);
        });
        
        args1.clear();
        args1.put("nbt", new NBTCompoundArgument());
        
        CommandAPI.getInstance().register("nbt", args1, (s, a) -> {
        	NBTContainer strs = (NBTContainer) a[0];
        	System.out.println(strs);
        });
        
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		arguments.put("duration", new TimeArgument());
		arguments.put("message", new GreedyStringArgument());
		
		CommandAPI.getInstance().register("bigmsg", arguments, (sender, args) -> {
			int duration = (int) args[0];
			String message = (String) args[1];
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.sendTitle(message, "", 10, duration, 20);
			}
		});
		
		arguments = new LinkedHashMap<>();
		arguments.put("int1", new IntegerArgument());
		arguments.put("operation", new MathOperationArgument());
		arguments.put("int2", new IntegerArgument());
		
		CommandAPI.getInstance().register("calc", arguments, (sender, args) -> {
			int int1 = (int) args[0];
			int int2 = (int) args[2];
			IntBinaryOperator op = (IntBinaryOperator) args[1];
			sender.sendMessage("=> " + op.applyAsInt(int1, int2));
		});
        
//LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
//arguments.put("worldname", new StringArgument());
//arguments.put("type", new EnvironmentArgument());
//
//CommandAPI.getInstance().register("createworld", arguments, (sender, args) -> {
//	String worldName = (String) args[0];
//	Environment environment = (Environment) args[1];
//    Bukkit.getServer().createWorld(new WorldCreator(worldName).environment(environment));
//	sender.sendMessage("World created!");
//});
        
        
//		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
//		arguments.put("message", new ChatArgument());
//		
//		CommandAPI.getInstance().register("personalmsg", arguments, (sender, args) -> {
//			BaseComponent[] message = (BaseComponent[]) args[0];
//			Bukkit.getServer().spigot().broadcast(message);
//		});
		
// Declare our arguments for /searchrange <IntegerRange> <ItemStack>
//LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
//arguments.put("range", new IntegerRangeArgument());
//arguments.put("item", new ItemStackArgument());
//
//CommandAPI.getInstance().register("searchrange", arguments, (sender, args) -> {
//	// Retrieve the range from the arguments
//	IntegerRange range = (IntegerRange) args[0];
//	ItemStack itemStack = (ItemStack) args[1];
//	
//	// Inform the sender that they must be a player
//	if(!(sender instanceof Player)) {
//		sender.sendMessage("You must be a player to run this command!");
//		return;
//	}
//	
//	Player player = (Player) sender;
//	
//	// Store the locations of chests with certain items
//	List<Location> locations = new ArrayList<>();
//	
//	// Iterate through all chunks, and then all tile entities within each chunk
//	for(Chunk chunk : player.getWorld().getLoadedChunks()) {
//		for(BlockState blockState : chunk.getTileEntities()) {
//			
//			// The distance between the block and the player
//			int distance = (int) blockState.getLocation().distance(player.getLocation());
//			
//			// Check if the distance is within the specified range 
//			if(range.isInRange(distance)) {
//				
//				// Check if the tile entity is a chest
//				if(blockState instanceof Chest) {
//					Chest chest = (Chest) blockState;
//					
//					// Check if the chest contains the item specified by the player
//					if(chest.getInventory().contains(itemStack.getType())) {
//						locations.add(chest.getLocation());
//					}
//				}
//			}
//			
//		}
//	}
//	
//	// Output the locations of the chests, or whether no chests were found
//	if(locations.isEmpty()) {
//		player.sendMessage("No chests were found");
//	} else {
//		player.sendMessage("Found " + locations.size() + " chests:");
//		locations.forEach(location -> {
//			player.sendMessage("  Found at: " 
//					+ location.getX() + ", " 
//					+ location.getY() + ", " 
//					+ location.getZ());
//		});
//	}
//});
        
//        new NBTContainer("").
//        new NBTContainer("").get;
        
        /*
         * Testing to do:
         * - TeamArgument   
         * - Objective Criteria 
         */
	}
	
}
