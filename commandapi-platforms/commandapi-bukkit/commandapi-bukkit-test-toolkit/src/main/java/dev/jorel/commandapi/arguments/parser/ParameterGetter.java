package dev.jorel.commandapi.arguments.parser;

/**
 * A class used to track previous context while parsing. The creator of this object promises to update the stored
 * value, which can be retrieved using the {@link #get()} method.
 *
 * @param <T> The type of object held.
 */
public class ParameterGetter<T> {
	// Idea for type safe parameter retrieval from https://github.com/JorelAli/CommandAPI/issues/544
	private T value;

	protected void set(T value) {
		this.value = value;
	}

	/**
	 * @return The currently stored value. This will be automatically updated when appropriate by this object's creator.
	 */
	public T get() {
		return value;
	}
}
