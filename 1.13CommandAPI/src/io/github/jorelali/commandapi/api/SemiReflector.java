package io.github.jorelali.commandapi.api;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.permissions.Permission;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.CustomArgument;
import io.github.jorelali.commandapi.api.arguments.CustomArgument.CustomArgumentException;
import io.github.jorelali.commandapi.api.arguments.CustomArgument.MessageBuilder;
import io.github.jorelali.commandapi.api.arguments.CustomProvidedArgument;
import io.github.jorelali.commandapi.api.arguments.CustomProvidedArgument.SuggestionProviders;
import io.github.jorelali.commandapi.api.arguments.DynamicSuggestedStringArgument;
import io.github.jorelali.commandapi.api.arguments.EntitySelectorArgument;
import io.github.jorelali.commandapi.api.arguments.LiteralArgument;
import io.github.jorelali.commandapi.api.arguments.LocationArgument;
import io.github.jorelali.commandapi.api.arguments.LocationArgument.LocationType;
import io.github.jorelali.commandapi.api.arguments.OverrideableSuggestions;
import io.github.jorelali.commandapi.api.arguments.StringArgument;
import io.github.jorelali.commandapi.api.arguments.SuggestedStringArgument;
import io.github.jorelali.commandapi.api.nms.NMS;
import io.github.jorelali.commandapi.api.nms.NMS_1_13_R2;

@SuppressWarnings({"rawtypes", "unchecked", "deprecation"})
/**
 * Class to access the main methods in NMS. The wrapper's
 * implementations occur here.
 */
public final class SemiReflector {
		
	private TreeMap<String, CommandPermission> permissionsToFix;

	//Cache maps
	private static Map<ClassCache, Field> fields;
		
	//NMS variables
	private static String packageName = null;
	private CommandDispatcher dispatcher;
	
	private int version;
	private String versionStr; //Just in case (v1_13_R2 or v1_14_R1)
	
	private NMS nms;
	
	protected SemiReflector() throws ClassNotFoundException {
		
		//Package checks
		if(Package.getPackage("com.mojang.brigadier") == null) {
			throw new ClassNotFoundException("Cannot hook into Brigadier (Are you running Minecraft 1.13 or above?)");
		}
		
		try {
			//Setup NMS
			Object server = Bukkit.getServer().getClass().getDeclaredMethod("getServer").invoke(Bukkit.getServer());
			SemiReflector.packageName = server.getClass().getPackage().getName();
			//net.minecraft.server.v1_13_R2.MinecraftServer
			version = Integer.parseInt(packageName.substring(24, 26));
			versionStr = packageName.split("\\Q.\\E")[3];
			
			switch(version) {
				case 13:
					nms = new NMS_1_13_R2();
					break;
				case 14:
					//nms =
					break;
			}
			
			//Everything from this line will use getNMSClass(), so we initialize our cache here
			fields = new HashMap<>();
			permissionsToFix = new TreeMap<>();
									
			this.dispatcher = nms.getDispatcher(server); 

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
	
	
	
	//Used in the register() method to generate the command to actually be registered
	private Command generateCommand(String commandName, LinkedHashMap<String, Argument> args, CustomCommandExecutor executor) throws CommandSyntaxException {
		
		//Generate our command from executor
		return (cmdCtx) -> {
			
			//Get the CommandSender via NMS
			CommandSender sender = nms.getSenderForCommand(cmdCtx);
						
			//Array for arguments for executor
			List<Object> argList = new ArrayList<>();
			
			//Populate array
			for(Entry<String, Argument> entry : args.entrySet()) {
				//If primitive (and simple), parse as normal
				if(entry.getValue().isSimple()) {
					argList.add(cmdCtx.getArgument(entry.getKey(), entry.getValue().getPrimitiveType()));
				} else {
					switch(entry.getValue().getArgumentType()) {
						case CHATCOLOR:
							argList.add(nms.getChatColor(cmdCtx, entry.getKey()));
							break;
						case CHAT_COMPONENT:
							argList.add(nms.getChatComponent(cmdCtx, entry.getKey()));
							break;
						case CUSTOM:
							CustomArgument arg = (CustomArgument) entry.getValue();
							String customresult = (String) cmdCtx.getArgument(entry.getKey(), String.class);
							try {
								argList.add(arg.getParser().apply(customresult));
							} catch(CustomArgumentException e) {
								throw e.toCommandSyntax(customresult, cmdCtx);
							} catch(Exception e) {
								String errorMsg = new MessageBuilder("Error in executing command ")
										.appendFullInput().append(" - ").appendArgInput().appendHere().toString()
										.replace("%input%", customresult).replace("%finput%", cmdCtx.getInput());
								throw new SimpleCommandExceptionType(() -> {return errorMsg;}).create();
							}
							break;
						case ENCHANTMENT:
							argList.add(nms.getEnchantment(cmdCtx, entry.getKey()));
							break;
						case ENTITY_SELECTOR:
							EntitySelectorArgument argument = (EntitySelectorArgument) entry.getValue();
							argList.add(nms.getEntitySelector(cmdCtx, entry.getKey(), argument.getEntitySelector()));
							break;
						case ENTITY_TYPE:
							argList.add(nms.getEntityType(cmdCtx, entry.getKey(), sender));
							break;
						case FUNCTION:
							argList.add(nms.getFunction(cmdCtx, entry.getKey()));
							break;
						case ITEMSTACK:
							argList.add(nms.getItemStack(cmdCtx, entry.getKey()));
							break;
						case LITERAL:
							break;
						case LOCATION:
							LocationType locationType = ((LocationArgument) entry.getValue()).getLocationType();
							argList.add(nms.getLocation(cmdCtx, entry.getKey(), locationType, sender));
							break;
						case LOOT_TABLE:
							argList.add(nms.getLootTable(cmdCtx, entry.getKey()));
							break;
						case PARTICLE:
							argList.add(nms.getParticle(cmdCtx, entry.getKey()));
							break;
						case PLAYER:
							argList.add(nms.getPlayer(cmdCtx, entry.getKey()));
							break;
						case POTION_EFFECT:
							argList.add(nms.getPotionEffect(cmdCtx, entry.getKey()));
							break;
						case SIMPLE_TYPE:
							break;
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
	
	protected void fixPermissions() throws InvocationTargetException {
		try {
			
			SimpleCommandMap map = nms.getSimpleCommandMap();
			Field f = getField(SimpleCommandMap.class, "knownCommands");
			Map<String, org.bukkit.command.Command> knownCommands = (Map<String, org.bukkit.command.Command>) f.get(map);
						
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
					if(nms.isVanillaCommandWrapper(knownCommands.get(cmdName))) {
						knownCommands.get(cmdName).setPermission("");
					}
					if(nms.isVanillaCommandWrapper(knownCommands.get("minecraft:" + cmdName))) {
						knownCommands.get(cmdName).setPermission("");
					}
				} else {
					if(perm.getPermission() != null) {
						if(CommandAPIMain.getConfiguration().hasVerboseOutput()) {
							CommandAPIMain.getLog().info(perm.getPermission() + " -> /" + cmdName);
						}
						if(nms.isVanillaCommandWrapper(knownCommands.get(cmdName))) {
							knownCommands.get(cmdName).setPermission(perm.getPermission());
						}
						if(nms.isVanillaCommandWrapper(knownCommands.get("minecraft:" + cmdName))) {
							knownCommands.get(cmdName).setPermission(perm.getPermission());
						}
					} else {
					}
				}
				
				
			});
		} catch (IllegalAccessException | IllegalArgumentException e) {
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
			
			nms.createDispatcherFile(file);
//			switch(version) {
//				case 13:
//					getMethod(getNMSClass("CommandDispatcher"), "a", File.class).invoke(this.nmsCommandDispatcher, file);
//					break;
//				case 14:
//					try {
//						Method jsonObject = getMethod(getNMSClass("ArgumentRegistry"), "a", CommandDispatcher.class, CommandNode.class);
//						JsonObject result = (JsonObject) jsonObject.invoke(null, dispatcher, dispatcher.getRoot());
//						Files.write((new GsonBuilder()).setPrettyPrinting().create().toJson(result), file, StandardCharsets.UTF_8);
//					} catch (IOException e) {
//						e.printStackTrace(System.out);
//					}
//					break;
//			}
		}
	}	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// SECTION: SuggestionProviders                                                                     //
	//////////////////////////////////////////////////////////////////////////////////////////////////////

	private CommandSender getCommandSender(Object clw) {
		return nms.getCommandSenderForCLW(clw);
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
		return nms.getSuggestionProvider(provider);
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
	
}
