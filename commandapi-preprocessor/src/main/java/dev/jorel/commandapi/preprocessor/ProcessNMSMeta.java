package dev.jorel.commandapi.preprocessor;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

import de.icongmbh.oss.maven.plugin.javassist.ClassTransformer;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.build.JavassistBuildException;

public class ProcessNMSMeta extends ClassTransformer {

	@Override
	public boolean shouldTransform(final CtClass candidateClass) throws JavassistBuildException {
		try {
			return candidateClass.getAnnotation(NMSMeta.class) != null;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	@Override
	public void applyTransformations(CtClass classToTransform) throws JavassistBuildException {
		try {
			// If we have a compatibleVersions method, trash it
			CtMethod toStringMethod = classToTransform.getDeclaredMethod("compatibleVersions");
			classToTransform.removeMethod(toStringMethod);
		} catch (NotFoundException e) {
		}

		String versions = "";
		try {
			NMSMeta source = (NMSMeta) classToTransform.getAnnotation(NMSMeta.class);
			versions = Arrays.stream(source.compatibleWith()).map(x -> "\"" + x + "\"").collect(Collectors.joining(", "));
		} catch (ClassNotFoundException e) {
			throw new JavassistBuildException(e);
		}

		try {
			CtMethod compatibleVersionsMethod = CtNewMethod.make("""
					public String[] compatibleVersions() {
						return new String[] { %s };
					}
					""".formatted(versions), classToTransform);
			classToTransform.addMethod(compatibleVersionsMethod);
		} catch (CannotCompileException e) {
			throw new JavassistBuildException(e);
		}
	}

	@Override
	public void configure(final Properties properties) {
		if (properties == null) {
			return;
		}
	}
}
