import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.ItemStack;
import org.mockito.Mockito;

import com.google.common.io.Files;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import be.seeseemelk.mockbukkit.WorldMock;
import dev.jorel.commandapi.arguments.SuggestionProviders;
import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.wrappers.ParticleData;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Advancements;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.arguments.ArgumentAnchor;
import net.minecraft.commands.arguments.ArgumentAngle;
import net.minecraft.commands.arguments.ArgumentChat;
import net.minecraft.commands.arguments.ArgumentChatComponent;
import net.minecraft.commands.arguments.ArgumentChatFormat;
import net.minecraft.commands.arguments.ArgumentCriterionValue;
import net.minecraft.commands.arguments.ArgumentDimension;
import net.minecraft.commands.arguments.ArgumentEnchantment;
import net.minecraft.commands.arguments.ArgumentEntity;
import net.minecraft.commands.arguments.ArgumentEntitySummon;
import net.minecraft.commands.arguments.ArgumentInventorySlot;
import net.minecraft.commands.arguments.ArgumentMathOperation;
import net.minecraft.commands.arguments.ArgumentMinecraftKeyRegistered;
import net.minecraft.commands.arguments.ArgumentMobEffect;
import net.minecraft.commands.arguments.ArgumentNBTBase;
import net.minecraft.commands.arguments.ArgumentNBTKey;
import net.minecraft.commands.arguments.ArgumentNBTTag;
import net.minecraft.commands.arguments.ArgumentParticle;
import net.minecraft.commands.arguments.ArgumentProfile;
import net.minecraft.commands.arguments.ArgumentScoreboardCriteria;
import net.minecraft.commands.arguments.ArgumentScoreboardObjective;
import net.minecraft.commands.arguments.ArgumentScoreboardSlot;
import net.minecraft.commands.arguments.ArgumentScoreboardTeam;
import net.minecraft.commands.arguments.ArgumentScoreholder;
import net.minecraft.commands.arguments.ArgumentTime;
import net.minecraft.commands.arguments.ArgumentUUID;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.ResourceOrTagLocationArgument;
import net.minecraft.commands.arguments.TemplateMirrorArgument;
import net.minecraft.commands.arguments.TemplateRotationArgument;
import net.minecraft.commands.arguments.blocks.ArgumentBlockPredicate;
import net.minecraft.commands.arguments.blocks.ArgumentTile;
import net.minecraft.commands.arguments.coordinates.ArgumentPosition;
import net.minecraft.commands.arguments.coordinates.ArgumentRotation;
import net.minecraft.commands.arguments.coordinates.ArgumentRotationAxis;
import net.minecraft.commands.arguments.coordinates.ArgumentVec2;
import net.minecraft.commands.arguments.coordinates.ArgumentVec2I;
import net.minecraft.commands.arguments.coordinates.ArgumentVec3;
import net.minecraft.commands.arguments.item.ArgumentItemPredicate;
import net.minecraft.commands.arguments.item.ArgumentItemStack;
import net.minecraft.commands.arguments.item.ArgumentTag;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.commands.synchronization.brigadier.ArgumentSerializerString;
import net.minecraft.commands.synchronization.brigadier.DoubleArgumentInfo;
import net.minecraft.commands.synchronization.brigadier.FloatArgumentInfo;
import net.minecraft.commands.synchronization.brigadier.IntegerArgumentInfo;
import net.minecraft.commands.synchronization.brigadier.LongArgumentInfo;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.AdvancementDataWorld;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.Vec3D;

public class MockNMS extends ArgumentNMS {

	public MockNMS(NMS<?> baseNMS) {
		super(baseNMS);
		try {
			initializeArgumentsInArgumentTypeInfos();
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String[] compatibleVersions() {
		return new String[] { "1.19" };
	}

	CommandDispatcher<CommandListenerWrapper> dispatcher;

	@Override
	public CommandDispatcher<CommandListenerWrapper> getBrigadierDispatcher() {
		if (this.dispatcher == null) {
			this.dispatcher = new CommandDispatcher<>();
		}
		return this.dispatcher;
	}

	@Override
	public CommandListenerWrapper getCLWFromCommandSender(CommandSender sender) {
		CommandListenerWrapper clw = Mockito.mock(CommandListenerWrapper.class);
		Mockito.when(clw.getBukkitSender()).thenReturn(sender);
		
		if(sender instanceof Player player) {
			Location loc = player.getLocation();
			Mockito.when(clw.e()).thenReturn(new Vec3D(loc.getX(), loc.getY(), loc.getZ()));
			
			MinecraftServer ms = Mockito.mock(MinecraftServer.class);
			Mockito.when(ms.az()).thenReturn(mockAdvancementDataWorld());
			Mockito.when(clw.m()).thenReturn(ms);
		}
		return clw;
	}
	
	public AdvancementDataWorld mockAdvancementDataWorld() {
		AdvancementDataWorld advancementDataWorld = new AdvancementDataWorld(null);
		Advancements advancements = (Advancements) getField(AdvancementDataWorld.class, "c", advancementDataWorld);
		
		advancements.b.put(new MinecraftKey("my:advancement"), new Advancement(new MinecraftKey("my:advancement"), null, null, null, new HashMap<>(), null));
		advancements.b.put(new MinecraftKey("my:advancement2"), new Advancement(new MinecraftKey("my:advancement2"), null, null, null, new HashMap<>(), null));
		return advancementDataWorld;
	}
	
	public Object getField(Class<?> className, String fieldName, Object instance) {
		try {
			Field field = className.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(instance);
		} catch (ReflectiveOperationException e) {
			return null;
		}
	}

	@Override
	public CommandSender getCommandSenderFromCSS(CommandListenerWrapper clw) {
		try {
			return clw.getBukkitSender();
		} catch (UnsupportedOperationException e) {
			return null;
		}
	}

	@Override
	public CommandSender getSenderForCommand(CommandContext<CommandListenerWrapper> cmdCtx, boolean forceNative) {
		CommandListenerWrapper css = cmdCtx.getSource();
		return css.getBukkitSender();
	}

	@Override
	public void createDispatcherFile(File file, CommandDispatcher<CommandListenerWrapper> dispatcher)
			throws IOException {
		Files
			.asCharSink(file, StandardCharsets.UTF_8)
			.write(new GsonBuilder()
				.setPrettyPrinting()
				.create()
				.toJson(DispatcherUtil.toJSON(dispatcher, dispatcher.getRoot())));
	}

	@SuppressWarnings("rawtypes")
	private void initializeArgumentsInArgumentTypeInfos() throws ReflectiveOperationException {
		Field f = ArgumentTypeInfos.class.getDeclaredField("a");
		f.setAccessible(true);
		@SuppressWarnings("unchecked")
		Map<Class<?>, ArgumentTypeInfo<?, ?>> map = (Map<Class<?>, ArgumentTypeInfo<?, ?>>) f.get(null);
		map.put(BoolArgumentType.class, SingletonArgumentInfo.a(BoolArgumentType::bool));
		map.put(FloatArgumentType.class, new FloatArgumentInfo());
		map.put(DoubleArgumentType.class, new DoubleArgumentInfo());
		map.put(IntegerArgumentType.class, new IntegerArgumentInfo());
		map.put(LongArgumentType.class, new LongArgumentInfo());
		map.put(StringArgumentType.class, new ArgumentSerializerString());
		map.put(ArgumentEntity.class, new ArgumentEntity.Info());
		map.put(ArgumentProfile.class, SingletonArgumentInfo.a(ArgumentProfile::a));
		map.put(ArgumentPosition.class, SingletonArgumentInfo.a(ArgumentPosition::a));
		map.put(ArgumentVec2I.class, SingletonArgumentInfo.a(ArgumentVec2I::a));
		map.put(ArgumentVec3.class, SingletonArgumentInfo.a((Supplier<ArgumentType<?>>) ArgumentVec3::a));
		map.put(ArgumentVec2.class, SingletonArgumentInfo.a((Supplier<ArgumentType<?>>) ArgumentVec2::a));
		map.put(ArgumentTile.class, SingletonArgumentInfo.a((Function<CommandBuildContext, ArgumentType<?>>) ArgumentTile::a));
		map.put(ArgumentBlockPredicate.class, SingletonArgumentInfo.a((Function<CommandBuildContext, ArgumentType<?>>) ArgumentBlockPredicate::a));
		map.put(ArgumentItemStack.class, SingletonArgumentInfo.a((Function<CommandBuildContext, ArgumentType<?>>) ArgumentItemStack::a));
		map.put(ArgumentItemPredicate.class, SingletonArgumentInfo.a((Function<CommandBuildContext, ArgumentType<?>>) ArgumentItemPredicate::a));
		map.put(ArgumentChatFormat.class, SingletonArgumentInfo.a(ArgumentChatFormat::a));
		map.put(ArgumentChatComponent.class, SingletonArgumentInfo.a(ArgumentChatComponent::a));
		map.put(ArgumentChat.class, SingletonArgumentInfo.a(ArgumentChat::a));
		map.put(ArgumentNBTTag.class, SingletonArgumentInfo.a(ArgumentNBTTag::a));
		map.put(ArgumentNBTBase.class, SingletonArgumentInfo.a(ArgumentNBTBase::a));
		map.put(ArgumentNBTKey.class, SingletonArgumentInfo.a((Supplier<ArgumentType<?>>) ArgumentNBTKey::a));
		map.put(ArgumentScoreboardObjective.class, SingletonArgumentInfo.a(ArgumentScoreboardObjective::a));
		map.put(ArgumentScoreboardCriteria.class, SingletonArgumentInfo.a(ArgumentScoreboardCriteria::a));
		map.put(ArgumentMathOperation.class, SingletonArgumentInfo.a((Supplier<ArgumentType<?>>) ArgumentMathOperation::a));
		map.put(ArgumentParticle.class, SingletonArgumentInfo.a(ArgumentParticle::a));
		map.put(ArgumentAngle.class, SingletonArgumentInfo.a(ArgumentAngle::a));
		map.put(ArgumentRotation.class, SingletonArgumentInfo.a(ArgumentRotation::a));
		map.put(ArgumentScoreboardSlot.class, SingletonArgumentInfo.a(ArgumentScoreboardSlot::a));
		map.put(ArgumentScoreholder.class, new ArgumentScoreholder.a());
		map.put(ArgumentRotationAxis.class, SingletonArgumentInfo.a(ArgumentRotationAxis::a));
		map.put(ArgumentScoreboardTeam.class, SingletonArgumentInfo.a(ArgumentScoreboardTeam::a));
		map.put(ArgumentInventorySlot.class, SingletonArgumentInfo.a(ArgumentInventorySlot::a));
		map.put(ArgumentMinecraftKeyRegistered.class, SingletonArgumentInfo.a(ArgumentMinecraftKeyRegistered::a));
		map.put(ArgumentMobEffect.class, SingletonArgumentInfo.a(ArgumentMobEffect::a));
		map.put(ArgumentTag.class, SingletonArgumentInfo.a(ArgumentTag::a));
		map.put(ArgumentAnchor.class, SingletonArgumentInfo.a(ArgumentAnchor::a));
		map.put(ArgumentCriterionValue.b.class, SingletonArgumentInfo.a(ArgumentCriterionValue::a));
		map.put(ArgumentCriterionValue.a.class, SingletonArgumentInfo.a(ArgumentCriterionValue::b));
		map.put(ArgumentEnchantment.class, SingletonArgumentInfo.a(ArgumentEnchantment::a));
		map.put(ArgumentEntitySummon.class, SingletonArgumentInfo.a((Supplier<ArgumentType<?>>) ArgumentEntitySummon::a));
		map.put(ArgumentDimension.class, SingletonArgumentInfo.a(ArgumentDimension::a));
		map.put(ArgumentTime.class, SingletonArgumentInfo.a(ArgumentTime::a));
		map.put(ResourceOrTagLocationArgument.class, new ResourceOrTagLocationArgument.a());
		map.put(ResourceKeyArgument.class, new ResourceKeyArgument.a());
		map.put(TemplateMirrorArgument.class, SingletonArgumentInfo.a(TemplateMirrorArgument::a));
		map.put(TemplateRotationArgument.class, SingletonArgumentInfo.a(TemplateRotationArgument::a));
		map.put(ArgumentUUID.class, SingletonArgumentInfo.a(ArgumentUUID::a));
	}
	
	@Override
	public World getWorldForCSS(CommandListenerWrapper clw) {
		return new WorldMock();
	}

	@Override
	public SimpleCommandMap getSimpleCommandMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addToHelpMap(Map<String, HelpTopic> helpTopicsToAdd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String convert(ItemStack is) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String convert(ParticleData<?> particle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HelpTopic generateHelpTopic(String commandName, String shortDescription, String fullDescription, String permission) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SuggestionProvider<CommandListenerWrapper> getSuggestionProvider(SuggestionProviders provider) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isVanillaCommandWrapper(Command command) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reloadDataPacks() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resendPackets(Player player) {
		// TODO Auto-generated method stub
		
	}

}
