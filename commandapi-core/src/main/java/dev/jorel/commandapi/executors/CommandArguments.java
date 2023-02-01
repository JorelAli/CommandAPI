package dev.jorel.commandapi.executors;

import javax.annotation.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

/**
 * This class stores the arguments for this command
 */
@SuppressWarnings("unchecked")
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
	
	// Access the inner structure directly

	/**
	 * @return The complete argument array of this command
	 */
	public Object[] args() {
		return args;
	}
	
	/**
	 * @return An unmodifiable clone of the mapping of node names to argument values
	 */
	public Map<String, Object> argsMap() {
		return Collections.unmodifiableMap(argsMap);
	}
	
	// Main accessing methods. In Kotlin, methods named get() allows it to
	// access these methods using array notation, as a part of operator overloading.
	// More information about operator overloading in Kotlin can be found here:
	// https://kotlinlang.org/docs/operator-overloading.html

	/**
	 * Returns an argument by its position
	 *
	 * @param index The position of this argument
	 * @return An argument which is placed at the given index, or {@code null} if the provided index does not point to an argument.
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
	
	// Optional accessing methods

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
	
	/** Unchecked methods. These are the same as the methods above, but use
	 * unchecked generics to conform to the type they are declared as. In Java,
	 * the normal methods (checked) require casting:
	 *
	 *   CommandArguments args = ...;
	 *   String myString = (String) args.get("target");
	 *
	 * However, these unchecked methods don't require casting:
	 *
	 *   CommandArguments args = ...;
	 *   String myString = args.getUnchecked("target");
	 *
	 * These methods are to be avoided in Kotlin as Kotlin's type inference
	 * system cannot infer the type variable T by default and would require
	 * explicit generic type parameters or type declaration, as well as a
	 * non-null assertion operator:
	 *
	 *   val args: CommandArguments = ...
	 *   val myString = args.<String>getUnchecked("target")!!  // Needs this
	 *   val myString: String = args.getUnchecked(0)!!         // Or this
	 */

	/**
	 * Returns an argument by its position
	 *
	 * @param index The position of this argument
	 * @return An argument which is placed at the given index, or {@code null} if the provided index does not point to an argument.
	 */
	@Nullable
	public <T> T getUnchecked(int index) {
		return (T) get(index);
	}
	
	/**
	 * Returns an argument by its node name
	 *
	 * @param nodeName The node name of this argument. This was set when initializing an argument
	 * @return An argument which has the given node name. Can be null if <code>nodeName</code> was not found.
	 */
	@Nullable
	public <T> T getUnchecked(String nodeName) {
		return (T) get(nodeName);
	}

	/**
	 * Returns an argument by its index
	 *
	 * @param index The position of this argument
	 * @param defaultValue The Object returned if the argument is not existent
	 * @return An argument which is placed at the given index, or the provided default value
	 */
	public <T> T getOrDefaultUnchecked(int index, T defaultValue) {
		return (T) getOrDefault(index, defaultValue);
	}

	/**
	 * Returns an argument by its node name
	 *
	 * @param nodeName     The node name of this argument. This was set when initializing an argument
	 * @param defaultValue The Object returned if the argument was not found.
	 * @return The argument with the specified node name or the provided default value
	 */
	public <T> T getOrDefaultUnchecked(String nodeName, T defaultValue) {
		return (T) getOrDefault(nodeName, defaultValue);
	}

	/**
	 * Returns an argument by its index
	 *
	 * @param index The position of this argument
	 * @param defaultValue The Object returned if the argument is not existent
	 * @return An argument which is placed at the given index, or the provided default value
	 */
	public <T> T getOrDefaultUnchecked(int index, Supplier<T> defaultValue) {
		return (T) getOrDefault(index, defaultValue);
	}

	/**
	 * Returns an argument by its node name
	 *
	 * @param nodeName     The node name of this argument. This was set when initializing an argument
	 * @param defaultValue The Object returned if the argument was not found.
	 * @return The argument with the specified node name or the provided default value
	 */
	public <T> T getOrDefaultUnchecked(String nodeName, Supplier<T> defaultValue) {
		return (T) getOrDefault(nodeName, defaultValue);
	}
}
