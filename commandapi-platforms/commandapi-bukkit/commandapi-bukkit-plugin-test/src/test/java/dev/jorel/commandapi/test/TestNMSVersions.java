package dev.jorel.commandapi.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.engine.execution.BeforeEachMethodAdapter;
import org.junit.jupiter.engine.extension.ExtensionRegistry;
import org.junit.platform.commons.util.AnnotationUtils;

/**
 * https://stackoverflow.com/a/69265907/4779071
 * https://code-case.hashnode.dev/how-to-pass-parameterized-test-parameters-to-beforeeachaftereach-method-in-junit5
 * https://github.com/lamektomasz/AfterBeforeParameterizedTestExtension/commit/7ca50e3bc8046625c21ce0b0863dc54022fff637
 */

public class TestNMSVersions implements BeforeEachMethodAdapter, ParameterResolver {
	private ParameterResolver parameterisedTestParameterResolver = null;

	@Override
	public void invokeBeforeEachMethod(ExtensionContext context, ExtensionRegistry registry) {
		Optional<ParameterResolver> resolverOptional = registry.getExtensions(ParameterResolver.class)
			.stream()
			.filter(parameterResolver -> parameterResolver.getClass().getName().contains("ParameterizedTestParameterResolver"))
			.findFirst();
		if (!resolverOptional.isPresent()) {
			throw new IllegalStateException("ParameterizedTestParameterResolver missed in the registry. Probably it's not a Parameterized Test");
		} else {
			parameterisedTestParameterResolver = resolverOptional.get();
		}
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		if (isExecutedOnAfterOrBeforeMethod(parameterContext)) {
			ParameterContext pContext = getMappedContext(parameterContext, extensionContext);
			return parameterisedTestParameterResolver.supportsParameter(pContext, extensionContext);
		}
		return false;
	}

	private MappedParameterContext getMappedContext(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return new MappedParameterContext(
			parameterContext.getIndex(),
			extensionContext.getRequiredTestMethod().getParameters()[parameterContext.getIndex()],
			Optional.of(parameterContext.getTarget()));
	}

	private boolean isExecutedOnAfterOrBeforeMethod(ParameterContext parameterContext) {
		return Arrays.stream(parameterContext.getDeclaringExecutable().getDeclaredAnnotations())
			.anyMatch(this::isAfterEachOrBeforeEachAnnotation);
	}

	private boolean isAfterEachOrBeforeEachAnnotation(Annotation annotation) {
		return annotation.annotationType() == BeforeEach.class; // || annotation.annotationType() == AfterEach.class;
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return parameterisedTestParameterResolver.resolveParameter(getMappedContext(parameterContext, extensionContext), extensionContext);
	}

	static class MappedParameterContext implements ParameterContext {

		private final int index;
		private final Parameter parameter;
		private final Optional<Object> target;

		public MappedParameterContext(int index, Parameter parameter, Optional<Object> target) {
			this.index = index;
			this.parameter = parameter;
			this.target = target;
		}

		@Override
		public boolean isAnnotated(Class<? extends Annotation> annotationType) {
			return AnnotationUtils.isAnnotated(parameter, annotationType);
		}

		@Override
		public <A extends Annotation> Optional<A> findAnnotation(Class<A> annotationType) {
			return AnnotationUtils.findAnnotation(parameter, annotationType);
		}

		@Override
		public <A extends Annotation> List<A> findRepeatableAnnotations(Class<A> annotationType) {
			return AnnotationUtils.findRepeatableAnnotations(parameter, annotationType);
		}

		@Override
		public Parameter getParameter() {
			return parameter;
		}

		@Override
		public int getIndex() {
			return index;
		}

		@Override
		public Optional<Object> getTarget() {
			return target;
		}
	}
}