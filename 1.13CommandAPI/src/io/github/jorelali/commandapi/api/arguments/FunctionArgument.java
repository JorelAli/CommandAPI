package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.ArgumentType;

import io.github.jorelali.commandapi.api.CommandPermission;
import io.github.jorelali.commandapi.api.FunctionWrapper;
import io.github.jorelali.commandapi.api.SemiReflector;

@SuppressWarnings("unchecked")
public class FunctionArgument implements Argument, CustomProvidedArgument {

	ArgumentType<?> rawType;
	
	/**
	 * A Minecraft 1.12 function. Plugin commands which plan to be used INSIDE
	 * a function MUST be registered in the onLoad() method of your plugin, NOT
	 * in the onEnable() method!
	 */
	public FunctionArgument() {
		rawType = SemiReflector.getNMS()._ArgumentTag();
	}
	
	@Override
	public <T> ArgumentType<T> getRawType() {
		return (ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) FunctionWrapper[].class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
	
	private CommandPermission permission = CommandPermission.NONE;
	
	@Override
	public FunctionArgument withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}

	@Override
	public CommandPermission getArgumentPermission() {
		return permission;
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
