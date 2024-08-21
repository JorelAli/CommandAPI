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

import dev.jorel.commandapi.annotations.reloaded.modules.base.CommandsClassModule;
import dev.jorel.commandapi.annotations.reloaded.parser.ImportsBuilder;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Interface for configuration data needed by the CommandAPI annotation system
 */
public interface Configuration {
	/**
	 * Allows the Commands class name to be altered to prevent conflicts when running test cases
	 *
	 * @return The Commands class name to use when generating the class
	 */
	String getCommandsClassName();

	/**
	 * @return A set of all supported command argument annotations
	 */
	Set<Class<? extends Annotation>> getArgumentAnnotations();

	/**
	 * @return A set of all other supported command annotations
	 */
	Set<Class<? extends Annotation>> getOtherAnnotations();

	/**
	 * @return A set of the canonical names of all supported annotations
	 */
	Set<String> getSupportedAnnotationTypes();

	/**
	 * @return The base module that contains the modular hierarchy for all features of the annotation processor
	 */
	CommandsClassModule getBaseModule();

	/**
	 * @return The imports builder to use for the generated commands class
	 */
	ImportsBuilder getImportsBuilder();

	/**
	 * @return Utility functions for working with java annotations
	 */
	AnnotationUtils getAnnotationUtils();
}
