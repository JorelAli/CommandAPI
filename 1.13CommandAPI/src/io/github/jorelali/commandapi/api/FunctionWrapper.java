package io.github.jorelali.commandapi.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.entity.Entity;

/**
 * A wrapper class for Minecraft 1.12's functions
 */
public class FunctionWrapper {	
	
	//Converts a Bukkit Entity to a Minecraft Entity to a CommandListenerWrapper
	@FunctionalInterface
	interface EntityMapper {
		Object convert(Entity entity);
	}
	
	private String minecraftKey;
	private EntityMapper mapper;
	private Method invoker;
	private Object funcData;
	private Object custFunc;
	private Object argB;
	
	protected FunctionWrapper(String minecraftKey, Method invoker, Object funcData, Object custFunc, Object argB, EntityMapper mapper) {
		this.minecraftKey = minecraftKey;
		this.invoker = invoker;
		this.mapper = mapper;
		this.funcData = funcData;
		this.custFunc = custFunc;
		this.argB = argB;
	}

	/**
	 * Gets the name of this function
	 * @return Name of the function, for example namespace:func
	 */
	public String getName() {
		return minecraftKey;
	}
	
	/**
	 * Executes this function as the executor of the command
	 */
	public void run() {
		run(argB);
	}
	
	/**
	 * Executes this function as an entity
	 * @param e The entity to perform the function
	 */
	public void runAs(Entity e) {
		run(mapper.convert(e));
	}
	
	private void run(Object clw) {
		try {
			invoker.invoke(funcData, custFunc, clw);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
}
