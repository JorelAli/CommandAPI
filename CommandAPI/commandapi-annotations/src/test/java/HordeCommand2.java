import java.util.function.Supplier;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.annotations.annotations.ArgumentParser;
import dev.jorel.commandapi.annotations.annotations.Command;
import dev.jorel.commandapi.annotations.annotations.Permission;
import dev.jorel.commandapi.annotations.annotations.Subcommand;
import dev.jorel.commandapi.annotations.annotations.Suggestion;
import dev.jorel.commandapi.annotations.annotations.Suggests;
import dev.jorel.commandapi.annotations.arguments.ACustomArgument;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException;
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentInfo;
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentInfoParser;
import dev.jorel.commandapi.arguments.SafeSuggestions;

@Command("horde")
public class HordeCommand2 {

	@AStringArgument
	String hiiiiii;
	
	@AIntegerArgument
	int byeeeeee;

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
			
			// TODO: This should fail because String is not an 
//			@Subcommand("area")
//			public void area2(CommandSender sender, @AIntegerArgument String size) {
//			}
			
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
		public void toggle(CommandSender sender) {
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
	
	// TODO: When testing, try moving this to its own separate .java file and see if that links properly
	@ArgumentParser
	class WorldArgument implements CustomArgumentInfoParser<World> {

		@Override
		public World apply(CustomArgumentInfo info) throws CustomArgumentException {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	@Subcommand("custom")
	public void custom(CommandSender sender, @ACustomArgument(WorldArgument.class) World world) {
		
	}
	
//	static void blah(Object[] args) {
//		HordeCommand2 command = new HordeCommand2();
//		command.byeeeeee = (int) args[0];
//		command.hiiiiii = (String) args[1];
//		HordeCommand2.HazardCommand.ModifyCommand command1 = command.new HazardCommand().new ModifyCommand();
//		command1.name = (String) args[2];
//		command1.area((Player) null, 2);
//	}
	
//	static void a(HordeCommand2 command){
//		new CommandAPICommand("modify")
//	    .withArguments(new StringArgument("hiiiiii"))
//	    .withArguments(new IntegerArgument("byeeeeee", -2147483648, 2147483647))
//	    .withArguments(new StringArgument("name").withPermission("hello")
//	.replaceSuggestions(new HordeCommand2().new HazardCommand().new ModifyCommand().new HazardSuggestions().get()))
//	    .withArguments(
//	        new MultiLiteralArgument("area")
//	            .setListed(false)
//	        )
//	    .executes((sender, args) -> {
//	        command.area(sender, args[0]);
//	    }
//
//	}
}