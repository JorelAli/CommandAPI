import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.annotations.annotations.Command;
import dev.jorel.commandapi.annotations.annotations.Subcommand;
import dev.jorel.commandapi.annotations.arguments.APlayerArgument;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;

/**
 * A test for repeating subcommand annotations
 */
@Command("perm")
public class AnnotatedCommandRepeatingSubcommands {

	// /perm set user <player> <permission>

//	@Subcommand("set")
//	@Subcommand("user")
//	public void execute(CommandSender sender,
//		@APlayerArgument Player player,
//		@AStringArgument String permission) {
//		// Do stuff
//	}
	
//	class SetUserSubcommand {
//
//		@APlayerArgument
//		Player player;
//		
//		@AStringArgument
//		String permission;
//
//		@Subcommand
//		public void execute(CommandSender sender) {
//			// Do stuff
//		}
//	}

}
