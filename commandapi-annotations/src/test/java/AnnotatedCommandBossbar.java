import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.annotations.annotations.Command;
import dev.jorel.commandapi.annotations.annotations.Subcommand;
import dev.jorel.commandapi.annotations.arguments.ABooleanArgument;
import dev.jorel.commandapi.annotations.arguments.AChatComponentArgument;
import dev.jorel.commandapi.annotations.arguments.AEntitySelectorArgument;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.jorel.commandapi.annotations.arguments.AMultiLiteralArgument;
import dev.jorel.commandapi.annotations.arguments.ANamespacedKeyArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import net.md_5.bungee.api.chat.BaseComponent;

/**
 * The annotated equivalent of BetterBossBars from the
 * examples/bukkit/commandtrees directory
 */
@Command("betterbossbar")
public class AnnotatedCommandBossbar {

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

	private Map<NamespacedKey, Integer> maxValues;

	public AnnotatedCommandBossbar() {
		this.maxValues = new HashMap<>();
	}

	@Subcommand("set")
	class SetSubcommand {

		@ANamespacedKeyArgument
		NamespacedKey id;

		@Subcommand("players")
		public void players(CommandSender sender,
			@AEntitySelectorArgument.ManyPlayers Collection<Player> targets) {

			Bukkit.getBossBar(id).removeAll();
			for (Player player : targets) {
				Bukkit.getBossBar(id).addPlayer(player);
			}
		}

		@Subcommand("style")
		public void style(CommandSender sender,
			@AMultiLiteralArgument({ "notched_6", "notched_10", "notched_12", "notched_20", "progress" }) String style) throws WrapperCommandSyntaxException {

			Bukkit.getBossBar(id).setStyle(
				switch (style) {
					case "notched_6" -> BarStyle.SEGMENTED_6;
					case "notched_10" -> BarStyle.SEGMENTED_10;
					case "notched_12" -> BarStyle.SEGMENTED_12;
					case "notched_20" -> BarStyle.SEGMENTED_20;
					case "progress" -> BarStyle.SOLID;
					default -> throw CommandAPI.failWithString(style + " is an invalid bossbar style");
				});
		}

		@Subcommand("value")
		public void value(CommandSender sender,
			@AIntegerArgument int value) {

			if (maxValues.containsKey(id)) {
				Bukkit.getBossBar(id).setProgress((double) value / (double) maxValues.get(id));
			} else {
				maxValues.put(id, value);
				Bukkit.getBossBar(id).setProgress(1.0D);
			}
		}

		@Subcommand("visible")
		public void visible(CommandSender sender,
			@ABooleanArgument boolean visible) {

			Bukkit.getBossBar(id).setVisible(visible);
		}

	}

	@Subcommand("remove")
	public void remove(CommandSender sender,
		@ANamespacedKeyArgument NamespacedKey id) {

		sender.sendMessage("Removed custom bossbar [" + Bukkit.getBossBar(id).getTitle() + "]");
		Bukkit.getBossBar(id).removeAll();
		Bukkit.removeBossBar(id);
	}

	@Subcommand("get")
	class GetSubcommand {

		@ANamespacedKeyArgument
		NamespacedKey id;

		@Subcommand("players")
		public void players(CommandSender sender) {
			List<Player> bossBarPlayers = Bukkit.getBossBar(id).getPlayers();
			String players = bossBarPlayers.stream().map(Player::getName).collect(Collectors.joining(", "));
			sender.sendMessage("Custom bossbar [" + Bukkit.getBossBar(id).getTitle() + "] has " + bossBarPlayers.size() + " players currently online: " + players);
		}

		@Subcommand("visible")
		public void visible(CommandSender sender) {
			BossBar bossBar = Bukkit.getBossBar(id);
			sender.sendMessage("Custom bossbar [" + bossBar.getTitle() + "] is currently " + (bossBar.isVisible() ? "shown" : "hidden"));
		}

		@Subcommand("max")
		public void max(CommandSender sender) {
			sender.sendMessage("Custom bossbar [" + Bukkit.getBossBar(id).getTitle() + "] has a maximum of " + maxValues.getOrDefault(id, 100));
		}

		@Subcommand("value")
		public void value(CommandSender sender) {
			BossBar bossBar = Bukkit.getBossBar(id);
			
			int value = (int) (bossBar.getProgress() * maxValues.getOrDefault(id, 100));
			sender.sendMessage("Custom bossbar [" + Bukkit.getBossBar(id).getTitle() + "] has a value of " + value);
		}
	}

	@Subcommand("add")
	class AddSubcommand {

		@Subcommand
		public void addBossbar(CommandSender sender,
			@ANamespacedKeyArgument NamespacedKey id,
			@AChatComponentArgument BaseComponent[] name) {

			Bukkit.createBossBar(id, BaseComponent.toLegacyText(name), BarColor.WHITE, BarStyle.SOLID);
			maxValues.put(id, 100);
			sender.sendMessage("Created custom bossbar [" + BaseComponent.toLegacyText(name) + ChatColor.WHITE + "]");
		}

	}

	@Subcommand("list")
	public void ListSubcommand(CommandSender sender) {
		Iterable<KeyedBossBar> bossBars = Bukkit::getBossBars;
		sender.sendMessage("List of custom bossbars: " +
			StreamSupport
				.stream(bossBars.spliterator(), false)
				.map(KeyedBossBar::getKey)
				.map(NamespacedKey::toString)
				.collect(Collectors.joining(", "))
		);
	}

}
