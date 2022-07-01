import java.util.ArrayDeque;
import java.util.Deque;

/**
 * A mutable wrapper of an object, as a list
 *
 * @param <T>
 */
public class Mut<T> {
	
	public static <T> Mut<T> of() {
		return new Mut<T>();
	}

	public static <T> Mut<T> of(T obj) {
		return new Mut<T>(obj);
	}
	
	private final Deque<T> value;
	
	private Mut() {
		value = new ArrayDeque<>();
	}
	
	private Mut(T obj) {
		this();
		set(obj);
	}
	
	public void set(T obj) {
		this.value.add(obj);
	}
	
	public T get() {
		if(this.value.size() == 0) {
			return null;
		} else {
			return this.value.remove();
		}
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}
	
}
