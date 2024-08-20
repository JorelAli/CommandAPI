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
package dev.jorel.commandapi.annotations.reloaded.semantics;

import java.util.List;

/**
 * Interface for analysing semantic rules for a feature of the CommandAPI annotation parser
 */
public interface SemanticAnalyzer {
	/**
	 * @return A list of semantic analysers for sub-features
	 */
	List<SemanticAnalyzer> subAnalyzers();

	/**
	 * @return A list of semantic rules to apply for this feature
	 */
	List<SemanticRule> rules();

	/**
	 * @return A name to use for this analyser in logs
	 */
	default String getName() {
		return getClass().getSimpleName();
	}

	/**
	 * @param context The context needed to analyse the set of semantic rules
	 * @return True, if all rules pass, or false, if any rule fails
	 */
	default boolean allPass(SemanticRuleContext context) {
		context.logging().info("Performing %s semantic checks".formatted(getName()));
		boolean passing = true;
		for (var rule : rules()) {
			if (rule.fails(context)) {
				passing = false;
				// Do not fail fast, we want full logs
			}
		}
		context.logging().info("%s semantic checks %s".formatted(getName(), passing ? "passed" : "failed"));
		for (var analyzer : subAnalyzers()) {
			if (analyzer.anyFail(context)) {
				passing = false;
				// Do not fail fast, we want full logs
			}
		}
		return passing;
	}

	/**
	 * @param context The context needed to analyse the set of semantic rules
	 * @return True, if any rule fails, or false, if all rule pass.
	 */
	default boolean anyFail(SemanticRuleContext context) {
		return !allPass(context);
	}
}
