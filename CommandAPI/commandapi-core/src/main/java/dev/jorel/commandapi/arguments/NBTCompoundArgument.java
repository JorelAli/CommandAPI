package dev.jorel.commandapi.arguments;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import dev.jorel.commandapi.CommandAPIHandler;

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
