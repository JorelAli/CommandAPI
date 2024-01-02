import java.util.function.Supplier;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.annotations.annotations.Command;
import dev.jorel.commandapi.annotations.annotations.Subcommand;
import dev.jorel.commandapi.annotations.annotations.Suggestion;
import dev.jorel.commandapi.annotations.annotations.Suggests;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;

/**
 * Annotated command that uses suggestion classes
 */
@Command("mycommand")
public class AnnotatedCommandWithSuggestions {

	// /mycommand <name>
	
	@AStringArgument
	@Suggests(ListOfNames.class)
	String name;

	@Subcommand
	public void myExecutor(Player player) {
		// ...
	}

	@Suggestion
	class ListOfNames implements Supplier<ArgumentSuggestions<CommandSender>> {

		@Override
		public ArgumentSuggestions<CommandSender> get() {
			return ArgumentSuggestions.strings("Player1", "Player2", "Player3");
		}

	}

}
