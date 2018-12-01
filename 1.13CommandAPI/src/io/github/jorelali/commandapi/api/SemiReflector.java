package io.github.jorelali.commandapi.api;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.potion.PotionEffectType;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import io.github.jorelali.commandapi.api.CommandPermission.PermissionNode;
import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.ChatColorArgument;
import io.github.jorelali.commandapi.api.arguments.ChatComponentArgument;
import io.github.jorelali.commandapi.api.arguments.DynamicSuggestedStringArgument;
import io.github.jorelali.commandapi.api.arguments.DynamicSuggestedStringArgument.DynamicSuggestions;
import io.github.jorelali.commandapi.api.arguments.EnchantmentArgument;
import io.github.jorelali.commandapi.api.arguments.EntitySelectorArgument;
import io.github.jorelali.commandapi.api.arguments.EntityTypeArgument;
import io.github.jorelali.commandapi.api.arguments.FunctionArgument;
import io.github.jorelali.commandapi.api.arguments.ItemStackArgument;
import io.github.jorelali.commandapi.api.arguments.LiteralArgument;
import io.github.jorelali.commandapi.api.arguments.LocationArgument;
import io.github.jorelali.commandapi.api.arguments.ParticleArgument;
import io.github.jorelali.commandapi.api.arguments.PlayerArgument;
import io.github.jorelali.commandapi.api.arguments.PotionEffectArgument;
import io.github.jorelali.commandapi.api.arguments.SuggestedStringArgument;
import io.github.jorelali.commandapi.api.exceptions.CantFindPlayerException;
import io.github.jorelali.commandapi.api.exceptions.ConflictingPermissionsException;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

@SuppressWarnings({"rawtypes", "unchecked"})
/**
 * Class to access the main methods in NMS. The wrapper's
 * implementations occur here.
 */
public final class SemiReflector {
		
	private Map<String, CommandPermission> permissionsToFix;

	//Cache maps
	private static Map<String, Class<?>> NMSClasses;
	private static Map<String, Class<?>> OBCClasses;
	private static Map<ClassCache, Method> methods;
	private static Map<ClassCache, Field> fields;
	
	//OBC
	private String obcPackageName = null;
	
	//NMS variables
	private static String packageName = null;
	private CommandDispatcher dispatcher;
	private Object cDispatcher;
	
	protected void fixPermissions() {
		try {
			
			SimpleCommandMap map = (SimpleCommandMap) getMethod(getOBCClass("CraftServer"), "getCommandMap").invoke(Bukkit.getServer());
			Field f = getField(SimpleCommandMap.class, "knownCommands");
			Map<String, org.bukkit.command.Command> knownCommands = (Map<String, org.bukkit.command.Command>) f.get(map);
			
			Class vcw = getOBCClass("command.VanillaCommandWrapper");
			
			permissionsToFix.forEach((cmdName, perm) -> {
				
				if(perm.equals(CommandPermission.NONE)) {
					if(CommandAPIMain.getConfiguration().hasVerboseOutput()) {
						CommandAPIMain.getLog().info("Linking permission NONE -> " + cmdName);
					}
					if(vcw.isInstance(knownCommands.get(cmdName))) {
						knownCommands.get(cmdName).setPermission("");
					}
					if(vcw.isInstance(knownCommands.get("minecraft:" + cmdName))) {
						knownCommands.get(cmdName).setPermission("");
					}
				} else {
					if(perm.getPermission() != null) {
						if(CommandAPIMain.getConfiguration().hasVerboseOutput()) {
							CommandAPIMain.getLog().info("Linking permission " + perm.getPermission() + " -> " + cmdName);
						}
						if(vcw.isInstance(knownCommands.get(cmdName))) {
							knownCommands.get(cmdName).setPermission(perm.getPermission());
						}
						if(vcw.isInstance(knownCommands.get("minecraft:" + cmdName))) {
							knownCommands.get(cmdName).setPermission(perm.getPermission());
						}
					} else {
					}
				}
				
				
			});
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
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
			
			//Everything from this line will use getNMSClass(), so we initialize our cache here
			NMSClasses = new HashMap<>();
			OBCClasses = new HashMap<>();
			methods = new HashMap<>();
			fields = new HashMap<>();
			permissionsToFix = new HashMap<>();
			
			this.cDispatcher = getField(getNMSClass("MinecraftServer"), "commandDispatcher").get(server);
						
			this.dispatcher = (CommandDispatcher) getNMSClass("CommandDispatcher").getDeclaredMethod("a").invoke(cDispatcher); 

		} catch(Exception e) {
			e.printStackTrace(System.out);
		}
		
	}
	
	protected void unregister(String commandName, boolean force) {
		try {
			Field children = getField(CommandNode.class, "children");
			
			Map<String, CommandNode<?>> c = (Map<String, CommandNode<?>>) children.get(dispatcher.getRoot());
					
			if(force) {
				List<String> keysToRemove = new ArrayList<>();
				for(String key : c.keySet()) {
					if(key.contains(":")) {
						if(key.split(":")[1].equalsIgnoreCase(commandName)) {
							keysToRemove.add(key);
						}
					}
				}
				for(String key : keysToRemove) {
					c.remove(key);
				}
			}
			c.remove(commandName);
						
			if(CommandAPIMain.getConfiguration().hasVerboseOutput()) {
				CommandAPIMain.getLog().info("Unregistering /" + commandName);
			}
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
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
	
	private Command generateCommand(String commandName, LinkedHashMap<String, Argument> args, CustomCommandExecutor executor) throws CommandSyntaxException {
		
		//Generate our command from executor
		return (cmdCtx) -> {
			
			//Get the CommandSender via NMS
			CommandSender sender = null;
			
			try {
				sender = (CommandSender) getMethod(getNMSClass("CommandListenerWrapper"), "getBukkitSender").invoke(cmdCtx.getSource());//getNMSClass("CommandListenerWrapper").getDeclaredMethod("getBukkitSender").invoke(cmdCtx.getSource());
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| SecurityException e) {
				e.printStackTrace(System.out);
			}
			
			//Handle proxied command senders via /execute as [Proxy]
			try {
				Object proxyEntity = getMethod(getNMSClass("CommandListenerWrapper"), "f").invoke(cmdCtx.getSource());
					
				if(proxyEntity != null) {
					//Force proxyEntity to be a NMS Entity object
					Object bukkitProxyEntity = getMethod(getNMSClass("Entity"), "getBukkitEntity").invoke(getNMSClass("Entity").cast(proxyEntity));
					CommandSender proxy  = (CommandSender) bukkitProxyEntity;
					
					if(!proxy.equals(sender)) {
						Class proxyClass = getOBCClass("command.ProxiedNativeCommandSender"); 
						//ProxiedNativeCommandSender(CommandListenerWrapper orig, CommandSender caller, CommandSender callee)
						Constructor proxyConstructor = proxyClass.getConstructor(getNMSClass("CommandListenerWrapper"), CommandSender.class, CommandSender.class);
						Object proxyInstance = proxyConstructor.newInstance(cmdCtx.getSource(), sender, proxy);
						sender = (ProxiedCommandSender) proxyInstance;
					}
				}
				
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException e1) {
				e1.printStackTrace(System.out);
			}
						
			//Array for arguments for executor
			List<Object> argList = new ArrayList<>();
			
			//Populate array
			for(Entry<String, Argument> entry : args.entrySet()) {
				//If primitive (and simple), parse as normal
				if(entry.getValue().isSimple()) {
					argList.add(cmdCtx.getArgument(entry.getKey(), entry.getValue().getPrimitiveType()));
				} else {
					
					//Deal with those complex arguments
					if(entry.getValue() instanceof ItemStackArgument) {
						try {
							//Parse Bukkit ItemStack from NMS 
							Method asBukkitCopy = getMethod(getOBCClass("inventory.CraftItemStack"), "asBukkitCopy", getNMSClass("ItemStack"));
							Object argumentIS = getMethod(getNMSClass("ArgumentItemStack"), "a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
							Object nmsIS = getMethod(argumentIS.getClass(), "a", int.class, boolean.class).invoke(argumentIS, 1, false);
							argList.add((ItemStack) asBukkitCopy.invoke(null, nmsIS));
						} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace(System.out);
						}
					} else if(entry.getValue() instanceof ParticleArgument) {
						try {
							//Particle Bukkit Particle from NMS
							Method toBukkit = getMethod(getOBCClass("CraftParticle"), "toBukkit", getNMSClass("ParticleParam"));
							Object particleParam = getMethod(getNMSClass("ArgumentParticle"), "a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
							argList.add((Particle) toBukkit.invoke(null, particleParam));
						} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace(System.out);
						}
					} else if(entry.getValue() instanceof PotionEffectArgument) {
						try {
							Constructor craftPotionType = getOBCClass("potion.CraftPotionEffectType").getConstructor(getNMSClass("MobEffectList"));
							Object mobEffect = getMethod(getNMSClass("ArgumentMobEffect"), "a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
							argList.add((PotionEffectType) craftPotionType.newInstance(mobEffect));
						} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
							e.printStackTrace(System.out);
						}
					} else if(entry.getValue() instanceof ChatColorArgument) {
						try {
							Method getColor = getMethod(getOBCClass("util.CraftChatMessage"), "getColor", getNMSClass("EnumChatFormat"));
							Object enumChatFormat = getMethod(getNMSClass("ArgumentChatFormat"), "a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
							argList.add((ChatColor) getColor.invoke(null, enumChatFormat));
						} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace(System.out);
						}
					} else if(entry.getValue() instanceof EnchantmentArgument) {
						try {
							Constructor craftEnchant = getOBCClass("enchantments.CraftEnchantment").getConstructor(getNMSClass("Enchantment"));
							Object nmsEnchantment = getMethod(getNMSClass("ArgumentEnchantment"), "a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
							argList.add((Enchantment) craftEnchant.newInstance(nmsEnchantment));
						} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
							e.printStackTrace(System.out);
						}
					} else if(entry.getValue() instanceof LocationArgument) {
						try {
							Object vec3D = getMethod(getNMSClass("ArgumentVec3"), "a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
							double x = getField(vec3D.getClass(), "x").getDouble(vec3D);
							double y = getField(vec3D.getClass(), "y").getDouble(vec3D);
							double z = getField(vec3D.getClass(),"z").getDouble(vec3D);
							World world = getCommandSenderWorld(sender);
							argList.add(new Location(world, x, y, z));
						} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace(System.out);
						}
					} else if(entry.getValue() instanceof EntityTypeArgument) {
						try {
							Object minecraftKey = getMethod(getNMSClass("ArgumentEntitySummon"), "a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
							World world = getCommandSenderWorld(sender);
							Object craftWorld = getOBCClass("CraftWorld").cast(world);
							Object handle = getMethod(craftWorld.getClass(), "getHandle").invoke(craftWorld);
							Object minecraftWorld = getNMSClass("World").cast(handle);
							
							Object entity = getMethod(getNMSClass("EntityTypes"), "a", getNMSClass("World"), getNMSClass("MinecraftKey")).invoke(null, minecraftWorld, minecraftKey);
							Object entityCasted = getNMSClass("Entity").cast(entity);
							Object bukkitEntity = getMethod(getNMSClass("Entity"), "getBukkitEntity").invoke(entityCasted);
							argList.add((EntityType) bukkitEntity.getClass().getDeclaredMethod("getType").invoke(bukkitEntity));
						} catch (Exception e) {
							e.printStackTrace(System.out);
						}
					} else if(entry.getValue() instanceof PlayerArgument) {
						try {
							Collection<?> collectionOfPlayers = (Collection<?>) getMethod(getNMSClass("ArgumentProfile"), "a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
							Object first = collectionOfPlayers.iterator().next();
							UUID uuid = (UUID) getMethod(first.getClass(), "getId").invoke(first);
							Player target = Bukkit.getPlayer(uuid);
							if(target == null) {
								throw new CantFindPlayerException((String) getMethod(first.getClass(), "getName").invoke(first));
							}
							argList.add(target);
						} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace(System.out);
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
										Collection<?> collectionOfEntities = (Collection<?>) getMethod(getNMSClass("ArgumentEntity"), "c", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
										Collection<Entity> entities = new ArrayList<>();
										for(Object nmsEntity : collectionOfEntities) {
											entities.add((Entity) getMethod(getNMSClass("Entity"), "getBukkitEntity").invoke(nmsEntity));
										}
										argList.add(entities);
									}
									catch(InvocationTargetException e) {
										if(e.getCause() instanceof CommandSyntaxException) {
											argList.add((Collection<Entity>) new ArrayList<Entity>());
										}
									}
									break;
								case MANY_PLAYERS:
									try {
										Collection<?> collectionOfPlayers = (Collection<?>) getMethod(getNMSClass("ArgumentEntity"), "d", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
										Collection<Player> players = new ArrayList<>();
										for(Object nmsPlayer : collectionOfPlayers) {
											players.add((Player) getMethod(getNMSClass("Entity"), "getBukkitEntity").invoke(nmsPlayer));
										}
										argList.add(players);
									} catch(InvocationTargetException e) {
										if(e.getCause() instanceof CommandSyntaxException) {
											argList.add((Collection<Player>) new ArrayList<Player>());
										}
									}
									break;
								case ONE_ENTITY:
									try {
										Object entity = (Object) getMethod(getNMSClass("ArgumentEntity"), "a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
										argList.add((Entity) getMethod(getNMSClass("Entity"), "getBukkitEntity").invoke(entity));
									} catch(InvocationTargetException e) {
										if(e.getCause() instanceof CommandSyntaxException) {
											throw (CommandSyntaxException) e.getCause();
										}
									}
									break;
								case ONE_PLAYER:
									try {
										Object player = (Object) getMethod(getNMSClass("ArgumentEntity"), "e", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
										argList.add((Player) getMethod(getNMSClass("Entity"), "getBukkitEntity").invoke(player));
									} catch(InvocationTargetException e) {
										if(e.getCause() instanceof CommandSyntaxException) {
											throw (CommandSyntaxException) e.getCause();
										}
									}
									break;								
							}
						} catch (SecurityException | IllegalAccessException | IllegalArgumentException e) {
							e.printStackTrace(System.out);
						}
					} else if(entry.getValue() instanceof ChatComponentArgument) {
						try {
							//Invokes public String IChatBaseComponent.ChatSerializer.a(IChatComponent c);
							Class chatSerializer = getNMSClass("IChatBaseComponent$ChatSerializer");
							Method m = getMethod(chatSerializer, "a", getNMSClass("IChatBaseComponent"));
							Object iChatBaseComponent = getMethod(getNMSClass("ArgumentChatComponent"), "a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
							Object resultantString = m.invoke(null, iChatBaseComponent);
							//Convert to spigot thing
							BaseComponent[] components = ComponentSerializer.parse((String) resultantString);
							argList.add((BaseComponent[]) components);
						} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace(System.out);
						}
					} else if(entry.getValue() instanceof FunctionArgument) {
						try {				 
							Collection<?> customFuncList = (Collection<?>) getMethod(getNMSClass("ArgumentTag"), "a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
							
							//Adds support for Tags
							FunctionWrapper[] result = new FunctionWrapper[customFuncList.size()];
							
							//Get first CustomFunction from the list.
							Object clw = cmdCtx.getSource();
							//Object customFunction = customFuncList.iterator().next();
							
							//Retrieve CustomFunctionData from main server
							Object minecraftServer = getMethod(getNMSClass("CommandListenerWrapper"), "getServer").invoke(clw);
							Object customFunctionData = getMethod(getNMSClass("MinecraftServer"), "getFunctionData").invoke(minecraftServer);
							
							//Method which invokes the function
							Method invoker = getMethod(getNMSClass("CustomFunctionData"), "a", getNMSClass("CustomFunction"), getNMSClass("CommandListenerWrapper"));
							
							//Get right correct CommandListenerWrapper to execute this command
							Object clwA = getMethod(getNMSClass("CommandListenerWrapper"), "a").invoke(clw);
							Object commandListenerWrapper = getMethod(getNMSClass("CommandListenerWrapper"), "b", int.class).invoke(clwA, 2);
										
							int count = 0;
							Iterator<?> it = customFuncList.iterator();
							while(it.hasNext()) {
								Object customFunction = it.next();
								
								//Parse the name of the MinecraftKey
								Object minecraftKey = getMethod(getNMSClass("CustomFunction"), "a").invoke(customFunction);
								String key = (String) getMethod(getNMSClass("MinecraftKey"), "toString").invoke(minecraftKey);
								
								//Create wrapper and implement a function to map Bukkit Entities to NMS CommandListenerWrappers
								FunctionWrapper wrapper = new FunctionWrapper(key, invoker, customFunctionData, customFunction, commandListenerWrapper, e -> {
									//Mapper function
									try {
										Object nmsEntity = getMethod(getOBCClass("entity.CraftEntity"), "getHandle").invoke(e);
										return getMethod(getNMSClass("CommandListenerWrapper"), "a", getNMSClass("Entity")).invoke(clw, nmsEntity);
									} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
											| ClassNotFoundException e1) {
										e1.printStackTrace();
									}
									
									return null;
								});
								result[count] = wrapper;
								count++;
							}
							
							//Add and finish.
							argList.add(result);

						} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace(System.out);
						}
					}
				}
			}
			
			//Parse executor type
			if(executor.hasResults()) {
				//Run resulting executor
				try {
					return executor.getResultingEx().run(sender, argList.toArray(new Object[argList.size()]));
				} catch (CommandSyntaxException e) {
					throw e;
				} catch (Exception e) {
					e.printStackTrace(System.out);
					return 0;
				}
			} else {
				//Run normal executor
				try {
					executor.getEx().run(sender, argList.toArray(new Object[argList.size()]));
					return 0;
				} catch (CommandSyntaxException e) {
					throw e;
				} catch (Exception e) {
					e.printStackTrace(System.out);
					return 0;
				}
			}
		};
	}
	
	private Predicate generatePermissions(String commandName, CommandPermission permission) {
		
		//First check for the situation where a user might register the same command (or subcommand)
		//under a different permission. Since this is not permitted, we must prevent this.
		if(permissionsToFix.containsKey(commandName.toLowerCase())) {
			if(!permissionsToFix.get(commandName.toLowerCase()).equals(permission)) {
				throw new ConflictingPermissionsException(commandName, permissionsToFix.get(commandName.toLowerCase()), permission);
			}
		} else {
			//add to a list to fix this
			permissionsToFix.put(commandName.toLowerCase(), permission);
		}
		
		//Register it to the Bukkit permissions registry
		if(permission.getPermission() != null) {
			try {
				Bukkit.getPluginManager().addPermission(new Permission(permission.getPermission()));
			} catch(IllegalArgumentException e) {}
		}
		
		return (cmdSender) -> {
			
        	//Generate CommandSender object
			CommandSender sender = null;
			try {
				sender = (CommandSender) getMethod(cmdSender.getClass(), "getBukkitSender").invoke(cmdSender);
				
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| SecurityException e) {
				e.printStackTrace(System.out);
			}
			
        	if(permission.getPermission() != null) {
        		return sender.hasPermission(permission.getPermission());
        	} else {
        		//If they're op
        		if(permission.getPermissionNode().equals(PermissionNode.OP)) {
        			return sender.isOp();
        		} else {
        			//PermissionNode = NONE, implies true
        			return true;
        		}
        	}
        };
	}
	
	private SuggestionProvider getFunctions() {
		try {
			return (SuggestionProvider) getField(getNMSClass("CommandFunction"), "a").get(null);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Builds our NMS command using the given arguments for this method, then registers it
	protected void register(String commandName, CommandPermission permissions, String[] aliases, final LinkedHashMap<String, Argument> args, CustomCommandExecutor executor) throws Exception {
		if(CommandAPIMain.getConfiguration().hasVerboseOutput()) {
			//Create a list of argument names
			StringBuilder builder = new StringBuilder();
			args.values().forEach(arg -> builder.append("<").append(arg.getClass().getSimpleName()).append("> "));
			CommandAPIMain.getLog().info("Registering command /" + commandName + " " + builder.toString());
		}
		
		Command command = generateCommand(commandName, args, executor);
		Predicate permission = generatePermissions(commandName, permissions);
		//Predicate permission = (a) -> {return true;};
		
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
	        LiteralCommandNode resultantNode = this.dispatcher.register((LiteralArgumentBuilder) getLiteralArgumentBuilder(commandName).requires(permission).executes(command));
	        
	        //Register aliases
	        for(String str : aliases) {
	        	this.dispatcher.register((LiteralArgumentBuilder) getLiteralArgumentBuilder(str).requires(permission).redirect(resultantNode));
	        }
		} else {
			//List of keys for reverse iteration
	        ArrayList<String> keys = new ArrayList<>(args.keySet());

	        //Link the last element to the executor
	        ArgumentBuilder inner;
	        Argument innerArg = args.get(keys.get(keys.size() - 1));
	        if(innerArg instanceof LiteralArgument) {
	        	String str = ((LiteralArgument) innerArg).getLiteral();
	        	inner = getLiteralArgumentBuilder(str).executes(command);
	        } else {
	        	if(innerArg instanceof SuggestedStringArgument) {
	        		inner = getRequiredArgumentBuilder(keys.get(keys.size() - 1), innerArg.getRawType(), ((SuggestedStringArgument) innerArg).getSuggestions()).executes(command);
        		} else if(innerArg instanceof FunctionArgument) {
        			inner = getRequiredArgumentBuilder(keys.get(keys.size() - 1), innerArg.getRawType(), getFunctions()).executes(command);
				} else if(innerArg instanceof DynamicSuggestedStringArgument) {
        			inner = getRequiredArgumentBuilder(keys.get(keys.size() - 1), innerArg.getRawType(), ((DynamicSuggestedStringArgument) innerArg).getDynamicSuggestions()).executes(command);
				} else {
					inner = getRequiredArgumentBuilder(keys.get(keys.size() - 1), innerArg.getRawType()).executes(command);
				}
	        }

	        //Link everything else up, except the first
	        ArgumentBuilder outer = inner;
	        for(int i = keys.size() - 2; i >= 0; i--) {
	        	Argument outerArg = args.get(keys.get(i));
	        	if(outerArg instanceof LiteralArgument) {
	        		String str = ((LiteralArgument) outerArg).getLiteral();
	        		outer = getLiteralArgumentBuilder(str).then(outer);
	        	} else {
	        		if(outerArg instanceof SuggestedStringArgument) {
	        			outer = getRequiredArgumentBuilder(keys.get(i), outerArg.getRawType(), ((SuggestedStringArgument) outerArg).getSuggestions()).then(outer);
	        		} else if(outerArg instanceof FunctionArgument) {
	        			outer = getRequiredArgumentBuilder(keys.get(i), outerArg.getRawType(), getFunctions()).then(outer);
	        		} else if(innerArg instanceof DynamicSuggestedStringArgument) {
	        			outer = getRequiredArgumentBuilder(keys.get(i), outerArg.getRawType(), ((DynamicSuggestedStringArgument) outerArg).getDynamicSuggestions()).then(outer);
					} else {
	        			outer = getRequiredArgumentBuilder(keys.get(i), outerArg.getRawType()).then(outer);
	        		}
	        	}
	        }        
	        
	        //Link command name to first argument and register        
	        LiteralCommandNode resultantNode = this.dispatcher.register((LiteralArgumentBuilder) getLiteralArgumentBuilder(commandName).requires(permission).then(outer));
	        
	        //Register aliases
	        for(String str : aliases) {
	        	this.dispatcher.register((LiteralArgumentBuilder) getLiteralArgumentBuilder(str).requires(permission).redirect(resultantNode));
	        }
		}
		
        
		//Produce the commandDispatch.json file for debug purposes
		if(CommandAPIMain.getConfiguration().willCreateDispatcherFile()) {
			File file = new File("command_registration.json");
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace(System.out);
			}
			
			getMethod(getNMSClass("CommandDispatcher"), "a", File.class).invoke(this.cDispatcher, file);
		}
	}	

	//Registers a LiteralArgumentBuilder for a command name
	private LiteralArgumentBuilder<?> getLiteralArgumentBuilder(String commandName) {
		return LiteralArgumentBuilder.literal(commandName);
	}
	
	//Registers a RequiredArgumentBuilder for an argument
	private <T> RequiredArgumentBuilder<?, T> getRequiredArgumentBuilder(String argumentName, com.mojang.brigadier.arguments.ArgumentType<T> type){
		return RequiredArgumentBuilder.argument(argumentName, type);
	}
	
	
	private <T> RequiredArgumentBuilder<?, T> getRequiredArgumentBuilder(String argumentName, com.mojang.brigadier.arguments.ArgumentType<T> type, DynamicSuggestions dynamicSuggestions) {
		SuggestionProvider provider = (context, builder) -> {
			//NMS' ICompletionProvider.a()
			String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);
			String[] suggestions = dynamicSuggestions.getSuggestions();
			for (int i = 0; i < suggestions.length; i++) {
				String str = suggestions[i];
				if (str.toLowerCase(Locale.ROOT).startsWith(remaining)) {
					builder.suggest(str);
				}
			}

			return builder.buildFuture();
		};
		return RequiredArgumentBuilder.argument(argumentName, type).suggests(provider);
	}
	
	//Registers a RequiredArgumentBuilder for an argument
	private <T> RequiredArgumentBuilder<?, T> getRequiredArgumentBuilder(String argumentName, com.mojang.brigadier.arguments.ArgumentType<T> type, String[] suggestions){
		SuggestionProvider provider = (context, builder) -> {
			//NMS' ICompletionProvider.a()
			String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);

			for (int i = 0; i < suggestions.length; i++) {
				String str = suggestions[i];
				if (str.toLowerCase(Locale.ROOT).startsWith(remaining)) {
					builder.suggest(str);
				}
			}

			return builder.buildFuture();
		};
		return RequiredArgumentBuilder.argument(argumentName, type).suggests(provider);
	}
	
	//Registers a RequiredArgumentBuilder for an argument
	private <T> RequiredArgumentBuilder<?, T> getRequiredArgumentBuilder(String argumentName, com.mojang.brigadier.arguments.ArgumentType<T> type, SuggestionProvider provider){
		return RequiredArgumentBuilder.argument(argumentName, type).suggests(provider);
	}
		
	/** Retrieves a net.minecraft.server class by using the dynamic package from
	 * the dedicated server */
	private Class<?> getNMSClass(final String className) {
		if(NMSClasses.containsKey(className)) {
			return NMSClasses.get(className);
		} else {
			try {
				return (Class.forName(SemiReflector.packageName + "." + className));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	private Method getMethod(Class<?> clazz, String name) {
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
	
	private Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
		ClassCache key = new ClassCache(clazz, name);
		if(methods.containsKey(key)) {
			return methods.get(key);
		} else {
			Method result = null;
			try {
				result = clazz.getDeclaredMethod(name, parameterTypes);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			methods.put(key, result);
			return result;
		}
	}
	
	private Field getField(Class<?> clazz, String name) {
		ClassCache key = new ClassCache(clazz, name);
		if(fields.containsKey(key)) {
			return fields.get(key);
		} else {
			Field result = null;
			try {
				result = clazz.getDeclaredField(name);
			} catch (NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
			result.setAccessible(true);
			fields.put(key, result);
			return result;
		}
	}
	
	/** Retrieves a craftbukkit class */
	private Class<?> getOBCClass(final String className) throws ClassNotFoundException {
		return OBCClasses.computeIfAbsent(className, key -> {
			try {
				return (Class.forName(obcPackageName + "." + className));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		});
	}

	/**
	 * Gets an instance of an NMS argument. Used in Arguments classes
	 */
	public static ArgumentType<?> getNMSArgumentInstance(String nmsClassName) {
		return getNMSArgumentInstance(nmsClassName, "a");		
	}
	
	public static ArgumentType<?> getNMSArgumentInstance(String nmsClassName, String methodName) {
		//Get class
		Class<?> clazz;
		
		if(NMSClasses.containsKey(nmsClassName)) {
			clazz = NMSClasses.get(nmsClassName);
		} else {
			try {
				clazz = (Class.forName(SemiReflector.packageName + "." + nmsClassName));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				clazz = null;
			}
		}
		
		//Create key
		ClassCache key = new ClassCache(clazz, methodName);
		
		//Check for key and invoke
		if(methods.containsKey(key)) {
			try {
				return (ArgumentType<?>) methods.get(key).invoke(null);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			}
		} else {
			//Get method and invoke
			Method result = null;
			try {
				result = clazz.getDeclaredMethod(methodName);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			methods.put(key, result);
			try {
				return (ArgumentType<?>) result.invoke(null);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;		
	}
	
}
