package dev.jorel.commandapi.spying;

import dev.jorel.commandapi.commandsenders.AbstractCommandSender;
import dev.jorel.commandapi.executors.ExecutionInfo;
import org.bukkit.command.CommandSender;
import org.opentest4j.AssertionFailedError;

import java.util.LinkedList;
import java.util.Queue;

public class ExecutionQueue {
	Queue<ExecutionInfo<CommandSender, AbstractCommandSender<? extends CommandSender>>> queue = new LinkedList<>();

	public void clear() {
		queue.clear();
	}

	public void add(ExecutionInfo<CommandSender, AbstractCommandSender<? extends CommandSender>> info) {
		queue.add(info);
	}

	public ExecutionInfo<CommandSender, AbstractCommandSender<? extends CommandSender>> poll() {
		return queue.poll();
	}

	public void assertNoMoreCommandsWereRun() {
		if (!queue.isEmpty()) {
			throw new AssertionFailedError("Expected no more commands to be run, but found " + queue.size() + " command(s) left");
		}
	}
}
