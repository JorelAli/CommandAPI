package dev.jorel.commandapi.test;

import dev.jorel.commandapi.CommandRegistrationStrategy;
import dev.jorel.commandapi.InternalSpigotConfig;
import dev.jorel.commandapi.SpigotCommandRegistration;
import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.nms.SpigotNMS_1_20_R3;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_20_R3.command.BukkitCommandWrapper;
import org.bukkit.craftbukkit.v1_20_R3.command.VanillaCommandWrapper;

/**
 * The Spigot platform for testing. This is the real {@link SpigotNMS_1_20_R3}, except that it wraps its Bukkit NMS
 * implementation in a {@link MockNMS}, like {@link SpigotNMS_1_20_R3} wraps the Bukkit {@code NMS_1_20_R3} implementation.
 */
public class TestSpigotNMS_1_20_R3 extends SpigotNMS_1_20_R3 {
	// This must not have an initializer. It is set by bukkitNMS(), which CommandAPISpigot's constructor
	//  calls before this class's field initializers would run.
	private MockNMS mockNMS;

	public TestSpigotNMS_1_20_R3(InternalSpigotConfig config) {
		super(config);
	}

	@Override
	public NMS<CommandSourceStack> bukkitNMS() {
		if (mockNMS == null) {
			mockNMS = new MockNMS(super.bukkitNMS());
		}
		return mockNMS;
	}

	@Override
	public CommandRegistrationStrategy<CommandSourceStack> createCommandRegistrationStrategy() {
		// Same as SpigotNMS_1_20_R3, but using the mocked MinecraftServer instead of the real Bukkit NMS one
		MinecraftServer server = mockNMS.getMinecraftServer();
		return new SpigotCommandRegistration<>(
			server.vanillaCommandDispatcher.getDispatcher(),
			(SimpleCommandMap) getCommandMap(),
			() -> server.getCommands().getDispatcher(),
			command -> command instanceof VanillaCommandWrapper,
			node -> new VanillaCommandWrapper(server.vanillaCommandDispatcher, node),
			node -> node.getCommand() instanceof BukkitCommandWrapper
		);
	}
}
