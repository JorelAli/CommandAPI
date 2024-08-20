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
package dev.jorel.commandapi.annotations.reloaded.arguments.utils;

import dev.jorel.commandapi.arguments.Argument;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * An interface that applies to all argument annotation classes
 */
public interface ArgumentAnnotation {
	/**
	 * @return The class of the annotation this mirrors
	 */
	Class<? extends Annotation> annotationType();

	/**
	 * @return The class of the argument this uses
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends Argument> argumentType();

	/**
	 * @return The variable types this argument parses to
	 */
	PrimitiveType primitiveType();

	/**
	 * @return The name of the command node and the variable this annotation is attached to
	 */
	String nodeName();

	/**
	 * @return Whether this argument should be marked as an optional argument
	 */
	boolean optional();

	/**
	 * @return True, if this argument should be listed in the Object args[] of the command executor
	 */
	default ArgumentListedOption listed() {
		return ArgumentListedOption.DEFAULT;
	}

	/**
	 * @return The serialized arguments to be passed to the constructor
	 */
	default List<Object> constructorParameters() {
		return List.of(nodeName());
	}
}
