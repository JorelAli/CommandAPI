package io.github.jorelali.commandapi.api;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;

import io.github.jorelali.commandapi.api.CommandPermission.PermissionNode;
import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.ChatColorArgument;
import io.github.jorelali.commandapi.api.arguments.EnchantmentArgument;
import io.github.jorelali.commandapi.api.arguments.EntitySelectorArgument;
import io.github.jorelali.commandapi.api.arguments.EntityTypeArgument;
import io.github.jorelali.commandapi.api.arguments.ItemStackArgument;
import io.github.jorelali.commandapi.api.arguments.LiteralArgument;
import io.github.jorelali.commandapi.api.arguments.LocationArgument;
import io.github.jorelali.commandapi.api.arguments.ParticleArgument;
import io.github.jorelali.commandapi.api.arguments.PlayerArgument;
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
	private static final boolean DEBUG = true;
	
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
	
	//Returns the world in which a command sender is from
	private World getCommandSenderWorld(CommandSender sender) {
		if(sender instanceof BlockCommandSender) {
			return ((BlockCommandSender) sender).getBlock().getWorld();
		} else if(sender instanceof ProxiedCommandSender) {
			CommandSender callee = ((ProxiedCommandSender) sender).getCallee();
			if(callee instanceof Entity) {
				return ((Entity) callee).getWorld();
			} else {
				return null;
			}
		} else if(sender instanceof Entity) {
			return ((Entity) sender).getWorld();
		} else {
			return null;
		}
	}
	
	private Command generateCommand(String commandName, final LinkedHashMap<String, Argument> args, CommandExecutor executor) {
		//Generate our command from executor
		return (cmdCtx) -> {

			//Get the CommandSender via NMS
			CommandSender sender = null;
			
			try {
				sender = (CommandSender) getNMSClass("CommandListenerWrapper").getDeclaredMethod("getBukkitSender").invoke(cmdCtx.getSource());
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
				e.printStackTrace(System.out);
			}
			
			//Handle proxied command senders via /execute as [Proxy]
			try {
				Object proxyEntity = cmdCtx.getSource().getClass().getDeclaredMethod("f").invoke(cmdCtx.getSource());
				
				if(proxyEntity != null) {
					CommandSender proxy = (CommandSender) getNMSClass("Entity").getDeclaredMethod("getBukkitEntity").invoke(proxyEntity);
					
					Class proxyClass = getOBCClass("command.ProxiedNativeCommandSender");
					Constructor proxyConstructor = proxyClass.getConstructor(getNMSClass("CommandListenerWrapper"), CommandSender.class, CommandSender.class);
					Object proxyInstance = proxyConstructor.newInstance(cmdCtx.getSource(), sender, proxy);
					sender = (ProxiedCommandSender) proxyInstance;
				}
				
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException e1) {
				e1.printStackTrace(System.out);
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
					
					//Deal with literal arguments
					if(entry.getValue() instanceof LiteralArgument) {
						LiteralArgument arg = (LiteralArgument) entry.getValue();
						System.out.println(arg.getLiteral());
						arr[count] = arg.getLiteral();
					} else if(entry.getValue() instanceof ItemStackArgument) {
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
					} else if(entry.getValue() instanceof ChatColorArgument) {
						try {
							Method getColor = getOBCClass("util.CraftChatMessage").getDeclaredMethod("getColor", getNMSClass("EnumChatFormat"));
							Object enumChatFormat = getNMSClass("ArgumentChatFormat").getDeclaredMethod("a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
							arr[count] = (ChatColor) getColor.invoke(null, enumChatFormat);
						} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
						}
					} else if(entry.getValue() instanceof EnchantmentArgument) {
						try {
							Constructor craftEnchant = getOBCClass("enchantments.CraftEnchantment").getConstructor(getNMSClass("Enchantment"));
							Object nmsEnchantment = getNMSClass("ArgumentEnchantment").getDeclaredMethod("a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
							arr[count] = (Enchantment) craftEnchant.newInstance(nmsEnchantment);
						} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
							e.printStackTrace();
						}
					} else if(entry.getValue() instanceof LocationArgument) {
						try {
							Object vec3D = getNMSClass("ArgumentVec3").getDeclaredMethod("a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
							double x = vec3D.getClass().getDeclaredField("x").getDouble(vec3D);
							double y = vec3D.getClass().getDeclaredField("y").getDouble(vec3D);
							double z = vec3D.getClass().getDeclaredField("z").getDouble(vec3D);
							World world = getCommandSenderWorld(sender);
							arr[count] = new Location(world, x, y, z);
						} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
							e.printStackTrace();
						}
					} else if(entry.getValue() instanceof EntityTypeArgument) {
						try {
							Object minecraftKey = getNMSClass("ArgumentEntitySummon").getDeclaredMethod("a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
							World world = getCommandSenderWorld(sender);
							Object craftWorld = getOBCClass("CraftWorld").cast(world);
							Object handle = craftWorld.getClass().getDeclaredMethod("getHandle").invoke(craftWorld);
							Object minecraftWorld = getNMSClass("World").cast(handle);
							
							Object entity = getNMSClass("EntityTypes").getDeclaredMethod("a", getNMSClass("World"), getNMSClass("MinecraftKey")).invoke(null, minecraftWorld, minecraftKey);
							Object entityCasted = getNMSClass("Entity").cast(entity);
							Object bukkitEntity = getNMSClass("Entity").getDeclaredMethod("getBukkitEntity").invoke(entityCasted);
							arr[count] = (EntityType) bukkitEntity.getClass().getDeclaredMethod("getType").invoke(bukkitEntity);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if(entry.getValue() instanceof PlayerArgument) {
						try {
							Collection<?> collectionOfPlayers = (Collection<?>) getNMSClass("ArgumentProfile").getDeclaredMethod("a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
							Object first = collectionOfPlayers.iterator().next();
							UUID uuid = (UUID) first.getClass().getDeclaredMethod("getId").invoke(first);
							arr[count] = Bukkit.getPlayer(uuid);
						} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
						}
					} else if(entry.getValue() instanceof EntitySelectorArgument) {
						try {
							/*
							 * single entity -> a
							 * many entities -> c (b if exception on empty)
							 * single player -> e
							 * many players  -> d (f if exception on empty)
							 */
							EntitySelectorArgument argument = (EntitySelectorArgument) entry.getValue();
							switch(argument.getEntitySelector()) {
								case MANY_ENTITIES:
								default:
									try {
										Collection<?> collectionOfEntities = (Collection<?>) getNMSClass("ArgumentEntity").getDeclaredMethod("c", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
										Collection<Entity> entities = new ArrayList<>();
										for(Object nmsEntity : collectionOfEntities) {
											entities.add((Entity) getNMSClass("Entity").getDeclaredMethod("getBukkitEntity").invoke(nmsEntity));
										}
										arr[count] = entities; 
									}
									catch(InvocationTargetException e) {
										if(e.getCause() instanceof CommandSyntaxException) {
											arr[count] = (Collection<Entity>) new ArrayList<Entity>(); 
										}
									}
									break;
								case MANY_PLAYERS:
									try {
										Collection<?> collectionOfPlayers = (Collection<?>) getNMSClass("ArgumentEntity").getDeclaredMethod("d", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
										Collection<Player> players = new ArrayList<>();
										for(Object nmsPlayer : collectionOfPlayers) {
											players.add((Player) getNMSClass("Entity").getDeclaredMethod("getBukkitEntity").invoke(nmsPlayer));
										}
										arr[count] = players;
									} catch(InvocationTargetException e) {
										if(e.getCause() instanceof CommandSyntaxException) {
											arr[count] = (Collection<Player>) new ArrayList<Player>(); 
										}
									}
									break;
								case ONE_ENTITY:
									try {
										Object entity = (Object) getNMSClass("ArgumentEntity").getDeclaredMethod("a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
										arr[count] = (Entity) getNMSClass("Entity").getDeclaredMethod("getBukkitEntity").invoke(entity);
									} catch(InvocationTargetException e) {
										if(e.getCause() instanceof CommandSyntaxException) {
											throw (CommandSyntaxException) e.getCause();
										}
									}
									break;
								case ONE_PLAYER:
									try {
										Object player = (Object) getNMSClass("ArgumentEntity").getDeclaredMethod("e", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
										arr[count] = (Player) getNMSClass("Entity").getDeclaredMethod("getBukkitEntity").invoke(player);
									} catch(InvocationTargetException e) {
										if(e.getCause() instanceof CommandSyntaxException) {
											throw (CommandSyntaxException) e.getCause();
										}
									}
									break;								
							}
						} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException e) {
							e.printStackTrace();
						}
					} 
				}
				
				count++;
			}
			
			//Run the code from executor and return
			executor.run(sender, arr);
			return 1;
		};
	}
	
	private Predicate generatePermissions(CommandPermission permissions) {
		return (cmdSender) -> {
			
        	//Generate CommandSender object
			CommandSender sender = null;
			try {
				sender = (CommandSender) cmdSender.getClass().getDeclaredMethod("getBukkitSender").invoke(cmdSender);
				//Object entity = cmdSender.getClass().getDeclaredMethod("f").invoke(cmdSender);
				//sender = (CommandSender) getNMSClass("Entity").getDeclaredMethod("getBukkitEntity").invoke(entity);
				
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}

        	if(permissions.getPermissions() != null) {
        		//If they don't have one of the required permissions, return false
        		for(String permission : permissions.getPermissions()) {
        			if(!sender.hasPermission(permission)) {
        				return false;
        			}
        		}
        		//Else return true
        		return true;
        	} else {
        		//If they're op
        		if(permissions.getPermissionNode().equals(PermissionNode.OP)) {
        			return sender.isOp();
        		} else {
        			//PermissionNode = NONE, implies true
        			return true;
        		}
        	}
        };
	}
	
	//Builds our NMS command using the given arguments for this method, then registers it
	protected void register(String commandName, CommandPermission permissions, String[] aliases, final LinkedHashMap<String, Argument> args, CommandExecutor executor) throws Exception {
		
		Command command = generateCommand(commandName, args, executor);
		Predicate permission = generatePermissions(permissions);
		
		/*
		 * The innermost argument needs to be connected to the executor.
		 * Then that argument needs to be connected to the previous argument
		 * etc.
		 * Then the first argument needs to be connected to the command name
		 * 
		 * CommandName -> Args1 -> Args2 -> ... -> ArgsN -> Executor
		 */
		
		if(args.isEmpty()) {
			//Link command name to the executor
	        LiteralCommandNode resultantNode = this.dispatcher.register((LiteralArgumentBuilder) reflectCommandDispatcherCommandName(commandName).requires(permission).executes(command));
	        
	        //Register aliases
	        for(String str : aliases) {
	        	this.dispatcher.register((LiteralArgumentBuilder) reflectCommandDispatcherCommandName(str).requires(permission).redirect(resultantNode));
	        }
		} else {
			//List of keys for reverse iteration
	        ArrayList<String> keys = new ArrayList<>(args.keySet());

	        //Link the last element to the executor
	        ArgumentBuilder inner;
	        Argument innerArg = args.get(keys.get(keys.size() - 1));
	        if(innerArg instanceof LiteralArgument) {
	        	LiteralArgumentBuilder literalBuilder = ((LiteralArgument) innerArg).getLiteralArgumentBuilder();
	        	inner = literalBuilder.executes(command);
	        } else {
	        	inner = reflectCommandDispatcherArgument(keys.get(keys.size() - 1), innerArg.getRawType()).executes(command);
	        }

	        //Link everything else up, except the first
	        ArgumentBuilder outer = inner;
	        for(int i = keys.size() - 2; i >= 0; i--) {
	        	Argument outerArg = args.get(keys.get(i));
	        	if(outerArg instanceof LiteralArgument) {
	        		LiteralArgumentBuilder literalBuilder = ((LiteralArgument) outerArg).getLiteralArgumentBuilder();
	        		outer = literalBuilder.then(outer);
	        	} else {
	        		outer = reflectCommandDispatcherArgument(keys.get(i), outerArg.getRawType()).then(outer);
	        	}
	        }        
	        
	        //Link command name to first argument and register        
	        LiteralCommandNode resultantNode = this.dispatcher.register((LiteralArgumentBuilder) reflectCommandDispatcherCommandName(commandName).requires(permission).then(outer));
	        
	        //Register aliases
	        for(String str : aliases) {
	        	this.dispatcher.register((LiteralArgumentBuilder) reflectCommandDispatcherCommandName(str).requires(permission).redirect(resultantNode));
	        }
		}
		
        
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
