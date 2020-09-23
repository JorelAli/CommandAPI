package dev.jorel.commandapi.arguments;

import org.bukkit.block.data.BlockData;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit BlockData object
 */
public class BlockStateArgument extends Argument {

	/**
	 * A BlockState argument
	 */
	public BlockStateArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getNMS()._ArgumentBlockState());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return BlockData.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.BLOCKSTATE;
	}
}
