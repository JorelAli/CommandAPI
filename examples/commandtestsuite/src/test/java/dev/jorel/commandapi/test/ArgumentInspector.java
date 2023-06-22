package dev.jorel.commandapi.test;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * A mutable wrapper of an object, as a list
 *
 * @param <T>
 */
public class ArgumentInspector<T> {
	
	public static <T> ArgumentInspector<T> of() {
		return new ArgumentInspector<T>();
	}
	
	private final Deque<Optional<T>> value;
	
	private ArgumentInspector() {
		value = new ArrayDeque<>();
	}
	
	public void set(T obj) {
		this.value.add(Optional.ofNullable(obj));
	}
	
	public T get() {
		if(this.value.size() == 0) {
			throw new NoSuchElementException();
		} else {
			return this.value.remove().orElse(null);
		}
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}
	
}
