package dev.jorel.commandapi;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import dev.jorel.commandapi.executors.*;
import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

/**
 * This class represents something that is executable. This is mostly, {@link CommandAPICommand} instances, or can also be {@link CommandTree} nodes and even {@link Argument} nodes in a tree
 *
 * @param <T> return type for chain calls
 */
abstract class Executable<T extends Executable<T>> {

	/**
	 * The CommandAPIExecutor for this executable implementation
	 */
	protected CommandAPIExecutor<?> executor = new CommandAPIExecutor<>();

	// Regular command executor

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(CommandSender, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @param types A list of executor types to use this executes method for.
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executes(CommandExecutor executor, ExecutorType... types) {
		if(types == null || types.length == 0) {
			this.executor.addNormalExecutor(executor);
		} else {
			for(ExecutorType type : types) {
				this.executor.addNormalExecutor(new CommandExecutor() {

					@Override
					public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
						run(sender, args, new LinkedHashMap<>());
					}

					@Override
					public void run(CommandSender sender, Object[] args, Map<String, Object> argsMap) throws WrapperCommandSyntaxException {
						executor.executeWith(sender, args, argsMap);
					}

					@Override
					public ExecutorType getType() {
						return type;
					}
				});
			}
		}
		return (T) this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(CommandSender, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @param types A list of executor types to use this executes method for.
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executes(CommandExecutionInfo executor, ExecutorType... types) {
		if(types == null || types.length == 0) {
			this.executor.addNormalExecutor(executor);
		} else {
			for(ExecutorType type : types) {
				this.executor.addNormalExecutor(new CommandExecutionInfo() {

					@Override
					public void run(CommandSender sender, Object[] args, Map<String, Object> argsMap) throws WrapperCommandSyntaxException {
						executor.executeWith(sender, args, argsMap);
					}

					@Override
					public ExecutorType getType() {
						return type;
					}
				});
			}
		}
		return (T) this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(CommandSender, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @param types A list of executor types to use this executes method for.
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executes(ResultingCommandExecutor executor, ExecutorType... types) {
		if(types == null || types.length == 0) {
			this.executor.addResultingExecutor(executor);
		} else {
			for(ExecutorType type : types) {
				this.executor.addResultingExecutor(new ResultingCommandExecutor() {

					@Override
					public int run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
						return run(sender, args, new LinkedHashMap<>());
					}

					@Override
					public int run(CommandSender sender, Object[] args, Map<String, Object> argsMap) throws WrapperCommandSyntaxException {
						return executor.executeWith(sender, args, argsMap);
					}

					@Override
					public ExecutorType getType() {
						return type;
					}
				});
			}
		}
		return (T) this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(CommandSender, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @param types A list of executor types to use this executes method for.
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executes(ResultingCommandExecutionInfo executor, ExecutorType... types) {
		if(types == null || types.length == 0) {
			this.executor.addResultingExecutor(executor);
		} else {
			for(ExecutorType type : types) {
				this.executor.addResultingExecutor(new ResultingCommandExecutionInfo() {

					@Override
					public int run(CommandSender sender, Object[] args, Map<String, Object> argsMap) throws WrapperCommandSyntaxException {
						return executor.executeWith(sender, args, argsMap);
					}

					@Override
					public ExecutorType getType() {
						return type;
					}
				});
			}
		}
		return (T) this;
	}

	// Player command executor

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Player, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesPlayer(PlayerCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return (T) this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param info A lambda of type <code>(Player, Object[], Map&lt;String, Object&gt;) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesPlayer(PlayerExecutionInfo info) {
		this.executor.addNormalExecutor(info);
		return (T) this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Player, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesPlayer(PlayerResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return (T) this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param info A lambda of type <code>(Player, Object[], Map&lt;String, Object&gt;) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesPlayer(PlayerResultingExecutionInfo info) {
		this.executor.addResultingExecutor(info);
		return (T) this;
	}

	// Entity command executor

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Entity, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesEntity(EntityCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return (T) this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param info A lambda of type <code>(Entity, Object[], Map&lt;String, Object&gt;) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesEntity(EntityExecutionInfo info) {
		this.executor.addNormalExecutor(info);
		return (T) this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Entity, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesEntity(EntityResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return (T) this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param info A lambda of type <code>(Entity, Object[], Map&lt;String, Object&gt;) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesEntity(EntityResultingExecutionInfo info) {
		this.executor.addResultingExecutor(info);
		return (T) this;
	}

	// Proxy command executor

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Entity, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesProxy(ProxyCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return (T) this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param info A lambda of type <code>(Entity, Object[], Map&lt;String, Object&gt;) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesProxy(ProxyExecutionInfo info) {
		this.executor.addNormalExecutor(info);
		return (T) this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(Entity, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesProxy(ProxyResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return (T) this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param info A lambda of type <code>(Entity, Object[], Map&lt;String, Object&gt;) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesProxy(ProxyResultingExecutionInfo info) {
		this.executor.addResultingExecutor(info);
		return (T) this;
	}

	// Command block command sender

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(BlockCommandSender, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesCommandBlock(CommandBlockCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return (T) this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param info A lambda of type <code>(BlockCommandSender, Object[], Map&lt;String, Object&gt;) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesCommandBlock(CommandBlockExecutionInfo info) {
		this.executor.addNormalExecutor(info);
		return (T) this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(BlockCommandSender, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesCommandBlock(CommandBlockResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return (T) this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param info A lambda of type <code>(BlockCommandSender, Object[], Map&lt;String, Object&gt;) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesCommandBlock(CommandBlockResultingExecutionInfo info) {
		this.executor.addResultingExecutor(info);
		return (T) this;
	}

	// Console command sender

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(ConsoleCommandSender, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesConsole(ConsoleCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return (T) this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param info A lambda of type <code>(ConsoleCommandSender, Object[], Map&lt;String, Object&gt;) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesConsole(ConsoleExecutionInfo info) {
		this.executor.addNormalExecutor(info);
		return (T) this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(ConsoleCommandSender, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesConsole(ConsoleResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return (T) this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param info A lambda of type <code>(ConsoleCommandSender, Object[], Map&lt;String, Object&gt;) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesConsole(ConsoleResultingExecutionInfo info) {
		this.executor.addResultingExecutor(info);
		return (T) this;
	}

	// Native command sender

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(NativeCommandExecutor, Object[]) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesNative(NativeCommandExecutor executor) {
		this.executor.addNormalExecutor(executor);
		return (T) this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param info A lambda of type <code>(NativeCommandExecutor, Object[], Map&lt;String, Object&gt;) -&gt; ()</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesNative(NativeExecutionInfo info) {
		this.executor.addNormalExecutor(info);
		return (T) this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param executor A lambda of type <code>(NativeCommandExecutor, Object[]) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesNative(NativeResultingCommandExecutor executor) {
		this.executor.addResultingExecutor(executor);
		return (T) this;
	}

	/**
	 * Adds an executor to the current command builder
	 * @param info A lambda of type <code>(NativeCommandExecutor, Object[], Map&lt;String, Object&gt;) -&gt; int</code> that will be executed when the command is run
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T executesNative(NativeResultingExecutionInfo info) {
		this.executor.addResultingExecutor(info);
		return (T) this;
	}

	/**
	 * Returns the executors that this command has
	 * @return the executors that this command has
	 */
	public CommandAPIExecutor<? extends CommandSender> getExecutor() {
		return executor;
	}

	/**
	 * Sets the executors for this command
	 * @param executor the executors for this command
	 */
	public void setExecutor(CommandAPIExecutor<? extends CommandSender> executor) {
		this.executor = executor;
	}

	/**
	 * Clear all executors from the current command builder
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T clearExecutors() {
		this.executor.setNormalExecutors(new ArrayList<>());
		this.executor.setResultingExecutors(new ArrayList<>());
		return (T) this;
	}

}
