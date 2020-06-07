package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.ArgumentType;

import io.github.jorelali.commandapi.api.CommandAPIHandler;
import io.github.jorelali.commandapi.api.CommandPermission;
import io.github.jorelali.commandapi.api.wrappers.Rotation;

@SuppressWarnings("unchecked")
public class RotationArgument extends Argument {

	ArgumentType<?> rawType;
	
	/**
	 * A Rotation argument. Represents pitch and yaw
	 */
	public RotationArgument() {
		rawType = CommandAPIHandler.getNMS()._ArgumentRotation();
	}
	
	@Override
	public <T> ArgumentType<T> getRawType() {
		return (ArgumentType<T>) rawType;
	}

	@Override
	public Class<?> getPrimitiveType() {
		return Rotation.class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
	
	private String[] suggestions;
	
	@Override
	public RotationArgument overrideSuggestions(String... suggestions) {
		this.suggestions = suggestions;
		return this;
	}
	
	@Override
	public String[] getOverriddenSuggestions() {
		return suggestions;
	}
	
	private CommandPermission permission = null;
	
	@Override
	public RotationArgument withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}

	@Override
	public CommandPermission getArgumentPermission() {
		return permission;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.ROTATION;
	}
}
