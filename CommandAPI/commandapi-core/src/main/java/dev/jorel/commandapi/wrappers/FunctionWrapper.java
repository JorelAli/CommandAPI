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
	
	public static FunctionWrapper fromSimpleFunctionWrapper(SimpleFunctionWrapper wrapper, Object commandListenerWrapper, Function<Entity, Object> mapper) {
		return new FunctionWrapper(wrapper.minecraftKey, wrapper.functionExecutor, commandListenerWrapper, mapper, wrapper.internalCommands);
	}

	/**
	 * Executes this function as the executor of the command.
	 */
	public void run() {
		runInternal(commandListenerWrapper);
	}
	
	/**
	 * Executes this function as an entity.
	 * @param e entity to execute this function
	 */
	public void runAs(Entity e) {
		runInternal(mapper.apply(e));
	}
	
}
