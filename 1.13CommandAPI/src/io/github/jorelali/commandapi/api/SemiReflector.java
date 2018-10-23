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
import java.util.LinkedHashMap;
import java.util.List;
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
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import io.github.jorelali.commandapi.CommandAPIMain;
import io.github.jorelali.commandapi.api.CommandPermission.PermissionNode;
import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.ChatColorArgument;
import io.github.jorelali.commandapi.api.arguments.ChatComponentArgument;
import io.github.jorelali.commandapi.api.arguments.EnchantmentArgument;
import io.github.jorelali.commandapi.api.arguments.EntitySelectorArgument;
import io.github.jorelali.commandapi.api.arguments.EntityTypeArgument;
import io.github.jorelali.commandapi.api.arguments.ItemStackArgument;
import io.github.jorelali.commandapi.api.arguments.LiteralArgument;
import io.github.jorelali.commandapi.api.arguments.LocationArgument;
import io.github.jorelali.commandapi.api.arguments.ParticleArgument;
import io.github.jorelali.commandapi.api.arguments.PlayerArgument;
import io.github.jorelali.commandapi.api.arguments.PotionEffectArgument;
import io.github.jorelali.commandapi.api.arguments.SuggestedStringArgument;
import io.github.jorelali.commandapi.api.exceptions.CantFindPlayerException;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

//Only uses reflection for NMS
@SuppressWarnings({"rawtypes", "unchecked"})
public final class SemiReflector {
	
	class ClassCache {
		
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
	
	private Map<String, Class<?>> NMSClasses;
	private Map<String, Class<?>> OBCClasses;
	private Map<ClassCache, Method> methods;
	private Map<ClassCache, Field> fields;
	
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
			
			this.cDispatcher = getField(getNMSClass("MinecraftServer"), "commandDispatcher").get(server);
						
			this.dispatcher = (CommandDispatcher) getNMSClass("CommandDispatcher").getDeclaredMethod("a").invoke(cDispatcher); 

		} catch(Exception e) {
			e.printStackTrace(System.out);
		}
		
	}
	
	public void unregister(String commandName) {
		try {
			Field children = getField(CommandNode.class, "children");
			
			Map<String, CommandNode<?>> c = (Map<String, CommandNode<?>>) children.get(dispatcher.getRoot());
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
	
	private Command generateCommand(String commandName, LinkedHashMap<String, Argument> args, CommandExecutor executor) {
		
		//Generate our command from executor
		return (cmdCtx) -> {
			
			//Get the CommandSender via NMS
			CommandSender sender = null;
			
			/*
			 * ProxiedCommandSender:
			 * callee - the thing calling the command unwillingly (e.g. a chicken)
			 * caller - the thing which literally typed the command (e.g. commandblock/console)
			 */
			
			//Parse all information about the CommandListenerWrapper to fix issue 20 - https://github.com/JorelAli/1.13-Command-API/issues/20
			if(CommandAPIMain.getConfiguration().runTestCode()) {
				try {
					/*
					 * Methods to explore:
					 * getName()
					 * f(), g(), h() (try and use the CommandSyntaxException to your advantage)
					 * base (getBukkitSender?)
					 */
					Class clw = getNMSClass("CommandListenerWrapper");
					
//					System.out.println("CLW(?): " + cmdCtx.getSource().getClass().getName());
					
					Object base = clw.getDeclaredField("base").get(cmdCtx.getSource());
					System.out.println("ICommandListener (base): " + base.getClass().getName());
					
					//THIS SHOULD BE THE CALLER
					Object baseBukkitSender = clw.getDeclaredMethod("getBukkitSender").invoke(cmdCtx.getSource());
					System.out.println("(Bukkit base): " + baseBukkitSender.getClass().getName());
					
//					Object getName = clw.getDeclaredMethod("getName").invoke(cmdCtx.getSource());
//					System.out.println("getName: " + getName);
					
					Object f = clw.getDeclaredMethod("f").invoke(cmdCtx.getSource());
					System.out.println("f (@Nullable Entity) [proxyEntity]: " + f.getClass().getName());
					
					
					Object fBukkit = getMethod(getNMSClass("Entity"), "getBukkitEntity").invoke(f);
					System.out.println("fBukkit [bukkitProxyEntity]: " + fBukkit.getClass().getName());
//					Object g = clw.getDeclaredMethod("g").invoke(cmdCtx.getSource());
//					System.out.println("g (Entity): " + g.getClass().getName());
//					
//					Object h = clw.getDeclaredMethod("h").invoke(cmdCtx.getSource());
//					System.out.println("h (Player): " + h.getClass().getName());
					System.out.println(" - ");
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException | InvocationTargetException | NoSuchMethodException e) {
					System.out.println("Err:");
					e.printStackTrace(System.out);
				}				
			}
			
			try {
				sender = (CommandSender) getMethod(getNMSClass("CommandListenerWrapper"), "getBukkitSender").invoke(cmdCtx.getSource());//getNMSClass("CommandListenerWrapper").getDeclaredMethod("getBukkitSender").invoke(cmdCtx.getSource());
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| SecurityException e) {
				e.printStackTrace(System.out);
			}
			
			//Handle proxied command senders via /execute as [Proxy]
			try {
				Object proxyEntity = getMethod(getNMSClass("CommandListenerWrapper"), "f").invoke(cmdCtx.getSource());
				
				if(CommandAPIMain.getConfiguration().runTestCode()) {
					System.out.println("--- Generating ProxiedCommandSender ---");
					if(proxyEntity == null) {
						System.out.println("proxyEntity is null");
					} 
					System.out.println("proxyEntity (f): " + proxyEntity.getClass().getName());
					System.out.println("proxyEntity instanceof NMSEntity? " + (getNMSClass("Entity").isInstance(proxyEntity)));
					System.out.println("---------------------------------");
				}
				
				//Parse the Entity (callee) from the CLW.
				
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
			
			if(CommandAPIMain.getConfiguration().runTestCode()) {
				System.out.println("--- Constructed CommandSender ---");
				System.out.println("CommandSender object: " + sender.getClass().getName());
				if(sender instanceof ProxiedCommandSender) {
					ProxiedCommandSender p = (ProxiedCommandSender) sender;
					System.out.println("Caller (e.g. console): " + p.getCaller().getClass().getName());
					System.out.println("Callee (e.g. chicken): " + p.getCallee().getClass().getName());
				}
				
				System.out.println("---------------------------------");
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
					}
				}
			}
			
			//Run the code from executor and return
			try {
				executor.run(sender, argList.toArray(new Object[argList.size()]));
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return 0;
			}
			return 1;
		};
	}
	
	private Predicate generatePermissions(CommandPermission permissions) {
		return (cmdSender) -> {
			
        	//Generate CommandSender object
			CommandSender sender = null;
			try {
				sender = (CommandSender) getMethod(cmdSender.getClass(), "getBukkitSender").invoke(cmdSender);
				//Object entity = cmdSender.getClass().getDeclaredMethod("f").invoke(cmdSender);
				//sender = (CommandSender) getNMSClass("Entity").getDeclaredMethod("getBukkitEntity").invoke(entity);
				
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| SecurityException e) {
				e.printStackTrace(System.out);
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
		if(CommandAPIMain.getConfiguration().hasVerboseOutput()) {
			//Create a list of argument names
			StringBuilder builder = new StringBuilder();
			args.values().forEach(arg -> builder.append("<").append(arg.getClass().getSimpleName()).append("> "));
			CommandAPIMain.getLog().info("Registering command /" + commandName + " " + builder.toString());
		}
		
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
	
	//Registers a RequiredArgumentBuilder for an argument
	private <T> RequiredArgumentBuilder<?, T> getRequiredArgumentBuilder(String argumentName, com.mojang.brigadier.arguments.ArgumentType<T> type, String[] suggestions){
		SuggestionProvider provider = (context, builder) -> {
			for(String str : suggestions) {
				builder = builder.suggest(str);
			}
			
			return builder.buildFuture();
			};
		return RequiredArgumentBuilder.argument(argumentName, type).suggests(provider);
	}
		
	/** Retrieves a net.minecraft.server class by using the dynamic package from
	 * the dedicated server */
	private Class<?> getNMSClass(final String className) {
		return NMSClasses.computeIfAbsent(className, key -> {
			try {
				return (Class.forName(SemiReflector.packageName + "." + key));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		});
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

	
}
