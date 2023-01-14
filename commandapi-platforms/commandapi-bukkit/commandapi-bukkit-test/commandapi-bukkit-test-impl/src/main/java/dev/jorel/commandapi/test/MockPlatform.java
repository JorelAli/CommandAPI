package dev.jorel.commandapi.test;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFactory;

import dev.jorel.commandapi.CommandAPIBukkit;

public abstract class MockPlatform<CLW> extends CommandAPIBukkit<CLW> {
	
	/*****************
	 * Instantiation *
	 *****************/
	
	private static MockPlatform<?> instance;
	
	@SuppressWarnings("unchecked")
	public static <CLW> MockPlatform<CLW> getInstance() {
		return (MockPlatform<CLW>) instance;
	}
	
	public MockPlatform() {
		if(instance == null) {
			instance = this;
		} else {
			// wtf why was this called twice?
		}
	}
	
	/******************
	 * Helper methods *
	 ******************/
	
	public static Object getField(Class<?> className, String fieldName, Object instance) {
		try {
			Field field = className.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(instance);
		} catch (ReflectiveOperationException e) {
			return null;
		}
	}

	public static void setField(Class<?> className, String fieldName, Object instance, Object value) {
		try {
			Field field = className.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(instance, value);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getFieldAs(Class<?> className, String fieldName, Object instance, Class<T> asType) {
		try {
			Field field = className.getDeclaredField(fieldName);
			field.setAccessible(true);
			return (T) field.get(instance);
		} catch (ReflectiveOperationException e) {
			return null;
		}
	}
	
	/***********************
	 * Bukkit "enum" lists *
	 ***********************/

	public abstract org.bukkit.enchantments.Enchantment[] getEnchantments();
	public abstract org.bukkit.entity.EntityType[] getEntityTypes();
	public abstract org.bukkit.loot.LootTables[] getLootTables();
	public abstract org.bukkit.potion.PotionEffectType[] getPotionEffects();
	
	public abstract List<String> getAllItemNames();
	
	/**
	 * Other stuff
	 */
	
	public abstract ItemFactory getItemFactory();
	public abstract org.bukkit.advancement.Advancement addAdvancement(NamespacedKey key);
	
}
