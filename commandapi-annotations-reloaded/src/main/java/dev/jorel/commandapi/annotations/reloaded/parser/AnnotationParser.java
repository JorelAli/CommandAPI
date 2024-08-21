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

import javax.lang.model.element.Element;
import java.util.Optional;

/**
 * Interface for parsing a CommandAPI annotation and retrieving some data.
 *
 * @param <AnElement>      The type of element that the annotation is applied to
 * @param <AParserContext> The type of contextual information needed to parse the data
 * @param <AReturnType>    The type of data returned
 */
public interface AnnotationParser<
	AnElement extends Element,
	AParserContext extends AnnotationParserContext<AnElement>,
	AReturnType
	> {

	/**
	 * Parse the given contextual information and attempt to retrieve data.
	 *
	 * @param context The context needed to retrieve the data
	 * @return The data, if it could be retrieved, or empty if it couldn't
	 */
	Optional<AReturnType> parse(AParserContext context);
}
