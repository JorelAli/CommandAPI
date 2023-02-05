package io.github.jorelali;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Predicate;

public class Main extends JavaPlugin {
	// This exact IntegerArgument is used many times so this method helps reduce that repetition a bit
	private static IntegerArgument ratingArgument() {
		return new IntegerArgument("rating", 0, 10);
	}

	// TODO: make and use an example of `new CustomArgument(...)`

	private static Predicate<CommandSender> isRated(int minimumLevel) {
		return sender -> {
			if (!(sender instanceof Player player)) return false;
			RatedPlayer ratedPlayer = RatedPlayer.get(player);
			if(ratedPlayer == null) return false;
			return minimumLevel <= ratedPlayer.getRank();
		};
	}

	@Override
	public void onEnable() {
		// Create new RatedPlayers
		new CommandAPICommand("rateplayer")
			.withArguments(
				new PlayerArgument("player"),
				ratingArgument()
			)
			.executes(info -> {
				new RatedPlayer(info.args().getUnchecked("player"), info.args().getUnchecked("rating"));
			})
			.register();

		// Manage RatedPlayers
		new CommandAPICommand("editrank")
			.withArguments(
				new RatedPlayerArgument("player"),
				ratingArgument()
			)
			.executes(info -> {
				((RatedPlayer) info.args().get("player")).setRank(info.args().getUnchecked("rating"));
			})
			.register();

		new CommandAPICommand("unrank")
			.withArguments(
				new RatedPlayerArgument("player")
			)
			.executes(info -> {
				RatedPlayer.unrank(info.args().getUnchecked("player"));
			})
			.register();

		// Rating restricted commands
		// Messaging command for those ranked 5+
		new CommandAPICommand("message")
			.withRequirement(isRated(5))
			.withArguments(
				new RatedPlayerArgument("target", 5),
				new GreedyStringArgument("message")
			)
			.executesPlayer(info -> {
				((RatedPlayer) info.args().get("target")).getPlayer().sendMessage(info.sender().getDisplayName() + " " + info.args().get("message"));
			})
			.register();

		// More commands...
	}
}
