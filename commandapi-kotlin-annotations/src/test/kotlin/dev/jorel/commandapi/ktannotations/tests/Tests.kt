package dev.jorel.commandapi.ktannotations.tests

import dev.jorel.commandapi.annotations.*
import dev.jorel.commandapi.annotations.arguments.APlayerArgument
import dev.jorel.commandapi.annotations.arguments.AStringArgument
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Command("teleport")
@Alias("tp", "tele")
class TeleportCommand : Testable {
	override val expectedOutput =
		"""
			new dev.jorel.commandapi.CommandAPICommand("teleport")
			.withAliases("tp", "tele")
			.withArguments(new dev.jorel.commandapi.arguments.PlayerArgument("target"))
			.executesPlayer((sender, args) -> {dev.jorel.commandapi.ktannotations.tests.TeleportCommand.teleport(sender, (org.bukkit.entity.Player) args[0]);})
			.register();
		""".trimIndent()

	companion object {
		@JvmStatic
		@Default
		fun teleport(player: Player, @APlayerArgument target: OfflinePlayer) {
			if (target.isOnline && target is Player) {
				player.teleport(target)
			}
		}
	}
}

@Command("warp")
class WarpCommand : Testable {
	override val expectedOutput =
		"""
			new dev.jorel.commandapi.CommandAPICommand("warp")
			.executes((sender, args) -> {dev.jorel.commandapi.ktannotations.tests.WarpCommand.warp(sender);})
			.register();
			new dev.jorel.commandapi.CommandAPICommand("warp")
			.withArguments(new dev.jorel.commandapi.arguments.StringArgument("warpName"))
			.executesPlayer((sender, args) -> {dev.jorel.commandapi.ktannotations.tests.WarpCommand.warp(sender, (String) args[0]);})
			.register();
			new dev.jorel.commandapi.CommandAPICommand("warp")
			.withArguments(
			new dev.jorel.commandapi.arguments.MultiLiteralArgument("create")
			.setListed(false)
			.withPermission("warps.create")
			)
			.withArguments(new dev.jorel.commandapi.arguments.StringArgument("warpName"))
			.executesPlayer((sender, args) -> {dev.jorel.commandapi.ktannotations.tests.WarpCommand.createWarp(sender, (String) args[0]);})
			.register();
		""".trimIndent()

	companion object {
		@JvmStatic
		private var warps: MutableMap<String, Location> = HashMap()

		@JvmStatic
		@Default
		fun warp(sender: CommandSender) {
			sender.sendMessage("--- Warp help ---")
			sender.sendMessage("/warp - Show this help")
			sender.sendMessage("/warp <warp> - Teleport to <warp>")
			sender.sendMessage("/warp create <warpname> - Creates a warp at your current location")
		}

		@JvmStatic
		@Default
		fun warp(player: Player, @AStringArgument warpName: String) {
			player.teleport(warps[warpName]!!)
		}

		@JvmStatic
		@Subcommand("create")
		@Permission("warps.create")
		fun createWarp(player: Player, @AStringArgument warpName: String) {
			warps[warpName] = player.location
		}
	}
}