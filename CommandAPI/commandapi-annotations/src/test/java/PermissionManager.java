import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.annotations.Arg;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.NeedsOp;
import dev.jorel.commandapi.annotations.Subcommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;

@Command("permissions")
public class PermissionManager {
	
	@Subcommand("user add")
	@NeedsOp
	@Arg(name = "target", type = PlayerArgument.class)
	@Arg(name = "permission", type = StringArgument.class)
	public void addPermission(CommandSender sender, Object[] args) {
		
	}
	
	@Subcommand("create")
	@NeedsOp
	@Arg(name = "name", type = StringArgument.class)
	@Arg(name = "owner", type = PlayerArgument.class)
	public void createParty(CommandSender sender, Object[] args) {
		// TODO: Create a party
	}
	
}
