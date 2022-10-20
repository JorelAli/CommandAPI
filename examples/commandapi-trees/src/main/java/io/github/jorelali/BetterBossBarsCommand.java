package io.github.jorelali;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.EntitySelector;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.NamespacedKeyArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

public class BetterBossBarsCommand {
	
	// /betterbossbar set <id> players <targets>
	// /betterbossbar set <id> style (notched_6|notched_10|notched_12|notched_20|progress)
	// /betterbossbar set <id> value <value>
	// /betterbossbar set <id> visible <visible>
	
	// /betterbossbar remove <id>
	
	// /betterbossbar get <id> players
	// /betterbossbar get <id> visible
	// /betterbossbar get <id> max
	// /betterbossbar get <id> value
	
	// /betterbossbar add <id> <name>
	
	// /betterbossbar list
	
	private BetterBossBarsCommand() {}

	public static void registerBetterBossBarCommand() {
		new CommandTree("betterbossbar")
			.then(new LiteralArgument("set")
				.then(new NamespacedKeyArgument("id")
					.then(new LiteralArgument("players")
						.then(new EntitySelectorArgument<Collection<Player>>("targets", EntitySelector.MANY_PLAYERS)
							.executes(BetterBossBarsCommand::setPlayers)
						)
					)
					.then(new LiteralArgument("style")
						.then(new MultiLiteralArgument("notched_6", "notched_10", "notched_12", "notched_20", "progress")
							.executes(BetterBossBarsCommand::setStyle)
						)
					)
					.then(new LiteralArgument("value")
						.then(new IntegerArgument("value")
							.executes(BetterBossBarsCommand::setValue)
						)
					)
					.then(new LiteralArgument("visible")
						.then(new BooleanArgument("visible")
							.executes(BetterBossBarsCommand::setVisible)
						)
					)
				)
			)
			.then(new LiteralArgument("remove")
				.then(new NamespacedKeyArgument("id")
					.executes(BetterBossBarsCommand::removeBossbar)
				)
			)
			.then(new LiteralArgument("get")
				.then(new NamespacedKeyArgument("id")
					.then(new LiteralArgument("players")
						.executes(BetterBossBarsCommand::getPlayers)
					)
					.then(new LiteralArgument("visible")
						.executes(BetterBossBarsCommand::getVisible)
					)
					.then(new LiteralArgument("max")
						.executes(BetterBossBarsCommand::getMax)
					)
					.then(new LiteralArgument("value")
						.executes(BetterBossBarsCommand::getValue)
					)
				)
			)
			.then(new LiteralArgument("list")
				.executes(BetterBossBarsCommand::list)
			)
			.register();
	}

	private static void setPlayers(CommandSender sender, Object[] args) {
		NamespacedKey id = (NamespacedKey) args[0];
		Collection<Player> targets = (Collection<Player>) args[1];
		
		Bukkit.getBossBar(id).removeAll();
		for(Player player : targets) {
			Bukkit.getBossBar(id).addPlayer(player);
		}
	}
	
	private static void setStyle(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
		NamespacedKey id = (NamespacedKey) args[0];
		String style = (String) args[1];
		
		Bukkit.getBossBar(id).setStyle(
			switch(style) {
				case "notched_6" -> BarStyle.SEGMENTED_6;
				case "notched_10" -> BarStyle.SEGMENTED_10;
				case "notched_12" -> BarStyle.SEGMENTED_12;
				case "notched_20" -> BarStyle.SEGMENTED_20;
				case "progress" -> BarStyle.SOLID;
				default -> throw CommandAPI.fail(style + " is an invalid bossbar style");
			}
		);
	}
	
	private static void setValue(CommandSender sender, Object[] args) {
		NamespacedKey id = (NamespacedKey) args[0];
		int value = (int) args[1];
		
		// TODO
	}
	
	private static void setVisible(CommandSender sender, Object[] args) {
		
	}
	
	private static void removeBossbar(CommandSender sender, Object[] args) {
		
	}
	
	private static void getPlayers(CommandSender sender, Object[] args) {
		
	}
	
	private static void getVisible(CommandSender sender, Object[] args) {
		
	}
	
	private static void getMax(CommandSender sender, Object[] args) {
		
	}
	
	private static void getValue(CommandSender sender, Object[] args) {
		
	}
	
	private static void list(CommandSender sender, Object[] args) {
		
	}

}
