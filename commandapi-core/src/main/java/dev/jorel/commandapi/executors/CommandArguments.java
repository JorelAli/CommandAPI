package dev.jorel.commandapi.executors;

import java.util.Map;

/**
 * This class stores the arguments for this command
 */
public class CommandArguments {

	private final Object[] args;
	private final Map<String, Object> argsMap;

	/**
	 * Constructs a new CommandArguments instance
	 *
	 * @param args The arguments for this command
	 * @param argsMap The arguments for this command mapped to the node names. This is an ordered map
	 */
	public CommandArguments(Object[] args, Map<String, Object> argsMap) {
		this.args = args;
		this.argsMap = argsMap;
	}

	/**
	 * @return The complete argument array of this command
	 */
	public Object[] args() {
		return args;
	}

	/**
	 * Returns an argument by its position
	 *
	 * @param index The position of this argument
	 * @return an argument which is placed at the given index
	 */
	public Object get(int index) {
		return args[index];
	}

	/**
	 * Returns an argument by its node name
	 *
	 * @param nodeName The node name of this argument. This was set when initializing an argument
	 * @return an argument which has the given node name
	 */
	public Object get(String nodeName) {
		return argsMap.get(nodeName);
 	}
}
