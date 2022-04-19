import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

import org.junit.jupiter.api.Test;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;

import dev.jorel.commandapi.annotations.Annotations;

public class AnnotationTests {
	
	{
		new HordeCommand2().new HazardCommand().new CreateCommand().fire(null, null);
		HordeCommand2 hc2 = new HordeCommand2();
		hc2.new HazardCommand();
	}

	@Test
	void googleCompileTestingInit() {
		Compilation compilation = javac().compile(JavaFileObjects.forSourceString("HelloWorld", "public class HelloWorld {}"));
		assertThat(compilation).succeeded();
	}
	
	@Test
	void googleCompileTestingInitFail() {
		Compilation compilation = javac().compile(JavaFileObjects.forSourceString("HelloWorld", "public class HelloWorld {} blah"));
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
	void nonTopLevelCommandTest() {
		Compilation compilation = javac()
			.withProcessors(new Annotations())
			.compile(JavaFileObjects.forResource("CommandOnNonTopLevel.java"));
		
		assertThat(compilation)
			.hadErrorContaining("@Command can only go on a top level class")
			.inFile(JavaFileObjects.forResource("CommandOnNonTopLevel.java"))
			.onLine(7);
	}
	
}