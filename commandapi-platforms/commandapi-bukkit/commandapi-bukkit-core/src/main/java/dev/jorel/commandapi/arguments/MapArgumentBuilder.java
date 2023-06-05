package dev.jorel.commandapi.arguments;

import java.util.List;

/**
 * A builder to create a {@link MapArgument}
 *
 * @param <K> The type of keys this map should contain
 * @param <V> The type of values this map should contain
 */
public class MapArgumentBuilder<K, V> {

	private final String nodeName;
	private final String delimiter;
	private final String separator;

	/**
	 * Creates a new MapArgumentBuilder with a specified node name. Defaults the
	 * delimiter for each key/value pair to a colon
	 *
	 * @param nodeName the name of the node for this argument
	 */
	public MapArgumentBuilder(String nodeName) {
		this(nodeName, ":");
	}

	/**
	 * Creates a new MapArgumentBuilder with a specified node name
	 *
	 * @param nodeName  the name of the node for this argument
	 * @param delimiter the separator for each key/value pair
	 * @deprecated use {@link MapArgumentBuilder#MapArgumentBuilder(String, String)} instead
	 */
	@Deprecated(since = "9.0.2")
	public MapArgumentBuilder(String nodeName, char delimiter) {
		this(nodeName, String.valueOf(delimiter));
	}

	/**
	 * Creates a new MapArgumentBuilder with a specified node name
	 *
	 * @param nodeName  the name of the node for this argument
	 * @param delimiter the separator for each key/value pair
	 */
	public MapArgumentBuilder(String nodeName, String delimiter) {
		this(nodeName, delimiter, " ");
	}

	/**
	 * Creates a new MapArgumentBuilder with a specified node name
	 *
	 * @param nodeName  the name of the node for this argument
	 * @param delimiter the separator for each key/value pair
	 * @param separator the separator between a key and a value
	 */
	public MapArgumentBuilder(String nodeName, String delimiter, String separator) {
		if(delimiter.length() == 0) throw new IllegalArgumentException("The delimiter cannot be an empty String!");
		if(separator.length() == 0) throw new IllegalArgumentException("The separator cannot be an empty String!");
		this.nodeName = nodeName;
		this.delimiter = delimiter;
		this.separator = separator;
	}

	/**
	 * This starts the builder for the {@link MapArgument}
	 *
	 * @return this map argument builder
	 */
	public MapArgumentBuilderValueMapper withKeyMapper(StringParser<K> keyMapper) {
		return new MapArgumentBuilderValueMapper(keyMapper);
	}

	/**
	 * An intermediary class for the {@link MapArgumentBuilder}
	 */
	public class MapArgumentBuilderValueMapper {

		private final StringParser<K> keyMapper;

		public MapArgumentBuilderValueMapper(StringParser<K> keyMapper) {
			this.keyMapper = keyMapper;
		}

		/**
		 * Specifies the mapping function to convert a <code>String</code>
		 * into the specific type <code>V</code>
		 *
		 * @param valueMapper the mapping function that creates an instance of <code>V</code>
		 * @return this map argument builder
		 */
		public MapArgumentBuilderSuggestsKey withValueMapper(StringParser<V> valueMapper) {
			return new MapArgumentBuilderSuggestsKey(valueMapper);
		}

		/**
		 * An intermediary class for the {@link MapArgumentBuilder}
		 */
		public class MapArgumentBuilderSuggestsKey {

			private final StringParser<V> valueMapper;

			public MapArgumentBuilderSuggestsKey(StringParser<V> valueMapper) {
				this.valueMapper = valueMapper;
			}

			/**
			 * Provides a list of values to suggest when a value was typed.
			 *
			 * @param keyList A list of keys to suggest.
			 * @return this map argument builder
			 */
			public MapArgumentBuilderSuggestsValue withKeyList(List<String> keyList) {
				return new MapArgumentBuilderSuggestsValue(keyList);
			}

			/**
			 * When using this method, no key suggestions are displayed
			 *
			 * @return this map argument builder
			 */
			public MapArgumentBuilderSuggestsValue withoutKeyList() {
				return withKeyList(null);
			}

			/**
			 * An intermediary class for the {@link MapArgumentBuilder}
			 */
			public class MapArgumentBuilderSuggestsValue {

				private final List<String> keyList;

				public MapArgumentBuilderSuggestsValue(List<String> keyList) {
					this.keyList = keyList;
				}

				/**
				 * Provides a list of values to suggest after a key was typed. Using this method will not allow writing values more than once!
				 * <p>
				 * If you want to allow that, please use the {@link #withValueList(List, boolean)} method!
				 *
				 * @param valueList A list of values to suggest. The values need to be Strings, so they can be suggested
				 * @return this map argument builder
				 */
				public MapArgumentBuilderFinished withValueList(List<String> valueList) {
					return withValueList(valueList, false);
				}

				/**
				 * Provides a list of values to suggest after a key was typed.
				 *
				 * @param valueList A list of values to suggest. The values need to be Strings, so they can be suggested
				 * @param allowDuplicates Decides if a value can be written more than once
				 * @return this map argument builder
				 */
				public MapArgumentBuilderFinished withValueList(List<String> valueList, boolean allowDuplicates) {
					return new MapArgumentBuilderFinished(valueList, allowDuplicates);
				}

				/**
				 * When using this method, no value suggestions are displayed! Using this method will also not allow writing values more than once!
				 * <p>
				 * If you want to allow that, please use the {@link #withValueList(List, boolean)} method!
				 *
				 * @return this map argument builder
				 */
				public MapArgumentBuilderFinished withoutValueList() {
					return withValueList(null);
				}

				/**
				 * When using this method, no value suggestions are displayed!
				 *
				 * @param allowDuplicates Decides if a value can be written more than once
				 * @return this map argument builder
				 */
				public MapArgumentBuilderFinished withoutValueList(boolean allowDuplicates) {
					return withValueList(null, allowDuplicates);
				}

				/**
				 * An intermediary class for the {@link MapArgumentBuilder}
				 */
				public class MapArgumentBuilderFinished {

					private final List<String> valueList;
					private final boolean allowValueDuplicates;

					public MapArgumentBuilderFinished(List<String> valueList, boolean allowValueDuplicates) {
						this.valueList = valueList;
						this.allowValueDuplicates = allowValueDuplicates;
					}

					/**
					 * Builds this {@link MapArgument}
					 *
					 * @return a new {@link MapArgument}
					 */
					public MapArgument<K, V> build() {
						return new MapArgument<>(nodeName, delimiter, separator, keyMapper, valueMapper, keyList, valueList, allowValueDuplicates);
					}
				}
			}
		}
	}
}
