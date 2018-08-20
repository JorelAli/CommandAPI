package io.github.jorelali.commandapi.api;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.ItemStackArgument;
import io.github.jorelali.commandapi.api.arguments.ParticleArgument;
import io.github.jorelali.commandapi.api.arguments.PotionEffectArgument;

//Only uses reflection for NMS
@SuppressWarnings({"rawtypes", "unchecked"})
public final class SemiReflector {
	
	//OBC
	private String obcPackageName = null;
	
	//NMS variables
	private static String packageName = null;
	private CommandDispatcher dispatcher;
	private Object cDispatcher;

	//DEBUG mode
	private static final boolean DEBUG = false;
	
	protected SemiReflector() throws ClassNotFoundException {
		
		//Package checks
		if(Package.getPackage("com.mojang.brigadier") == null) {
			throw new ClassNotFoundException("Cannot hook into Brigadier (Are you running Minecraft 1.13 or above?)");
		}
		
		try {
			//Setup NMS
			Object server = Bukkit.getServer().getClass().getDeclaredMethod("getServer").invoke(Bukkit.getServer());
			SemiReflector.packageName = server.getClass().getPackage().getName();
			obcPackageName = Bukkit.getServer().getClass().getPackage().getName();
			this.cDispatcher = getNMSClass("MinecraftServer").getDeclaredField("commandDispatcher").get(server);
						
			//This is our "z"
			this.dispatcher = (CommandDispatcher) getNMSClass("CommandDispatcher").getDeclaredMethod("a").invoke(cDispatcher); 

		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//Builds our NMS command using the given arguments for this method, then registers it
	protected void register(String commandName, final LinkedHashMap<String, Argument> args, CommandExecutor executor) throws Exception {
		
		//Generate our command from executor
		Command command = (cmdCtx) -> {
			
			//Get the CommandSender via NMS
			CommandSender sender = null;
			try {
				sender = (CommandSender) getNMSClass("CommandListenerWrapper").getDeclaredMethod("getBukkitSender").invoke(cmdCtx.getSource());
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
				e.printStackTrace();
			}

			//Array for arguments for executor
			Object[] arr = new Object[args.size()];
			
			//Populate array
			int count = 0;
			for(Entry<String, Argument> entry : args.entrySet()) {
				//If primitive (and simple), parse as normal
				if(entry.getValue().isSimple()) {
					arr[count] = cmdCtx.getArgument(entry.getKey(), entry.getValue().getPrimitiveType());
				} else {
					
					//Deal with complex argument types
					
					if(entry.getValue() instanceof ItemStackArgument) {
						try {
							//Parse Bukkit ItemStack from NMS 
							Method asBukkitCopy = getOBCClass("inventory.CraftItemStack").getDeclaredMethod("asBukkitCopy", getNMSClass("ItemStack"));
							Object argumentIS = getNMSClass("ArgumentItemStack").getDeclaredMethod("a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
							Object nmsIS = argumentIS.getClass().getDeclaredMethod("a", int.class, boolean.class).invoke(argumentIS, 1, false);
							arr[count] = (ItemStack) asBukkitCopy.invoke(null, nmsIS);
						} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
						}
					} else if(entry.getValue() instanceof ParticleArgument) {
						try {
							//Particle Bukkit Particle from NMS
							Method toBukkit = getOBCClass("CraftParticle").getDeclaredMethod("toBukkit", getNMSClass("ParticleParam"));
							Object particleParam = getNMSClass("ArgumentParticle").getDeclaredMethod("a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
							arr[count] = (Particle) toBukkit.invoke(null, particleParam);
						} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
						}
					} else if(entry.getValue() instanceof PotionEffectArgument) {
						try {
							Constructor craftPotionType = getOBCClass("potion.CraftPotionEffectType").getConstructor(getNMSClass("MobEffectList"));
							Object mobEffect = getNMSClass("ArgumentMobEffect").getDeclaredMethod("a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
							arr[count] = (PotionEffectType) craftPotionType.newInstance(mobEffect);
						} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
							e.printStackTrace();
						}
					}
					//ArgumentItemStack.a(cmdCtx, "item").a(0, false);
					//Otherwise, deal with ItemStack (for example)
					//cmdCtx.getArgument(entry.getKey(), arg1)
					//ItemStack is = CraftItemStack.asBukkitCopy(ArgumentItemStack.a(cmdCtx, "item").a((int) cmdCtx.getArgument("amount", int.class), false));
				}
				
				count++;
			}
			
			//Run the code from executor and return
			executor.run(sender, arr);
			return 1;
		};
		
				
		/*
		 * The innermost argument needs to be connected to the executor.
		 * Then that argument needs to be connected to the previous argument
		 * etc.
		 * Then the first argument needs to be connected to the command name
		 * 
		 * CommandName -> Args1 -> Args2 -> ... -> ArgsN -> Executor
		 */
		
		//List of keys for reverse iteration
        ArrayList<String> keys = new ArrayList<>(args.keySet());

        //Link the last element to the executor
        RequiredArgumentBuilder inner = reflectCommandDispatcherArgument(keys.get(keys.size() - 1), args.get(keys.get(keys.size() - 1)).getRawType()).executes(command);

        //Link everything else up, except the first
        RequiredArgumentBuilder outer = inner;
        for(int i = keys.size() - 2; i >= 0; i--) {
        	outer = reflectCommandDispatcherArgument(keys.get(i), args.get(keys.get(i)).getRawType()).then(outer);
        }

        //Link command name to first argument
		this.dispatcher.register(reflectCommandDispatcherCommandName(commandName).then(outer));
		
		//Produce the commandDispatch.json file for debug purposes
		if(DEBUG) {
			File file = new File("commandDispatch.json");
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			getNMSClass("CommandDispatcher").getDeclaredMethod("a", File.class).invoke(this.cDispatcher, file);
		}
	}
		
	//Registers a LiteralArgumentBuilder for a command name
	private LiteralArgumentBuilder<?> reflectCommandDispatcherCommandName(String commandName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		return (LiteralArgumentBuilder) getNMSClass("CommandDispatcher").getDeclaredMethod("a", String.class).invoke(null, commandName);
	}
	
	//Registers a RequiredArgumentBuilder for an argument
	private <T> RequiredArgumentBuilder<?, T> reflectCommandDispatcherArgument(String argumentName, com.mojang.brigadier.arguments.ArgumentType<T> type) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		return (RequiredArgumentBuilder<?, T>) getNMSClass("CommandDispatcher").getDeclaredMethod("a", String.class, com.mojang.brigadier.arguments.ArgumentType.class).invoke(null, argumentName, type);
	}
		
	/** Retrieves a net.minecraft.server class by using the dynamic package from
	 * the dedicated server */
	private Class<?> getNMSClass(final String className) throws ClassNotFoundException {
		return (Class.forName(SemiReflector.packageName + "." + className));
	}
	
	/** Retrieves a craftbukkit class */
	private Class<?> getOBCClass(final String className) throws ClassNotFoundException {
		return (Class.forName(obcPackageName + "." + className));
	}
	
}
