package dev.jorel.commandapi;

public class CommandAPIConfig {

	public boolean verboseOutput;

	/**
	 * Creates a new CommandAPI configuration
	 */
	public CommandAPIConfig() {
		verboseOutput = false;
	}

	/**
	 * Returns whether verbose output is enabled
	 * 
	 * @return true if verbose output is enabled
	 */
	public boolean isVerboseOutput() {
		return verboseOutput;
	}

	/**
	 * Sets verbose output
	 * 
	 * @param verboseOutput whether verbose output should be enabled
	 */
	public void setVerboseOutput(boolean verboseOutput) {
		this.verboseOutput = verboseOutput;
	}

}
