package dev.jorel.commandapi.wrappers;

import java.util.Set;
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
	
	/**
	 * Returns a SimpleFunctionWrapper[], which is an array of all of the functions that this tag contains 
	 * @param key a NamespacedKey representation of the tag. This key should not include a # symbol.
	 * @return a SimpleFunctionWrapper[], which is an array of all of the functions that this tag contains
	 */
	public static SimpleFunctionWrapper[] getTag(NamespacedKey key) {
		return CommandAPIHandler.getInstance().getNMS().getTag(key);
	}
	
	/**
	 * Returns a SimpleFunctionWrapper representation of the Minecraft function for the provided NamespacedKey
	 * @param key a NamespacedKey representation of the function
	 * @return a SimpleFunctionWrapper representation of the Minecraft function for the provided NamespacedKey
	 */
	public static SimpleFunctionWrapper getFunction(NamespacedKey key) {
		return CommandAPIHandler.getInstance().getNMS().getFunction(key);
	}
	
	/**
	 * Returns a set of all functions that the server has registered
	 * @return a set of all functions that the server has registered
	 */
	public static Set<NamespacedKey> getFunctions() {
		return CommandAPIHandler.getInstance().getNMS().getFunctions();
	}
	
	/**
	 * Returns a set of all tags that the server has registered
	 * @return a set of all tags that the server has registered
	 */
	public static Set<NamespacedKey> getTags() {
		return CommandAPIHandler.getInstance().getNMS().getTags();
	}
	
	/**
	 * Runs this function with a given CommandSender
	 * @param sender the sender to use to run this function
	 * @return the result of running this command
	 */
	public int run(CommandSender sender) {
		return runInternal(CommandAPIHandler.getInstance().getNMS().getCLWFromCommandSender(sender));
	}
	
	/**
	 * Returns an array of commands that will be executed by this FunctionWrapper 
	 * @return the commands that are defined by this custom function
	 */
	public String[] getCommands() {
		return this.internalCommands;
	}
	
	int runInternal(Object clw) {
		return functionExecutor.applyAsInt(clw);
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
