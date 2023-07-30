package dev.jorel.commandapi;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * A wrapper around MethodHandle with better type safety using generics and a
 * toggleable underlying implementation depending on whether we're using mojang
 * mappings or non-mojang mappings. This implementation only works for static
 * methods that have one parameter.
 *
 * @param <ReturnType>
 * @param <ParameterType>
 */
public class SafeStaticOneParameterMethodHandle<ReturnType, ParameterType> {

    private final MethodHandle handle;

    private SafeStaticOneParameterMethodHandle(MethodHandle handle) {
        this.handle = handle;
    }

    private static <ReturnType, ParameterType> SafeStaticOneParameterMethodHandle<ReturnType, ParameterType> of(
            Class<?> classType,
            String methodName, String mojangMappedMethodName,
            Class<? super ReturnType> returnType,
            Class<? super ParameterType> parameterType
    ) throws ReflectiveOperationException {
        return new SafeStaticOneParameterMethodHandle<>(MethodHandles.privateLookupIn(classType, MethodHandles.lookup()).findStatic(classType, SafeVarHandle.USING_MOJANG_MAPPINGS ? mojangMappedMethodName : methodName, MethodType.methodType(returnType, parameterType)));
    }

    public static <ReturnType, ParameterType> SafeStaticOneParameterMethodHandle<ReturnType, ParameterType> ofOrNull(
            Class<?> classType,
            String methodName, String mojangMappedMethodName,
            Class<? super ReturnType> returnType,
            Class<? super ParameterType> parameterType
    ) {
        try {
            return of(classType, methodName, mojangMappedMethodName, returnType, parameterType);
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
