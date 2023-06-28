package dev.jorel.commandapi.executors;

import javax.annotation.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * This class stores the arguments for this command
 */
@SuppressWarnings("unchecked")
public class CommandArguments {

	private final Object[] args;
	private final String[] rawArgs;
	private final Map<String, Object> argsMap;
	private final Map<String, String> rawArgsMap;
	private final String fullInput;

	/**
	 * Constructs a new CommandArguments instance
	 *
	 * @param args      The arguments for this command
	 * @param argsMap   The arguments for this command mapped to their node names. This is an ordered map
	 * @param rawArgs   The raw arguments for this command.
	 * @param rawArgsMap   The raw arguments for this command mapped to their node names. This is an ordered map
	 * @param fullInput The raw command a player has entered
	 */
	public CommandArguments(Object[] args, Map<String, Object> argsMap, String[] rawArgs, Map<String, String> rawArgsMap, String fullInput) {
		this.args = args;
		this.rawArgs = rawArgs;
		this.argsMap = argsMap;
		this.rawArgsMap = rawArgsMap;
		this.fullInput = fullInput;
	}

	// Access the inner structure directly

	/**
	 * @return The complete argument array of this command
	 */
	public Object[] args() {
		return args;
	}

	/**
	 * @return The complete raw argument array of this command
	 */
	public String[] rawArgs() {
		return rawArgs;
	}

	/**
	 * @return An unmodifiable clone of the mapping of node names to argument values
	 */
	public Map<String, Object> argsMap() {
		return Collections.unmodifiableMap(argsMap);
	}

	/**
	 * @return An unmodifiable clone of the mapping of node names to raw arguments
	 */
	public Map<String, String> rawArgsMap() {
		return Collections.unmodifiableMap(rawArgsMap);
	}

	/**
	 * This returns the raw command string a player has entered
	 *
	 * @return The raw command string a player has entered
	 */
	public String getFullInput() {
		return fullInput;
	}

	/**
	 * @return The number of arguments in this object
	 */
	public int count() {
		return args.length;
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

	/**
	 * Returns an <code>Optional</code> holding the argument by its index
	 *
	 * @param index The position of this argument
	 * @return An optional holding the argument which is placed at the given index, or an empty optional if index is invalid
	 */
	public Optional<Object> getOptional(int index) {
		if (args.length <= index) {
			return Optional.empty();
		} else {
			return Optional.of(args[index]);
		}
	}

	/**
	 * Returns an argument by its node name
	 *
	 * @param nodeName     The node name of this argument. This was set when initializing an argument
	 * @return An optional holding the argument with the specified node name or an empty optional if the node name was not found
	 */
	public Optional<Object> getOptional(String nodeName) {
		if (!argsMap.containsKey(nodeName)) {
			return Optional.empty();
		}
		return Optional.of(argsMap.get(nodeName));
	}

	/**
	 * Returns a raw argument by its position
	 *
	 * @param index The position of this argument
	 * @return An argument which is placed at the given index, or {@code null} if the provided index does not point to an argument.
	 */
	@Nullable
	public String getRaw(int index) {
		if (rawArgs.length <= index) {
			return null;
		} else {
			return rawArgs[index];
		}
	}

	/**
	 * Returns a raw argument by its node name
	 *
	 * @param nodeName The node name of this argument. This was set when initializing an argument
	 * @return A raw argument which has the given node name. Can be null if <code>nodeName</code> was not found.
	 */
	@Nullable
	public String getRaw(String nodeName) {
		return rawArgsMap.get(nodeName);
	}

	/**
	 * Returns a raw argument by its index
	 *
	 * @param index The position of this argument
	 * @param defaultValue The String returned if the raw argument is not existent
	 * @return A raw argument which is placed at the given index, or the provided default value
	 */
	public String getOrDefaultRaw(int index, String defaultValue) {
		if (rawArgs.length <= index) {
			return defaultValue;
		} else {
			return rawArgs[index];
		}
	}

	/**
	 * Returns a raw argument by its node name
	 *
	 * @param nodeName     The node name of this argument. This was set when initializing an argument
	 * @param defaultValue The String returned if the raw argument was not found.
	 * @return A raw argument with the specified node name or the provided default value
	 */
	public String getOrDefaultRaw(String nodeName, String defaultValue) {
		return rawArgsMap.getOrDefault(nodeName, defaultValue);
	}

	/**
	 * Returns a raw argument by its index
	 *
	 * @param index The position of this argument
	 * @param defaultValue The String returned if the raw argument is not existent
	 * @return A raw argument which is placed at the given index, or the provided default value
	 */
	public String getOrDefaultRaw(int index, Supplier<String> defaultValue) {
		if (rawArgs.length <= index) {
			return defaultValue.get();
		} else {
			return rawArgs[index];
		}
	}

	/**
	 * Returns a raw argument by its node name
	 *
	 * @param nodeName     The node name of this raw argument. This was set when initializing an argument
	 * @param defaultValue The String returned if the raw argument was not found.
	 * @return A raw argument with the specified node name or the provided default value
	 */
	public String getOrDefaultRaw(String nodeName, Supplier<String> defaultValue) {
		return rawArgsMap.getOrDefault(nodeName, defaultValue.get());
	}

	/**
	 * Returns an <code>Optional</code> holding the raw argument by its index
	 *
	 * @param index The position of this argument
	 * @return An optional holding the raw argument which is placed at the given index, or an empty optional if index is invalid
	 */
	public Optional<String> getRawOptional(int index) {
		if (rawArgs.length <= index) {
			return Optional.empty();
		} else {
			return Optional.of(rawArgs[index]);
		}
	}

	/**
	 * Returns an argument by its node name
	 *
	 * @param nodeName     The node name of this argument. This was set when initializing an argument
	 * @return An optional holding the argument with the specified node name or an empty optional if the node name was not found
	 */
	public Optional<String> getRawOptional(String nodeName) {
		if (!argsMap.containsKey(nodeName)) {
			return Optional.empty();
		}
		return Optional.of(rawArgsMap.get(nodeName));
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

	/**
	 * Returns an <code>Optional</code> holding the argument at its index
	 *
	 * @param index The position of this argument
	 * @return An optional holding the argument at its index, or an empty optional if the index was invalid
	 */
	public <T> Optional<T> getOptionalUnchecked(int index) {
		return (Optional<T>) getOptional(index);
	}

	/**
	 * Returns an <code>Optional</code> holding the argument by its node name
	 *
	 * @param nodeName     The node name of this argument. This was set when initializing an argument
	 * @return An optional holding the argument with the specified node name, or an empty optional if the index was invalid
	 */
	public <T> Optional<T> getOptionalUnchecked(String nodeName) {
		return (Optional<T>) getOptional(nodeName);
	}

}
