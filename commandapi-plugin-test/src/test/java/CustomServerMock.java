import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import be.seeseemelk.mockbukkit.AsyncCatcher;
import be.seeseemelk.mockbukkit.ServerMock;
import dev.jorel.commandapi.Brigadier;

public class CustomServerMock extends ServerMock {

	@SuppressWarnings("unchecked")
	@Override
	public boolean dispatchCommand(CommandSender sender, String commandLine) {
		String[] commands = commandLine.split(" ");
		String commandLabel = commands[0];
		Command command = getCommandMap().getCommand(commandLabel);
		
		if(command != null) {
			return super.dispatchCommand(sender, commandLine);
		} else {
			AsyncCatcher.catchOp("command dispatch");
			@SuppressWarnings("rawtypes")
			CommandDispatcher dispatcher = Brigadier.getCommandDispatcher();
			Object css = Brigadier.getBrigadierSourceFromCommandSender(sender);
			try {
				return dispatcher.execute(commandLine, css) != 0;
			} catch (CommandSyntaxException e) {
				e.printStackTrace();
			}
			return false;
		}
	}
	
	public void die() {
		
	}
	
}
