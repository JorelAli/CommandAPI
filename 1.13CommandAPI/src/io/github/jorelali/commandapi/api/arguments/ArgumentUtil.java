package io.github.jorelali.commandapi.api.arguments;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;

public final class ArgumentUtil {

	private static String packageName;
	
	static {
		try {
			ArgumentUtil.packageName = Bukkit.getServer().getClass().getDeclaredMethod("getServer").invoke(Bukkit.getServer()).getClass().getPackage().getName();
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
		}
	}
	
	protected static Class<?> getNMS(String className) throws ClassNotFoundException {
		return (Class.forName(ArgumentUtil.packageName + "." + className));
	}
	
}
