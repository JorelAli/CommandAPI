package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * An argument that represents a key-value pair.
 *
 * @param <K> The type of keys this map will contain
 * @param <V> The type of values this map will contain
 * @apiNote Returns a {@link LinkedHashMap} object
 * @since 9.0.0
 */
@SuppressWarnings("rawtypes")
public class MapArgument<K, V> extends Argument<LinkedHashMap> implements GreedyArgument {

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
	MapArgument(String nodeName, char delimiter, Function<String, K> keyMapper, Function<String, V> valueMapper, List<String> keyList, List<String> valueList, boolean allowValueDuplicates) {
		super(nodeName, StringArgumentType.greedyString());

		this.delimiter = delimiter;
		this.keyMapper = keyMapper;
		this.valueMapper = valueMapper;

		this.keyList = keyList == null ? new ArrayList<>() : new ArrayList<>(keyList);
		this.valueList = valueList == null ? new ArrayList<>() : new ArrayList<>(valueList);
		this.allowValueDuplicates = allowValueDuplicates;

		applySuggestions();
	}

	private void applySuggestions() {
		super.replaceSuggestions((info, builder) -> {
			String currentArgument = info.currentArg();

			List<String> keyValues = new ArrayList<>(keyList);
			List<String> valueValues = new ArrayList<>(valueList);

			MapArgumentSuggestionInfo suggestionInfo = getSuggestionCode(currentArgument, keyValues, valueValues);

			switch (suggestionInfo.getSuggestionCode()) {
				case 0 -> {
					builder = builder.createOffset(builder.getStart() + currentArgument.length() - suggestionInfo.getCurrentKey().length());
					for (String key : keyValues) {
						if (key.startsWith(suggestionInfo.getCurrentKey())) {
							builder.suggest(key);
						}
					}
				}
				case 1 -> {
					builder = builder.createOffset(builder.getStart() + currentArgument.length());
					builder.suggest(String.valueOf(delimiter));
				}
				case 2 -> {
					builder = builder.createOffset(builder.getStart() + currentArgument.length());
					builder.suggest("\"");
				}
				case 3 -> {
					builder = builder.createOffset(builder.getStart() + currentArgument.length() - suggestionInfo.getCurrentValue().length());
					for (String value : valueValues) {
						if (value.startsWith(suggestionInfo.getCurrentValue())) {
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
	private MapArgumentSuggestionInfo getSuggestionCode(String currentArgument, List<String> keys, List<String> values) throws CommandSyntaxException {
		K mapKey = null;
		V mapValue;

		String currentKey = "";
		String currentValue = "";

		MapArgumentSuggestionInfo suggestionInfo = new MapArgumentSuggestionInfo(currentKey, currentValue, -1, 0);

		if (currentArgument.equals("")) {
			suggestionInfo.setSuggestionCode(0);
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
				suggestionInfo.setCurrentValue(currentValue);
				if (currentChar == delimiter) {
					mapKey = (K) keyMapper.apply(keyBuilder.toString());
					keyBuilder.setLength(0);
					isAKeyBeingBuilt = false;
					isAValueBeingBuilt = true;
					suggestionInfo.setSuggestionCode(2);
					continue;
				}
				if (currentChar == '"') {
					throw throwValueEarlyStart(visitedCharacters, String.valueOf(delimiter));
				}
				keyBuilder.append(currentChar);
				currentKey = keyBuilder.toString();
				suggestionInfo.setCurrentKey(currentKey);
				validateKey(visitedCharacters, keyPattern, keyBuilder);
				for (String key : keys) {
					if (key.equals(keyBuilder.toString())) {
						suggestionInfo.setSuggestionCode(1);
						break;
					}
					suggestionInfo.setSuggestionCode(0);
				}
			} else if (isAValueBeingBuilt) {
				if (isFirstValueCharacter) {
					validateValueStart(currentChar, visitedCharacters); // currentChar should be a quotation mark
					suggestionInfo.setSuggestionCode(3);
					isFirstValueCharacter = false;
					continue;
				}
				if (currentChar == '\\') {
					if (rawValuesChars[currentIndex] == '\\' && rawValuesChars[currentIndex - 1] == '\\') {
						valueBuilder.append('\\');
						for (String value : values) {
							if (value.equals(valueBuilder.toString())) {
								suggestionInfo.setSuggestionCode(2);
								break;
							}
							suggestionInfo.setSuggestionCode(3);
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
								suggestionInfo.setSuggestionCode(2);
								break;
							}
							suggestionInfo.setSuggestionCode(3);
						}
						continue;
					}
					mapValue = valueMapper.apply(valueBuilder.toString());
					currentKey = "";
					suggestionInfo.setCurrentKey(currentKey);
					valueBuilder.setLength(0);
					isFirstValueCharacter = true;
					enteredValues.add(mapKey + ":\"" + mapValue + "\"");
					keys.remove(enteredValues.get(enteredValues.size() - 1).split(":")[0]);
					if (!allowValueDuplicates) {
						values.remove(enteredValues.get(enteredValues.size() - 1).split(":")[0].replace("\"", ""));
					}
					mapKey = null;

					isAValueBeingBuilt = false;
					suggestionInfo.setSuggestionCode(0);
					continue;
				}
				valueBuilder.append(currentChar);
				currentValue = valueBuilder.toString();
				suggestionInfo.setCurrentValue(currentValue);
				for (String value : values) {
					if (value.equals(valueBuilder.toString())) {
						suggestionInfo.setSuggestionCode(2);
						break;
					}
					suggestionInfo.setSuggestionCode(3);
				}
			} else {
				if (currentChar != ' ') {
					isAKeyBeingBuilt = true;
					keyBuilder.append(currentChar);
					suggestionInfo.setSuggestionCode(0);
					suggestionInfo.setCurrentKey(keyBuilder.toString());
				}
			}
		}
		return suggestionInfo;
	}

	@Override
	public Class<LinkedHashMap> getPrimitiveType() {
		return LinkedHashMap.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.MAP;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <Source> LinkedHashMap<K, V> parseArgument(CommandContext<Source> cmdCtx, String key, Object[] previousArgs) throws CommandSyntaxException {
		String rawValues = cmdCtx.getArgument(key, String.class);
		LinkedHashMap<K, V> results = new LinkedHashMap<>();

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

	private static class MapArgumentSuggestionInfo {

		private String currentKey;
		private String currentValue;
		private int suggestionCode;
		private int builderStart;

		MapArgumentSuggestionInfo(String currentKey, String currentValue, int suggestionCode, int builderStart) {
			this.currentKey = currentKey;
			this.currentValue = currentValue;
			this.suggestionCode = suggestionCode;
			this.builderStart = builderStart;
		}

		public String getCurrentKey() {
			return currentKey;
		}

		public void setCurrentKey(String currentKey) {
			this.currentKey = currentKey;
		}

		public String getCurrentValue() {
			return currentValue;
		}

		public void setCurrentValue(String currentValue) {
			this.currentValue = currentValue;
		}

		public int getSuggestionCode() {
			return suggestionCode;
		}

		public void setSuggestionCode(int suggestionCode) {
			this.suggestionCode = suggestionCode;
		}

		public int getBuilderStart() {
			return builderStart;
		}

		public void setBuilderStart(int builderStart) {
			this.builderStart = builderStart;
		}
	}

}
