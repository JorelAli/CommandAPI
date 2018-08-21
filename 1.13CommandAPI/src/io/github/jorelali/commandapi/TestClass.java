package io.github.jorelali.commandapi;

import java.util.LinkedHashMap;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.jorelali.commandapi.api.CommandAPI;
import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.EntityTypeArgument;
import io.github.jorelali.commandapi.api.arguments.IntegerArgument;
import io.github.jorelali.commandapi.api.arguments.ItemStackArgument;
import io.github.jorelali.commandapi.api.arguments.LocationArgument;
import io.github.jorelali.commandapi.api.arguments.PlayerArgument;
import net.minecraft.server.v1_13_R1.EntityPig;

public class TestClass extends JavaPlugin  {
			
	/**
	 * TODO: Add ALIASES!
	 */
	
	
	@Override
	public void onEnable() {
		try {
			
			playerTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void playerTest() throws Exception {
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		
//		Field f = PotionEffectType.class.getDeclaredField("byName");
//		f.setAccessible(true);
//		Map<String, PotionEffectType> byName = (Map<String, PotionEffectType>) f.get(null);
//		byName.forEach((a, b) -> System.out.println(a));
		
		//arguments.put("a b c", new PotionEffectArgument());
		//arguments.put("particle", new ParticleArgument());
		
		/**
		 * List of arguments:
		 * Argument			COMPLETION	TEST STATUS		ISSUES
		 * ---------------------------------------------------	
		 * Particles 		COMPLETE	TESTED		
		 * Potion Effects	COMPLETE	TESTED
		 * ChatColor		COMPLETE	TESTED
		 * Enchanting		COMPLETE	TESTED
		 * Entity types		COMPLETE	TESTED
		 * Players			COMPLETE	TESTED
		 * Locations		COMPLETE	TESTED			Assumes sender is player
		 * Enums			DROPPED
		 */
		
		//EntityType
		
		
		arguments.put("entity", new EntityTypeArgument());
		//arguments.put("player", new PlayerArgument());
		//arguments.put("loc", new LocationArgument());
		
		
		
		
		//ArgumentEnchantment
//		arguments.put("chatColor", new _TestArgument2("ArgumentChatFormat")); //chat color
//		arguments.put("spawn", new _TestArgument("ArgumentEntitySummon")); //?!
//		arguments.put("entity", new _ENTITYARG()); //FIX 
//		arguments.put("loc", new _VEC3ARG()); //FIX 
		
		//arguments.put("loc", new _TestArgument("ArgumentVec3")); //FIX 
		//arguments.put("slot", new _TestArgument("ArgumentInventorySlot")); //REJECTED 
		
		//arguments.put("particle", new _TestArgument("ArgumentParticle"));  //ACCEPTED COMPLETE
		
		//Register the command
		CommandAPI.getInstance().register("ztp", arguments, (sender, args) -> {
			Player player = (Player) sender;
			player.getWorld().spawnEntity(player.getLocation(), (EntityType) args[0]);
			//((Player) args[0]).setGameMode(GameMode.SURVIVAL);
			//player.teleport((Location) args[1]);
			});
		
	}
	
	public void registerGiveCmd() {
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		arguments.put("item", new ItemStackArgument()); 
		
		//Register the command
		CommandAPI.getInstance().register("gimme", arguments, (sender, args) -> {
			((ItemStack) args[0]).getType();
			((Player) sender).getInventory().addItem((ItemStack) args[0]);
			});
		
		arguments.put("amount", new IntegerArgument(1, 64)); 
		CommandAPI.getInstance().register("gimme", arguments, (sender, args) -> {
			ItemStack is = (ItemStack) args[0];
			is.setAmount((int) args[1]);
			((Player) sender).getInventory().addItem(is);
			});
	}
	
}
