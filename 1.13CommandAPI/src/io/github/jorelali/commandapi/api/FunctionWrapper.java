package io.github.jorelali.commandapi.api;

import java.util.function.ToIntBiFunction;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;

/**
 * A wrapper class for Minecraft 1.12's functions
 */
public class FunctionWrapper implements Keyed {	
	
	//Converts a Bukkit Entity to a Minecraft Entity to a CommandListenerWrapper
	@FunctionalInterface
	public interface EntityMapper {
		
		/* In Reflection, where clw is the instance of a CommandListenerWrapper:
		    e -> {
				Object nmsEntity = getMethod(getOBCClass("entity.CraftEntity"), "getHandle").invoke(e);
				return getMethod(getNMSClass("CommandListenerWrapper"), "a", getNMSClass("Entity")).invoke(clw, nmsEntity);
			}
		 */
		
		Object convert(Entity entity);
	}
	
//	private String minecraftKey;
//	private EntityMapper mapper;
//	private Method invoker;
//	private Object funcData;
//	private Object custFunc;
//	private Object argB;
	
	/**
	 * Creates a FunctionWrapper
	 * @param minecraftKey - The MinecraftKey which is used to reference this function
	 * @param invoker A method which, when invoked, runs the function
	 * @param funcData The NMS CustomFunctionData object (From MinecraftServer)
	 * @param custFunc A CustomFunction which contains the information about the function
	 * @param argB the instance of the CommandListenerWrapper which executed this command
	 * @param mapper A function which maps a Bukkit Entity into a Minecraft Entity
	 */
//	public FunctionWrapper(String minecraftKey, Method invoker, Object funcData, Object custFunc, Object argB, EntityMapper mapper) {
//		this.minecraftKey = minecraftKey;
//		this.invoker = invoker;
//		this.mapper = mapper;
//		this.funcData = funcData;
//		this.custFunc = custFunc;
//		this.argB = argB;
//	}
	
	private final NamespacedKey minecraftKey;
	private final ToIntBiFunction<Object, Object> tibfInvoker;
	private final Object custFunc;
	private final Object argB;
	private final EntityMapper mapper;
	
	@SuppressWarnings("unchecked")
	public FunctionWrapper(NamespacedKey minecraftKey, @SuppressWarnings("rawtypes") ToIntBiFunction invoker, Object custFunc, Object argB, EntityMapper mapper) {
		this.minecraftKey = minecraftKey;
		this.tibfInvoker = invoker;
		this.custFunc = custFunc;
		this.argB = argB;
		this.mapper = mapper;
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
		tibfInvoker.applyAsInt(custFunc, clw);
	}

	@Override
	public NamespacedKey getKey() {
		return minecraftKey;
	}
	
}
