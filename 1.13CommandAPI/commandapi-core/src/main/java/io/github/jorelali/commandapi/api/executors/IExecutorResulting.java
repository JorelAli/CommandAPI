package io.github.jorelali.commandapi.api.executors;

import org.bukkit.command.CommandSender;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import io.github.jorelali.commandapi.api.exceptions.WrapperCommandSyntaxException;

public interface IExecutorResulting<T extends CommandSender> {
	
	@SuppressWarnings("unchecked")
	default int executeWith(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
		Class<?> type = this.getClass().getDeclaredMethods()[0].getParameterTypes()[0];
		if(type.isInstance(sender)) {
			return this.run((T) type.cast(sender), args);
		} else {
			throw new WrapperCommandSyntaxException(
				new SimpleCommandExceptionType(
					new LiteralMessage("You must be a " + type.getSimpleName().toLowerCase() + " to run this command")
				).create()
			);
		}
	}
	
	default ExecutorType getType() {
		return ExecutorType.ALL;
	}

	int run(T sender, Object[] args) throws WrapperCommandSyntaxException;
	
}
