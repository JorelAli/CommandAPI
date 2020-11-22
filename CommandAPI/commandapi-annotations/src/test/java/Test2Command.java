import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.annotations.Alias;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.NeedsOp;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.ADoubleArgument;
import dev.jorel.commandapi.annotations.arguments.AFloatArgument;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.jorel.commandapi.annotations.arguments.ALiteralArgument;
import dev.jorel.commandapi.annotations.arguments.ALongArgument;
import dev.jorel.commandapi.annotations.arguments.AMultiLiteralArgument;

/* ANCHOR: teleport_command */
@Command("teleport")	
@Alias({"tp", "tele"})
public class Test2Command {
/* ANCHOR_END: teleport_command */
	
	{
		{
/* ANCHOR: teleport_command_needsop */
@Command("teleport")	
@NeedsOp
class TeleportCommand {
/* ANCHOR_END: teleport_command_needsop */
}
		}
		{
/* ANCHOR: teleport_command_perms */
@Command("teleport")	
@Permission("myplugin.tp")
class TeleportCommand {
/* ANCHOR_END: teleport_command_perms */
}
		}
	}
}

class AA {
/* ANCHOR: number_arguments */
@Default
public static void command(CommandSender sender, 
	@ADoubleArgument(min = 0.0, max = 10.0) double someDouble,
	@AFloatArgument(min = 5.0f, max = 10.0f) float someFloat,
	@AIntegerArgument(max = 100) int someInt,
	@ALongArgument(min = -10) long someLong
) {
	// Command implementation here
}
/* ANCHOR_END: number_arguments */

/* ANCHOR: literal_arguments */
@Default
public static void command(CommandSender sender, 
	@ALiteralArgument("myliteral") String literal,
	@AMultiLiteralArgument({"literal", "anotherliteral"}) String multipleLiterals
) {
	// Command implementation here
}
/* ANCHOR_END: literal_arguments */

}