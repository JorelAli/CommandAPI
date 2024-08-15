package dev.jorel.commandapi.executors;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import dev.jorel.commandapi.arguments.AbstractArgument;

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

	private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = Map.of(
		boolean.class, Boolean.class,
		char.class, Character.class,
		byte.class, Byte.class,
		short.class, Short.class,
		int.class, Integer.class,
		long.class, Long.class,
		float.class, Float.class,
		double.class, Double.class
	);

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

	/*****************************************
	 ********** SAFE-CAST ARGUMENTS **********
	 *****************************************/

	/**
	 * Returns an argument purely based on its CommandAPI representation. This also attempts to directly cast the argument to the type represented by {@link dev.jorel.commandapi.arguments.AbstractArgument#getPrimitiveType()}
	 *
	 * @param argumentType The argument instance used to create the argument
	 * @return The argument represented by the CommandAPI argument, or null if the argument was not found.
	 */
	@Nullable
	public <T> T getByArgument(AbstractArgument<T, ?, ?, ?> argumentType) {
		return castArgument(get(argumentType.getNodeName()), argumentType.getPrimitiveType(), argumentType.getNodeName());
	}

	/**
	 * Returns an argument purely based on its CommandAPI representation or a default value if the argument wasn't found.
	 * <p>
	 * If the argument was found, this also attempts to directly cast the argument to the type represented by {@link dev.jorel.commandapi.arguments.AbstractArgument#getPrimitiveType()}
	 *
	 * @param argumentType The argument instance used to create the argument
	 * @param defaultValue The default value to return if the argument wasn't found
	 * @return The argument represented by the CommandAPI argument, or the default value if the argument was not found.
	 */
	public <T> T getByArgumentOrDefault(AbstractArgument<T, ?, ?, ?> argumentType, T defaultValue) {
		T argument = getByArgument(argumentType);
		return (argument != null) ? argument : defaultValue;
	}

	/**
	 * Returns an <code>Optional</code> holding the provided argument. This <code>Optional</code> can be empty if the argument was not given when running the command.
	 * <p>
	 * This attempts to directly cast the argument to the type represented by {@link dev.jorel.commandapi.arguments.AbstractArgument#getPrimitiveType()}
	 *
	 * @param argumentType The argument instance used to create the argument
	 * @return An <code>Optional</code> holding the argument, or an empty <code>Optional</code> if the argument was not found.
	 */
	public <T> Optional<T> getOptionalByArgument(AbstractArgument<T, ?, ?, ?> argumentType) {
		return Optional.ofNullable(getByArgument(argumentType));
	}

	/**
	 * Returns an argument based on its node name. This also attempts to directly cast the argument to the type represented by the {@code argumentType} parameter.
	 *
	 * @param nodeName The node name of the argument
	 * @param argumentType The class that represents the argument
	 * @return The argument with the given node name, or null if the argument was not found.
	 */
	@Nullable
	public <T> T getByClass(String nodeName, Class<T> argumentType) {
		return castArgument(get(nodeName), argumentType, nodeName);
	}

	/**
	 * Returns an argument based on its node name or a default value if the argument wasn't found.
	 * <p>
	 * If the argument was found, this method attempts to directly cast the argument to the type represented by the {@code argumentType} parameter.
	 *
	 * @param nodeName The node name of the argument
	 * @param argumentType The class that represents the argument
	 * @param defaultValue The default value to return if the argument wasn't found
	 * @return The argument with the given node name, or the default value if the argument was not found.
	 */
	public <T> T getByClassOrDefault(String nodeName, Class<T> argumentType, T defaultValue) {
		T argument = getByClass(nodeName, argumentType);
		return (argument != null) ? argument : defaultValue;
	}

	/**
	 * Returns an <code>Optional</code> holding the argument with the given node name. This <code>Optional</code> can be empty if the argument was not given when running the command.
	 * <p>
	 * This attempts to directly cast the argument to the type represented by the {@code argumentType} parameter.
	 *
	 * @param nodeName The node name of the argument
	 * @param argumentType The class that represents the argument
	 * @return An <code>Optional</code> holding the argument, or an empty <code>Optional</code> if the argument was not found.
	 */
	public <T> Optional<T> getOptionalByClass(String nodeName, Class<T> argumentType) {
		return Optional.ofNullable(getByClass(nodeName, argumentType));
	}

	/**
	 * Returns an argument based on its index. This also attempts to directly cast the argument to the type represented by the {@code argumentType} parameter.
	 *
	 * @param index The index of the argument
	 * @param argumentType The class that represents the argument
	 * @return The argument at the given index, or null if the argument was not found.
	 */
	@Nullable
	public <T> T getByClass(int index, Class<T> argumentType) {
		return castArgument(get(index), argumentType, index);
	}

	/**
	 * Returns an argument based on its index or a default value if the argument wasn't found.
	 * <p>
	 * If the argument was found, this method attempts to directly cast the argument to the type represented by the {@code argumentType} parameter.
	 *
	 * @param index The index of the argument
	 * @param argumentType The class that represents the argument
	 * @param defaultValue The default value to return if the argument wasn't found
	 * @return The argument at the given index, or the default value if the argument was not found.
	 */
	public <T> T getByClassOrDefault(int index, Class<T> argumentType, T defaultValue) {
		T argument = getByClass(index, argumentType);
		return (argument != null) ? argument : defaultValue;
	}

	/**
	 * Returns an <code>Optional</code> holding the argument at the given index. This <code>Optional</code> can be empty if the argument was not given when running the command.
	 * <p>
	 * This attempts to directly cast the argument to the type represented by the {@code argumentType} parameter.
	 *
	 * @param index The index of the argument
	 * @param argumentType The class that represents the argument
	 * @return An <code>Optional</code> holding the argument, or an empty <code>Optional</code> if the argument was not found.
	 */
	public <T> Optional<T> getOptionalByClass(int index, Class<T> argumentType) {
		return Optional.ofNullable(getByClass(index, argumentType));
	}

	private <T> T castArgument(Object argument, Class<T> argumentType, Object argumentNameOrIndex) {
		if (argument == null) {
			return null;
		}
		if (!PRIMITIVE_TO_WRAPPER.getOrDefault(argumentType, argumentType).isAssignableFrom(argument.getClass())) {
			throw new IllegalArgumentException(buildExceptionMessage(argumentNameOrIndex, argument.getClass().getSimpleName(), argumentType.getSimpleName()));
		}
		return (T) argument;
	}

	private String buildExceptionMessage(Object argumentNameOrIndex, String expectedClass, String actualClass) {
		if (argumentNameOrIndex instanceof Integer i) {
			return "Argument at index '" + i + "' is defined as " + expectedClass + ", not " + actualClass;
		}
		if (argumentNameOrIndex instanceof String s) {
			return "Argument '" + s + "' is defined as " + expectedClass + ", not " + actualClass;
		}
		throw new IllegalStateException("Unexpected behaviour detected while building exception message!" +
			"This should never happen - if you're seeing this message, please" +
			"contact the developers of the CommandAPI, we'd love to know how you managed to get this error!");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(args);
		result = prime * result + Arrays.hashCode(rawArgs);
		result = prime * result + Objects.hash(argsMap, fullInput, rawArgsMap);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommandArguments other = (CommandArguments) obj;
		return Arrays.deepEquals(args, other.args) && Objects.equals(argsMap, other.argsMap)
				&& Objects.equals(fullInput, other.fullInput) && Arrays.equals(rawArgs, other.rawArgs)
				&& Objects.equals(rawArgsMap, other.rawArgsMap);
	}

}
