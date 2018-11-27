package io.github.jorelali.commandapi.api;

class CustomCommandExecutor {
	private CommandExecutor ex;
	private ResultingCommandExecutor rEx;

	public CustomCommandExecutor(CommandExecutor ex, ResultingCommandExecutor rEx) {
		this.ex = ex;
		this.rEx = rEx;
	}

	public boolean hasResults() {
		return rEx != null;
	}

	public CommandExecutor getEx() {
		return ex;
	}

	public ResultingCommandExecutor getResultingEx() {
		return rEx;
	}
}