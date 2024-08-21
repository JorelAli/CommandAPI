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
package dev.jorel.commandapi.annotations.reloaded.modules.arguments.parsers;

import dev.jorel.commandapi.annotations.reloaded.annotations.NodeName;
import dev.jorel.commandapi.annotations.reloaded.parser.AnnotationParser;
import dev.jorel.commandapi.annotations.reloaded.parser.AnnotationParserContext;

import javax.lang.model.element.Element;
import java.util.Optional;

/**
 * Parses the node name for an argument, using the {@link NodeName} annotation
 * if one is present, or the parameter or field name if not.
 */
public class NodeNameParser implements AnnotationParser<Element, AnnotationParserContext<Element>, String> {
	@Override
	public Optional<String> parse(AnnotationParserContext<Element> context) {
		Element element = context.element();
		if (element.getAnnotation(NodeName.class) != null) {
			return Optional.of(element.getAnnotation(NodeName.class).value());
		} else {
			return Optional.of(element.getSimpleName().toString());
		}
	}
}
