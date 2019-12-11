package io.github.jorelali.commandapi.api;

import org.bukkit.command.CommandSender;

import io.github.jorelali.commandapi.api.executors.IExecutorN;
import io.github.jorelali.commandapi.api.executors.IExecutorR;

class CustomCommandExecutor {
	private IExecutorN<? extends CommandSender> ex;
	private IExecutorR<? extends CommandSender> rEx;

	public CustomCommandExecutor(IExecutorN<? extends CommandSender> ex) {
		this.ex = ex;
		this.rEx = null;
	}
	
	public CustomCommandExecutor(IExecutorR<? extends CommandSender> rEx) {
		this.ex = null;
		this.rEx = rEx;
	}

	public boolean hasResults() {
		return rEx != null;
	}

	public IExecutorN<? extends CommandSender> getEx() {
		return ex;
	}

	public IExecutorR<? extends CommandSender> getResultingEx() {
		return rEx;
	}
}