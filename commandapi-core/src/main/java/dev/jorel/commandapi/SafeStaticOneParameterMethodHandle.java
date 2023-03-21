package dev.jorel.commandapi;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class SafeStaticOneParameterMethodHandle<ReturnType, ParameterType> {

	private final MethodHandle handle;

	private SafeStaticOneParameterMethodHandle(MethodHandle handle) {
		this.handle = handle;
	}

	private static <ReturnType, ParameterType> SafeStaticOneParameterMethodHandle<ReturnType, ParameterType> of(Class<?> classType, String fieldName, String mojangMappedFieldName, Class<ReturnType> returnType, Class<ParameterType> parameterType) throws ReflectiveOperationException {
		return new SafeStaticOneParameterMethodHandle<>(MethodHandles.privateLookupIn(classType, MethodHandles.lookup()).findStatic(classType, SafeVarHandle.USING_MOJANG_MAPPINGS ? mojangMappedFieldName : fieldName, MethodType.methodType(returnType, parameterType)));
	}

	public static <ReturnType, ParameterType> SafeStaticOneParameterMethodHandle<ReturnType, ParameterType> ofOrNull(Class<?> classType, String fieldName, String mojangMappedFieldName, Class<ReturnType> returnType, Class<ParameterType> parameterType) {
		try {
			return of(classType, fieldName, mojangMappedFieldName, returnType, parameterType);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ReturnType invoke(ParameterType parameter) throws Throwable {
		return (ReturnType) handle.invoke(parameter);
	}

	public ReturnType invokeOrNull(ParameterType parameter) {
		try {
			return invoke(parameter);
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
}