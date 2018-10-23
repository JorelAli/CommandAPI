package io.github.jorelali.commandapi.api;

/*
 * Class to store cached methods and fields 
 * 
 * This is required because each
 * key is made up of a class and a field or method name
 */
public class ClassCache {

	private Class<?> clazz;
	private String nameame;

	public ClassCache(Class<?> clazz, String name) {
		this.clazz = clazz;
		this.nameame = name;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public String getName() {
		return nameame;
	}
}