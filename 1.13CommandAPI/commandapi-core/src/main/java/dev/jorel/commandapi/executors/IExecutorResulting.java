package dev.jorel.commandapi.executors;

import org.bukkit.command.CommandSender;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

public interface IExecutorResulting<T extends CommandSender> {
	
	/**
	 * Executes the command executor with the provided command sender and the provided arguments.
	 * @param sender the command sender for this command
	 * @param args the arguments provided to this command
	 * @return the value returned by this command if the command succeeds, 0 if the command fails
	 * @throws WrapperCommandSyntaxException if an error occurs during the execution of this command
	 */
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
	
	/**
	 * Returns the type of the sender of the current executor.
	 * @return the type of the sender of the current executor
	 */
	default ExecutorType getType() {
		return ExecutorType.ALL;
	}

	/**
	 * Executes the command.
	 * @param sender the command sender for this command
	 * @param args the arguments provided to this command
	 * @return the value returned by this command
	 * @throws WrapperCommandSyntaxException if an error occurs during the execution of this command
	 */
	int run(T sender, Object[] args) throws WrapperCommandSyntaxException;
	
}
