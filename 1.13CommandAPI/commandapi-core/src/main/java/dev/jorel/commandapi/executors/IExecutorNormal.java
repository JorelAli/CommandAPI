package dev.jorel.commandapi.executors;

import org.bukkit.command.CommandSender;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

public interface IExecutorNormal<T extends CommandSender> {
	
	@SuppressWarnings("unchecked")
	default int executeWith(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
		Class<?> type = this.getClass().getDeclaredMethods()[0].getParameterTypes()[0];
		if(type.isInstance(sender)) {
			this.run((T) type.cast(sender), args);
			return 1;
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
	
	void run(T sender, Object[] args) throws WrapperCommandSyntaxException;

}
