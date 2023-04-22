package dev.jorel.commandapi;

import com.velocitypowered.api.command.CommandSource;
import dev.jorel.commandapi.commandsenders.VelocityCommandSender;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.*;

public interface VelocityExecutable<Impl extends VelocityExecutable<Impl>> extends PlatformExecutable<Impl, CommandSource> {
	// Regular command executor

	/**
	 * Adds an executor to the current command builder
	 *
	 * @param executor A lambda of type <code>(CommandSource, Object[]) -&gt; ()</code> that will be executed when the command is run
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
					public void run(CommandSource sender, CommandArguments args) throws WrapperCommandSyntaxException {
						executor.executeWith(new VelocityExecutionInfo<>(sender, CommandAPIVelocity.get().wrapCommandSender(sender), args));
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
	 * @param executor A lambda of type <code>(CommandSource, CommandArguments) -&gt; ()</code> that will be executed when the command is run
	 * @param types    A list of executor types to use this executes method for.
	 * @return this command builder
	 */
	default Impl executes(CommandExecutionInfo executor, ExecutorType... types) {
		if (types == null || types.length == 0) {
			getExecutor().addNormalExecutor(executor);
		} else {
			for (ExecutorType type : types) {
				getExecutor().addNormalExecutor(new CommandExecutionInfo() {

					@Override
					public void run(ExecutionInfo<CommandSource, VelocityCommandSender<? extends CommandSource>> info) throws WrapperCommandSyntaxException {
						executor.executeWith(info);
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
	 * @param executor A lambda of type <code>(CommandSource, Object[]) -&gt; int</code> that will be executed when the command is run
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
					public int run(CommandSource sender, CommandArguments args) throws WrapperCommandSyntaxException {
						return executor.executeWith(new VelocityExecutionInfo<>(sender, CommandAPIVelocity.get().wrapCommandSender(sender), args));
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

	// Player command executor

	/**
	 * Adds an executor to the current command builder
	 *
	 * @param executor A lambda of type <code>(Player, CommandArguments) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	default Impl executesPlayer(PlayerCommandExecutor executor) {
		getExecutor().addNormalExecutor(executor);
		return instance();
	}

	/**
	 * Adds an executor to the current command builder
	 *
	 * @param executor A lambda of type <code>(ExecutionInfo) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	default Impl executesPlayer(PlayerExecutionInfo executor) {
		getExecutor().addNormalExecutor(executor);
		return instance();
	}

	/**
	 * Adds an executor to the current command builder
	 *
	 * @param executor A lambda of type <code>(Player, CommandArguments) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	default Impl executesPlayer(PlayerResultingCommandExecutor executor) {
		getExecutor().addResultingExecutor(executor);
		return instance();
	}

	/**
	 * Adds an executor to the current command builder
	 *
	 * @param executor A lambda of type <code>(ExecutionInfo) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	default Impl executesPlayer(PlayerResultingExecutionInfo executor) {
		getExecutor().addResultingExecutor(executor);
		return instance();
	}

	// Console command executor

	/**
	 * Adds an executor to the current command builder
	 *
	 * @param executor A lambda of type <code>(ConsoleCommandSource, CommandArguments) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	default Impl executesConsole(ConsoleCommandExecutor executor) {
		getExecutor().addNormalExecutor(executor);
		return instance();
	}

	/**
	 * Adds an executor to the current command builder
	 *
	 * @param executor A lambda of type <code>(ExecutionInfo) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	default Impl executesConsole(ConsoleExecutionInfo executor) {
		getExecutor().addNormalExecutor(executor);
		return instance();
	}

	/**
	 * Adds an executor to the current command builder
	 *
	 * @param executor A lambda of type <code>(ConsoleCommandSource, CommandArguments) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	default Impl executesConsole(ConsoleResultingCommandExecutor executor) {
		getExecutor().addResultingExecutor(executor);
		return instance();
	}

	/**
	 * Adds an executor to the current command builder
	 *
	 * @param executor A lambda of type <code>(ExecutionInfo) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	default Impl executesConsole(ConsoleResultingExecutionInfo executor) {
		getExecutor().addResultingExecutor(executor);
		return instance();
	}
}
