/*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import de.tr7zw.nbtapi.NBTContainer;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.nms.NMS;

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
	
	@Override
	public <CommandListenerWrapper> Object parseArgument(NMS<CommandListenerWrapper> nms,
			CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return nms.getNBTCompound(cmdCtx, key);
	}
}
