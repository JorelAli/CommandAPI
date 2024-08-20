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
package dev.jorel.commandapi.annotations.reloaded.test;

import com.google.testing.compile.Compilation;
import dev.jorel.commandapi.annotations.reloaded.Annotations;
import dev.jorel.commandapi.annotations.reloaded.TestConfiguration;
import org.junit.jupiter.api.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;
import static com.google.testing.compile.JavaFileObjects.forResource;
import static com.google.testing.compile.JavaFileObjects.forSourceString;

public class AnnotationTests {

	Compilation compile(String classToCompile) {
		return javac().withProcessors(new Annotations(new TestConfiguration())).compile(forResource(classToCompile));
	}

	/**
	 * Testing that the testing framework works
	 */
	@Test
	void googleCompileTestingInit() {
		Compilation compilation = javac().compile(forSourceString("HelloWorld", "public class HelloWorld {}"));
		assertThat(compilation).succeeded();
	}

	@Test
	void googleCompileTestingInitFail() {
		Compilation compilation = javac().compile(forSourceString("HelloWorld", "public class HelloWorld {} blah"));
		assertThat(compilation).failed();
	}

	@Test
	void hordeTest() {
//		Compilation compilation = javac()
//			.withProcessors(new Annotations())
//			.compile(forResource("HordeCommand.java"));
//		assertThat(compilation).succeeded();
	}

	/**
	 * Testing that a {@code @Command} on a type inside another type with {@code @Command} fails to compile
	 */
	@Test
	void noNestedCommands() {
		assertThat(compile("NestedCommand.java"))
			.hadErrorContaining("@Command can only go on a top level class")
			.inFile(forResource("NestedCommand.java"))
			.onLine(27);
	}

	/**
	 * Testing that a {@code @Command} class has at least one executor
	 */
	@Test
	void checkCommandHasExecutor() {
		assertThat(compile("ClassWithNoExecutor.java"))
			.hadWarningContaining("@Command class has no executors")
			.inFile(forResource("ClassWithNoExecutor.java"))
			.onLine(24);
	}

}