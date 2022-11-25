/*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import dev.jorel.commandapi.executors.*;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

/**
 * CommandAPIExecutor is the main executor implementation for command
 * executors. It contains a list of all executors (normal and resulting
 * executors) and switches its execution implementation based on the provided
 * command executor types.
 *
 * @param <T> a command sender
 */
public class CommandAPIExecutor<T extends CommandSender> {

	private List<IExecutorNormal<T>> normalExecutors;
	private List<IExecutorResulting<T>> resultingExecutors;

	public CommandAPIExecutor() {
		normalExecutors = new ArrayList<>();
		resultingExecutors = new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	public <S extends IExecutorNormal<?>> void addNormalExecutor(S executor) {
		this.normalExecutors.add((IExecutorNormal<T>) executor);
	}

	@SuppressWarnings("unchecked")
	public <S extends IExecutorResulting<?>> void addResultingExecutor(S executor) {
		this.resultingExecutors.add((IExecutorResulting<T>) executor);
	}

	public int execute(ExecutionInfo<CommandSender> info) throws CommandSyntaxException {

		// Parse executor type
		if (!resultingExecutors.isEmpty()) {
			// Run resulting executor
			try {
				return execute(resultingExecutors, info);
			} catch (WrapperCommandSyntaxException e) {
				throw e.getException();
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return 0;
			}
		} else {
			// Run normal executor
			try {
				return execute(normalExecutors, info);
			} catch (WrapperCommandSyntaxException e) {
				throw e.getException();
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return 0;
			}
		}
	}

	private <Sender extends CommandSender> int execute(List<? extends IExecutorTyped> executors, ExecutionInfo<Sender> info)
			throws WrapperCommandSyntaxException {
		if (isForceNative()) {
			return execute(executors, info, ExecutorType.NATIVE);
		} else if (info.sender() instanceof Player && matches(executors, ExecutorType.PLAYER)) {
			return execute(executors, info, ExecutorType.PLAYER);
		} else if (info.sender() instanceof Entity && matches(executors, ExecutorType.ENTITY)) {
			return execute(executors, info, ExecutorType.ENTITY);
		} else if (info.sender() instanceof ConsoleCommandSender && matches(executors, ExecutorType.CONSOLE)) {
			return execute(executors, info, ExecutorType.CONSOLE);
		} else if (info.sender() instanceof BlockCommandSender && matches(executors, ExecutorType.BLOCK)) {
			return execute(executors, info, ExecutorType.BLOCK);
		} else if (info.sender() instanceof ProxiedCommandSender && matches(executors, ExecutorType.PROXY)) {
			return execute(executors, info, ExecutorType.PROXY);
		} else if (matches(executors, ExecutorType.ALL)) {
			return execute(executors, info, ExecutorType.ALL);
		} else {
			throw new WrapperCommandSyntaxException(new SimpleCommandExceptionType(
					new LiteralMessage(CommandAPI.getConfiguration().getMissingImplementationMessage()
							.replace("%s", info.sender().getClass().getSimpleName().toLowerCase())
							.replace("%S", info.sender().getClass().getSimpleName()))).create());
		}
	}

	private <Sender extends CommandSender> int execute(List<? extends IExecutorTyped> executors, ExecutionInfo<Sender> info, ExecutorType type) throws WrapperCommandSyntaxException {
		for (IExecutorTyped executor : executors) {
			if (executor.getType() == type) {
				return executor.executeWith(info);
			}
		}
		throw new NoSuchElementException("Executor had no valid executors for type " + type.toString());
	}

	public List<IExecutorNormal<T>> getNormalExecutors() {
		return normalExecutors;
	}

	public List<IExecutorResulting<T>> getResultingExecutors() {
		return resultingExecutors;
	}

	public boolean hasAnyExecutors() {
		return !(normalExecutors.isEmpty() && resultingExecutors.isEmpty());
	}

	public boolean isForceNative() {
		return matches(normalExecutors, ExecutorType.NATIVE) || matches(resultingExecutors, ExecutorType.NATIVE);
	}

	private boolean matches(List<? extends IExecutorTyped> executors, ExecutorType type) {
		for (IExecutorTyped executor : executors) {
			if (executor.getType() == type) {
				return true;
			}
		}
		return false;
	}

	CommandAPIExecutor<T> mergeExecutor(CommandAPIExecutor<T> executor) {
		CommandAPIExecutor<T> result = new CommandAPIExecutor<>();
		result.normalExecutors = new ArrayList<>(normalExecutors);
		result.resultingExecutors = new ArrayList<>(resultingExecutors);
		result.normalExecutors.addAll(executor.normalExecutors);
		result.resultingExecutors.addAll(executor.resultingExecutors);
		return result;
	}

	public void setNormalExecutors(List<IExecutorNormal<T>> normalExecutors) {
		this.normalExecutors = normalExecutors;
	}

	public void setResultingExecutors(List<IExecutorResulting<T>> resultingExecutors) {
		this.resultingExecutors = resultingExecutors;
	}
}