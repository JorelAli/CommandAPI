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
package dev.jorel.commandapi.wrappers;

import java.util.function.Function;
import java.util.function.ToIntFunction;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;

/**
 * A wrapper class for Minecraft 1.12's functions
 */
public class FunctionWrapper extends SimpleFunctionWrapper {	
	
	private final Object commandListenerWrapper;
	private final Function<Entity, Object> mapper;
	
	/**
	 * Creates a FunctionWrapper
	 * @param minecraftKey the MinecraftKey which is used to reference this function
	 * @param invoker a method which, when invoked, runs the function
	 * @param clwArgB the instance of the CommandListenerWrapper which executed this command
	 * @param mapper a function that maps a Bukkit Entity to a Minecraft Entity
	 * @param internalCommands a String[] of internal commands that this customFunction represents. Typically customFunction.b().map(Object::toString)
	 */
	public FunctionWrapper(NamespacedKey minecraftKey, @SuppressWarnings("rawtypes") ToIntFunction invoker, Object clwArgB, Function<Entity, Object> mapper, String[] internalCommands) {
		super(minecraftKey, invoker, internalCommands);
		this.commandListenerWrapper = clwArgB;
		this.mapper = mapper;
	}
	
	/**
	 * Converts a SimpleFunctionWrapper into a FunctionWrapper
	 * @param wrapper the SimpleFunctionWrapper to convert
	 * @param commandListenerWrapper the instance of the CommandListenerWrapper which will be applied to this function
	 * @param mapper a function that maps a Bukkit Entity to a Minecraft Entity
	 * @return A FunctionWrapper which is a child of the provided SimpleFunctionWrapper
	 */
	public static FunctionWrapper fromSimpleFunctionWrapper(SimpleFunctionWrapper wrapper, Object commandListenerWrapper, Function<Entity, Object> mapper) {
		return new FunctionWrapper(wrapper.minecraftKey, wrapper.functionExecutor, commandListenerWrapper, mapper, wrapper.internalCommands);
	}

	/**
	 * Executes this function as the executor of the command.
	 * @return the result of running this command
	 */
	public int run() {
		return runInternal(commandListenerWrapper);
	}
	
	/**
	 * Executes this function as an entity.
	 * @param e entity to execute this function
	 * @return the result of running this command
	 */
	public int runAs(Entity e) {
		return runInternal(mapper.apply(e));
	}
	
}
