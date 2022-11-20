package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.nms.NMS;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

/**
 * An argument that represents the Bukkit {@link World} object
 *
 * @apiNote Returns a {@link World} object
 */
public class WorldArgument extends Argument<World> {

	public WorldArgument(String nodeName) {
		super(nodeName, StringArgumentType.word());

		addSuggestions();
	}

	private void addSuggestions() {
		super.replaceSuggestions((info, builder) -> {
			String currentArg = info.currentArg();

			for (World world : Bukkit.getWorlds()) {
				String worldName = world.getName();
				if (worldName.startsWith(currentArg)) {
					builder.suggest(worldName);
				}
			}

			return builder.buildFuture();
		});
	}

	@Override
	public Class<World> getPrimitiveType() {
		return World.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.WORLD;
	}

	@Override
	public <CommandSourceStack> World parseArgument(NMS<CommandSourceStack> nms, CommandContext<CommandSourceStack> cmdCtx, String key, Object[] previousArgs) throws CommandSyntaxException {
		// Get provided argument
		String world = cmdCtx.getArgument(key, String.class);

		// Verify argument
		World result = Bukkit.getWorld(world);
		if (result == null) {
			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(new StringReader("Unknown world: " + world));
		}

		return result;
	}
}
