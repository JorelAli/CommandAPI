package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.nms.NMS;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CommandArgument extends Argument<CommandResult> implements IGreedyArgument {
	public CommandArgument(String nodeName) {
		super(nodeName, StringArgumentType.greedyString());

		applySuggestions();
	}

	private void applySuggestions() {
		this.replaceSuggestions((info, builder) -> {
			CommandSender sender = info.sender();
			// The greedy string, being filled by a command
			String command = info.currentArg();

			// Identify the position of the current argument
			int start;
			if (command.contains(" ")) {
				// Current command contains spaces - argument starts after the last space and after the start of the command.
				start = builder.getStart() + command.lastIndexOf(' ') + 1;
			} else {
				// Input starts at the start of this argument
				start = builder.getStart();
			}

			// get location sender is looking at if they are a Player, matching vanilla behavior
			// no builtin Commands seem to use the location parameter, but they could
			Location location = null;
			if (sender instanceof Player player) {
				Block block = player.getTargetBlockExact(5, FluidCollisionMode.NEVER);
				if (block != null) location = block.getLocation();
			}

			// Tab complete using Server's command map
			List<String> results = CommandAPIHandler.getInstance().getNMS().getSimpleCommandMap().tabComplete(sender, command, location);

			// No applicable commands
			if (results == null)
				throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(new StringReader(command));

			// remove / that gets prefixed to command name if the sender is a player
			if (!command.contains(" ") && sender instanceof Player)
				results = results.stream().map(s -> s.substring(1)).toList();

			// build suggestions for new argument
			builder = builder.createOffset(start);
			results.forEach(builder::suggest);
			return builder.buildFuture();
		});
	}

	@Override
	public Class<CommandResult> getPrimitiveType() {
		return CommandResult.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.COMMAND;
	}

	@Override
	public <CommandSourceStack> CommandResult parseArgument(NMS<CommandSourceStack> nms, CommandContext<CommandSourceStack> cmdCtx, String key, Object[] previousArgs) throws CommandSyntaxException {
		// extract information
		String command = cmdCtx.getArgument(key, String.class);
		CommandMap commandMap = nms.getSimpleCommandMap();

		// verify command
		String[] args = StringUtils.split(command, ' ');
		String commandLabel = args[0];
		Command target = commandMap.getCommand(commandLabel);
		if (target == null)
			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(new StringReader(command));

		return new CommandResult(target, Arrays.copyOfRange(args, 1, args.length));
	}
}

