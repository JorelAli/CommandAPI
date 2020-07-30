package dev.jorel.commandapi;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.loot.LootTables;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTItem;
import dev.jorel.commandapi.arguments.AdvancementArgument;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.AxisArgument;
import dev.jorel.commandapi.arguments.BiomeArgument;
import dev.jorel.commandapi.arguments.BlockStateArgument;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.ChatArgument;
import dev.jorel.commandapi.arguments.ChatColorArgument;
import dev.jorel.commandapi.arguments.ChatComponentArgument;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.EnchantmentArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.EntityTypeArgument;
import dev.jorel.commandapi.arguments.EnvironmentArgument;
import dev.jorel.commandapi.arguments.FloatArgument;
import dev.jorel.commandapi.arguments.FloatRangeArgument;
import dev.jorel.commandapi.arguments.FunctionArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.IntegerRangeArgument;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import dev.jorel.commandapi.arguments.Location2DArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LongArgument;
import dev.jorel.commandapi.arguments.LootTableArgument;
import dev.jorel.commandapi.arguments.MathOperationArgument;
import dev.jorel.commandapi.arguments.NBTCompoundArgument;
import dev.jorel.commandapi.arguments.ObjectiveArgument;
import dev.jorel.commandapi.arguments.ObjectiveCriteriaArgument;
import dev.jorel.commandapi.arguments.ParticleArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.PotionEffectArgument;
import dev.jorel.commandapi.arguments.RecipeArgument;
import dev.jorel.commandapi.arguments.RotationArgument;
import dev.jorel.commandapi.arguments.ScoreHolderArgument;
import dev.jorel.commandapi.arguments.ScoreHolderArgument.ScoreHolderType;
import dev.jorel.commandapi.arguments.ScoreboardSlotArgument;
import dev.jorel.commandapi.arguments.SoundArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.arguments.TeamArgument;
import dev.jorel.commandapi.arguments.TextArgument;
import dev.jorel.commandapi.arguments.TimeArgument;
import dev.jorel.commandapi.wrappers.FloatRange;
import dev.jorel.commandapi.wrappers.MathOperation;
import dev.jorel.commandapi.wrappers.Rotation;
import dev.jorel.commandapi.wrappers.ScoreboardSlot;
import dev.jorel.commandapi.wrappers.Time;

public class CommandAPIMain extends JavaPlugin implements Listener {
	
	private static Logger logger;
	
	public static Logger getLog() {
		return logger;
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
		new CommandAPI();

		//Convert all plugins to be converted
		for(Entry<Plugin, String[]> pluginToConvert : config.pluginsToConvert.entrySet()) {
			if(pluginToConvert.getValue().length == 0) {
				Converter.convert(pluginToConvert.getKey());
			} else {
				for(String command : pluginToConvert.getValue()) {
					Converter.convert(pluginToConvert.getKey(), command);
				}
			}
		}
		
	}
	
	@Override
	public void onEnable() {
		
		//Prevent command registration after server has loaded
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
			CommandAPI.cleanup();
		}, 0L);
        
        getServer().getPluginManager().registerEvents(this, this);
        
        //TODO: Remove before release
        {
        	//success
	        LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	        arguments.put("time", new TimeArgument().safeOverrideSuggestions(Time.days(2), Time.seconds(10)));
	        arguments.put("floats", new FloatArgument().safeOverrideSuggestions(2f, 3f));
	        
	        new CommandAPICommand("a")
	        .withArguments(arguments)
	        .executes((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        }
        {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
        	arguments.put("nbt", new NBTCompoundArgument());

        	new CommandAPICommand("award")
        	    .withArguments(arguments)
        	    .executes((sender, args) -> {
        	        NBTContainer nbt = (NBTContainer) args[0];
        	        NBTItem.convertNBTtoItem(nbt);
        	        
        	    })
        	    .register();
        }
        {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
//	        arguments.put("text", new TextArgument().safeOverrideSuggestions("hello", "world!"));
	        arguments.put("fr", new FloatRangeArgument().safeOverrideSuggestions(FloatRange.floatRangeGreaterThanOrEq(2), new FloatRange(20, 40)));
	        
	        new CommandAPICommand("b")
	        .withArguments(arguments)
	        .executes((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
        	// buggy environment - enum values are just wrong
	        arguments.put("environment", new EnvironmentArgument().safeOverrideSuggestions(Environment.NETHER, Environment.NORMAL));
			arguments.put("loc", new LocationArgument().safeOverrideSuggestions(
				s -> new Location[] { 
						((Player) s).getLocation(), 
						((Player) s).getWorld().getSpawnLocation() 
				})
			);
	        
	        new CommandAPICommand("c")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	        arguments.put("biome", new BiomeArgument().safeOverrideSuggestions(Biome.BADLANDS_PLATEAU));
	        arguments.put("adv", new AdvancementArgument().safeOverrideSuggestions(Bukkit.getAdvancement(new NamespacedKey("minecraft", "end/kill_dragon"))));
	        
	        new CommandAPICommand("d")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	        arguments.put("axis", new AxisArgument().safeOverrideSuggestions(EnumSet.allOf(Axis.class)));
	        arguments.put("cc", new ChatColorArgument().safeOverrideSuggestions(ChatColor.DARK_GREEN, ChatColor.RED));
	        
	        new CommandAPICommand("e")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        }  {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	        arguments.put("enchant", new EnchantmentArgument().safeOverrideSuggestions(Enchantment.DAMAGE_ALL));
	        arguments.put("entitytype", new EntityTypeArgument().safeOverrideSuggestions(EntityType.PIG, EntityType.ZOMBIE));
	        arguments.put("is", new ItemStackArgument().safeOverrideSuggestions(new ItemStack(Material.DIRT)));
	        
	        new CommandAPICommand("f")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	        arguments.put("particle", new ParticleArgument().safeOverrideSuggestions(Particle.BARRIER, Particle.CLOUD));
	        
	        new CommandAPICommand("g")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	        arguments.put("math", new MathOperationArgument().safeOverrideSuggestions(MathOperation.ADD, MathOperation.DIVIDE));
	        
	        new CommandAPICommand("h")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
        	
        	ItemStack is = new ItemStack(Material.DIAMOND_SWORD);
        	is.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        	
        	ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, "myitem"), new ItemStack(Material.DIAMOND));
        	recipe.shape("AAA", "AAA", "AAA");
        	recipe.setIngredient('A', Material.CRAFTING_TABLE);
        	getServer().addRecipe(recipe);
        	
	        arguments.put("nbt", new NBTCompoundArgument().safeOverrideSuggestions(NBTItem.convertItemtoNBT(is)));
	        arguments.put("recipe", new RecipeArgument().safeOverrideSuggestions(recipe));
	        arguments.put("scoreboardslot", new ScoreboardSlotArgument().safeOverrideSuggestions(ScoreboardSlot.of(DisplaySlot.BELOW_NAME), ScoreboardSlot.ofTeamColor(ChatColor.AQUA)));
	        
	        new CommandAPICommand("i")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	
        	
        	Field f = null;
			try {
				f = PotionEffectType.class.getDeclaredField("byId");
			} catch (NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	f.setAccessible(true);
        	PotionEffectType[] byId = null;
			try {
				byId = (PotionEffectType[]) f.get(null);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	for(PotionEffectType p : byId) {
        		try {
        			System.out.println(p.getName());
        		} catch(Exception e) {}
        	}
        	CommandAPIHandler.getNMS().convert(PotionEffectType.FAST_DIGGING);
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	        arguments.put("rot", new RotationArgument().safeOverrideSuggestions(new Rotation(2, 3)));
	        
	        new CommandAPICommand("j")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	Argument[] arguments = new Argument[] {
        		new AdvancementArgument(),
//        		new AxisArgument(),
        		new BiomeArgument(),
        		new BlockStateArgument(),
        		new BooleanArgument(),
        		new ChatArgument(),
        		new ChatColorArgument(),
        		new ChatComponentArgument(),
//        		new CustomArgument(),
        		new DoubleArgument(),
        		new EnchantmentArgument(),
//        		new EntitySelectorArgument(),
        		new EnchantmentArgument(),
        		new EntitySelectorArgument(EntitySelector.MANY_ENTITIES),
        		new EntityTypeArgument(),
        		new EnvironmentArgument(),
        		new FloatArgument(),
        		new FloatRangeArgument(),
        		new FunctionArgument(),
        		new GreedyStringArgument(),
        		new IntegerArgument(),
        		new IntegerRangeArgument(),
        		new ItemStackArgument(),
//        		new LiteralArgument(""),
        		new Location2DArgument(),
        		new LocationArgument(),
        		new LongArgument(),
        		new LootTableArgument(),
        		new MathOperationArgument(),
        		new NBTCompoundArgument(),
        		new ObjectiveArgument(),
        		new ObjectiveCriteriaArgument(),
        		new ParticleArgument(),
        		new PlayerArgument(),
        		new PotionEffectArgument(),
        		new RecipeArgument(),
        		new RotationArgument(),
        		new ScoreboardSlotArgument(),
        		new ScoreHolderArgument(ScoreHolderType.MULTIPLE),
        		new SoundArgument(),
        		new StringArgument(),
        		new TeamArgument(),
        		new TextArgument(),
        		new TimeArgument()
        	};
        	
        	for(Argument a : arguments) {
        		LinkedHashMap<String, Argument> args = new LinkedHashMap<>();
        		args.put(a.getClass().getSimpleName(), a);
        		
        		new CommandAPICommand(a.getClass().getSimpleName())
        		.withArguments(args)
        		.executes((c, arg) -> {
        		}).register();
        	}
        } {
        	
        	LinkedHashMap<String, Argument> args = new LinkedHashMap<>();
    		args.put("AxisArgument", new AxisArgument().safeOverrideSuggestions(
    			EnumSet.of(Axis.X),
    			EnumSet.of(Axis.Y),
    			EnumSet.of(Axis.Z),
    			EnumSet.of(Axis.X, Axis.Z),
    			EnumSet.of(Axis.X, Axis.Y),
    			EnumSet.of(Axis.Y, Axis.Z),
    			EnumSet.allOf(Axis.class)
			));
    		
    		new CommandAPICommand("AxisArgument")
    		.withArguments(args)
    		.executes((c, arg) -> {
    		}).register();
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	        arguments.put("snd", new SoundArgument().safeOverrideSuggestions(Sound.AMBIENT_BASALT_DELTAS_ADDITIONS, Sound.BLOCK_BONE_BLOCK_HIT));
	        
	        new CommandAPICommand("k")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	        arguments.put("loot", new LootTableArgument().safeOverrideSuggestions(LootTables.BURIED_TREASURE.getLootTable(), LootTables.PANDA.getLootTable()));

	        new CommandAPICommand("l")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
        	
	        arguments.put("team", new TeamArgument().safeOverrideSuggestions(Bukkit.getScoreboardManager().getMainScoreboard().getTeams().toArray(new Team[0])));

	        new CommandAPICommand("m")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
        	
	        arguments.put("obj", new ObjectiveArgument().safeOverrideSuggestions(Bukkit.getScoreboardManager().getMainScoreboard().getObjectives().toArray(new Objective[0])));

	        new CommandAPICommand("n")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        }
        
        
	}
	
	/** 
	 * Configuration wrapper class.
	 * The config.yml file used by the CommandAPI is only ever read from,
	 * nothing is ever written to it. That's why there's only getter methods.
	 */
	class Config {
		
		//Output registering and unregistering of commands
		private final boolean verboseOutput;
		
		//Create a command_registration.json file
		private final boolean createDispatcherFile;
		
		//List of plugins to convert
		private final Map<Plugin, String[]> pluginsToConvert; 
				
		public Config(FileConfiguration fileConfig) {
			verboseOutput = fileConfig.getBoolean("verbose-outputs");
			createDispatcherFile = fileConfig.getBoolean("create-dispatcher-json");
			pluginsToConvert = new HashMap<>();
			
			for(Map<?, ?> map : fileConfig.getMapList("plugins-to-convert")) {
				String pluginName = (String) map.keySet().stream().findFirst().get();
				Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
				if(plugin == null) {
					getLog().severe("Plugin '" + pluginName + "' was not found. Did you add loadbefore: [CommandAPI] to " + pluginName + "'s plugin.yml file?");
					continue;
				}
				
				String[] pluginCommands;
				if(map.values() == null) {
					pluginCommands = new String[0];
				} else {
					@SuppressWarnings("unchecked")
					List<String> commands = (List<String>) map.values().stream().findFirst().get();
					pluginCommands = commands.toArray(new String[0]);
				}
				pluginsToConvert.put(plugin, pluginCommands);
			}
		}
		
		public boolean hasVerboseOutput() {
			return verboseOutput;
		}
		
		public boolean willCreateDispatcherFile() {
			return createDispatcherFile;
		}
		
	}
	
}
