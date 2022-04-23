import dev.jorel.commandapi.annotations.annotations.Command;

@Command("outer")
public class NestedCommand {

	@Command("inner")
	class InnerCommand {
	}
}