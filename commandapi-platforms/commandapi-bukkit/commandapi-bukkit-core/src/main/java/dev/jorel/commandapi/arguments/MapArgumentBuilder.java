package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.wrappers.MapArgumentKeyType;

import java.util.function.Function;

/**
 * A builder to create a {@link MapArgument}
 *
 * @param <V> The type of values this map should contain
 */
public class MapArgumentBuilder<V> {

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
	public MapArgumentBuilderValueMapper withKeyType(MapArgumentKeyType keyType) {
		return new MapArgumentBuilderValueMapper(keyType);
	}

	/**
	 * An intermediary class for the {@link MapArgumentBuilder}
	 */
	public class MapArgumentBuilderValueMapper {

		private final MapArgumentKeyType keyType;

		public MapArgumentBuilderValueMapper(MapArgumentKeyType keyType) {
			this.keyType = keyType;
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
			public MapArgument<?, V> build() {
				return switch (keyType) {
					case STRING -> new MapArgument<String, V>(nodeName, delimiter, keyType, valueMapper);
					case FLOAT -> new MapArgument<Float, V>(nodeName, delimiter, keyType, valueMapper);
					case INT -> new MapArgument<Integer, V>(nodeName, delimiter, keyType, valueMapper);
				};
			}

		}

	}


}
