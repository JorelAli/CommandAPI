package dev.jorel.commandapi.arguments.parser;

//////////////////
// Build parser //
//////////////////
public class ParameterGetter<T> {
	// Idea for type safe parameter retrieval from https://github.com/JorelAli/CommandAPI/issues/544
	private T value;

	protected void set(T value) {
		this.value = value;
	}

	public T get() {
		return value;
	}
}
