import dev.jorel.commandapi.annotations.Alias;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.NeedsOp;
import dev.jorel.commandapi.annotations.Permission;

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