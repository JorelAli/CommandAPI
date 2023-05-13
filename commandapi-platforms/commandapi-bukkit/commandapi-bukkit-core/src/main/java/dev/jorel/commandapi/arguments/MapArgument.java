package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.executors.CommandArguments;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;

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
	private final String separator;
	private final Function<String, K> keyMapper;
	private final Function<String, V> valueMapper;

	private final List<String> keyList;
	private final List<String> valueList;
	private final boolean forceQuoteKeys;
	private final boolean allowValueDuplicates;
	private final boolean keyListEmpty;
	private final boolean valueListEmpty;

	/**
	 * Constructs a {@link MapArgument}
	 *
	 * @param nodeName  the name to assign to this argument node
	 * @param delimiter This is used to separate key-value pairs
	 */
	MapArgument(String nodeName, char delimiter, String separator, Function<String, K> keyMapper, Function<String, V> valueMapper, List<String> keyList, List<String> valueList, boolean forceQuoteKeys, boolean allowValueDuplicates) {
		super(nodeName, StringArgumentType.greedyString());

		this.delimiter = delimiter;
		this.separator = separator;
		this.keyMapper = keyMapper;
		this.valueMapper = valueMapper;

		this.keyList = keyList == null ? new ArrayList<>() : new ArrayList<>(keyList);
		this.valueList = valueList == null ? new ArrayList<>() : new ArrayList<>(valueList);
		this.forceQuoteKeys = forceQuoteKeys;
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
				case SEPARATOR_SUGGESTION -> {
					builder = builder.createOffset(builder.getStart() + currentArgument.length() - suggestionInfo.getCurrentSeparator().length());
					builder.suggest(separator);
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
	private MapArgumentSuggestionInfo getSuggestionCode(String currentArgument, List<String> keys, List<String> values) throws CommandSyntaxException {
		String currentKey = "";
		String currentValue = "";
		String currentSeparator = "";

		MapArgumentSuggestionInfo suggestionInfo = new MapArgumentSuggestionInfo(currentKey, currentValue, currentSeparator, SuggestionCode.KEY_SUGGESTION);

		if (currentArgument.equals("")) {
			suggestionInfo.setSuggestionCode(SuggestionCode.KEY_SUGGESTION);
		}

		boolean isAKeyBeingBuilt = true;
		boolean isAValueBeingBuilt = false;

		boolean isFirstKeyCharacter = true;
		boolean isFirstValueCharacter = true;

		boolean isCurrentKeyQuoted = forceQuoteKeys;

		StringBuilder keyBuilder = new StringBuilder();
		StringBuilder valueBuilder = new StringBuilder();
		StringBuilder separatorBuilder = new StringBuilder();
		StringBuilder visitedCharacters = new StringBuilder();

		char[] rawValuesChars = currentArgument.toCharArray();
		int currentIndex = -1;
		for (char currentChar : rawValuesChars) {
			currentIndex++;
			visitedCharacters.append(currentChar);
			if (isAKeyBeingBuilt) {
				currentValue = "";
				suggestionInfo.setCurrentValue(currentValue);
				if (isFirstKeyCharacter) {
					isFirstKeyCharacter = false;
					isCurrentKeyQuoted = forceQuoteKeys || currentChar == '"';
					suggestionInfo.setSuggestionCode(SuggestionCode.KEY_SUGGESTION);
					continue;
				}
				if (currentChar == delimiter) {
					isAKeyBeingBuilt = false;
					isAValueBeingBuilt = true;
					suggestionInfo.setSuggestionCode(SuggestionCode.QUOTATION_MARK_SUGGESTION);
					continue;
				}
				if (currentChar == '\\') {
					if (rawValuesChars[currentIndex - 1] == '\\') {
						keyBuilder.append("\\");
						for (String key : keys) {
							if (key.equals(keyBuilder.toString())) {
								suggestionInfo.setSuggestionCode((isCurrentKeyQuoted) ? SuggestionCode.QUOTATION_MARK_SUGGESTION : SuggestionCode.DELIMITER_SUGGESTION);
								break;
							}
							suggestionInfo.setSuggestionCode(SuggestionCode.KEY_SUGGESTION);
						}
					}
					continue;
				}
				if (currentChar == '"') {
					if (rawValuesChars[currentIndex - 1] == '\\' && rawValuesChars[currentIndex - 2] != '\\') {
						keyBuilder.append('"');
						for (String key : keys) {
							if (key.equals(keyBuilder.toString())) {
								suggestionInfo.setSuggestionCode((isCurrentKeyQuoted) ? SuggestionCode.QUOTATION_MARK_SUGGESTION : SuggestionCode.DELIMITER_SUGGESTION);
								break;
							}
							suggestionInfo.setSuggestionCode(SuggestionCode.KEY_SUGGESTION);
						}
					}
					continue;
				}
				keyBuilder.append(currentChar);
				currentKey = keyBuilder.toString();
				suggestionInfo.setCurrentKey(currentKey);
				for (String key : keys) {
					if (key.equals(keyBuilder.toString())) {
						suggestionInfo.setSuggestionCode(SuggestionCode.DELIMITER_SUGGESTION);
						break;
					}
					suggestionInfo.setSuggestionCode(SuggestionCode.KEY_SUGGESTION);
				}
			}
			if (isAValueBeingBuilt) {
				if (isFirstValueCharacter) {
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
					currentSeparator = "";
					suggestionInfo.setCurrentSeparator(currentSeparator);
					isFirstValueCharacter = true;

					keys.remove(keyBuilder.toString());
					if (!allowValueDuplicates) {
						values.remove(valueBuilder.toString());
					}

					keyBuilder.setLength(0);
					valueBuilder.setLength(0);

					isAValueBeingBuilt = false;
					suggestionInfo.setSuggestionCode(SuggestionCode.SEPARATOR_SUGGESTION);
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
			}
			if (!isAKeyBeingBuilt && !isAValueBeingBuilt) {
				// Appends every character after the closing quotation mark to build the separator
				separatorBuilder.append(currentChar);
				suggestionInfo.setSuggestionCode(SuggestionCode.SEPARATOR_SUGGESTION);
				suggestionInfo.setCurrentSeparator(separatorBuilder.toString());
				if (separatorBuilder.toString().equals(separator)) {
					separatorBuilder.setLength(0);
					isAKeyBeingBuilt = true;
					suggestionInfo.setSuggestionCode((forceQuoteKeys) ? SuggestionCode.QUOTATION_MARK_SUGGESTION : SuggestionCode.KEY_SUGGESTION);
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

	@Override
	public <Source> LinkedHashMap<K, V> parseArgument(CommandContext<Source> cmdCtx, String key, CommandArguments previousArgs) throws CommandSyntaxException {
		StringReader reader = new StringReader(cmdCtx.getArgument(key, String.class));
		LinkedHashMap<K, V> results = new LinkedHashMap<>();

		while (reader.canRead()) {
			K mapKey = parseKey(reader);
			if (results.containsKey(mapKey)) {
				// Delimiter has already been processed, create new StringReader with one position less
				StringReader duplicateKey = new StringReader(reader);
				duplicateKey.setCursor(reader.getCursor() - 1);
				throw duplicateKey(duplicateKey);
			}
			if (!reader.canRead()) {
				throw missingQuotationMark(reader);
			}
			V mapValue = parseValue(reader);
			if (results.containsValue(mapValue) && !allowValueDuplicates) {
				throw duplicateValue(reader);
			}
			reader.skip();
			if (reader.canRead()) {
				checkSeparator(reader);
			}
			results.put(mapKey, mapValue);
		}

		return results;
	}

	private K parseKey(StringReader reader) throws CommandSyntaxException {
		StringBuilder builder = new StringBuilder();
		while (reader.canRead()) {
			if (reader.peek() == '"') {
				// Don't skip it, the quote is required for parsing the value
				// The current key is not quoted, the previous character has to be the delimiter
				StringReader delimiterRequired = new StringReader(reader);
				delimiterRequired.setCursor(reader.getCursor() - 1);
				if (delimiterRequired.peek() != delimiter) {
					throw throwValueEarlyStart(delimiterRequired, String.valueOf(delimiter));
				}
				// Previous character is the delimiter, try parsing the key
				if (!keyList.contains(builder.toString()) && !keyListEmpty) {
					throw throwInvalidKey(delimiterRequired, builder.toString());
				}
				return tryParseKey(delimiterRequired, builder.toString());
			} else if (reader.peek() == delimiter) {
				if (!reader.canRead(2)) {
					reader.skip();
					throw missingQuotationMark(reader);
				}
				reader.skip();
			} else {
				builder.append(reader.peek());
				if (keyList.contains(builder.toString()) && !reader.canRead(2)) {
					throw missingDelimiter(reader);
				}
				if (keyList.contains(builder.toString()) && reader.peek(1) == '"') {
					StringReader delimiterRequired = new StringReader(reader);
					delimiterRequired.skip();
					throw throwValueEarlyStart(delimiterRequired, String.valueOf(delimiter));
				}
				if ((!keyList.contains(builder.toString()) && !keyListEmpty) && !reader.canRead(2)) {
					throw throwInvalidKey(reader, builder.toString());
				}
				reader.skip();
			}
		}
		return null;
	}

	private V parseValue(StringReader reader) throws CommandSyntaxException {
		StringBuilder builder = new StringBuilder();
		validateValueStart(reader);
		if (!reader.canRead()) {
			throw missingValue(reader);
		}
		while (reader.canRead()) {
			if (reader.peek() == '\\') {
				// Reached an escape character, skip it and add the next one to the builder
				reader.skip();
				if (reader.peek() == '\\' || reader.peek() == '"') {
					builder.append(reader.read());
				}
			} else if (reader.peek() == '"') {
				if (!valueList.contains(reader.toString()) && !valueListEmpty) {
					throw throwInvalidValue(reader, builder.toString());
				}
				return tryParseValue(reader, builder.toString());
			} else {
				builder.append(reader.read());
				if (valueList.contains(builder.toString()) && !reader.canRead()) {
					validateValueInput(reader, builder.toString());
				}
				if ((!valueList.contains(builder.toString()) && !valueListEmpty) && !reader.canRead()) {
					throw throwInvalidValue(reader, builder.toString());
				}
			}
		}
		return null;
	}

	private void checkSeparator(StringReader reader) throws CommandSyntaxException {
		StringBuilder builder = new StringBuilder();
		while (reader.canRead()) {
			// If the built separator is longer than it should be, error!
			if (builder.length() > separator.length()) {
				throw invalidSeparator(reader, builder.toString());
			}

			// If the built separator doesn't match the required separator, error!
			if (!separator.startsWith(builder.toString())) {
				throw invalidSeparator(reader, builder.toString());
			}

			builder.append(reader.read());
			if (!reader.canRead()) {
				throw missingKey(reader);
			}
			if (separator.equals(builder.toString())) {
				break;
			}
		}
	}

	private String handleQuotedKey(StringReader reader) throws CommandSyntaxException {
		return "";
	}

	private K tryParseKey(StringReader reader, String key) throws CommandSyntaxException {
		K mapKey;
		try {
			mapKey = keyMapper.apply(key);
		} catch (Exception e) {
			throw cannotParseKey(reader, key);
		}
		return mapKey;
	}

	private V tryParseValue(StringReader reader, String value) throws CommandSyntaxException {
		V mapValue;
		try {
			mapValue = valueMapper.apply(value);
		} catch (Exception e) {
			throw cannotParseValue(reader, value);
		}
		return mapValue;
	}

	/*
	@SuppressWarnings("unchecked")
	public <Source> LinkedHashMap<K, V> parseArgumentt(CommandContext<Source> cmdCtx, String key, CommandArguments previousArgs) throws CommandSyntaxException {
		String rawValues = cmdCtx.getArgument(key, String.class);
		LinkedHashMap<K, V> results = new LinkedHashMap<>();

		K mapKey = null;
		V mapValue = null;

		boolean isAKeyBeingBuilt = true;
		boolean isAValueBeingBuilt = false;
		boolean isFirstValueCharacter = true;

		StringBuilder keyValueSeparatorBuffer = new StringBuilder();
		StringBuilder visitedCharacters = new StringBuilder();

		char[] rawValuesChars = rawValues.toCharArray();
		int currentIndex = -1;
		for (char currentChar : rawValuesChars) {
			currentIndex++;
			visitedCharacters.append(currentChar);
			if (isAKeyBeingBuilt) {
				if (currentChar == delimiter) {
					if (!keyList.contains(keyValueSeparatorBuffer.toString()) && !keyListEmpty) {
						throw throwInvalidKey(visitedCharacters, keyValueSeparatorBuffer.toString(), true);
					}

					if (currentIndex == rawValuesChars.length - 1) {
						throw missingQuotationMark(visitedCharacters);
					}

					try {
						mapKey = keyMapper.apply(keyValueSeparatorBuffer.toString());
					} catch (Exception e) {
						throw cannotParseKey(visitedCharacters, keyValueSeparatorBuffer);
					}

					if (results.containsKey(mapKey)) {
						throw duplicateKey(visitedCharacters);
					}

					// No need to check the key here because we already know it only consists of letters

					keyValueSeparatorBuffer.setLength(0);
					isAKeyBeingBuilt = false;
					isAValueBeingBuilt = true;
					continue;
				}
				if (currentChar == '"') {
					throw throwValueEarlyStart(visitedCharacters, String.valueOf(delimiter));
				}
				keyValueSeparatorBuffer.append(currentChar);

				final boolean isInvalidKey = (!keyList.contains(keyValueSeparatorBuffer.toString()) && !keyListEmpty);
				if (currentIndex == rawValuesChars.length - 1) {
					if (isInvalidKey) {
						throw throwInvalidKey(visitedCharacters, keyValueSeparatorBuffer.toString(), false);
					} else {
						throw missingDelimiter(visitedCharacters);
					}
				}
			}
			if (isAValueBeingBuilt) {
				if (isFirstValueCharacter) {
					validateValueStart(currentChar, visitedCharacters);
					if (currentIndex == rawValuesChars.length - 1) {
						throw missingValue(visitedCharacters);
					}
					isFirstValueCharacter = false;
					continue;
				}
				if (currentChar == '\\') {
					if (rawValuesChars[currentIndex] == '\\' && rawValuesChars[currentIndex - 1] == '\\') {
						keyValueSeparatorBuffer.append('\\');
						continue;
					}
					continue;
				}
				if (currentChar == '"') {
					if (rawValuesChars[currentIndex - 1] == '\\' && rawValuesChars[currentIndex - 2] != '\\') {
						keyValueSeparatorBuffer.append('"');
						continue;
					}
					if (!valueList.contains(keyValueSeparatorBuffer.toString()) && !valueListEmpty) {
						throw throwInvalidValue(visitedCharacters, keyValueSeparatorBuffer.toString());
					}

					try {
						mapValue = valueMapper.apply(keyValueSeparatorBuffer.toString());
					} catch (Exception e) {
						throw cannotParseValue(visitedCharacters, keyValueSeparatorBuffer);
					}

					if (results.containsValue(mapValue) && !allowValueDuplicates) {
						throw duplicateValue(visitedCharacters);
					}

					keyValueSeparatorBuffer.setLength(0);
					isFirstValueCharacter = true;
					results.put(mapKey, mapValue);
					mapKey = null;

					isAValueBeingBuilt = false;
					continue;
				}
				keyValueSeparatorBuffer.append(currentChar);
			}
			if (!isAKeyBeingBuilt && !isAValueBeingBuilt) {
				// The separator has been completed, building happens under the if statement
				if (keyValueSeparatorBuffer.toString().equals(separator)) {
					// currentChar is the start of a new value
					keyValueSeparatorBuffer.setLength(0);
					isAKeyBeingBuilt = true;
					keyValueSeparatorBuffer.append(currentChar);
					continue;
				}
				keyValueSeparatorBuffer.append(currentChar);
				if (!separator.startsWith(keyValueSeparatorBuffer.toString())) {
					throw invalidSeparator(visitedCharacters, keyValueSeparatorBuffer.toString());
				}
			}
		}
		validateValueInput(keyValueSeparatorBuffer, visitedCharacters);
		return results;
	}
	 */

	private boolean isKeyQuoted(StringReader reader, boolean forceQuoteKeys) throws CommandSyntaxException {
		if (forceQuoteKeys) {
			try {
				reader.expect('"');
				return true;
			} catch (CommandSyntaxException e) {
				throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "The current key must start with a quotation mark");
			}
		}
		if (reader.peek() == '"') {
			reader.skip();
			return true;
		} else  {
			return false;
		}
	}

	private void validateValueStart(StringReader reader) throws CommandSyntaxException {
		try {
			reader.expect('"');
		} catch (CommandSyntaxException e) {
			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "A value must start with a quotation mark");
		}
	}

	private void validateValueInput(StringReader reader, String value) throws CommandSyntaxException {
		if (value.length() != 0) {
			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "A value must end with a quotation mark");
		}
	}

	private CommandSyntaxException throwValueEarlyStart(StringReader reader, String delimiter) {
		return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "You must separate a key/value pair with a '" + delimiter + "'");
	}

	private CommandSyntaxException throwInvalidKey(StringReader reader, String key) {
		return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "Invalid key: " + key);
	}

	private CommandSyntaxException throwInvalidValue(StringReader reader, String value) {
		return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "Invalid value: " + value);
	}

	private CommandSyntaxException invalidSeparator(StringReader reader, String separator) {
		return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "Invalid separator: " + separator);
	}

	private CommandSyntaxException duplicateKey(StringReader reader) {
		return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "Duplicate keys are not allowed");
	}

	private CommandSyntaxException duplicateValue(StringReader reader) {
		return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "Duplicate values are not allowed here");
	}

	private CommandSyntaxException missingKey(StringReader reader) {
		return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "Key required after writing the separator");
	}

	private CommandSyntaxException missingDelimiter(StringReader reader) {
		return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "Delimiter required after writing a key");
	}

	private CommandSyntaxException missingQuotationMark(StringReader reader) {
		return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "Quotation mark required after writing the delimiter");
	}

	private CommandSyntaxException missingValue(StringReader reader) {
		return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "Value required after opening quotation mark");
	}

	private CommandSyntaxException cannotParseKey(StringReader reader, String key) throws CommandSyntaxException {
		throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "Invalid key (" + key + "): cannot be converted to a key");
	}

	private CommandSyntaxException cannotParseValue(StringReader reader, String value) throws CommandSyntaxException {
		throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, "Invalid value (" + value + "): cannot be converted to a value");
	}

	private static class MapArgumentSuggestionInfo {

		private String currentKey;
		private String currentValue;
		private String currentSeparator;
		private SuggestionCode suggestionCode;

		MapArgumentSuggestionInfo(String currentKey, String currentValue, String currentSeparator, SuggestionCode suggestionCode) {
			this.currentKey = currentKey;
			this.currentValue = currentValue;
			this.currentSeparator = currentSeparator;
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

		public String getCurrentSeparator() {
			return currentSeparator;
		}

		public void setCurrentSeparator(String currentSeparator) {
			this.currentSeparator = currentSeparator;
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
		VALUE_SUGGESTION,
		SEPARATOR_SUGGESTION
	}

}