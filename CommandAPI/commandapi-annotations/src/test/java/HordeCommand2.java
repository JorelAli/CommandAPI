import java.util.function.Supplier;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.annotations.annotations.Command;
import dev.jorel.commandapi.annotations.annotations.Permission;
import dev.jorel.commandapi.annotations.annotations.Subcommand;
import dev.jorel.commandapi.annotations.annotations.Suggestion;
import dev.jorel.commandapi.annotations.annotations.Suggests;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.SafeSuggestions;

@Command("horde")
public class HordeCommand2 {

	@Subcommand("hazard")
	class HazardCommand {

		@Subcommand("create")
		class CreateCommand {

			@Subcommand("fire")
			public void fire(CommandSender executor, @AStringArgument String name) {
			}
		}

		@Subcommand("modify")
		class ModifyCommand {

			@Permission("hello")
			@Suggests(HazardSuggestions.class)
			@AStringArgument
			String name;

			// horde hazard modify <name> area <size>
			@Subcommand("area")
			public void area(Player player, @AIntegerArgument int size) {
			}

			// horde hazard modify <name> area <size>
			@Subcommand("area")
			public void area(CommandSender sender, @AIntegerArgument int size) {
			}
			
			@Suggestion
			class HazardSuggestions implements Supplier<ArgumentSuggestions> {

				@Override
				public ArgumentSuggestions get() {
					return ArgumentSuggestions.strings("fire", "water", "poison");
				}
				
			}
			
			@Suggestion
			class LocationSuggestions implements Supplier<SafeSuggestions<Location>> {

				@Override
				public SafeSuggestions<Location> get() {
					return SafeSuggestions.suggest(new Location[0]);
				}
				
			}
		}

		@Subcommand("toggle")
		public void toggle() {
		}
	}
	

	
	@Suggestion
	class EnableSuggestions implements Supplier<ArgumentSuggestions> {

		@Override
		public ArgumentSuggestions get() {
			return ArgumentSuggestions.strings("fire", "water", "poison");
		}
		
	}
	
	@Suggestion
	class LocationSuggestions implements Supplier<SafeSuggestions<Location>> {

		@Override
		public SafeSuggestions<Location> get() {
			return SafeSuggestions.suggest(new Location[0]);
		}
		
	}

	@Subcommand("enable")
	public void enable(Player sender) {
	}
	
	@Subcommand("disable")
	public void disable(CommandSender sender) {
	}
}