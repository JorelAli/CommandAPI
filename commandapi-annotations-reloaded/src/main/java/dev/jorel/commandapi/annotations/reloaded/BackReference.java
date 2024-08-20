/*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi.annotations.reloaded;

import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * A reference back to an object previously added to a data structure to allow for cyclical structures.
 * <p>
 * This is intended to be used to handle situations where nesting occurs. Such as parsing subclasses within subclasses.
 * //TODO this could be moved to a more general location
 *
 * @param <T> The type of object referenced
 */
public class BackReference<T> {
	// Use a weak reference to make things easier on the garbage collector
	private WeakReference<T> objRef = null;

	/**
	 * @param obj The object to store a reference to
	 * @throws IllegalStateException on an attempt to re-initialise. It is not possible to re-use.
	 * @throws NullPointerException  if obj was null
	 */
	public void initialise(T obj) {
		if (objRef != null) {
			throw new IllegalStateException("Attempted to initialise twice");
		}
		objRef = new WeakReference<>(Objects.requireNonNull(obj));
	}

	/**
	 * @return The object that was stored earlier
	 * @throws IllegalStateException if there was no call to {@link #initialise(Object)}
	 */
	public T get() {
		if (objRef == null) {
			throw new IllegalStateException("Attempted to get before initialisation");
		}
		return Objects.requireNonNull(objRef.get());
	}
}
