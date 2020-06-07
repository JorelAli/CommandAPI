package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.Bukkit;

import de.tr7zw.nbtapi.NBTContainer;
import io.github.jorelali.commandapi.api.CommandAPIHandler;
import io.github.jorelali.commandapi.api.exceptions.NBTAPINotFoundException;

public class NBTCompoundArgument extends Argument {

	/**
	 * An NBT Compound Argument. Represents Minecraft's NBT Compound Tag using the NBT API
	 */
	public NBTCompoundArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentNBTCompound());
		
		if(Bukkit.getPluginManager().getPlugin("NBTAPI") == null) {
			throw new NBTAPINotFoundException(this.getClass());
		}
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return NBTContainer.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.NBT_COMPOUND;
	}
}
