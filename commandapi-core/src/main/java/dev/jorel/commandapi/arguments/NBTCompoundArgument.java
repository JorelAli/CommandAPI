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

import java.util.function.Function;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.nms.NMS;

/**
 * An argument that represents an NBTContainer from the NBT API
 */
public class NBTCompoundArgument<NBTContainer> extends SafeOverrideableArgument<NBTContainer, NBTContainer> {

	private static Class<?> nbtContainerClass;
	private static Function<Object, ?> nbtContainerConstructor;

	/**
	 * Initializes the NBTCompoundArgument with an instance of the NBT API's
	 * NBTContainer class and NBTContainer constructor
	 * 
	 * @param <NBTContainer>
	 * @param nbtContainerClass       the NBTContainer class that you'd like to use.
	 *                                This is typically {@code NBTContainer.class}
	 * @param nbtContainerConstructor the constructor of the NBTContainer class, as
	 *                                a function. This is typically
	 *                                {@code NBTContainer::new}
	 */
	public static <NBTContainer> void initializeNBTCompoundArgument(Class<NBTContainer> nbtContainerClass,
			Function<Object, NBTContainer> nbtContainerConstructor) {
		NBTCompoundArgument.nbtContainerClass = nbtContainerClass;
		NBTCompoundArgument.nbtContainerConstructor = nbtContainerConstructor;
	}

	/**
	 * An NBT Compound Argument. Represents Minecraft's NBT Compound Tag using the
	 * NBT API
	 * 
	 * @param nodeName the name of the node for this argument
	 */
	public NBTCompoundArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentNBTCompound(), NBTContainer::toString);
		if (NBTCompoundArgument.nbtContainerClass == null || NBTCompoundArgument.nbtContainerConstructor == null) {
			throw new NullPointerException(
					"The NBTCompoundargument hasn't been initialized properly! Use NBTCompoundArgument.initializeNBTCompoundArgument()");
		}
	}

	/**
	 * An NBT Compound Argument. Represents Minecraft's NBT Compound Tag using the
	 * NBT API
	 * 
	 * @param nodeName                the name of the node for this argument
	 * @param nbtContainerClass       the NBTContainer class that you'd like to use.
	 *                                This is typically {@code NBTContainer.class}
	 * @param nbtContainerConstructor the constructor of the NBTContainer class, as
	 *                                a function. This is typically
	 *                                {@code NBTContainer::new}
	 */
	public NBTCompoundArgument(String nodeName, Class<NBTContainer> nbtContainerClass,
			Function<Object, NBTContainer> nbtContainerConstructor) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentNBTCompound(), NBTContainer::toString);
		NBTCompoundArgument.nbtContainerClass = nbtContainerClass;
		NBTCompoundArgument.nbtContainerConstructor = nbtContainerConstructor;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<NBTContainer> getPrimitiveType() {
		return (Class<NBTContainer>) nbtContainerClass;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.NBT_COMPOUND;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <CommandListenerWrapper> NBTContainer parseArgument(NMS<CommandListenerWrapper> nms,
			CommandContext<CommandListenerWrapper> cmdCtx, String key, Object[] previousArgs)
			throws CommandSyntaxException {
		return (NBTContainer) nms.getNBTCompound(cmdCtx, key, nbtContainerConstructor);
	}
}
