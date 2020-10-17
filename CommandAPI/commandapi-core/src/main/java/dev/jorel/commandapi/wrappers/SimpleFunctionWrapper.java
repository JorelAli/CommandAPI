package dev.jorel.commandapi.wrappers;

import java.util.List;
import java.util.function.ToIntFunction;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * A wrapper class for Minecraft 1.12's functions
 */
public class SimpleFunctionWrapper implements Keyed {	
	
	final NamespacedKey minecraftKey;
	final ToIntFunction<Object> functionExecutor; //What the function does
	final String[] internalCommands;
	
	/**
	 * Creates a SimpleFunctionWrapper
	 * @param minecraftKey the MinecraftKey which is used to reference this function
	 * @param invoker a method which, when invoked, runs the function
	 * @param internalCommands a String[] of internal commands that this customFunction represents. Typically customFunction.b().map(Object::toString)
	 */
	@SuppressWarnings("unchecked")
	public SimpleFunctionWrapper(NamespacedKey minecraftKey, @SuppressWarnings("rawtypes") ToIntFunction invoker, String[] internalCommands) {
		this.minecraftKey = minecraftKey;
		this.functionExecutor = invoker;
		this.internalCommands = internalCommands;
	}
	
	SimpleFunctionWrapper(SimpleFunctionWrapper functionWrapper) {
		this.minecraftKey = functionWrapper.minecraftKey;
		this.functionExecutor = functionWrapper.functionExecutor;
		this.internalCommands = functionWrapper.internalCommands;
	}
	
	public static SimpleFunctionWrapper[] fromNamespace(NamespacedKey key) {
		return CommandAPIHandler.getNMS().convertFunction(key);
	}
	
	public static List<NamespacedKey> getFunctions() {
		return null;
	}
	
	public static List<NamespacedKey> getFunctionTags() {
		return null;
	}
	
	public void run(CommandSender sender) {
		runInternal(CommandAPIHandler.getNMS().getCLWFromCommandSender(sender));
	}
	
	/**
	 * Returns an array of commands that will be executed by this FunctionWrapper 
	 * @return the commands that are defined by this custom function
	 */
	public String[] getCommands() {
		return this.internalCommands;
	}
	
	void runInternal(Object clw) {
		functionExecutor.applyAsInt(clw);
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
