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

	public String getName() {
		return minecraftKey;
	}
	
	public void run() {
		run(argB);
	}
	
	public void runAs(Entity e) {
		Object o = mapper.convert(e);
		run(o);
	}
	
	private void run(Object clw) {
		try {
			invoker.invoke(funcData, custFunc, clw);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
}
