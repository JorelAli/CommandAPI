package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.arguments.AbstractArgument;

import javax.annotation.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * This class stores the arguments for this command
 *
 * @param args      The arguments for this command
 * @param argsMap   The arguments for this command mapped to their node names. This is an ordered map
 * @param rawArgs   The raw arguments for this command.
 * @param rawArgsMap   The raw arguments for this command mapped to their node names. This is an ordered map
 * @param fullInput The command string a player has entered (including the /)
 */
@SuppressWarnings("unchecked")
public record CommandArguments(

	/**
	 * @param The arguments for this command
	 */
	Object[] args,

	/**
	 *  @param The arguments for this command mapped to their node names. This is an ordered map
	 */
	Map<String, Object> argsMap,

	/**
	 * @param The raw arguments for this command
	 */
	String[] rawArgs,

	/**
	 * @param The raw arguments for this command mapped to their node names. This is an ordered map
	 */
	Map<String, String> rawArgsMap,

	/**
	 * @param The command string a player has entered (including the /)
	 */
	String fullInput
) {

	private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = new HashMap<>() {{
		put(boolean.class, Boolean.class);
		put(char.class, Character.class);
		put(byte.class, Byte.class);
		put(short.class, Short.class);
		put(int.class, Integer.class);
		put(long.class, Long.class);
		put(float.class, Float.class);
		put(double.class, Double.class);
	}};

	// Access the inner structure directly

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
	 * @deprecated This method has been deprecated in favour of {@link CommandArguments#fullInput()}
	 * @return The raw command string a player has entered
	 */
	@Deprecated(since = "9.1.0", forRemoval = true)
	public String getFullInput() {
		return fullInput;
	}

	/**
	 * @return The number of arguments for this command
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
	 * @return An argument which has the given node name. Can be {@code null} if <code>nodeName</code> was not found.
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
	 * <p>
	 * A raw argument is the {@link String} form of an argument as written in a command. For example take this command:
	 * <p>
	 * {@code /mycommand @e 15.3}
	 * <p>
	 * When using this method to access these arguments, {@code @e} and {@code 15.3} will be available as {@link String}s and not as a {@link Collection} and {@link Double}
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
	 * <p>
	 * A raw argument is the {@link String} form of an argument as written in a command. For example take this command:
	 * <p>
	 * {@code /mycommand @e 15.3}
	 * <p>
	 * When using this method to access these arguments, {@code @e} and {@code 15.3} will be available as {@link String}s and not as a {@link Collection} and {@link Double}
	 *
	 * @param nodeName The node name of this argument. This was set when initializing an argument
	 * @return A raw argument which has the given node name. Can be {@code null} if <code>nodeName</code> was not found.
	 */
	@Nullable
	public String getRaw(String nodeName) {
		return rawArgsMap.get(nodeName);
	}

	/**
	 * Returns a raw argument by its index
	 * <p>
	 * A raw argument is the {@link String} form of an argument as written in a command. For example take this command:
	 * <p>
	 * {@code /mycommand @e 15.3}
	 * <p>
	 * When using this method to access these arguments, {@code @e} and {@code 15.3} will be available as {@link String}s and not as a {@link Collection} and {@link Double}
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
	 * <p>
	 * A raw argument is the {@link String} form of an argument as written in a command. For example take this command:
	 * <p>
	 * {@code /mycommand @e 15.3}
	 * <p>
	 * When using this method to access these arguments, {@code @e} and {@code 15.3} will be available as {@link String}s and not as a {@link Collection} and {@link Double}
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
	 * <p>
	 * A raw argument is the {@link String} form of an argument as written in a command. For example take this command:
	 * <p>
	 * {@code /mycommand @e 15.3}
	 * <p>
	 * When using this method to access these arguments, {@code @e} and {@code 15.3} will be available as {@link String}s and not as a {@link Collection} and {@link Double}
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
	 * <p>
	 * A raw argument is the {@link String} form of an argument as written in a command. For example take this command:
	 * <p>
	 * {@code /mycommand @e 15.3}
	 * <p>
	 * When using this method to access these arguments, {@code @e} and {@code 15.3} will be available as {@link String}s and not as a {@link Collection} and {@link Double}
	 *
	 * @param nodeName     The node name of this raw argument. This was set when initializing an argument
	 * @param defaultValue The String returned if the raw argument was not found.
	 * @return A raw argument with the specified node name or the provided default value
	 */
	public String getOrDefaultRaw(String nodeName, Supplier<String> defaultValue) {
		return rawArgsMap.getOrDefault(nodeName, defaultValue.get());
	}

	/**
	 * Returns an {@link Optional} holding the raw argument by its index
	 * <p>
	 * A raw argument is the {@link String} form of an argument as written in a command. For example take this command:
	 * <p>
	 * {@code /mycommand @e 15.3}
	 * <p>
	 * When using this method to access these arguments, {@code @e} and {@code 15.3} will be available as {@link String}s and not as a {@link Collection} and {@link Double}
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
	 * Returns an  {@link Optional} holding the raw argument by its node name
	 * <p>
	 * A raw argument is the {@link String} form of an argument as written in a command. For example take this command:
	 * <p>
	 * {@code /mycommand @e 15.3}
	 * <p>
	 * When using this method to access these arguments, {@code @e} and {@code 15.3} will be available as {@link String}s and not as a {@link Collection} and {@link Double}
	 *
	 * @param nodeName     The node name of this argument. This was set when initializing an argument
	 * @return An optional holding the argument with the specified node name or an empty optional if the node name was not found
	 */
	public Optional<String> getRawOptional(String nodeName) {
		if (!rawArgsMap.containsKey(nodeName)) {
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

	/**
	 * Returns an argument purely based on its CommandAPI representation. This also attempts to directly cast the argument to the type represented by {@link dev.jorel.commandapi.arguments.AbstractArgument#getPrimitiveType()}
	 *
	 * @param argumentType The argument instance used to create the argument
	 * @return The argument represented by the CommandAPI argument, or null if the argument's class cannot be cast to the type represented by {@link dev.jorel.commandapi.arguments.AbstractArgument#getPrimitiveType()}
	 */
	@Nullable
	public <T> T getByArgument(AbstractArgument<T, ?, ?, ?> argumentType) {
		Object argument = get(argumentType.getNodeName());
		return castArgument(argument, argumentType.getPrimitiveType());
	}

	/**
	 * Returns an argument based on its node name. This also attempts to directly cast the argument to the type represented by the {@code argumentType} parameter.
	 *
	 * @param nodeName The node name of the argument
	 * @param argumentType The class that represents the argument
	 * @return The argument with the given node name, or null if the argument's class cannot be cast to the type represented by the given {@code argumentType} argument
	 */
	@Nullable
	public <T> T getByClass(String nodeName, Class<T> argumentType) {
		Object argument = get(nodeName);
		return castArgument(argument, argumentType);
	}

	/**
	 * Returns an argument based on its index. This also attempts to directly cast the argument to the type represented by the {@code argumentType} parameter.
	 *
	 * @param index The position of the argument
	 * @param argumentType The class that represents the argument
	 * @return The argument at the given index, or null if the argument's class cannot be cast to the type represented by the given {@code argumentType} argument
	 */
	@Nullable
	public <T> T getByClass(int index, Class<T> argumentType) {
		Object argument = get(index);
		return castArgument(argument, argumentType);
	}

	private <T> T castArgument(Object argument, Class<T> argumentType) {
		if (argument == null) {
			return null;
		}
		if (!argument.getClass().equals(PRIMITIVE_TO_WRAPPER.getOrDefault(argumentType, argumentType))) {
			return null;
		}
		if (!PRIMITIVE_TO_WRAPPER.getOrDefault(argumentType, argumentType).isAssignableFrom(argument.getClass())) {
			return null;
		}
		return (T) argument;
	}

}
