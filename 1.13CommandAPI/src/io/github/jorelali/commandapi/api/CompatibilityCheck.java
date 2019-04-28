package io.github.jorelali.commandapi.api;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;

import net.minecraft.server.v1_14_R1.Entity;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.World;

public final class CompatibilityCheck {

	private static String packageName;
	private static String obcPackageName;
	
	public static void runChecks() throws Exception {
		//Setup NMS
		Object server = Bukkit.getServer().getClass().getDeclaredMethod("getServer").invoke(Bukkit.getServer());
		packageName = server.getClass().getPackage().getName();
		obcPackageName = Bukkit.getServer().getClass().getPackage().getName();

		checkPackages();
		checkClasses();
		checkFields();
		checkMethods();
	}
	
	private static void checkPackages() throws Exception {
		System.out.println(Package.getPackage("com.mojang.bridagier"));
	}
	
	private static void checkClasses() throws Exception {
		String[] classes = new String[] { "ArgumentChatComponent", "ArgumentChatFormat", "ArgumentEnchantment",
				"ArgumentEntity", "ArgumentEntitySummon", "ArgumentItemStack", "ArgumentMinecraftKeyRegistered",
				"ArgumentMobEffect", "ArgumentParticle", "ArgumentPosition", "ArgumentProfile", "ArgumentTag",
				"ArgumentVec3", "BaseBlockPosition", "CommandAdvancement", "CommandDispatcher",
				"CommandListenerWrapper", "CompletionProviders", "CustomFunction", "CustomFunctionData", "Enchantment",
				"Entity", "EntityTypes", "EnumChatFormat", "IChatBaseComponent", "IChatBaseComponent$ChatSerializer",
				"ICompletionProvider", "ItemStack", "LootTable", "LootTableRegistry", "MinecraftKey", "MinecraftServer",
				"MobEffectList", "ParticleParam", "Tags", "World" };
		
		for(String str : classes) {
			getNMSClass(str);
		}
	}
	
	private static void checkFields() throws Exception {
		//Object vec3D = getMethod(getNMSClass("ArgumentVec3"), "a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
		getField(getNMSClass("MinecraftServer"), "commandDispatcher");
		getField(CommandNode.class, "children");
		getField(getNMSClass("CommandListenerWrapper"), "k");
		
		getField(getNMSClass("Vec3D"), "x");
		getField(getNMSClass("Vec3D"), "y");
		getField(getNMSClass("Vec3D"),"z");
		getField(getNMSClass("ArgumentProfile"), "a");
		getField(SimpleCommandMap.class, "knownCommands");
		getField(getNMSClass("CompletionProviders"), "b");
		getField(getNMSClass("CompletionProviders"), "c");
		getField(getNMSClass("CommandAdvancement"), "a");
		getField(getNMSClass("LootTableRegistry"), "e"); 
	}
	
	private static void checkMethods() throws Exception {
		getMethod(getNMSClass("CommandListenerWrapper"), "getBukkitSender");
		getMethod(getNMSClass("CommandListenerWrapper"), "getEntity");
		getMethod(getNMSClass("Entity"), "getBukkitEntity");
		getMethod(getOBCClass("inventory.CraftItemStack"), "asBukkitCopy", getNMSClass("ItemStack"));
		getMethod(getNMSClass("ArgumentItemStack"), "a", CommandContext.class, String.class);
		//Object argumentIS = getMethod(getNMSClass("ArgumentItemStack"), "a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
		System.out.println("TODO: argumentIS");
		//getMethod(argumentIS.getClass(), "a", int.class, boolean.class);
		getMethod(getOBCClass("CraftParticle"), "toBukkit", getNMSClass("ParticleParam"));
		getMethod(getNMSClass("ArgumentParticle"), "a", CommandContext.class, String.class);
		getMethod(getNMSClass("ArgumentMobEffect"), "a", CommandContext.class, String.class);
		getMethod(getOBCClass("util.CraftChatMessage"), "getColor", getNMSClass("EnumChatFormat"));
		getMethod(getNMSClass("ArgumentChatFormat"), "a", CommandContext.class, String.class);
		getMethod(getNMSClass("ArgumentEnchantment"), "a", CommandContext.class, String.class);
		getMethod(getNMSClass("ArgumentPosition"), "a", CommandContext.class, String.class);
		getMethod(getNMSClass("BaseBlockPosition"), "getX");
		getMethod(getNMSClass("BaseBlockPosition"), "getY");
		getMethod(getNMSClass("BaseBlockPosition"), "getZ");
		getMethod(getNMSClass("ArgumentVec3"), "a", CommandContext.class, String.class);
		getMethod(getNMSClass("ArgumentEntitySummon"), "a", CommandContext.class, String.class);
		getMethod(getOBCClass("CraftWorld").getClass(), "getHandle");
		getMethod(getNMSClass("EntityTypes"), "a", getNMSClass("World"), getNMSClass("MinecraftKey")); //XXX Broken
		/**
		 * 	@Nullable
			public static Entity a(World world, MinecraftKey minecraftkey) {
				return a(world, (EntityTypes) IRegistry.ENTITY_TYPE.get(minecraftkey));
			}
			
			->	aZ = b<T>
			 	public static interface b<T extends Entity> {
					public T create(EntityTypes<T> var1, World var2);
				}
				
				@Nullable
				public T a(World world) {
					return this.aZ.create(this, world);
				}
		 */
		getMethod(getNMSClass("Entity"), "getBukkitEntity");
		getMethod(getNMSClass("ArgumentProfile"), "a", CommandContext.class, String.class);
		/*
		 * } else if(entry.getValue() instanceof PlayerArgument) {
						try {
							Collection<?> collectionOfPlayers = (Collection<?>) getMethod(getNMSClass("ArgumentProfile"), "a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
							Object first = collectionOfPlayers.iterator().next();
							UUID uuid = (UUID) getMethod(first.getClass(), "getId").invoke(first);
		 */
		System.out.println("TODO: whatever 'first' is");
		//getMethod(first.getClass(), "getId");
		getMethod(getNMSClass("ArgumentEntity"), "c", CommandContext.class, String.class);
		getMethod(getNMSClass("Entity"), "getBukkitEntity");
		getMethod(getNMSClass("ArgumentEntity"), "d", CommandContext.class, String.class);
		getMethod(getNMSClass("Entity"), "getBukkitEntity");
		getMethod(getNMSClass("ArgumentEntity"), "a", CommandContext.class, String.class);
		getMethod(getNMSClass("Entity"), "getBukkitEntity");
		getMethod(getNMSClass("ArgumentEntity"), "e", CommandContext.class, String.class);
		getMethod(getNMSClass("Entity"), "getBukkitEntity");
		getMethod(getNMSClass("IChatBaseComponent$ChatSerializer"), "a", getNMSClass("IChatBaseComponent"));
		getMethod(getNMSClass("ArgumentChatComponent"), "a", CommandContext.class, String.class);
		getMethod(getNMSClass("ArgumentTag"), "a", CommandContext.class, String.class);
		getMethod(getNMSClass("CommandListenerWrapper"), "getServer");
		getMethod(getNMSClass("MinecraftServer"), "getFunctionData");
		getMethod(getNMSClass("CustomFunctionData"), "a", getNMSClass("CustomFunction"), getNMSClass("CommandListenerWrapper"));
		getMethod(getNMSClass("CommandListenerWrapper"), "a");
		getMethod(getNMSClass("CommandListenerWrapper"), "b", int.class);
		getMethod(getNMSClass("CustomFunction"), "a");
		getMethod(getNMSClass("MinecraftKey"), "toString");
		getMethod(getOBCClass("entity.CraftEntity"), "getHandle");
		getMethod(getNMSClass("CommandListenerWrapper"), "a", getNMSClass("Entity"));
		getMethod(getNMSClass("ArgumentMinecraftKeyRegistered"), "c", CommandContext.class, String.class);
		getMethod(getNMSClass("MinecraftKey"), "b");
		getMethod(getNMSClass("MinecraftKey"), "getKey");
		getMethod(getNMSClass("CommandListenerWrapper"), "getServer");
		//getLootTableRegistry
		getMethod(getNMSClass("MinecraftServer"), "getLootTableRegistry"); //XXX broken???
		getMethod(getNMSClass("LootTableRegistry"), "getLootTable");
		getMethod(getOBCClass("CraftServer"), "getCommandMap");
		getMethod(getNMSClass("CommandDispatcher"), "a", File.class); ///XXX Broken. Method has been removed.
		/*
		 * 	public void a(File file) {
				try {
					Files.write((new GsonBuilder()).setPrettyPrinting().create()
							.toJson(ArgumentRegistry.a(this.b, this.b.getRoot())), file, StandardCharsets.UTF_8);
				} catch (IOException var3) {
					a.error("Couldn't write out command tree!", var3);
				}
		
			}
		 */
		getMethod(getNMSClass("CommandListenerWrapper"), "getBukkitSender");
		getMethod(getNMSClass("CommandListenerWrapper"), "getServer");
		getMethod(getNMSClass("MinecraftServer"), "getFunctionData");
		getMethod(getNMSClass("CustomFunctionData"), "g");
		getMethod(getNMSClass("Tags"), "a");
		getMethod(getNMSClass("ICompletionProvider"), "a", Iterable.class, SuggestionsBuilder.class, String.class);
		getMethod(getNMSClass("CustomFunctionData"), "c");
		getMethod(getNMSClass("ICompletionProvider"), "a", Iterable.class, SuggestionsBuilder.class);
		getMethod(getNMSClass("CommandListenerWrapper"), "getServer");
		getMethod(getNMSClass("MinecraftServer"), "getLootTableRegistry");
		getMethod(getNMSClass("ICompletionProvider"), "a", Iterable.class, SuggestionsBuilder.class);
		
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// SECTION: Reflection                                                                              //
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** Retrieves a net.minecraft.server class by using the dynamic package from
	 * the dedicated server 
	 * @throws ClassNotFoundException */
	private static Class<?> getNMSClass(final String className) {
		try {
			return (Class.forName(packageName + "." + className));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Method getMethod(Class<?> clazz, String name) {
		try {
			return clazz.getDeclaredMethod(name);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
		try {
			return clazz.getDeclaredMethod(name, parameterTypes);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Field getField(Class<?> clazz, String name)  {
		try {
			return clazz.getDeclaredField(name);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Class<?> getOBCClass(final String className) {
		try {
			return (Class.forName(obcPackageName + "." + className));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
