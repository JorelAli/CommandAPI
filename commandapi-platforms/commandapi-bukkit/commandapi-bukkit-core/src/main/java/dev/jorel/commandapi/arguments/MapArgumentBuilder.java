package dev.jorel.commandapi.arguments;

import java.util.function.Function;

/**
 * A builder to create a {@link MapArgument}
 *
 * @param <K>
 * @param <V>
 */
public class MapArgumentBuilder<K, V> {

	private final String nodeName;
	private final char delimiter;

	/**
	 * Creates a new MapArgumentBuilder with a specified node name. Defaults the
	 * delimiter for each key/value pair to a colon
	 *
	 * @param nodeName the name of the node for this argument
	 */
	public MapArgumentBuilder(String nodeName) {
		this(nodeName, ':');
	}

	/**
	 * Creates a new MapArgumentBuilder with a specified node name
	 *
	 * @param nodeName  the name of the node for this argument
	 * @param delimiter the separator for each key/value pair
	 */
	public MapArgumentBuilder(String nodeName, char delimiter) {
		this.nodeName = nodeName;
		this.delimiter = delimiter;
	}

	/**
	 * This starts the builder for the {@link MapArgument}
	 *
	 * @return this map argument builder
	 */
	public MapArgumentBuilderValueMapper withKeyMapper(Function<String, K> keyMapper) {
		return new MapArgumentBuilderValueMapper(keyMapper);
	}

	/**
	 * An intermediary class for the {@link MapArgumentBuilder}
	 */
	public class MapArgumentBuilderValueMapper {

		private final Function<String, K> keyMapper;

		public MapArgumentBuilderValueMapper(Function<String, K> keyMapper) {
			this.keyMapper = keyMapper;
		}

		/**
		 * Specifies the mapping function to convert a <code>String</code>
		 * into the specific type <code>V</code>
		 *
		 * @param valueMapper the mapping function that creates an instance of <code>V</code>
		 * @return this map argument builder
		 */
		public MapArgumentBuilderFinished withValueMapper(Function<String, V> valueMapper) {
			return new MapArgumentBuilderFinished(valueMapper);
		}

		/**
		 * An intermediary class for the {@link MapArgumentBuilder}
		 */
		public class MapArgumentBuilderFinished {

			private final Function<String, V> valueMapper;

			public MapArgumentBuilderFinished(Function<String, V> valueMapper) {
				this.valueMapper = valueMapper;
			}

			/**
			 * Builds this {@link MapArgument}
			 *
			 * @return a new {@link MapArgument}
			 */
			public MapArgument<K, V> build() {
				return new MapArgument<>(nodeName, delimiter, keyMapper, valueMapper);
			}

		}

	}


}
