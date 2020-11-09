import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.annotations.Alias;
import dev.jorel.commandapi.annotations.Arg;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Description;
import dev.jorel.commandapi.annotations.Executor;
import dev.jorel.commandapi.annotations.Executor.ExecutorType;
import dev.jorel.commandapi.annotations.NeedsOp;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.Subcommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;

@Command("party")
public class PartyCommand {
	
	static void register() {}
		
	@Subcommand("tp")
	@Executor(ExecutorType.PLAYER)
	@Permission("party.tp")
	@Description("Teleport to a party member")
	@Alias({"tele", "teleport"})
	@Arg(name = "target", type = PlayerArgument.class)
	public void teleport(Player player, Object[] args) {
		Player target = (Player) args[0];
		player.teleport(target);
	}
	
	@Subcommand("create")
	@NeedsOp
	@Description("Creates a new party")
	@Arg(name = "name", type = StringArgument.class)
	@Arg(name = "owner", type = PlayerArgument.class)
	public void createParty(CommandSender sender, Object[] args) {
		// TODO: Create a party
	}
	
	
	
}
