import org.bukkit.entity.Player;

import dev.jorel.commandapi.annotations.Alias;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Subcommand;
import dev.jorel.commandapi.annotations.arguments.APlayerArgument;

/* ANCHOR: teleport_command */
@Command("teleport")	
@Alias({"tp", "tele"})
public class TeleportCommand {
/* ANCHOR_END: teleport_command */
	
/* ANCHOR: teleport_subcommand */
@Subcommand({"teleport", "tp"})
public static void teleport(Player player, @APlayerArgument Player target) {
	player.teleport(target);
}
/* ANCHOR_END: teleport_subcommand */

}