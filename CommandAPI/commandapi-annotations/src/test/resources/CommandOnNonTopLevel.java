import dev.jorel.commandapi.annotations.annotations.Command;

@Command("horde")
public class CommandOnNonTopLevel {

	@Command("horde")
	class HazardCommand {
	}
}