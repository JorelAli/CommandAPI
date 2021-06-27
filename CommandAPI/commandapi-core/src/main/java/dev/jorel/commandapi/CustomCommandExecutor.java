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
import java.util.NoSuchElementException;

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
import dev.jorel.commandapi.executors.ExecutorType;
import dev.jorel.commandapi.executors.IExecutorNormal;
import dev.jorel.commandapi.executors.IExecutorResulting;
import dev.jorel.commandapi.executors.IExecutorTyped;

class CustomCommandExecutor<T extends CommandSender> {
	
	private List<IExecutorNormal<T>> normalExecutors;
	private List<IExecutorResulting<T>> resultingExecutors;
	
	public CustomCommandExecutor() {
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
	
	public int execute(CommandSender sender, Object[] arguments) throws CommandSyntaxException {
		
		//Parse executor type
        if (!resultingExecutors.isEmpty()) {
            //Run resulting executor
            try {
                return execute(resultingExecutors, sender, arguments);
            } catch (WrapperCommandSyntaxException e) {
                throw e.getException();
            } catch (Exception e) {
                e.printStackTrace(System.out);
                return 0;
            }
        } else {
            //Run normal executor
            try {
                return execute(normalExecutors, sender, arguments);
            } catch (WrapperCommandSyntaxException e) {
                throw e.getException();
            } catch (Exception e) {
                e.printStackTrace(System.out);
                return 0;
            }
        }
	}
	
	private int execute(List<? extends IExecutorTyped> executors, CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
		if(isForceNative()) {
			return execute(executors, sender, args, ExecutorType.NATIVE);
		} else if (sender instanceof Player && matches(executors, ExecutorType.PLAYER)) {
			return execute(executors, sender, args, ExecutorType.PLAYER);
		} else if (sender instanceof Entity && matches(executors, ExecutorType.ENTITY)) {
			return execute(executors, sender, args, ExecutorType.ENTITY);
		} else if (sender instanceof ConsoleCommandSender && matches(executors, ExecutorType.CONSOLE)) {
			return execute(executors, sender, args, ExecutorType.CONSOLE);
		} else if (sender instanceof BlockCommandSender && matches(executors, ExecutorType.BLOCK)) {
			return execute(executors, sender, args, ExecutorType.BLOCK);
		} else if (sender instanceof ProxiedCommandSender && matches(executors, ExecutorType.PROXY)) {
			return execute(executors, sender, args, ExecutorType.PROXY);
		} else if (matches(executors, ExecutorType.ALL)) {
			return execute(executors, sender, args, ExecutorType.ALL);
		} else {
			throw new WrapperCommandSyntaxException(new SimpleCommandExceptionType(
					new LiteralMessage(CommandAPI.getConfiguration().getMissingImplementationMessage()
							.replace("%s", sender.getClass().getSimpleName().toLowerCase())
							.replace("%S", sender.getClass().getSimpleName()))).create());
		}
	}
	
	private int execute(List<? extends IExecutorTyped> executors, CommandSender sender, Object[] args, ExecutorType type) throws WrapperCommandSyntaxException {
		for(IExecutorTyped executor : executors) {
			if(executor.getType() == type) {
				return executor.executeWith(sender, args);
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

	public boolean isEmpty() {
		return normalExecutors.isEmpty() && resultingExecutors.isEmpty();
	}

	public boolean isForceNative() {
		return matches(normalExecutors, ExecutorType.NATIVE) || matches(resultingExecutors, ExecutorType.NATIVE);
	}
	
	private boolean matches(List<? extends IExecutorTyped> executors, ExecutorType type) {
		for(IExecutorTyped executor : executors) {
			if(executor.getType() == type) {
				return true;
			}
		}
		return false;
	}
	
	CustomCommandExecutor<T> mergeExecutor(CustomCommandExecutor<T> executor) {
		CustomCommandExecutor<T> result = new CustomCommandExecutor<>();
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