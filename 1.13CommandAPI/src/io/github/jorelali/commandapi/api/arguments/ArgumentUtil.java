package io.github.jorelali.commandapi.api.arguments;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.bukkit.Bukkit;

import io.github.jorelali.commandapi.api.ClassCache;

public final class ArgumentUtil {

	private static String packageName;
	
	//Cons: contains lots of duplicates from SemiReflector...
	private static Map<String, Class<?>> NMSClasses;
	private static Map<ClassCache, Method> methods;

	
	static {
		try {
			ArgumentUtil.packageName = Bukkit.getServer().getClass().getDeclaredMethod("getServer").invoke(Bukkit.getServer()).getClass().getPackage().getName();
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
		}
	}
	
	protected static Class<?> getNMS(String className) throws ClassNotFoundException {
		if(NMSClasses.containsKey(className)) {
			return NMSClasses.get(className);
		} else {
			Class<?> result = (Class.forName(ArgumentUtil.packageName + "." + className));
			NMSClasses.put(className, result);
			return result;
		} 
	}
	
	protected static Method getMethod(Class<?> clazz, String name) {
		ClassCache key = new ClassCache(clazz, name);
		if(methods.containsKey(key)) {
			return methods.get(key);
		} else {
			Method result = null;
			try {
				result = clazz.getDeclaredMethod(name);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			methods.put(key, result);
			return result;
		}
	}
	
}
