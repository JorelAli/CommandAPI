package dev.jorel.commandapi.test

import java.util.*

/**
 * A mutable wrapper of an object, as a list
 *
 * @param <T>
 */
class Mut<T : Any> private constructor() {
	private val value: Deque<Optional<T>>

	init {
		value = ArrayDeque()
	}

	private constructor(obj: T) : this() {
		set(obj)
	}

	fun set(obj: T) {
		value.add(Optional.ofNullable(obj))
	}

	fun get(): T {
		return if (value.size == 0) {
			throw NoSuchElementException()
		} else {
			value.remove().orElse(null)
		}
	}

	override fun toString(): String {
		return value.toString()
	}

	companion object {
		fun <T : Any> of(): Mut<T> {
			return Mut()
		}

		fun <T : Any> of(obj: T): Mut<T> {
			return Mut(obj)
		}
	}
}