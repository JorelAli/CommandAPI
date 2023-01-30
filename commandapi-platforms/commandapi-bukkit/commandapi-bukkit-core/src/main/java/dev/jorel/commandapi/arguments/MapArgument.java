package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.wrappers.MapArgumentKeyType;

import java.util.HashMap;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * An argument that represents a key-value pair.
 *
 * @since 9.0.0
 *
 * @apiNote Returns a {@link HashMap} object
 */
@SuppressWarnings("rawtypes")
// TODO: Enable commandapi-bukkit-kotlin-test again
//  it is disabled because it takes ages to compile on my computer
//  and since I develop the MapArgument I thought it might be good
//  to disable that module
public class MapArgument<K, V> extends Argument<HashMap> implements GreedyArgument {

	private final char delimiter;
	private final Function<String, ?> keyMapper;
	private final Function<String, V> valueMapper;

	/**
	 * Constructs a {@link MapArgument}
	 *
	 * @param nodeName the name to assign to this argument node
	 * @param delimiter This is used to separate key-value pairs
	 */
	MapArgument(String nodeName, char delimiter, MapArgumentKeyType keyType, Function<String, V> valueMapper) {
		super(nodeName, StringArgumentType.greedyString());

		this.delimiter = delimiter;
		this.valueMapper = valueMapper;

		this.keyMapper = switch (keyType) {
			case STRING -> (s -> s);
			case INT -> (Integer::valueOf);
			case FLOAT -> (Float::valueOf);
		};
	}

	@Override
	public Class<HashMap> getPrimitiveType() {
		return HashMap.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.MAP;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <Source> HashMap<K, V> parseArgument(CommandContext<Source> cmdCtx, String key, Object[] previousArgs) throws CommandSyntaxException {
		String rawValues = cmdCtx.getArgument(key, String.class);
		HashMap<K, V> results = new HashMap<>();

		K mapKey = null;
		V mapValue;

		boolean isAKeyBeingBuilt = true;
		boolean isAValueBeingBuilt = false;
		boolean isFirstValueCharacter = true;

		Pattern keyPattern = Pattern.compile("([a-zA-Z0-9\\.]+)");

		StringBuilder keyBuilder = new StringBuilder();
		StringBuilder valueBuilder = new StringBuilder();
		StringBuilder visitedCharacters = new StringBuilder();

		char[] rawValuesChars = rawValues.toCharArray();
		int currentIndex = -1;
		for (char currentChar : rawValuesChars) {
			currentIndex++;
			visitedCharacters.append(currentChar);
			if (isAKeyBeingBuilt) {
				if (currentChar == delimiter) {
					mapKey = (K) keyMapper.apply(keyBuilder.toString());

					// No need to check the key here because we already know it only consists of letters

					keyBuilder.setLength(0);
					isAKeyBeingBuilt = false;
					isAValueBeingBuilt = true;
					continue;
				}
				if (currentChar == '"') {
					throw throwValueEarlyStart(visitedCharacters, String.valueOf(delimiter));
				}
				keyBuilder.append(currentChar);
				validateKey(visitedCharacters, keyPattern, keyBuilder);
			} else if (isAValueBeingBuilt) {
				if (isFirstValueCharacter) {
					validateValueStart(currentChar, visitedCharacters);
					isFirstValueCharacter = false;
					continue;
				}
				if (currentChar == '\\') {
					if (rawValuesChars[currentIndex] == '\\' && rawValuesChars[currentIndex - 1] == '\\') {
						valueBuilder.append('\\');
						continue;
					}
					continue;
				}
				if (currentChar == '"') {
					if (rawValuesChars[currentIndex - 1] == '\\' && rawValuesChars[currentIndex - 2] != '\\') {
						valueBuilder.append('"');
						continue;
					}
					mapValue = valueMapper.apply(valueBuilder.toString());
					valueBuilder.setLength(0);
					isFirstValueCharacter = true;
					results.put(mapKey, mapValue);
					mapKey = null;

					isAValueBeingBuilt = false;
					continue;
				}
				valueBuilder.append(currentChar);
			} else {
				if (currentChar != ' ') {
					isAKeyBeingBuilt = true;
					keyBuilder.append(currentChar);
				}
			}
		}
		validateValueInput(valueBuilder, visitedCharacters);
		return results;
	}

	private void validateKey(StringBuilder visitedCharacters, Pattern keyPattern, StringBuilder keyBuilder) throws CommandSyntaxException {
		if (!keyPattern.matcher(keyBuilder.toString()).matches()) {
			throw throwInvalidKeyCharacter(visitedCharacters);
		}
	}

	private void validateValueStart(char currentChar, StringBuilder visitedCharacters) throws CommandSyntaxException {
		if (currentChar != '"') {
			String context = visitedCharacters.toString();
			StringReader reader = new StringReader(context.substring(0, context.length() - 1));
			reader.setCursor(context.substring(0, context.length() - 1).length());
			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "A value has to start with a quotation mark!");
		}
	}

	private void validateValueInput(StringBuilder valueBuilder, StringBuilder visitedCharacters) throws CommandSyntaxException {
		if (valueBuilder.length() != 0) {
			StringReader reader = new StringReader(visitedCharacters.toString());
			reader.setCursor(visitedCharacters.toString().length());
			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "A value has to end with a quotation mark!");
		}
	}

	private CommandSyntaxException throwInvalidKeyCharacter(StringBuilder visitedCharacters) {
		String context = visitedCharacters.toString();
		StringReader reader = new StringReader(context);
		reader.setCursor(context.length());
		return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "A key must only contain letters from a-z and A-Z, numbers and periods!");
	}

	private CommandSyntaxException throwValueEarlyStart(StringBuilder visitedCharacters, String delimiter) {
		String context = visitedCharacters.toString();
		StringReader reader = new StringReader(context);
		reader.setCursor(context.length());
		return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "You have to separate a key/value pair with a '" + delimiter + "'!");
	}

}
