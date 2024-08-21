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
package dev.jorel.commandapi.annotations.reloaded.generators;

/**
 * Renders Java literal values
 */
public class LiteralValueRenderer implements Renderer<Object> {
	private final ConstructorClassNameRenderer constructorClassNameRenderer;

	public LiteralValueRenderer() {
		this(new ConstructorClassNameRenderer());
	}

	public LiteralValueRenderer(ConstructorClassNameRenderer constructorClassNameRenderer) {
		this.constructorClassNameRenderer = constructorClassNameRenderer;
	}

	@Override
	public String render(Object obj) {
		if (obj instanceof Byte val) {
			return "%d".formatted(val);
		} else if (obj instanceof Short val) {
			return "%d".formatted(val);
		} else if (obj instanceof Integer val) {
			return "%d".formatted(val);
		} else if (obj instanceof Long val) {
			return "%dL".formatted(val);
		} else if (obj instanceof Float val) {
			return "%fF".formatted(val);
		} else if (obj instanceof Double val) {
			return "%fD".formatted(val);
		} else if (obj instanceof Character val) {
			return "'%c'".formatted(val);
		} else if (obj instanceof String val) {
			return "\"%s\"".formatted(val);
		} else if (obj instanceof Boolean val) {
			return "%b".formatted(val);
		} else if (obj instanceof Enum<?> val) {
			return "%s.%s".formatted(val.getDeclaringClass().getSimpleName(), val.name());
		} else if (obj instanceof Class<?> val) {
			return constructorClassNameRenderer.render(val);
		} else {
			return "%s".formatted(obj);
		}
	}
}
