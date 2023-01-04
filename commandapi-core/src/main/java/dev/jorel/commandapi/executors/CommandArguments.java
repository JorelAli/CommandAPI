package dev.jorel.commandapi.executors;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

/**
 * This class stores the arguments for this command
 */
public class CommandArguments {

	private final Object[] args;
	private final Map<String, Object> argsMap;

	/**
	 * Constructs a new CommandArguments instance
	 *
	 * @param args    The arguments for this command
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
	@Nullable
	public Object get(int index) {
		if (args.length <= index) {
			return null;
		} else {
			return args[index];
		}
	}

	/**
	 * Returns an argument by its node name
	 *
	 * @param nodeName The node name of this argument. This was set when initializing an argument
	 * @return An argument which has the given node name. Can be null if <code>nodeName</code> was not found.
	 */
	@Nullable
	public Object get(String nodeName) {
		return argsMap.get(nodeName);
	}

	/**
	 * Returns an argument by its index
	 *
	 * @param index The position of this argument
	 * @param defaultValue The Object returned if the argument is not existent
	 * @return An argument which is placed at the given index, or the provided default value
	 */
	public Object getOrDefault(int index, Object defaultValue) {
		if (args.length <= index) {
			return defaultValue;
		} else {
			return args[index];
		}
	}

	/**
	 * Returns an argument by its node name
	 *
	 * @param nodeName     The node name of this argument. This was set when initializing an argument
	 * @param defaultValue The Object returned if the argument was not found.
	 * @return The argument with the specified node name or the provided default value
	 */
	public Object getOrDefault(String nodeName, Object defaultValue) {
		return argsMap.getOrDefault(nodeName, defaultValue);
	}

	/**
	 * Returns an argument by its index
	 *
	 * @param index The position of this argument
	 * @param defaultValue The Object returned if the argument is not existent
	 * @return An argument which is placed at the given index, or the provided default value
	 */
	public Object getOrDefault(int index, Supplier<?> defaultValue) {
		if (args.length <= index) {
			return defaultValue.get();
		} else {
			return args[index];
		}
	}

	/**
	 * Returns an argument by its node name
	 *
	 * @param nodeName     The node name of this argument. This was set when initializing an argument
	 * @param defaultValue The Object returned if the argument was not found.
	 * @return The argument with the specified node name or the provided default value
	 */
	public Object getOrDefault(String nodeName, Supplier<?> defaultValue) {
		return argsMap.getOrDefault(nodeName, defaultValue.get());
	}
}
