package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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

			return suggestionInfo.provideSuggestions(builder, currentArgument, keyValues, valueValues);
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
	private MapArgumentSuggestionInfo getSuggestionCode(String currentArgument, List<String> keys, List<String> values) throws CommandSyntaxException {
		K mapKey = null;

		String currentKey = "";
		String currentValue = "";

		MapArgumentSuggestionInfo suggestionInfo = new MapArgumentSuggestionInfo(currentKey, currentValue, -1);

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
				ValueHolder valueHolder = aKeyIsSuggested(currentChar, suggestionInfo, currentKey, currentValue, keyBuilder, null, visitedCharacters, keys);
				mapKey = valueHolder.getMapKey();
				isAKeyBeingBuilt = valueHolder.isAKeyBeingBuilt();
				isAValueBeingBuilt = valueHolder.isAValueBeingBuilt();
			} else if (isAValueBeingBuilt) {
				ValueHolder valueHolder = aValueIsSuggested(isFirstValueCharacter, currentChar, rawValuesChars,
					currentIndex, visitedCharacters, valueBuilder, suggestionInfo, keys, values, currentKey, currentValue, mapKey, null);
				isAKeyBeingBuilt = valueHolder.isAKeyBeingBuilt();
				isAValueBeingBuilt = valueHolder.isAValueBeingBuilt();
				isFirstValueCharacter = valueHolder.isFirstValueCharacter();
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

	@Override
	public <Source> LinkedHashMap<K, V> parseArgument(CommandContext<Source> cmdCtx, String key, Object[] previousArgs) throws CommandSyntaxException {
		String rawValues = cmdCtx.getArgument(key, String.class);
		LinkedHashMap<K, V> results = new LinkedHashMap<>();

		K mapKey = null;

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
				ValueHolder valueHolder = aKeyIsBuilt(currentChar, keyBuilder, mapKey, visitedCharacters);
				mapKey = valueHolder.getMapKey();
				isAKeyBeingBuilt = valueHolder.isAKeyBeingBuilt();
				isAValueBeingBuilt = valueHolder.isAValueBeingBuilt();
			} else if (isAValueBeingBuilt) {
				ValueHolder valueHolder = aValueIsBuilt(currentChar, rawValuesChars, currentIndex, isFirstValueCharacter, visitedCharacters, mapKey, valueBuilder, results);
				isAKeyBeingBuilt = valueHolder.isAKeyBeingBuilt();
				isAValueBeingBuilt = valueHolder.isAValueBeingBuilt();
				isFirstValueCharacter = valueHolder.isFirstValueCharacter();
				mapKey = valueHolder.getMapKey();
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

	@SuppressWarnings("unchecked")
	private ValueHolder aKeyIsBuilt(char currentChar, StringBuilder keyBuilder, K mapKey, StringBuilder visitedCharacters) throws CommandSyntaxException {
		if (currentChar == delimiter) {
			mapKey = (K) keyMapper.apply(keyBuilder.toString());

			// No need to check the key here because we already know it only consists of letters

			keyBuilder.setLength(0);
			return new ValueHolder(false, true, false, mapKey, null);
		}
		if (currentChar == '"') {
			throw throwValueEarlyStart(visitedCharacters, String.valueOf(delimiter));
		}
		keyBuilder.append(currentChar);
		validateKey(visitedCharacters, keyPattern, keyBuilder);
		return new ValueHolder(true, false, false, mapKey, null);
	}

	private ValueHolder aValueIsBuilt(char currentChar, char[] rawValuesChars, int currentIndex, boolean isFirstValueCharacter, StringBuilder visitedCharacters,
	                                  K mapKey, StringBuilder valueBuilder, LinkedHashMap<K, V> results) throws CommandSyntaxException {
		if (isFirstValueCharacter) {
			validateValueStart(currentChar, visitedCharacters);
			return new ValueHolder(false, true, false, mapKey, null);
		}
		if (currentChar == '\\') {
			if (rawValuesChars[currentIndex] == '\\' && rawValuesChars[currentIndex - 1] == '\\') {
				valueBuilder.append('\\');
				return new ValueHolder(false, true, false, mapKey, null);
			}
			return new ValueHolder(false, true, false, mapKey, null);
		}
		if (currentChar == '"') {
			if (rawValuesChars[currentIndex - 1] == '\\' && rawValuesChars[currentIndex - 2] != '\\') {
				valueBuilder.append('"');
				return new ValueHolder(false, true, false, mapKey, null);
			}
			V mapValue = valueMapper.apply(valueBuilder.toString());
			valueBuilder.setLength(0);
			results.put(mapKey, mapValue);

			return new ValueHolder(false, false, true, null, null);
		}
		valueBuilder.append(currentChar);
		return new ValueHolder(false, true, false, mapKey, null);
	}

	@SuppressWarnings("unchecked")
	private ValueHolder aKeyIsSuggested(char currentChar, MapArgumentSuggestionInfo suggestionInfo, String currentKey, String currentValue,
	                                    StringBuilder keyBuilder, K mapKey, StringBuilder visitedCharacters, List<String> keys) throws CommandSyntaxException {
		currentValue = "";
		suggestionInfo.setCurrentValue(currentValue);
		if (currentChar == delimiter) {
			mapKey = (K) keyMapper.apply(keyBuilder.toString());
			keyBuilder.setLength(0);
			suggestionInfo.setSuggestionCode(2);
			return new ValueHolder(false, true, false, mapKey, null);
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
		return new ValueHolder(true, false, false, mapKey, null);
	}

	private ValueHolder aValueIsSuggested(boolean isFirstValueCharacter, char currentChar, char[] rawValuesChars, int currentIndex,
	                                      StringBuilder visitedCharacters, StringBuilder valueBuilder, MapArgumentSuggestionInfo suggestionInfo,
	                                      List<String> keys, List<String> values, String currentKey, String currentValue,
	                                      K mapKey, V mapValue) throws CommandSyntaxException {
		if (isFirstValueCharacter) {
			validateValueStart(currentChar, visitedCharacters); // currentChar should be a quotation mark
			suggestionInfo.setSuggestionCode(3);
			return new ValueHolder(false, true, false, mapKey, mapValue);
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
				return new ValueHolder(false, true, false, mapKey, mapValue);
			}
			return new ValueHolder(false, true, false, mapKey, mapValue);
		}
		if (currentChar == '"') {
			return currentCharacterQuotationMark(rawValuesChars, currentIndex, valueBuilder, values, keys, suggestionInfo, mapKey, mapValue, currentKey);
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
		return new ValueHolder(false, true, false, mapKey, mapValue);
	}

	private ValueHolder currentCharacterQuotationMark(char[] rawValuesChars, int currentIndex, StringBuilder valueBuilder, List<String> values, List<String> keys,
	                                                  MapArgumentSuggestionInfo suggestionInfo, K mapKey, V mapValue, String currentKey) {
		if (rawValuesChars[currentIndex - 1] == '\\' && rawValuesChars[currentIndex - 2] != '\\') {
			valueBuilder.append('"');
			for (String value : values) {
				if (value.equals(valueBuilder.toString())) {
					suggestionInfo.setSuggestionCode(2);
					break;
				}
				suggestionInfo.setSuggestionCode(3);
			}
			return new ValueHolder(false, true, false, mapKey, mapValue);
		}
		mapValue = valueMapper.apply(valueBuilder.toString());
		currentKey = "";
		suggestionInfo.setCurrentKey(currentKey);

		enteredValues.add(mapKey + ":\"" + mapValue + "\"");
		keys.remove(enteredValues.get(enteredValues.size() - 1).split(":")[0]);
		if (!allowValueDuplicates) {
			values.remove(valueBuilder.toString());
		}

		valueBuilder.setLength(0);
		suggestionInfo.setSuggestionCode(0);

		return new ValueHolder(false, false, true, null, mapValue);
	}

	private class MapArgumentSuggestionInfo {

		private String currentKey;
		private String currentValue;
		private int suggestionCode;

		MapArgumentSuggestionInfo(String currentKey, String currentValue, int suggestionCode) {
			this.currentKey = currentKey;
			this.currentValue = currentValue;
			this.suggestionCode = suggestionCode;
		}

		public CompletableFuture<Suggestions> provideSuggestions(SuggestionsBuilder builder, String currentArgument, List<String> keyValues, List<String> valueValues) {
			switch (getSuggestionCode()) {
				case 0 -> {
					builder = builder.createOffset(builder.getStart() + currentArgument.length() - getCurrentKey().length());
					for (String key : keyValues) {
						if (key.startsWith(getCurrentKey())) {
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
					builder = builder.createOffset(builder.getStart() + currentArgument.length() - getCurrentValue().length());
					for (String value : valueValues) {
						if (value.startsWith(getCurrentValue())) {
							builder.suggest(value);
						}
					}
				}
				default -> {}
			}
			return builder.buildFuture();
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
	}

	private class ValueHolder {

		K mapKey;
		V mapValue;
		boolean isAKeyBeingBuilt;
		boolean isAValueBeingBuilt;
		boolean isFirstValueCharacter;

		ValueHolder(boolean isAKeyBeingBuilt, boolean isAValueBeingBuilt, boolean isFirstValueCharacter, K mapKey, V mapValue) {
			this.mapKey = mapKey;
			this.mapValue = mapValue;
			this.isAKeyBeingBuilt = isAKeyBeingBuilt;
			this.isAValueBeingBuilt = isAValueBeingBuilt;
			this.isFirstValueCharacter = isFirstValueCharacter;
		}

		public K getMapKey() {
			return mapKey;
		}

		public V getMapValue() {
			return mapValue;
		}

		public boolean isAKeyBeingBuilt() {
			return isAKeyBeingBuilt;
		}

		public boolean isAValueBeingBuilt() {
			return isAValueBeingBuilt;
		}

		public boolean isFirstValueCharacter() {
			return isFirstValueCharacter;
		}

	}

}
