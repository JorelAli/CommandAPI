package io.github.jorelali.commandapi;

import java.util.LinkedHashMap;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.jorelali.commandapi.api.CommandAPI;
import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.IntegerArgument;
import io.github.jorelali.commandapi.api.arguments.ItemStackArgument;
import io.github.jorelali.commandapi.api.arguments.ParticleArgument;
import io.github.jorelali.commandapi.api.arguments.PotionEffectArgument;
import io.github.jorelali.commandapi.api.arguments._ENTITYARG;
import io.github.jorelali.commandapi.api.arguments._TestArgument;
import io.github.jorelali.commandapi.api.arguments._TestArgument2;
import io.github.jorelali.commandapi.api.arguments._VEC3ARG;

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
		 * Argument			COMPLETION	TEST STATUS
		 * ----------------------------------------
		 * Particles 		COMPLETE	TESTED
		 * Potion Effects	COMPLETE	TESTED
		 * ChatColor
		 * Enchanting
		 * Entity types
		 * Players
		 * Locations
		 */
		
		
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
			player.addPotionEffect(new PotionEffect((PotionEffectType) args[0], 10000, 2));
			player.getLocation().getWorld().spawnParticle((Particle) args[1], player.getLocation(), 50);
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
