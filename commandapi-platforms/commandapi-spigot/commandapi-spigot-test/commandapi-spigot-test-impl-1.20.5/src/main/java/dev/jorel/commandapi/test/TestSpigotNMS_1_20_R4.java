package dev.jorel.commandapi.test;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.CommandRegistrationStrategy;
import dev.jorel.commandapi.InternalSpigotConfig;
import dev.jorel.commandapi.SpigotCommandRegistration;
import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.nms.NMS_1_20_R4;
import dev.jorel.commandapi.nms.SpigotNMS_1_20_R4;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.flag.FeatureFlags;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_20_R4.command.BukkitCommandWrapper;
import org.bukkit.craftbukkit.v1_20_R4.command.VanillaCommandWrapper;

/**
 * The Spigot platform for testing. This is the real {@link SpigotNMS_1_20_R4}, except that it wraps its Bukkit NMS
 * implementation in a {@link MockNMS}, like {@link SpigotNMS_1_20_R4} wraps the Bukkit {@code NMS_1_20_R4} implementation.
 */
public class TestSpigotNMS_1_20_R4 extends SpigotNMS_1_20_R4 {
	// This must not have an initializer. It is set by bukkitNMS(), which CommandAPISpigot's constructor
	//  calls before this class's field initializers would run.
	private MockNMS mockNMS;

	// SpigotNMS_1_20_R4 only creates its CommandBuildContext on a CraftServer, so this one uses the built-in registries.
	//  Like mockNMS, this must not have an initializer. It is created when first used, after MockNMS bootstraps Minecraft.
	private CommandBuildContext commandBuildContext;

	public TestSpigotNMS_1_20_R4(InternalSpigotConfig config) {
		super(config);
	}

	private CommandBuildContext getCommandBuildContext() {
		if (commandBuildContext == null) {
			commandBuildContext = CommandBuildContext.simple(
				RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), FeatureFlags.REGISTRY.allFlags()
			);
		}
		return commandBuildContext;
	}

	@Override
	public NMS<CommandSourceStack> bukkitNMS() {
		if (mockNMS == null) {
			// Same as SpigotNMS_1_20_R4, but with our CommandBuildContext
			mockNMS = new MockNMS(new NMS_1_20_R4(this::getCommandBuildContext));
		}
		return mockNMS;
	}

	@Override
	public BaseComponent[] getChat(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		// Same as SpigotNMS_1_20_R4, but with our CommandBuildContext
		return ComponentSerializer.parse(Component.Serializer.toJson(MessageArgument.getMessage(cmdCtx, key), getCommandBuildContext()));
	}

	@Override
	public BaseComponent[] getChatComponent(CommandContext<CommandSourceStack> cmdCtx, String key) {
		// Same as SpigotNMS_1_20_R4, but with our CommandBuildContext
		return ComponentSerializer.parse(Component.Serializer.toJson(ComponentArgument.getComponent(cmdCtx, key), getCommandBuildContext()));
	}

	@Override
	public CommandRegistrationStrategy<CommandSourceStack> createCommandRegistrationStrategy() {
		// Same as SpigotNMS_1_20_R4, but using the mocked MinecraftServer instead of the real Bukkit NMS one
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
