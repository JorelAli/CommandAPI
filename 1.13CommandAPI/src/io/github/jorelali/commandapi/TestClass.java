package io.github.jorelali.commandapi;

import java.util.LinkedHashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.jorelali.commandapi.api.CommandAPI;
import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.IntegerArgument;
import io.github.jorelali.commandapi.api.arguments.ItemStackArgument;
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
		playerTest();
		
	}
	
	public void playerTest() {
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		//TODO: same name, diofferent argument
		
		//ArgumentEnchantment
		arguments.put("effect", new _TestArgument("ArgumentMobEffect")); //potion effect
		arguments.put("chatColor", new _TestArgument2("ArgumentChatFormat")); //chat color
		arguments.put("spawn", new _TestArgument("ArgumentEntitySummon")); //?!
		arguments.put("entity", new _ENTITYARG()); //FIX 
		arguments.put("loc", new _VEC3ARG()); //FIX 
		
		//arguments.put("loc", new _TestArgument("ArgumentVec3")); //FIX 
		//arguments.put("slot", new _TestArgument("ArgumentInventorySlot")); //REJECTED 
		
		arguments.put("particle", new _TestArgument("ArgumentParticle"));  //ACCEPTED
		
		//Register the command
		CommandAPI.getInstance().register("ztp", arguments, (sender, args) -> {
			System.out.println("a");
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
