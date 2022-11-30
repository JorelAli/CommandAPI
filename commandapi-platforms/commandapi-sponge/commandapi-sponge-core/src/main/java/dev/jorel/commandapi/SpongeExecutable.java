package dev.jorel.commandapi;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import dev.jorel.commandapi.executors.ExecutorType;
import dev.jorel.commandapi.executors.ResultingCommandExecutor;

// TODO: Replace CommandSource with the class Sponge uses to send commands
public interface SpongeExecutable<Impl extends SpongeExecutable<Impl>> extends PlatformExecutable<Impl, CommandSource> {
	// Regular command executor

	/**
	 * Adds an executor to the current command builder
	 *
	 * @param executor A lambda of type <code>(CommandSender, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @param types    A list of executor types to use this executes method for.
	 * @return this command builder
	 */
	default Impl executes(CommandExecutor executor, ExecutorType... types) {
		if (types == null || types.length == 0) {
			getExecutor().addNormalExecutor(executor);
		} else {
			for (ExecutorType type : types) {
				getExecutor().addNormalExecutor(new CommandExecutor() {

					@Override
					public void run(CommandSource sender, Object[] args) throws WrapperCommandSyntaxException {
						executor.executeWith(CommandAPISponge.get().wrapCommandSender(sender), args);
					}

					@Override
					public ExecutorType getType() {
						return type;
					}
				});
			}
		}
		return instance();
	}

	/**
	 * Adds an executor to the current command builder
	 *
	 * @param executor A lambda of type <code>(CommandSender, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @param types    A list of executor types to use this executes method for.
	 * @return this command builder
	 */
	default Impl executes(ResultingCommandExecutor executor, ExecutorType... types) {
		if (types == null || types.length == 0) {
			getExecutor().addResultingExecutor(executor);
		} else {
			for (ExecutorType type : types) {
				getExecutor().addResultingExecutor(new ResultingCommandExecutor() {

					@Override
					public int run(CommandSource sender, Object[] args) throws WrapperCommandSyntaxException {
						return executor.executeWith(CommandAPISponge.get().wrapCommandSender(sender), args);
					}

					@Override
					public ExecutorType getType() {
						return type;
					}
				});
			}
		}
		return instance();
	}

	// TODO: Add methods for the different types of CommandSenders provided by Sponge
}
