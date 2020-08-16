package dev.jorel.commandapi.wrappers;

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
	private final String[] internalCommands;
	
	/**
	 * Creates a FunctionWrapper
	 * @param minecraftKey the MinecraftKey which is used to reference this function
	 * @param invoker a method which, when invoked, runs the function
	 * @param customFunction an NMS CustomFunction that contains the information about the function
	 * @param clwArgB the instance of the CommandListenerWrapper which executed this command
	 * @param mapper a function that maps a Bukkit Entity to a Minecraft Entity
	 * @param internalCommands a String[] of internal commands that this customFunction represents. Typically customFunction.b().map(Object::toString)
	 */
	@SuppressWarnings("unchecked")
	public FunctionWrapper(NamespacedKey minecraftKey, @SuppressWarnings("rawtypes") ToIntBiFunction invoker, Object customFunction, Object clwArgB, Function<Entity, Object> mapper, String[] internalCommands) {
		this.minecraftKey = minecraftKey;
		this.functionExecutor = invoker;
		this.customFunction = customFunction;
		this.commandListenerWrapper = clwArgB;
		this.mapper = mapper;
		this.internalCommands = internalCommands;
	}
	
	/**
	 * Returns an array of commands that will be executed by this FunctionWrapper 
	 * @return the commands that are defined by this custom function
	 */
	public String[] getCommands() {
		return this.internalCommands;
	}

	/**
	 * Executes this function as the executor of the command.
	 */
	public void run() {
		run(commandListenerWrapper);
	}
	
	/**
	 * Executes this function as an entity.
	 * @param e entity to execute this function
	 */
	public void runAs(Entity e) {
		run(mapper.apply(e));
	}
	
	private void run(Object clw) {
		functionExecutor.applyAsInt(customFunction, clw);
	}

	/**
	 * Returns the NamespacedKey that uniquely represents this object
	 * @return the NamespacedKey that uniquely represents this object
	 */
	@Override
	public NamespacedKey getKey() {
		return minecraftKey;
	}
	
}
