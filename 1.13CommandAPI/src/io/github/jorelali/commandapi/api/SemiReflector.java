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
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
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
import org.bukkit.loot.LootTable;
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
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.ChatColorArgument;
import io.github.jorelali.commandapi.api.arguments.ChatComponentArgument;
import io.github.jorelali.commandapi.api.arguments.CustomArgument;
import io.github.jorelali.commandapi.api.arguments.CustomArgument.CustomArgumentException;
import io.github.jorelali.commandapi.api.arguments.CustomArgument.MessageBuilder;
import io.github.jorelali.commandapi.api.arguments.CustomProvidedArgument;
import io.github.jorelali.commandapi.api.arguments.CustomProvidedArgument.SuggestionProviders;
import io.github.jorelali.commandapi.api.arguments.DynamicSuggestedStringArgument;
import io.github.jorelali.commandapi.api.arguments.EnchantmentArgument;
import io.github.jorelali.commandapi.api.arguments.EntitySelectorArgument;
import io.github.jorelali.commandapi.api.arguments.EntityTypeArgument;
import io.github.jorelali.commandapi.api.arguments.FunctionArgument;
import io.github.jorelali.commandapi.api.arguments.ItemStackArgument;
import io.github.jorelali.commandapi.api.arguments.LiteralArgument;
import io.github.jorelali.commandapi.api.arguments.LocationArgument;
import io.github.jorelali.commandapi.api.arguments.LocationArgument.LocationType;
import io.github.jorelali.commandapi.api.arguments.LootTableArgument;
import io.github.jorelali.commandapi.api.arguments.OverrideableSuggestions;
import io.github.jorelali.commandapi.api.arguments.ParticleArgument;
import io.github.jorelali.commandapi.api.arguments.PlayerArgument;
import io.github.jorelali.commandapi.api.arguments.PotionEffectArgument;
import io.github.jorelali.commandapi.api.arguments.StringArgument;
import io.github.jorelali.commandapi.api.arguments.SuggestedStringArgument;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

@SuppressWarnings({"rawtypes", "unchecked", "deprecation"})
/**
 * Class to access the main methods in NMS. The wrapper's
 * implementations occur here.
 */
public final class SemiReflector {
		
	private TreeMap<String, CommandPermission> permissionsToFix;

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
			permissionsToFix = new TreeMap<>();
			
			this.cDispatcher = getField(getNMSClass("MinecraftServer"), "commandDispatcher").get(server);
						
			this.dispatcher = (CommandDispatcher) getNMSClass("CommandDispatcher").getDeclaredMethod("a").invoke(cDispatcher); 

		} catch(Exception e) {
			e.printStackTrace(System.out);
		}
		
	}
	
	//Unregister a command
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
				CommandAPIMain.getLog().info("Unregistering command /" + commandName);
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
	
	//Used in the register() method to generate the command to actually be registered
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
				//getMethod(getNMSClass("CommandListenerWrapper"), "f").invoke(cmdCtx.getSource()); -> getMethod(getNMSClass("CommandListenerWrapper"), "getEntity").invoke(cmdCtx.getSource());
				//Both of these return field CommandListenerWrapper.k
				Object proxyEntity = getField(getNMSClass("CommandListenerWrapper"), "k").get(cmdCtx.getSource());
					
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
						
						LocationType locationType = ((LocationArgument) entry.getValue()).getLocationType();
						switch(locationType) {
							case BLOCK_POSITION:
								try {
									Object blockPosition = getMethod(getNMSClass("ArgumentPosition"), "a", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
									int x = (int) getMethod(getNMSClass("BaseBlockPosition"), "getX").invoke(blockPosition);
									int y = (int) getMethod(getNMSClass("BaseBlockPosition"), "getY").invoke(blockPosition);
									int z = (int) getMethod(getNMSClass("BaseBlockPosition"), "getZ").invoke(blockPosition);
									World world = getCommandSenderWorld(sender);
									argList.add(new Location(world, x, y, z));
								} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
									e.printStackTrace(System.out);
								}
								break;
							case PRECISE_POSITION:
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
								break;
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
								throw ((SimpleCommandExceptionType) getField(getNMSClass("ArgumentProfile"), "a").get(null)).create();
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
							Method m = getMethod(getNMSClass("IChatBaseComponent$ChatSerializer"), "a", getNMSClass("IChatBaseComponent"));
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
					} else if(entry.getValue() instanceof CustomArgument) {
						CustomArgument arg = (CustomArgument) entry.getValue();
						String result = (String) cmdCtx.getArgument(entry.getKey(), String.class);
						try {
							argList.add(arg.getParser().apply(result));
						} catch(CustomArgumentException e) {
							throw e.toCommandSyntax(result, cmdCtx);
						} catch(Exception e) {
							String errorMsg = new MessageBuilder("Error in executing command ")
									.appendFullInput().append(" - ").appendArgInput().appendHere().toString()
									.replace("%input%", result).replace("%finput%", cmdCtx.getInput());
							throw new SimpleCommandExceptionType(() -> {return errorMsg;}).create();
						}
					} else if(entry.getValue() instanceof LootTableArgument) {
						try {
							//Get namespaced key
							Object minecraftKey = getMethod(getNMSClass("ArgumentMinecraftKeyRegistered"), "c", CommandContext.class, String.class).invoke(null, cmdCtx, entry.getKey());
							String namespace = (String) getMethod(getNMSClass("MinecraftKey"), "b").invoke(minecraftKey);
							String key = (String) getMethod(getNMSClass("MinecraftKey"), "getKey").invoke(minecraftKey);
							NamespacedKey nameSpacedKey = new NamespacedKey(namespace, key);
							
							//Get loot table
							Object NMSServer = getMethod(getNMSClass("CommandListenerWrapper"), "getServer").invoke(cmdCtx.getSource());
							Object lootTableRegistry = getMethod(getNMSClass("MinecraftServer"), "getLootTableRegistry").invoke(NMSServer);
							Object nmsLootTable = getMethod(getNMSClass("LootTableRegistry"), "getLootTable").invoke(lootTableRegistry, minecraftKey);

							LootTable lootTable = (LootTable) getOBCClass("CraftLootTable").getConstructor(NamespacedKey.class, getNMSClass("LootTable")).newInstance(nameSpacedKey, nmsLootTable);
							
							argList.add(lootTable);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | NoSuchMethodException | SecurityException | ClassNotFoundException e1) {
							e1.printStackTrace(System.out);
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
					return 1;
				} catch (CommandSyntaxException e) {
					throw e;
				} catch (Exception e) {
					e.printStackTrace(System.out);
					return 0;
				}
			}
		};
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// SECTION: Permissions                                                                             //
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Predicate generatePermissions(String commandName, CommandPermission permission) {
		
		//If we've already registered a permission, set it to the "parent" permission.
		/*
		 * This permission generation setup ONLY works iff:
		 * - You register the parent permission node FIRST.
		 * - Example:
		 * 	 /mycmd			- permission node my.perm
		 *   /mycmd <arg>	- permission node my.perm.other
		 *   
		 * the my.perm.other permission node is revoked for the COMMAND REGISTRATION, however:
		 * - The permission node IS REGISTERED.
		 * - The permission node, if used for an argument (as in this case), will be used for
		 * 	 suggestions for said argument
		 */
		if(permissionsToFix.containsKey(commandName.toLowerCase())) {
			if(!permissionsToFix.get(commandName.toLowerCase()).equals(permission)) {
				permission = permissionsToFix.get(commandName.toLowerCase());
			}
//			if(!permissionsToFix.get(commandName.toLowerCase()).equals(permission)) {
//				throw new ConflictingPermissionsException(commandName, permissionsToFix.get(commandName.toLowerCase()), permission);
//			}
		} else {
			//Add permission to a list to fix conflicts with minecraft:permissions
			permissionsToFix.put(commandName.toLowerCase(), permission);
		}
		
		CommandPermission finalPermission = permission;
		
		//Register it to the Bukkit permissions registry
		if(finalPermission.getPermission() != null) {
			try {
				Bukkit.getPluginManager().addPermission(new Permission(finalPermission.getPermission()));
			} catch(IllegalArgumentException e) {}
		}
		
		return (clw) -> {
			return permissionCheck(getCommandSender(clw), finalPermission);
        };
	}
	
	//Checks if a CommandSender has permission permission from CommandPermission permission
	private boolean permissionCheck(CommandSender sender, CommandPermission permission) {
		if(permission.equals(CommandPermission.NONE)) {
			return true;
		} else if(permission.equals(CommandPermission.OP)) {
			return sender.isOp();
		} else {
			return sender.hasPermission(permission.getPermission());
		}
	}
	
	protected void fixPermissions() {
		try {
			
			SimpleCommandMap map = (SimpleCommandMap) getMethod(getOBCClass("CraftServer"), "getCommandMap").invoke(Bukkit.getServer());
			Field f = getField(SimpleCommandMap.class, "knownCommands");
			Map<String, org.bukkit.command.Command> knownCommands = (Map<String, org.bukkit.command.Command>) f.get(map);
			
			Class vcw = getOBCClass("command.VanillaCommandWrapper");
			
			CommandAPIMain.getLog().info("Linking permissions to commands:");
			
			permissionsToFix.forEach((cmdName, perm) -> {
				
				if(perm.equals(CommandPermission.NONE)) {
					if(CommandAPIMain.getConfiguration().hasVerboseOutput()) {
						CommandAPIMain.getLog().info("NONE -> /" + cmdName);
					}
					/*
					 * org.bukkit.command.Command.testPermissionSilent() ->
					 * if ((permission == null) || (permission.length() == 0)) {
				     *     return true;
				     * }
					 */
					if(vcw.isInstance(knownCommands.get(cmdName))) {
						knownCommands.get(cmdName).setPermission("");
					}
					if(vcw.isInstance(knownCommands.get("minecraft:" + cmdName))) {
						knownCommands.get(cmdName).setPermission("");
					}
				} else {
					if(perm.getPermission() != null) {
						if(CommandAPIMain.getConfiguration().hasVerboseOutput()) {
							CommandAPIMain.getLog().info(perm.getPermission() + " -> /" + cmdName);
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
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// SECTION: Registration                                                                            //
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	
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

		LiteralCommandNode resultantNode;
		if(args.isEmpty()) {
			//Link command name to the executor
	        resultantNode = this.dispatcher.register((LiteralArgumentBuilder) getLiteralArgumentBuilder(commandName).requires(permission).executes(command));

		} else {
			
			//Replace SSA with StringArg
			for(Entry<String, Argument> entry : ((LinkedHashMap<String, Argument>) args.clone()).entrySet()) { 
				if(entry.getValue() instanceof SuggestedStringArgument) {
					CommandAPIMain.getLog().warning("Command /" + commandName + " uses a SuggestedStringArgument. These are deprecated as of 1.9. Consider using StringArgument().overrideSuggestions(...)");
					SuggestedStringArgument ssa = (SuggestedStringArgument) entry.getValue();
					args.put(entry.getKey(), new StringArgument().overrideSuggestions(ssa.getSuggestions()));
				}
			}
			
			//List of keys for reverse iteration
	        ArrayList<String> keys = new ArrayList<>(args.keySet());

	        //Link the last element to the executor
	        ArgumentBuilder inner;
	        //New scope used here to prevent innerArg accidentally being used below
	        {
		        Argument innerArg = args.get(keys.get(keys.size() - 1));
		        if(innerArg instanceof LiteralArgument) {
		        	String str = ((LiteralArgument) innerArg).getLiteral();
		        	inner = getLiteralArgumentBuilderArgument(str, innerArg.getArgumentPermission()).executes(command);
		        } else {
		        	if(innerArg instanceof CustomProvidedArgument) {
	        			inner = getRequiredArgumentBuilder(keys.get(keys.size() - 1), innerArg.getRawType(), innerArg.getArgumentPermission(), ((CustomProvidedArgument) innerArg).getSuggestionProvider()).executes(command);
					} else if(innerArg instanceof DynamicSuggestedStringArgument) {
	        			inner = getRequiredArgumentBuilder(keys.get(keys.size() - 1), (DynamicSuggestedStringArgument) innerArg, innerArg.getArgumentPermission()).executes(command);
					} else {
						if(innerArg instanceof OverrideableSuggestions) {
							inner = getRequiredArgumentBuilderWithOverride(keys.get(keys.size() - 1), innerArg, innerArg.getArgumentPermission()).executes(command);
						} else {
							inner = getRequiredArgumentBuilder(keys.get(keys.size() - 1), innerArg.getRawType(), innerArg.getArgumentPermission()).executes(command);
						}
					}
		        }
	        }

	        //Link everything else up, except the first
	        ArgumentBuilder outer = inner;
	        for(int i = keys.size() - 2; i >= 0; i--) {
	        	Argument outerArg = args.get(keys.get(i));
	        	if(outerArg instanceof LiteralArgument) {
	        		String str = ((LiteralArgument) outerArg).getLiteral();
	        		outer = getLiteralArgumentBuilderArgument(str, outerArg.getArgumentPermission()).then(outer);
	        	} else {
	        		if(outerArg instanceof CustomProvidedArgument) {
	        			outer = getRequiredArgumentBuilder(keys.get(i), outerArg.getRawType(), outerArg.getArgumentPermission(), ((CustomProvidedArgument) outerArg).getSuggestionProvider()).then(outer);
	        		} else if(outerArg instanceof DynamicSuggestedStringArgument) {
	        			outer = getRequiredArgumentBuilder(keys.get(i), (DynamicSuggestedStringArgument) outerArg, outerArg.getArgumentPermission()).then(outer);
					}  else {
						if(outerArg instanceof OverrideableSuggestions) {
							outer = getRequiredArgumentBuilderWithOverride(keys.get(i), outerArg, outerArg.getArgumentPermission()).then(outer);
						} else {
							outer = getRequiredArgumentBuilder(keys.get(i), outerArg.getRawType(), outerArg.getArgumentPermission()).then(outer);
						}
	        		}
	        	}
	        }        
	        
	        //Link command name to first argument and register        
			resultantNode = this.dispatcher.register((LiteralArgumentBuilder) getLiteralArgumentBuilder(commandName).requires(permission).then(outer));

		}

		//Register aliases
	    for(String str : aliases) {
	    	if(CommandAPIMain.getConfiguration().hasVerboseOutput()) {
				CommandAPIMain.getLog().info("Registering alias /" + str + " -> " + resultantNode.getName());
			}
	      	this.dispatcher.register((LiteralArgumentBuilder) getLiteralArgumentBuilder(str).requires(permission).redirect(resultantNode));
	    }
        
		//Try moving all aliases down here, regardless of whether they have 1 or less arguments
		//Think about it - tp command redirects to teleport
		//not tp <args> redirect to teleport <args>
		//If args are redirected, this could override original redirects anyway.
		/*
		for Str str : aliases:
			register str (redirect to) -> whatever we're about to register right now?
		*/
		
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
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// SECTION: SuggestionProviders                                                                     //
	//////////////////////////////////////////////////////////////////////////////////////////////////////

	private CommandSender getCommandSender(Object clw) {
		CommandSender sender = null;
		try {
			sender = (CommandSender) getMethod(getNMSClass("CommandListenerWrapper"), "getBukkitSender").invoke(clw);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
			e.printStackTrace(System.out);
		}
		return sender;
	}
	
	//NMS ICompletionProvider.a()
	private CompletableFuture<Suggestions> getSuggestionsBuilder(SuggestionsBuilder builder, String[] array) {
		String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);
		for (int i = 0; i < array.length; i++) {
			String str = array[i];
			if (str.toLowerCase(Locale.ROOT).startsWith(remaining)) {
				builder.suggest(str);
			}
		}
		return builder.buildFuture();
	}
	
	private SuggestionProvider getSuggestionProvider(SuggestionProviders provider) {
		switch(provider) {
			case FUNCTION:
				return (context, builder) -> {
					try {
						//CommandFunction.a:
						Object NMSServer = getMethod(getNMSClass("CommandListenerWrapper"), "getServer").invoke(context.getSource());
						Object FunctionData = getMethod(getNMSClass("MinecraftServer"), "getFunctionData").invoke(NMSServer);
						Object Tags = getMethod(getNMSClass("CustomFunctionData"), "g").invoke(FunctionData);
						Object MinecraftKeys = getMethod(getNMSClass("Tags"), "a").invoke(Tags);
						getMethod(getNMSClass("ICompletionProvider"), "a", Iterable.class, SuggestionsBuilder.class, String.class).invoke(null, MinecraftKeys, builder, "#");
						Map m = (Map) getMethod(getNMSClass("CustomFunctionData"), "c").invoke(FunctionData);
						return (CompletableFuture<Suggestions>) getMethod(getNMSClass("ICompletionProvider"), "a", Iterable.class, SuggestionsBuilder.class).invoke(null, m.keySet(), builder);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
						return Suggestions.empty();
					}
				};
			case RECIPES:
				try {
					return (SuggestionProvider<?>) getField(getNMSClass("CompletionProviders"), "b").get(null);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
					return (context, builder) -> Suggestions.empty();
				}
			case SOUNDS:
				try {
					return (SuggestionProvider<?>) getField(getNMSClass("CompletionProviders"), "c").get(null);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
					return (context, builder) -> Suggestions.empty();
				}
			case ADVANCEMENTS:
				try {
					return (SuggestionProvider<?>) getField(getNMSClass("CommandAdvancement"), "a").get(null);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
					return (context, builder) -> Suggestions.empty();
				}
			case LOOT_TABLES:
				return (context, builder) -> {
					try {
						Object NMSServer = getMethod(getNMSClass("CommandListenerWrapper"), "getServer").invoke(context.getSource());
						Object lootTableRegistry = getMethod(getNMSClass("MinecraftServer"), "getLootTableRegistry").invoke(NMSServer);
						Map<?, ?> lootTables = (Map<?, ?>) getField(getNMSClass("LootTableRegistry"), "e").get(lootTableRegistry);
						return (CompletableFuture<Suggestions>) getMethod(getNMSClass("ICompletionProvider"), "a", Iterable.class, SuggestionsBuilder.class).invoke(null, lootTables.keySet(), builder);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
						return Suggestions.empty();
					}
				};		
			default:
				return (context, builder) -> Suggestions.empty();
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// SECTION: Argument Builders                                                                       //
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//Gets a LiteralArgumentBuilder for a command name
	private LiteralArgumentBuilder<?> getLiteralArgumentBuilder(String commandName) {
		return LiteralArgumentBuilder.literal(commandName);
	}
	
	private LiteralArgumentBuilder<?> getLiteralArgumentBuilderArgument(String commandName, CommandPermission permission) {
		return LiteralArgumentBuilder.literal(commandName).requires(clw -> {
			return permissionCheck(getCommandSender(clw), permission);
		});
	}
	
	//Gets a RequiredArgumentBuilder for an argument
	private <T> RequiredArgumentBuilder<?, T> getRequiredArgumentBuilder(String argumentName, ArgumentType<T> type, CommandPermission permission) {
		return RequiredArgumentBuilder.argument(argumentName, type).requires(clw -> {
			return permissionCheck(getCommandSender(clw), permission);
		});
	}
	
	//Gets a RequiredArgumentBuilder for a DynamicSuggestedStringArgument
	private <T> RequiredArgumentBuilder<?, T> getRequiredArgumentBuilder(String argumentName, DynamicSuggestedStringArgument type, CommandPermission permission) {
		
		SuggestionProvider provider = null;
		
		if(type.getDynamicSuggestions() == null) {
			//withCS
			provider = (context, builder) -> {
				return getSuggestionsBuilder(builder, type.getDynamicSuggestionsWithCommandSender().getSuggestions(getCommandSender(context.getSource())));
			};
		} else if(type.getDynamicSuggestionsWithCommandSender() == null) {
			provider = (context, builder) -> {
				return getSuggestionsBuilder(builder, type.getDynamicSuggestions().getSuggestions());
			};
		} else {
			throw new RuntimeException("Invalid DynamicSuggestedStringArgument found!");
		}
		
		return getRequiredArgumentBuilder(argumentName, type.getRawType(), permission, provider);
	}
		
	//Gets a RequiredArgumentBuilder for an argument, given a SuggestionProvider
	private <T> RequiredArgumentBuilder<?, T> getRequiredArgumentBuilder(String argumentName, ArgumentType<T> type, CommandPermission permission, SuggestionProviders provider){
		return getRequiredArgumentBuilder(argumentName, type, permission, getSuggestionProvider(provider));
	}
	
	//Gets a RequiredArgumentBuilder for an argument, given a SuggestionProvider
	private <T> RequiredArgumentBuilder<?, T> getRequiredArgumentBuilder(String argumentName, ArgumentType<T> type, CommandPermission permission, SuggestionProvider provider){
		return RequiredArgumentBuilder.argument(argumentName, type).requires(clw -> {
			return permissionCheck(getCommandSender(clw), permission);
		}).suggests(provider);
	}
	
	//Gets a RequiredArgumentBuilder for an argument, given that said argument uses OverrideableSuggestions
	private <T> RequiredArgumentBuilder<?, T> getRequiredArgumentBuilderWithOverride(String argumentName, Argument type, CommandPermission permission){
		String[] newSuggestions = ((OverrideableSuggestions) type).getOverriddenSuggestions();
		if(newSuggestions == null || newSuggestions.length == 0) {
			return getRequiredArgumentBuilder(argumentName, type.getRawType(), permission);
		} else {
			
			//Use NMS ICompletionProvider.a() on newSuggestions
			SuggestionProvider provider = (context, builder) -> {
				return getSuggestionsBuilder(builder, newSuggestions);
			};
			
			return RequiredArgumentBuilder.argument(argumentName, type.getRawType()).requires(clw -> {
				return permissionCheck(getCommandSender(clw), permission);
			}).suggests(provider);
		}
		
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// SECTION: Reflection                                                                              //
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
