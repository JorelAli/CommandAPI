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
	private final boolean keyListEmpty;
	private final boolean valueListEmpty;

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

		this.keyListEmpty = keyList == null;
		this.valueListEmpty = valueList == null;

		applySuggestions();
	}

	private void applySuggestions() {
		super.replaceSuggestions((info, builder) -> {
			String currentArgument = info.currentArg();

			List<String> keyValues = new ArrayList<>(keyList);
			List<String> valueValues = new ArrayList<>(valueList);

			MapArgumentSuggestionInfo suggestionInfo = getSuggestionCode(currentArgument, keyValues, valueValues);

			switch (suggestionInfo.getSuggestionCode()) {
				case KEY_SUGGESTION -> {
					builder = builder.createOffset(builder.getStart() + currentArgument.length() - suggestionInfo.getCurrentKey().length());
					for (String key : keyValues) {
						if (key.startsWith(suggestionInfo.getCurrentKey())) {
							builder.suggest(key);
						}
					}
				}
				case DELIMITER_SUGGESTION -> {
					builder = builder.createOffset(builder.getStart() + currentArgument.length());
					builder.suggest(String.valueOf(delimiter));
				}
				case QUOTATION_MARK_SUGGESTION -> {
					builder = builder.createOffset(builder.getStart() + currentArgument.length());
					builder.suggest("\"");
				}
				case VALUE_SUGGESTION -> {
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
	 * Parses the current argument and returns an enum value based on what should be suggested
	 * <ul>
	 *     <li><code>KEY_SUGGESTION</code> if a key should be suggested</li>
	 *     <li><code>DELIMITER_SUGGESTION</code> if the delimiter should be suggested</li>
	 *     <li><code>QUOTATION_MARK_SUGGESTION</code> if a quotation mark should be suggested</li>
	 *     <li><code>VALUE_SUGGESTION</code> if a value should be suggested</li>
	 * </ul>
	 *
	 * @return An enum value based on what to suggest
	 */
	@SuppressWarnings("unchecked")
	private MapArgumentSuggestionInfo getSuggestionCode(String currentArgument, List<String> keys, List<String> values) throws CommandSyntaxException {
		K mapKey = null;
		V mapValue;

		String currentKey = "";
		String currentValue = "";

		MapArgumentSuggestionInfo suggestionInfo = new MapArgumentSuggestionInfo(currentKey, currentValue, SuggestionCode.KEY_SUGGESTION);

		if (currentArgument.equals("")) {
			suggestionInfo.setSuggestionCode(SuggestionCode.KEY_SUGGESTION);
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
					suggestionInfo.setSuggestionCode(SuggestionCode.QUOTATION_MARK_SUGGESTION);
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
						suggestionInfo.setSuggestionCode(SuggestionCode.DELIMITER_SUGGESTION);
						break;
					}
					suggestionInfo.setSuggestionCode(SuggestionCode.KEY_SUGGESTION);
				}
			} else if (isAValueBeingBuilt) {
				if (isFirstValueCharacter) {
					validateValueStart(currentChar, visitedCharacters); // currentChar should be a quotation mark
					suggestionInfo.setSuggestionCode(SuggestionCode.VALUE_SUGGESTION);
					isFirstValueCharacter = false;
					continue;
				}
				if (currentChar == '\\') {
					if (rawValuesChars[currentIndex] == '\\' && rawValuesChars[currentIndex - 1] == '\\') {
						valueBuilder.append('\\');
						for (String value : values) {
							if (value.equals(valueBuilder.toString())) {
								suggestionInfo.setSuggestionCode(SuggestionCode.QUOTATION_MARK_SUGGESTION);
								break;
							}
							suggestionInfo.setSuggestionCode(SuggestionCode.VALUE_SUGGESTION);
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
								suggestionInfo.setSuggestionCode(SuggestionCode.QUOTATION_MARK_SUGGESTION);
								break;
							}
							suggestionInfo.setSuggestionCode(SuggestionCode.VALUE_SUGGESTION);
						}
						continue;
					}
					mapValue = valueMapper.apply(valueBuilder.toString());
					currentKey = "";
					suggestionInfo.setCurrentKey(currentKey);
					isFirstValueCharacter = true;

					enteredValues.add(mapKey + ":\"" + mapValue + "\"");
					keys.remove(enteredValues.get(enteredValues.size() - 1).split(":")[0]);
					if (!allowValueDuplicates) {
						values.remove(valueBuilder.toString());
					}

					valueBuilder.setLength(0);
					mapKey = null;

					isAValueBeingBuilt = false;
					suggestionInfo.setSuggestionCode(SuggestionCode.KEY_SUGGESTION);
					continue;
				}
				valueBuilder.append(currentChar);
				currentValue = valueBuilder.toString();
				suggestionInfo.setCurrentValue(currentValue);
				for (String value : values) {
					if (value.equals(valueBuilder.toString())) {
						suggestionInfo.setSuggestionCode(SuggestionCode.QUOTATION_MARK_SUGGESTION);
						break;
					}
					suggestionInfo.setSuggestionCode(SuggestionCode.VALUE_SUGGESTION);
				}
			} else {
				if (currentChar != ' ') {
					isAKeyBeingBuilt = true;
					keyBuilder.append(currentChar);
					suggestionInfo.setSuggestionCode(SuggestionCode.KEY_SUGGESTION);
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
					if (!keyList.contains(keyBuilder.toString()) && !keyListEmpty) {
						throw throwInvalidKey(visitedCharacters, keyBuilder.toString());
					}

					mapKey = (K) keyMapper.apply(keyBuilder.toString());
					if (results.containsKey(mapKey)) {
						throw duplicateKey(visitedCharacters);
					}

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
					if (!valueList.contains(valueBuilder.toString()) && !valueListEmpty) {
						throw throwInvalidValue(visitedCharacters, valueBuilder.toString());
					}
					mapValue = valueMapper.apply(valueBuilder.toString());

					if (results.containsValue(mapValue) && !allowValueDuplicates) {
						throw duplicateValue(visitedCharacters);
					}

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

	private CommandSyntaxException throwInvalidKey(StringBuilder visitedCharacters, String key) {
		String context = visitedCharacters.toString();
		StringReader reader = new StringReader(context.substring(0, context.length() - 1));
		reader.setCursor(context.length());
		return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "Invalid key: " + key);
	}

	private CommandSyntaxException throwInvalidValue(StringBuilder visitedCharacters, String value) {
		String context = visitedCharacters.toString();
		StringReader reader = new StringReader(context.substring(0, context.length() - 1));
		reader.setCursor(context.length());
		return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "Invalid value: " + value);
	}

	private CommandSyntaxException duplicateKey(StringBuilder visitedCharacters) {
		String context = visitedCharacters.toString();
		StringReader reader = new StringReader(context.substring(0, context.length() - 1));
		reader.setCursor(context.length());
		return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "Duplicate keys are not allowed");
	}

	private CommandSyntaxException duplicateValue(StringBuilder visitedCharacters) {
		String context = visitedCharacters.toString();
		StringReader reader = new StringReader(context.substring(0, context.length() - 1));
		reader.setCursor(context.length());
		return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "Duplicate values are not allowed here");
	}

	private static class MapArgumentSuggestionInfo {

		private String currentKey;
		private String currentValue;
		private SuggestionCode suggestionCode;

		MapArgumentSuggestionInfo(String currentKey, String currentValue, SuggestionCode suggestionCode) {
			this.currentKey = currentKey;
			this.currentValue = currentValue;
			this.suggestionCode = suggestionCode;
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

		public SuggestionCode getSuggestionCode() {
			return suggestionCode;
		}

		public void setSuggestionCode(SuggestionCode suggestionCode) {
			this.suggestionCode = suggestionCode;
		}
	}

	private enum SuggestionCode {
		KEY_SUGGESTION,
		DELIMITER_SUGGESTION,
		QUOTATION_MARK_SUGGESTION,
		VALUE_SUGGESTION
	}

}