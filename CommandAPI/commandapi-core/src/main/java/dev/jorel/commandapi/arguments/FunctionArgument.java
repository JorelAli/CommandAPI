package dev.jorel.commandapi.arguments;

import org.bukkit.NamespacedKey;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.wrappers.FunctionWrapper;

/**
 * An argument that represents Minecraft functions and tags
 */
public class FunctionArgument extends SafeOverrideableArgument<NamespacedKey> implements ICustomProvidedArgument {

	/**
	 * A Minecraft function. Plugin commands which plan to be used INSIDE a Minecraft
	 * a function MUST be registered in the onLoad() method of your plugin, NOT
	 * in the onEnable() method!
	 * @param nodeName the name of the node for this argument
	 */
	public FunctionArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentTag(), fromKey(n -> n));
	}

	@Override
	public Class<?> getPrimitiveType() {
		return FunctionWrapper[].class;
	}
	
	@Override
	public SuggestionProviders getSuggestionProvider() {
		return SuggestionProviders.FUNCTION;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.FUNCTION;
	}
}
