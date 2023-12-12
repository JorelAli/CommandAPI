package io.github.jorelali;

import org.bukkit.plugin.java.JavaPlugin;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkit;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		// Required for using the NBTCommand
		CommandAPIBukkit.initializeNBTAPI(NBTContainer.class, NBTContainer::new);
		
		// Register commands using a reference to their class
		CommandAPI.registerCommand(BreakCommand.class);
		CommandAPI.registerCommand(EffectCommand.class);
		CommandAPI.registerCommand(NBTCommand.class);
	}
}
