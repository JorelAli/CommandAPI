package dev.jorel.commandapi;

import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandBlockCommandExecutor;
import dev.jorel.commandapi.executors.CommandBlockResultingCommandExecutor;
import dev.jorel.commandapi.executors.CommandExecutor;
import dev.jorel.commandapi.executors.ConsoleCommandExecutor;
import dev.jorel.commandapi.executors.ConsoleResultingCommandExecutor;
import dev.jorel.commandapi.executors.EntityCommandExecutor;
import dev.jorel.commandapi.executors.EntityResultingCommandExecutor;
import dev.jorel.commandapi.executors.ExecutorType;
import dev.jorel.commandapi.executors.NativeCommandExecutor;
import dev.jorel.commandapi.executors.NativeResultingCommandExecutor;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import dev.jorel.commandapi.executors.PlayerResultingCommandExecutor;
import dev.jorel.commandapi.executors.ProxyCommandExecutor;
import dev.jorel.commandapi.executors.ProxyResultingCommandExecutor;
import dev.jorel.commandapi.executors.ResultingCommandExecutor;

public class CommandAPICommand extends AbstractCommandAPICommand {

	public CommandAPICommand(CommandAPICommand original) {
		super(original);
	}
	
	public CommandAPICommand(CommandMetaData meta) {
		super(meta);
	}
	
	public CommandAPICommand(String commandName) {
		super(commandName);
	}
	


	// Regular command executor

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(CommandSender, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @param types A list of executor types to use this executes method for.
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public CommandAPICommand executes(CommandExecutor executor, ExecutorType... types) {
		if(types == null || types.length == 0) {
			this.executor.addNormalExecutor(executor);
		} else {
			for(ExecutorType type : types) {
				this.executor.addNormalExecutor(new CommandExecutor() {

					@Override
					public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
						executor.executeWith(sender, args);
					}
					
					@Override
					public ExecutorType getType() {
						return type;
					}
				});
			}
		}
		return this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(CommandSender, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @param types A list of executor types to use this executes method for.
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public CommandAPICommand executes(ResultingCommandExecutor executor, ExecutorType... types) {
		if(types == null || types.length == 0) {
			this.executor.addResultingExecutor(executor);
		} else {
			for(ExecutorType type : types) {
				this.executor.addResultingExecutor(new ResultingCommandExecutor() {

					@Override
					public int run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
						return executor.executeWith(sender, args);
					}
					
					@Override
					public ExecutorType getType() {
						return type;
					}
				});
			}
		}
		return this;
	}

	// Player command executor

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Player, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesPlayer(PlayerCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Player, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesPlayer(PlayerResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return this;
	}

	// Entity command executor

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Entity, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesEntity(EntityCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Entity, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesEntity(EntityResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return this;
	}

	// Proxy command executor

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Entity, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesProxy(ProxyCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Entity, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesProxy(ProxyResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return this;
	}

	// Command block command sender

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(BlockCommandSender, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesCommandBlock(CommandBlockCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(BlockCommandSender, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesCommandBlock(CommandBlockResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return this;
	}

	// Console command sender

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(BlockCommandSender, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesConsole(ConsoleCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(BlockCommandSender, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesConsole(ConsoleResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(NativeCommandExecutor, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesNative(NativeCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(NativeCommandExecutor, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	public CommandAPICommand executesNative(NativeResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return this;
	}
	
}
