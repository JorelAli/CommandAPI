package io.github.jorelali.commandapi.api;

import java.util.function.Function;
import java.util.function.ToIntBiFunction;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;

/**
 * A wrapper class for Minecraft 1.12's functions
 */
public class FunctionWrapper implements Keyed {	
	
	private final NamespacedKey minecraftKey;
	private final ToIntBiFunction<Object, Object> functionExecutor; //What the function does
	private final Object customFunction; //The function to actually run
	private final Object commandListenerWrapper;
	private final Function<Entity, Object> mapper;
	
	/**
	 * Creates a FunctionWrapper
	 * @param minecraftKey - The MinecraftKey which is used to reference this function
	 * @param invoker A method which, when invoked, runs the function
	 * @param funcData The NMS CustomFunctionData object (From MinecraftServer)
	 * @param customFunction A CustomFunction which contains the information about the function
	 * @param commandListenerWrapper the instance of the CommandListenerWrapper which executed this command
	 * @param mapper A function which maps a Bukkit Entity into a Minecraft Entity
	 */
	@SuppressWarnings("unchecked")
	public FunctionWrapper(NamespacedKey minecraftKey, @SuppressWarnings("rawtypes") ToIntBiFunction invoker, Object custFunc, Object argB, Function<Entity, Object> mapper) {
		this.minecraftKey = minecraftKey;
		this.functionExecutor = invoker;
		this.customFunction = custFunc;
		this.commandListenerWrapper = argB;
		this.mapper = mapper;
	}

	/**
	 * Executes this function as the executor of the command
	 */
	public void run() {
		run(commandListenerWrapper);
	}
	
	/**
	 * Executes this function as an entity
	 * @param e The entity to perform the function
	 */
	public void runAs(Entity e) {
		run(mapper.apply(e));
	}
	
	private void run(Object clw) {
		functionExecutor.applyAsInt(customFunction, clw);
	}

	@Override
	public NamespacedKey getKey() {
		return minecraftKey;
	}
	
}
