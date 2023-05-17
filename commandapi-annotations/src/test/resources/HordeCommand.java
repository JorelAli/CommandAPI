import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.annotations.annotations.Command;
import dev.jorel.commandapi.annotations.annotations.Subcommand;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;

@Command("horde")
public class HordeCommand {

	@Subcommand("hazard")
	class HazardCommand {

		@Subcommand("create")
		class CreateCommand {

			@Subcommand("fire")
			public void fire(CommandSender executor, String name) {
			}
		}

		@Subcommand("modify")
		class ModifyCommand {

			@AStringArgument
			String name;

			// horde hazard modify <name> area <size>
			@Subcommand("area")
			public void area(Player player, int size) {
			}

			// horde hazard modify <name> area <size>
			@Subcommand("area")
			public void area(CommandSender sender, int size) {
			}
		}

		@Subcommand("toggle")
		public void toggle() {
		}
	}
}