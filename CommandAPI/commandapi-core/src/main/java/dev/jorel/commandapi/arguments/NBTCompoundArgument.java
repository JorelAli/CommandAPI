package dev.jorel.commandapi.arguments;

import org.bukkit.Bukkit;

import de.tr7zw.nbtapi.NBTContainer;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.exceptions.NBTAPINotFoundException;

/**
 * An argument that represents an NBTContainer from the NBTAPI
 */
public class NBTCompoundArgument extends SafeOverrideableArgument<NBTContainer> {

	/**
	 * An NBT Compound Argument. Represents Minecraft's NBT Compound Tag using the NBT API
	 * @param nodeName the name of the node for this argument
	 */
	public NBTCompoundArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentNBTCompound(), NBTContainer::toString);
		
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
