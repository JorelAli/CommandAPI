package dev.jorel.commandapi.test;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import dev.jorel.commandapi.CommandRegistrationStrategy;
import dev.jorel.commandapi.MockPaperCommandRegistration;
import dev.jorel.commandapi.commandsenders.AbstractCommandSender;
import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.nms.PaperNMS;
import dev.jorel.commandapi.nms.NMS_1_20_R4;
import dev.jorel.commandapi.nms.PaperNMS_1_20_R4;
import io.papermc.paper.command.brigadier.argument.VanillaArgumentProviderImpl.NativeWrapperArgumentType;
import net.minecraft.SharedConstants;
import net.minecraft.commands.CommandSigningContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import io.papermc.paper.command.brigadier.PaperCommands;
import io.papermc.paper.registry.PaperRegistryAccess;
import com.mojang.serialization.Lifecycle;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.server.Bootstrap;
import net.minecraft.network.chat.ChatDecorator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.TaskChainer;
import net.minecraft.world.flag.FeatureFlags;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.entity.Entity;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Paper version of {@link MockNMS}. Like {@link PaperNMS_1_20_R4} wraps the Bukkit {@code NMS_1_20_R4}
 * implementation, this wraps the Bukkit {@link MockNMS} and adds the Paper-specific parts.
 */
public class PaperMockNMS extends MockNMS implements PaperNMS<CommandSourceStack> {
	public PaperMockNMS() {
		// Same as PaperNMS_1_20_R4#bukkitNMS, except PaperNMS_1_20_R4 gets its CommandBuildContext from the CraftServer
		super(setupRegistries(new NMS_1_20_R4(PaperMockNMS::getCommandBuildContext)));

		// Paper's argument types API gets its CommandBuildContext from PaperCommands, which is normally set up by the server
		PaperCommands.INSTANCE.setDispatcher(this.<MinecraftServer>getMinecraftServer().getCommands(), getCommandBuildContext());

		// Some of Paper's argument resolvers (e.g. for worlds) use the static MinecraftServer#getServer
		setField(MinecraftServer.class, "SERVER", null, this.<MinecraftServer>getMinecraftServer());
	}

	/**
	 * The built-in registries, plus the biome registry, which is normally loaded from data packs. This is shared by all
	 * tests, because building the biome registry from the vanilla data takes a while.
	 */
	private static RegistryAccess.Frozen registryAccess;

	private static CommandBuildContext commandBuildContext;

	private static CommandBuildContext getCommandBuildContext() {
		if (commandBuildContext == null) {
			commandBuildContext = CommandBuildContext.simple(registryAccess, FeatureFlags.REGISTRY.allFlags());
		}
		return commandBuildContext;
	}

	/**
	 * Paper's Bukkit registries (potion effects, enchantments, biomes, etc.) are backed by the real Minecraft registries,
	 * so these need to exist before {@link MockNMS} uses them. Otherwise, {@link MockNMS} replaces them with a mock.
	 */
	private static <T> T setupRegistries(T baseNMS) {
		SharedConstants.tryDetectVersion();
		Bootstrap.bootStrap();
		if (registryAccess == null) {
			MappedRegistry<Biome> biomes = new MappedRegistry<>(Registries.BIOME, Lifecycle.stable());
			VanillaRegistries.createLookup().lookupOrThrow(Registries.BIOME).listElements()
				.forEach(biome -> biomes.register(biome.key(), biome.value(), RegistrationInfo.BUILT_IN));
			biomes.freeze();

			List<Registry<?>> registries = new ArrayList<>(BuiltInRegistries.REGISTRY.stream().toList());
			registries.add(biomes);
			registryAccess = new RegistryAccess.ImmutableRegistryAccess(registries).freeze();

			// Paper normally registers the Bukkit registries for data pack registries once those are loaded
			PaperRegistryAccess.instance().registerRegistry(Registries.BIOME, biomes);
		}
		if (CraftRegistry.getMinecraftRegistry() == null) {
			CraftRegistry.setMinecraftRegistry(registryAccess);
		}
		return baseNMS;
	}

	@Override
	public NMS<CommandSourceStack> bukkitNMS() {
		return this;
	}

	@Override
	public CommandRegistrationStrategy<CommandSourceStack> createCommandRegistrationStrategy() {
		// Same dispatcher as PaperNMS_1_20_R4 uses
		return new MockPaperCommandRegistration<>(
			() -> this.<MinecraftServer>getMinecraftServer().getCommands().getDispatcher()
		);
	}

	@Override
	protected void setupCommandDispatchers(MinecraftServer minecraftServerMock) {
		// Paper has neither MinecraftServer#vanillaCommandDispatcher nor the no-argument Commands constructor
		Commands resourcesCommands = Mockito.mock(Commands.class);
		Mockito.when(resourcesCommands.getDispatcher()).thenReturn(getMockResourcesDispatcher());
		Mockito.when(minecraftServerMock.getCommands()).thenReturn(resourcesCommands);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void createDispatcherFile(File file, CommandDispatcher dispatcher) throws IOException {
		// On Paper, commands go through Paper's command registrar before the dispatcher file is written, which
		//  replaces Paper's argument types with the vanilla ones they wrap. MockPaperCommandRegistration doesn't
		//  do that, so we do it here, otherwise vanilla can't serialize the Paper argument types.
		RootCommandNode root = (RootCommandNode) unwrapPaperArguments(dispatcher.getRoot(), new IdentityHashMap<>());
		super.createDispatcherFile(file, new CommandDispatcher<>(root));
	}

	/**
	 * Copies a command tree, replacing Paper's {@link NativeWrapperArgumentType}s with the vanilla argument types
	 * they wrap
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static CommandNode unwrapPaperArguments(CommandNode node, Map<CommandNode, CommandNode> copies) {
		CommandNode existing = copies.get(node);
		if (existing != null) {
			return existing;
		}

		CommandNode copy;
		if (node instanceof RootCommandNode) {
			copy = new RootCommandNode<>();
		} else {
			ArgumentBuilder builder;
			if (node instanceof ArgumentCommandNode argument && argument.getType() instanceof NativeWrapperArgumentType wrapper) {
				builder = RequiredArgumentBuilder.argument(argument.getName(), wrapper.nativeNmsArgumentType())
					.suggests(argument.getCustomSuggestions())
					.requires(argument.getRequirement())
					.executes(argument.getCommand());
			} else {
				builder = node.createBuilder();
			}
			if (node.getRedirect() != null) {
				builder.forward(unwrapPaperArguments(node.getRedirect(), copies), node.getRedirectModifier(), node.isFork());
			}
			copy = builder.build();
		}

		copies.put(node, copy);
		for (Object child : node.getChildren()) {
			copy.addChild(unwrapPaperArguments((CommandNode) child, copies));
		}
		return copy;
	}

	@Override
	public CommandSourceStack getBrigadierSourceFromCommandSender(AbstractCommandSender<? extends CommandSender> senderWrapper) {
		CommandSourceStack css = super.getBrigadierSourceFromCommandSender(senderWrapper);

		// Paper's Brigadier uses this field in CommandNode#canUse. It's normally initialized in the constructor,
		//  which isn't called for a mock.
		css.currentCommand = new ConcurrentHashMap<>();

		// On Paper, the NMS CommandSourceStack also implements Paper's CommandSourceStack API, which Paper's
		//  argument resolvers use. These need to be stubbed because the source is a mock.
		CommandSender sender = senderWrapper.getSource();
		Mockito.when(css.getSender()).thenReturn(sender);
		Mockito.when(css.getExecutor()).thenAnswer(i -> sender instanceof Entity entity ? entity : null);
		Mockito.when(css.getLocation()).thenAnswer(i -> sender instanceof Entity entity ? entity.getLocation() : null);

		// Paper's signed message resolver (used by ChatArgument) looks for signed arguments. These tests don't sign them,
		//  so the message is resolved as an unsigned one, which goes through the server's chat decorator.
		Mockito.when(css.getSigningContext()).thenReturn(CommandSigningContext.ANONYMOUS);
		Mockito.when(css.getChatMessageChainer()).thenReturn(TaskChainer.immediate(Runnable::run));
		Mockito.when(this.<MinecraftServer>getMinecraftServer().getChatDecorator()).thenReturn(new ChatDecorator() {
			// Paper's ChatDecorator#PLAIN doesn't implement the version used for commands
			@Override
			public CompletableFuture<Component> decorate(ServerPlayer player, Component message) {
				return CompletableFuture.completedFuture(message);
			}

			@Override
			public CompletableFuture<Component> decorate(ServerPlayer player, CommandSourceStack source, Component message) {
				return CompletableFuture.completedFuture(message);
			}
		});

		return css;
	}
}
