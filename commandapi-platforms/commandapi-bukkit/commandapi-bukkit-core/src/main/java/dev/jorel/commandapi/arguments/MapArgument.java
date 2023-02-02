package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.jorel.commandapi.wrappers.MapArgumentKeyType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * An argument that represents a key-value pair.
 *
 * @param <K> The type of keys this map will contain
 * @param <V> The type of values this map will contain
 * @apiNote Returns a {@link HashMap} object
 * @since 9.0.0
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

	private final List<String> keyList;
	private final List<String> valueList;
	private final boolean allowValueDuplicates;

	private final List<String> enteredValues = new ArrayList<>();

	private final Pattern keyPattern = Pattern.compile("([a-zA-Z0-9\\.]+)");

	/**
	 * Constructs a {@link MapArgument}
	 *
	 * @param nodeName  the name to assign to this argument node
	 * @param delimiter This is used to separate key-value pairs
	 */
	MapArgument(String nodeName, char delimiter, MapArgumentKeyType keyType, Function<String, V> valueMapper, List<String> keyList, List<String> valueList, boolean allowValueDuplicates) {
		super(nodeName, StringArgumentType.greedyString());

		this.delimiter = delimiter;
		this.valueMapper = valueMapper;

		this.keyList = new ArrayList<>(keyList);
		this.valueList = new ArrayList<>(valueList);
		this.allowValueDuplicates = allowValueDuplicates;

		this.keyMapper = switch (keyType) {
			case STRING -> (s -> s);
			case INT -> (Integer::valueOf);
			case FLOAT -> (Float::valueOf);
		};

		applySuggestions();
	}

	private String currentKey = "";
	private String currentValue = "";

	private void applySuggestions() {
		super.replaceSuggestions((info, builder) -> {
			String currentArgument = info.currentArg();

			builder = builder.createOffset(builder.getStart() + currentArgument.length());

			List<String> keyValues = new ArrayList<>(keyList);
			List<String> valueValues = new ArrayList<>(valueList);

			switch (getSuggestionCode(currentArgument, keyValues, valueValues)) {
				case 0 -> {
					for (String key : keyValues) {
						if (key.startsWith(currentKey)) {
							builder.suggest(key);
						}
					}
				}
				case 1 -> builder.suggest(String.valueOf(delimiter));
				case 2 -> builder.suggest("\"");
				case 3 -> {
					for (String value : valueValues) {
						if (value.startsWith(currentValue)) {
							builder.suggest(value);
						}
					}
				}
			}

			return builder.buildFuture();
		});
	}

	/**
	 * Parses the current argument and returns a value based on what should be suggested
	 * <ul>
	 *     <li><code>0</code> if a key should be suggested</li>
	 *     <li><code>1</code> if the delimiter should be suggested</li>
	 *     <li><code>2</code> if a quotation mark should be suggested</li>
	 *     <li><code>3</code> if a value should be suggested</li>
	 * </ul>
	 *
	 * @return An integer based on what to suggest
	 */
	@SuppressWarnings("unchecked")
	private int getSuggestionCode(String currentArgument, List<String> keys, List<String> values) throws CommandSyntaxException {
		K mapKey = null;
		V mapValue;

		int returnCode = -1;

		if (currentArgument.equals("")) {
			returnCode = 0;
		}

		boolean isAKeyBeingBuilt = true;
		boolean isAValueBeingBuilt = false;
		boolean isFirstValueCharacter = true;

		StringBuilder keyBuilder = new StringBuilder();
		StringBuilder valueBuilder = new StringBuilder();
		StringBuilder visitedCharacters = new StringBuilder();

		char[] rawValuesChars = currentArgument.toCharArray();
		int currentIndex = -1;
		for (char currentChar : rawValuesChars) {
			currentIndex++;
			visitedCharacters.append(currentChar);
			if (isAKeyBeingBuilt) {
				currentValue = "";
				if (currentChar == delimiter) {
					mapKey = (K) keyMapper.apply(keyBuilder.toString());
					keyBuilder.setLength(0);
					isAKeyBeingBuilt = false;
					isAValueBeingBuilt = true;
					returnCode = 2;
					continue;
				}
				if (currentChar == '"') {
					throw throwValueEarlyStart(visitedCharacters, String.valueOf(delimiter));
				}
				keyBuilder.append(currentChar);
				currentKey = keyBuilder.toString();
				validateKey(visitedCharacters, keyPattern, keyBuilder);
				for (String key : keys) {
					if (key.equals(keyBuilder.toString())) {
						returnCode = 1;
						break;
					}
					returnCode = 0;
				}
			} else if (isAValueBeingBuilt) {
				if (isFirstValueCharacter) {
					validateValueStart(currentChar, visitedCharacters); // currentChar should be a quotation mark
					returnCode = 3;
					isFirstValueCharacter = false;
					continue;
				}
				if (currentChar == '\\') {
					if (rawValuesChars[currentIndex] == '\\' && rawValuesChars[currentIndex - 1] == '\\') {
						valueBuilder.append('\\');
						for (String value : values) {
							if (value.equals(valueBuilder.toString())) {
								returnCode = 2;
								break;
							}
							returnCode = 3;
						}
						continue;
					}
					continue;
				}
				if (currentChar == '"') {
					if (rawValuesChars[currentIndex - 1] == '\\' && rawValuesChars[currentIndex - 2] != '\\') {
						valueBuilder.append('"');
						for (String value : values) {
							if (value.equals(valueBuilder.toString())) {
								returnCode = 2;
								break;
							}
							returnCode = 3;
						}
						continue;
					}
					mapValue = valueMapper.apply(valueBuilder.toString());
					currentKey = "";
					valueBuilder.setLength(0);
					isFirstValueCharacter = true;
					enteredValues.add(mapKey + ":\"" + mapValue + "\"");
					keys.remove(enteredValues.get(enteredValues.size() - 1).split(":")[0]);
					if (!allowValueDuplicates) {
						values.remove(enteredValues.get(enteredValues.size() - 1).split(":")[0].replace("\"", ""));
					}
					mapKey = null;

					isAValueBeingBuilt = false;
					returnCode = 0;
					continue;
				}
				valueBuilder.append(currentChar);
				currentValue = valueBuilder.toString();
				for (String value : values) {
					if (value.equals(valueBuilder.toString())) {
						returnCode = 2;
						break;
					}
					returnCode = 3;
				}
			} else {
				if (currentChar != ' ') {
					isAKeyBeingBuilt = true;
					keyBuilder.append(currentChar);
				}
			}
		}
		return returnCode;
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
			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "A value must start with a quotation mark");
		}
	}

	private void validateValueInput(StringBuilder valueBuilder, StringBuilder visitedCharacters) throws CommandSyntaxException {
		if (valueBuilder.length() != 0) {
			StringReader reader = new StringReader(visitedCharacters.toString());
			reader.setCursor(visitedCharacters.toString().length());
			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "A value must end with a quotation mark");
		}
	}

	private CommandSyntaxException throwInvalidKeyCharacter(StringBuilder visitedCharacters) {
		String context = visitedCharacters.toString();
		StringReader reader = new StringReader(context);
		reader.setCursor(context.length());
		return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "A key must only contain letters from a-z and A-Z, numbers and periods");
	}

	private CommandSyntaxException throwValueEarlyStart(StringBuilder visitedCharacters, String delimiter) {
		String context = visitedCharacters.toString();
		StringReader reader = new StringReader(context);
		reader.setCursor(context.length());
		return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "You must separate a key/value pair with a '" + delimiter + "'");
	}

}
