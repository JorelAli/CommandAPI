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
package dev.jorel.commandapi.annotations.reloaded.modules;

import dev.jorel.commandapi.annotations.reloaded.generators.CodeGenerator;
import dev.jorel.commandapi.annotations.reloaded.generators.GeneratorContext;
import dev.jorel.commandapi.annotations.reloaded.parser.AnnotationParser;
import dev.jorel.commandapi.annotations.reloaded.parser.AnnotationParserContext;
import dev.jorel.commandapi.annotations.reloaded.semantics.SemanticAnalyzer;

import javax.lang.model.element.Element;

/**
 * A module for containing a discrete feature of the annotation system.
 *
 * @param <AnElement> The element that the relevant CommandAPI annotation is applied to
 * @param <AParserContext> Context needed to parse all data for this feature
 * @param <AGeneratorContext> Context needed to generate all needed code for this feature
 */
public interface AnalyzerParserGeneratorModule<
	AnElement extends Element,
	AParserContext extends AnnotationParserContext<AnElement>,
	AGeneratorContext extends GeneratorContext
	> extends
	SemanticAnalyzer,
	AnnotationParser<AnElement, AParserContext, AGeneratorContext>,
	CodeGenerator<AGeneratorContext> {

}
