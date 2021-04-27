package dev.jorel.commandapi;

public class CommandAPIConfig {

	public boolean verboseOutput;
	public boolean createDispatcherFile;

	public CommandAPIConfig() {
		verboseOutput = false;
		createDispatcherFile = false;
	}

	public boolean isVerboseOutput() {
		return verboseOutput;
	}

	public void setVerboseOutput(boolean verboseOutput) {
		this.verboseOutput = verboseOutput;
	}

	public boolean isCreateDispatcherFile() {
		return createDispatcherFile;
	}

	public void setCreateDispatcherFile(boolean createDispatcherFile) {
		this.createDispatcherFile = createDispatcherFile;
	}

}