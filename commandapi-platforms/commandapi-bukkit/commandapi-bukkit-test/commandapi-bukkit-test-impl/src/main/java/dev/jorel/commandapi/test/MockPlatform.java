package dev.jorel.commandapi.test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.commandsenders.BukkitCommandSender;
import dev.jorel.commandapi.wrappers.ParticleData;

public abstract class MockPlatform<CLW> extends CommandAPIBukkit<CLW> {

	/*****************
	 * Instantiation *
	 *****************/

	private static MockPlatform<?> instance = null;

	@SuppressWarnings("unchecked")
	public static <CLW> MockPlatform<CLW> getInstance() {
		return (MockPlatform<CLW>) instance;
	}

	public MockPlatform() {
		if (instance == null) {
			instance = this;
		} else {
			// wtf why was this called twice?
		}
	}

	/************************************
	 * CommandAPIBukkit implementations *
	 ************************************/

	private CommandDispatcher<CLW> dispatcher = null;

	@Override
	public final CommandDispatcher<CLW> getBrigadierDispatcher() {
		if (this.dispatcher == null) {
			this.dispatcher = new CommandDispatcher<>();
		}
		return this.dispatcher;
	}

	@Override
	public final BukkitCommandSender<? extends CommandSender> getSenderForCommand(CommandContext<CLW> cmdCtx, boolean forceNative) {
		return getCommandSenderFromCommandSource(cmdCtx.getSource());
	}

	@Override
	public final void resendPackets(Player player) {
		return; // There's nothing to do here, we can't "send packets to players"
	}

	@Override
	public final void addToHelpMap(Map<String, HelpTopic> helpTopicsToAdd) {
		throw new Error("unimplemented");
	}

	@Override
	public final String convert(ItemStack is) {
		throw new Error("unimplemented");
	}

	@Override
	public final String convert(ParticleData<?> particle) {
		throw new Error("unimplemented");
	}

	@Override
	public final String convert(PotionEffectType potion) {
		throw new Error("unimplemented");
	}

	@Override
	public final String convert(Sound sound) {
		throw new Error("unimplemented");
	}

	@Override
	public final HelpTopic generateHelpTopic(String commandName, String shortDescription, String fullDescription, String permission) {
		throw new Error("unimplemented");
	}

	@Override
	public final boolean isVanillaCommandWrapper(Command command) {
		throw new Error("unimplemented");
	}

	@Override
	public final void reloadDataPacks() {
		throw new Error("unimplemented");
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

	/***************
	 * Other stuff *
	 ***************/

	public abstract ItemFactory getItemFactory();

	public abstract org.bukkit.advancement.Advancement addAdvancement(NamespacedKey key);

	/**
	 * Converts 1.16.5 and below potion effect names to NamespacedKey names. For
	 * example, converts "effect.minecraft.speed" into "minecraft:speed"
	 * 
	 * @param potionEffectType the potion effect to get the namespaced key for
	 * @return a Minecraft namespaced key name for a potion effect
	 */
	public abstract String getNMSPotionEffectName_1_16_5(PotionEffectType potionEffectType);

	/***********************
	 * Bukkit "enum" lists *
	 ***********************/

	public abstract org.bukkit.enchantments.Enchantment[] getEnchantments();

	public abstract org.bukkit.entity.EntityType[] getEntityTypes();

	public abstract org.bukkit.loot.LootTables[] getLootTables();

	public abstract org.bukkit.potion.PotionEffectType[] getPotionEffects();

	/**
	 * @return A list of all item names, sorted in alphabetical order. Each item
	 * is prefixed with {@code minecraft:}
	 */
	public abstract List<String> getAllItemNames();

}
