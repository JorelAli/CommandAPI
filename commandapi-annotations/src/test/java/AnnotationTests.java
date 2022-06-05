import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.google.testing.compile.Compilation;
import static com.google.testing.compile.JavaFileObjects.forSourceString;
import static com.google.testing.compile.JavaFileObjects.forResource;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.annotations.Annotations;

public class AnnotationTests {

	Compilation compile(String classToCompile) {
		return javac().withProcessors(new Annotations(true)).compile(forResource(classToCompile));
	}
	
	/*
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
//			.compile(JavaFileObjects.forResource("HordeCommand.java"));
//		assertThat(compilation).succeeded();
	}
	
	@Test
	/**
	 * Testing that a {@code @Command} on a type inside another type with {@code @Command} fails to compile
	 */
	void noNestedCommands() {
		assertThat(compile("NestedCommand.java"))
			.hadErrorContaining("@Command can only go on a top level class")
			.inFile(forResource("NestedCommand.java"))
			.onLine(7);
	}
	
	@Test
	/**
	 * Testing that a {@code @Command} class has at least one executor
	 */
	void checkCommandHasExecutor() {
		assertThat(compile("ClassWithNoExecutor.java"))
			.hadWarningContaining("Command has no command executors")
			.inFile(forResource("ClassWithNoExecutor.java"))
			.onLine(4);
	}
	
}