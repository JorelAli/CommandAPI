package io.github.jorelali.commandapi;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_13_R1.CraftServer;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.v1_13_R1.ArgumentItemStack;
import net.minecraft.server.v1_13_R1.CommandDispatcher;
import net.minecraft.server.v1_13_R1.CommandListenerWrapper;
import net.minecraft.server.v1_13_R1.MinecraftServer;

@Deprecated
public class Main_OLD extends JavaPlugin implements Listener {
	
	public static final String CMD_NAME = "alpha";
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getCommand(CMD_NAME).setExecutor(this);
		
		reflectIntoDispatcher();
		

		
		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		
		CommandDispatcher cDispatcher = server.commandDispatcher;
		
		
		// USING CMDAPI
		
//		CommandAPI api = new CommandAPI();
//		LinkedHashMap<String, ArgumentType> args = new LinkedHashMap<>();
//		args.put("name", ArgumentType.STRING);
		//args.put("age", ArgumentType.INTEGER);
		//api.register("mycustomcmd", args, (sender, cmdArgs) -> { System.out.println("WHOA"); System.out.println(cmdArgs[0]); return 1;}); 
		
//		//PrintCommandDispatcher
		File file = new File("commandDispatch.json");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		cDispatcher.a(file);
		
		
//		// INIT REFLECTOR
//		try {
//			//new SemiReflector();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
	}		
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase(CMD_NAME)) {
			//Player player = (Player) sender;
			return true;
		}
		return false;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		e.getPlayer().setOp(true);
	}
	
	public void reflectIntoDispatcher() {
		//Get an instance of the CommandDispatcher via MinecraftServer
		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		CommandDispatcher cDispatcher = server.commandDispatcher;
		
		com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> b = cDispatcher.a();
		reflectIntoDispatcherNodes(b);
	}
	
	public void reflectIntoDispatcherNodes(com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> z) {

		z.register(CommandDispatcher.a(CMD_NAME)
				.then(CommandDispatcher.a("qwvueriborbywuiero", IntegerArgumentType.integer())
						.executes(new com.mojang.brigadier.Command<CommandListenerWrapper>() {
							
							@Override
							public int run(CommandContext<CommandListenerWrapper> cmdCtx) throws CommandSyntaxException {
								System.out.println("YO!");
								return 0;
							}
						})));
		
		z.register(CommandDispatcher.a("beta")
				.then(CommandDispatcher.a("meaningfuldescriptionbro", IntegerArgumentType.integer(2, 10))
						.executes(new com.mojang.brigadier.Command<CommandListenerWrapper>() {
							
							@Override
							public int run(CommandContext<CommandListenerWrapper> cmdCtx) throws CommandSyntaxException {
								cmdCtx.getSource().getBukkitSender().sendMessage("betaTestSuccess");
								System.out.println("betaTest");
								return 1;
							}
						})));
		
		z.register(CommandDispatcher.a("gamma")
				.then(CommandDispatcher.a("username", StringArgumentType.word())
						.executes(new com.mojang.brigadier.Command<CommandListenerWrapper>() {
							
							@Override
							public int run(CommandContext<CommandListenerWrapper> cmdCtx) throws CommandSyntaxException {
								cmdCtx.getSource().getBukkitSender().sendMessage("gamma: " + cmdCtx.getArgument("username", String.class));
								return 1;
							}
						})));

		z.register(CommandDispatcher.a("delta")
				.then(CommandDispatcher.a("username", StringArgumentType.word())
						.executes(cmdCtx -> {
							cmdCtx.getSource().getBukkitSender().sendMessage("delta: " + cmdCtx.getArgument("username", String.class));
							return 1;
							//OPTIONAL ARGUMENT
						}).then(CommandDispatcher.a("size", IntegerArgumentType.integer(0, 10))
								.executes((cmdCtx) -> {System.out.println("size: " + cmdCtx.getArgument("size", int.class));return 1;
								}))));
		
		//Forces BOTH arguments to be required in order for execution
		z.register(CommandDispatcher.a("sig")
				.then(CommandDispatcher.a("username", StringArgumentType.word())
						.then(CommandDispatcher.a("size", IntegerArgumentType.integer(0, 10))
								.executes((cmdCtx) -> {System.out.println("size: " + cmdCtx.getArgument("size", int.class) + cmdCtx.getArgument("username", String.class));return 1;
								}))));
		
		//Using bi-variant first arguments
		z.register(CommandDispatcher.a("eps")
				.then(CommandDispatcher.a("username", StringArgumentType.word())
						.then(CommandDispatcher.a("size", IntegerArgumentType.integer(0, 10))
								.executes((cmdCtx) -> {System.out.println("size: " + cmdCtx.getArgument("size", int.class) + cmdCtx.getArgument("username", String.class));return 1;
								})))
				.then(CommandDispatcher.a("speed", IntegerArgumentType.integer(2)) //Must be bigger or equal to 2
						.executes((cmdCtx) -> {cmdCtx.getSource().getBukkitSender().sendMessage("speed " + cmdCtx.getArgument("speed", int.class)); return 1;})));
		
//		z.register(CommandDispatcher.a("zet")
//				.then(CommandDispatcher.a("operation", ArgumentMathOperation.a())
//						.then(CommandDispatcher.a("value1", IntegerArgumentType.integer(0, 10))
//								.then(CommandDispatcher.a("value2", IntegerArgumentType.integer(0, 10))
//										.executes((cmdCtx) -> {
//											
//											System.out.println(cmdCtx.getArgument("value1", int.class) + ArgumentMathOperation.a(cmdCtx, "operation").apply(, arg1) + cmdCtx.getArgument("value2", int.class) );
//											return 1;
//										})))));
		
		z.register(CommandDispatcher.a("zetgive")
				.then(CommandDispatcher.a("item", ArgumentItemStack.a())
						.then(CommandDispatcher.a("amount", IntegerArgumentType.integer(1, 64))
									.executes((cmdCtx) -> {
										
										
										
										ItemStack is = CraftItemStack.asBukkitCopy(ArgumentItemStack.a(cmdCtx, "item").a(cmdCtx.getArgument("amount", int.class), false));
										
										((Player) cmdCtx.getSource().getBukkitSender()).getInventory().addItem(is);
										
										return 1;
									}))));
	}

}
