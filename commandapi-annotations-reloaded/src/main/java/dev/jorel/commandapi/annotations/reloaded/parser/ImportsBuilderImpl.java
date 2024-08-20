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
package dev.jorel.commandapi.annotations.reloaded.parser;

import javax.lang.model.element.TypeElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Default implementation for building a set of imports for a generated class.
 * <p>
 * In case of class name conflicts, the first package will get the import and use the simple name, whereas all
 * additional packages will use a qualified name.
 */
public class ImportsBuilderImpl implements ImportsBuilder {
	private final Map<String, String> imports;

	public ImportsBuilderImpl() {
		this(new HashMap<>());
	}

	ImportsBuilderImpl(Map<String, String> imports) {
		this.imports = imports;
	}

	String withImport(String simpleName, String qualifiedName) {
		if (imports.containsKey(simpleName)) {
			if (imports.get(simpleName).equals(qualifiedName)) {
				return simpleName; // We already imported this
			}
			return qualifiedName; // Conflicting name, can't import so use fully qualified name instead
		}
		imports.put(simpleName, qualifiedName);
		return simpleName;
	}

	@Override
	public String withImport(Class<?> clazz) {
		return withImport(clazz.getSimpleName(), clazz.getCanonicalName());
	}

	@Override
	public String withImport(TypeElement typeElement) {
		var simpleName = typeElement.getSimpleName().toString();
		var qualifiedName = typeElement.getQualifiedName();
		if (qualifiedName.isEmpty()) {
			throw new IllegalArgumentException("You can't import an element with no qualified name: %s"
				.formatted(typeElement));
		}
		return withImport(simpleName, qualifiedName.toString());
	}

	@Override
	public Set<String> getAllFullyQualifiedNames() {
		return new TreeSet<>(imports.values());
	}
}
