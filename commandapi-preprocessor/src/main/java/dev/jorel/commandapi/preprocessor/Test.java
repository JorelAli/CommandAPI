package dev.jorel.commandapi.preprocessor;

import java.util.Properties;

import de.icongmbh.oss.maven.plugin.javassist.ClassTransformer;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.build.JavassistBuildException;

public class Test extends ClassTransformer {
	private static final String APPEND_VALUE_PROPERTY_KEY = "append.value";

	private String appendValue;

	/**
	 * We'll only transform subtypes of MyInterface.
	 */
	@Override
	public boolean shouldTransform(final CtClass candidateClass) throws JavassistBuildException {
		try {
			return candidateClass.getAnnotation(Source.class) != null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Hack the toString() method.
	 */
	@Override
	public void applyTransformations(CtClass classToTransform) throws JavassistBuildException {
		try {
			// Actually you must test if it exists, but it's just an example...
			CtMethod toStringMethod = classToTransform.getDeclaredMethod("toString");
			classToTransform.removeMethod(toStringMethod);

			CtMethod hackedToStringMethod = CtNewMethod
					.make("public String toString() { return \"toString() hacked by Javassist"
							+ (this.appendValue != null ? this.appendValue : "") + "\"; }", classToTransform);
			classToTransform.addMethod(hackedToStringMethod);
		} catch (CannotCompileException | NotFoundException e) {
			throw new JavassistBuildException(e);
		}
	}

	@Override
	public void configure(final Properties properties) {
		if (null == properties) {
			return;
		}
		this.appendValue = properties.getProperty(APPEND_VALUE_PROPERTY_KEY);
	}
}
