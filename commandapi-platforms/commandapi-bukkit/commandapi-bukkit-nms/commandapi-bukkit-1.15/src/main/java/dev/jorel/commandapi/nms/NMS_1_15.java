package dev.jorel.commandapi.nms;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import com.mojang.brigadier.tree.CommandNode;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_15_R1.CraftLootTable;
import org.bukkit.craftbukkit.v1_15_R1.CraftParticle;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftSound;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_15_R1.command.BukkitCommandWrapper;
import org.bukkit.craftbukkit.v1_15_R1.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.v1_15_R1.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.help.CustomHelpTopic;
import org.bukkit.craftbukkit.v1_15_R1.help.SimpleHelpMap;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_15_R1.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftChatMessage;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.ComplexRecipe;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import com.google.common.io.Files;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.SafeVarHandle;
import dev.jorel.commandapi.arguments.ArgumentSubType;
import dev.jorel.commandapi.arguments.SuggestionProviders;
import dev.jorel.commandapi.commandsenders.AbstractCommandSender;
import dev.jorel.commandapi.commandsenders.BukkitCommandSender;
import dev.jorel.commandapi.commandsenders.BukkitNativeProxyCommandSender;
import dev.jorel.commandapi.exceptions.UnimplementedArgumentException;
import dev.jorel.commandapi.preprocessor.Differs;
import dev.jorel.commandapi.preprocessor.NMSMeta;
import dev.jorel.commandapi.preprocessor.RequireField;
import dev.jorel.commandapi.wrappers.ComplexRecipeImpl;
import dev.jorel.commandapi.wrappers.FloatRange;
import dev.jorel.commandapi.wrappers.FunctionWrapper;
import dev.jorel.commandapi.wrappers.IntegerRange;
import dev.jorel.commandapi.wrappers.Location2D;
import dev.jorel.commandapi.wrappers.MathOperation;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import dev.jorel.commandapi.wrappers.ParticleData;
import dev.jorel.commandapi.wrappers.Rotation;
import dev.jorel.commandapi.wrappers.ScoreboardSlot;
import dev.jorel.commandapi.wrappers.SimpleFunctionWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_15_R1.Advancement;
import net.minecraft.server.v1_15_R1.ArgumentBlockPredicate;
import net.minecraft.server.v1_15_R1.ArgumentChat;
import net.minecraft.server.v1_15_R1.ArgumentChatComponent;
import net.minecraft.server.v1_15_R1.ArgumentChatFormat;
import net.minecraft.server.v1_15_R1.ArgumentCriterionValue;
import net.minecraft.server.v1_15_R1.ArgumentDimension;
import net.minecraft.server.v1_15_R1.ArgumentEnchantment;
import net.minecraft.server.v1_15_R1.ArgumentEntity;
import net.minecraft.server.v1_15_R1.ArgumentEntitySummon;
import net.minecraft.server.v1_15_R1.ArgumentItemPredicate;
import net.minecraft.server.v1_15_R1.ArgumentItemStack;
import net.minecraft.server.v1_15_R1.ArgumentMathOperation;
import net.minecraft.server.v1_15_R1.ArgumentMinecraftKeyRegistered;
import net.minecraft.server.v1_15_R1.ArgumentMobEffect;
import net.minecraft.server.v1_15_R1.ArgumentNBTTag;
import net.minecraft.server.v1_15_R1.ArgumentParticle;
import net.minecraft.server.v1_15_R1.ArgumentPosition;
import net.minecraft.server.v1_15_R1.ArgumentPredicateItemStack;
import net.minecraft.server.v1_15_R1.ArgumentProfile;
import net.minecraft.server.v1_15_R1.ArgumentRegistry;
import net.minecraft.server.v1_15_R1.ArgumentRotation;
import net.minecraft.server.v1_15_R1.ArgumentRotationAxis;
import net.minecraft.server.v1_15_R1.ArgumentScoreboardCriteria;
import net.minecraft.server.v1_15_R1.ArgumentScoreboardObjective;
import net.minecraft.server.v1_15_R1.ArgumentScoreboardSlot;
import net.minecraft.server.v1_15_R1.ArgumentScoreboardTeam;
import net.minecraft.server.v1_15_R1.ArgumentScoreholder;
import net.minecraft.server.v1_15_R1.ArgumentTag;
import net.minecraft.server.v1_15_R1.ArgumentTile;
import net.minecraft.server.v1_15_R1.ArgumentTime;
import net.minecraft.server.v1_15_R1.ArgumentVec2;
import net.minecraft.server.v1_15_R1.ArgumentVec2I;
import net.minecraft.server.v1_15_R1.ArgumentVec3;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.BlockPosition2D;
import net.minecraft.server.v1_15_R1.CommandListenerWrapper;
import net.minecraft.server.v1_15_R1.CompletionProviders;
import net.minecraft.server.v1_15_R1.CriterionConditionValue;
import net.minecraft.server.v1_15_R1.CustomFunction;
import net.minecraft.server.v1_15_R1.CustomFunctionData;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.EntitySelector;
import net.minecraft.server.v1_15_R1.EnumChatFormat;
import net.minecraft.server.v1_15_R1.EnumDirection.EnumAxis;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_15_R1.ICompletionProvider;
import net.minecraft.server.v1_15_R1.IRecipe;
import net.minecraft.server.v1_15_R1.IRegistry;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.ParticleParam;
import net.minecraft.server.v1_15_R1.ParticleParamBlock;
import net.minecraft.server.v1_15_R1.ParticleParamItem;
import net.minecraft.server.v1_15_R1.ParticleParamRedstone;
import net.minecraft.server.v1_15_R1.ParticleType;
import net.minecraft.server.v1_15_R1.ShapeDetectorBlock;
import net.minecraft.server.v1_15_R1.Vec2F;
import net.minecraft.server.v1_15_R1.Vec3D;

/**
 * NMS implementation for Minecraft 1.15, 1.15.1 and 1.15.2
 */
@NMSMeta(compatibleWith = { "1.15", "1.15.1", "1.15.2" })
@RequireField(in = SimpleHelpMap.class, name = "helpTopics", ofType = Map.class)
@RequireField(in = EntitySelector.class, name = "checkPermissions", ofType = boolean.class)
@RequireField(in = ParticleParamBlock.class, name = "c", ofType = IBlockData.class)
@RequireField(in = ParticleParamItem.class, name = "c", ofType = ItemStack.class)
@RequireField(in = ParticleParamRedstone.class, name = "f", ofType = float.class)
@RequireField(in = ArgumentPredicateItemStack.class, name = "c", ofType = NBTTagCompound.class)
@RequireField(in = CraftSound.class, name = "minecraftKey", ofType = String.class)
public class NMS_1_15 extends NMSWrapper_1_15 {

	private static final SafeVarHandle<SimpleHelpMap, Map<String, HelpTopic>> helpMapTopics;
	private static final Field entitySelectorCheckPermissions;
	private static final SafeVarHandle<ParticleParamBlock, IBlockData> particleParamBlockData;
	private static final SafeVarHandle<ParticleParamItem, ItemStack> particleParamItemStack;
	private static final SafeVarHandle<ParticleParamRedstone, Float> particleParamRedstoneSize;
	private static final SafeVarHandle<ArgumentPredicateItemStack, NBTTagCompound> itemStackPredicateArgument;
	private static final SafeVarHandle<CraftSound, String> craftSoundMinecraftKey;

	// Compute all var handles all in one go so we don't do this during main server
	// runtime
	static {
		helpMapTopics = SafeVarHandle.ofOrNull(SimpleHelpMap.class, "helpTopics", "helpTopics", Map.class);
		// For some reason, MethodHandles fails for this field, but Field works okay
		entitySelectorCheckPermissions = CommandAPIHandler.getField(EntitySelector.class, "checkPermissions", "checkPermissions");
		particleParamBlockData = SafeVarHandle.ofOrNull(ParticleParamBlock.class, "c", "c", IBlockData.class);
		particleParamItemStack = SafeVarHandle.ofOrNull(ParticleParamItem.class, "c", "c", ItemStack.class);
		particleParamRedstoneSize = SafeVarHandle.ofOrNull(ParticleParamRedstone.class, "f", "f", float.class);
		itemStackPredicateArgument = SafeVarHandle.ofOrNull(ArgumentPredicateItemStack.class, "c", "c", NBTTagCompound.class);
		craftSoundMinecraftKey = SafeVarHandle.ofOrNull(CraftSound.class, "minecraftKey", "minecraftKey", String.class);
	}

	@SuppressWarnings("deprecation")
	private static NamespacedKey fromMinecraftKey(MinecraftKey key) {
		return new NamespacedKey(key.getNamespace(), key.getKey());
	}

	@Override
	public ArgumentType<?> _ArgumentAngle() {
		throw new UnimplementedArgumentException("AngleArgument", "1.16.2");
	}

	@Override
	public ArgumentType<?> _ArgumentAxis() {
		return ArgumentRotationAxis.a();
	}

	@Override
	public ArgumentType<?> _ArgumentBlockPredicate() {
		return ArgumentBlockPredicate.a();
	}

	@Override
	public ArgumentType<?> _ArgumentBlockState() {
		return ArgumentTile.a();
	}

	@Override
	public ArgumentType<?> _ArgumentChat() {
		return ArgumentChat.a();
	}

	@Override
	public ArgumentType<?> _ArgumentChatComponent() {
		return ArgumentChatComponent.a();
	}

	@Override
	public ArgumentType<?> _ArgumentChatFormat() {
		return ArgumentChatFormat.a();
	}

	@Override
	public ArgumentType<?> _ArgumentDimension() {
		return ArgumentDimension.a();
	}

	@Override
	public ArgumentType<?> _ArgumentEnchantment() {
		return ArgumentEnchantment.a();
	}

	@Override
	public ArgumentType<?> _ArgumentEntity(ArgumentSubType subType) {
		return switch (subType) {
			case ENTITYSELECTOR_MANY_ENTITIES -> ArgumentEntity.multipleEntities();
			case ENTITYSELECTOR_MANY_PLAYERS -> ArgumentEntity.d();
			case ENTITYSELECTOR_ONE_ENTITY -> ArgumentEntity.a();
			case ENTITYSELECTOR_ONE_PLAYER -> ArgumentEntity.c();
			default -> throw new IllegalArgumentException("Unexpected value: " + subType);
		};
	}

	@Override
	public ArgumentType<?> _ArgumentEntitySummon() {
		return ArgumentEntitySummon.a();
	}

	@Override
	public ArgumentType<?> _ArgumentFloatRange() {
		return new ArgumentCriterionValue.a();
	}

	@Override
	public ArgumentType<?> _ArgumentIntRange() {
		return new ArgumentCriterionValue.b();
	}

	@Override
	public ArgumentType<?> _ArgumentItemPredicate() {
		return ArgumentItemPredicate.a();
	}

	@Override
	public ArgumentType<?> _ArgumentItemStack() {
		return ArgumentItemStack.a();
	}

	@Override
	public ArgumentType<?> _ArgumentMathOperation() {
		return ArgumentMathOperation.a();
	}

	@Override
	public ArgumentType<?> _ArgumentMinecraftKeyRegistered() {
		return ArgumentMinecraftKeyRegistered.a();
	}

	@Override
	public ArgumentType<?> _ArgumentMobEffect() {
		return ArgumentMobEffect.a();
	}

	@Override
	public ArgumentType<?> _ArgumentNBTCompound() {
		return ArgumentNBTTag.a();
	}

	@Override
	public ArgumentType<?> _ArgumentParticle() {
		return ArgumentParticle.a();
	}

	@Override
	public ArgumentType<?> _ArgumentPosition() {
		return ArgumentPosition.a();
	}

	@Override
	public ArgumentType<?> _ArgumentPosition2D() {
		return ArgumentVec2I.a();
	}

	@Override
	public ArgumentType<?> _ArgumentProfile() {
		return ArgumentProfile.a();
	}

	@Override
	public ArgumentType<?> _ArgumentRotation() {
		return ArgumentRotation.a();
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardCriteria() {
		return ArgumentScoreboardCriteria.a();
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardObjective() {
		return ArgumentScoreboardObjective.a();
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardSlot() {
		return ArgumentScoreboardSlot.a();
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardTeam() {
		return ArgumentScoreboardTeam.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentScoreholder(ArgumentSubType subType) {
		return switch(subType) {
			case SCOREHOLDER_SINGLE -> ArgumentScoreholder.a();
			case SCOREHOLDER_MULTIPLE -> ArgumentScoreholder.b();
			default -> throw new IllegalArgumentException("Unexpected value: " + subType);
		};
	}

	@Override
	public ArgumentType<?> _ArgumentSyntheticBiome() {
		throw new UnimplementedArgumentException("BiomeArgument", "1.16");
	}

	@Override
	public ArgumentType<?> _ArgumentTag() {
		return ArgumentTag.a();
	}

	@Override
	public ArgumentType<?> _ArgumentTime() {
		return ArgumentTime.a();
	}

	@Override
	public ArgumentType<?> _ArgumentUUID() {
		throw new UnimplementedArgumentException("UUIDArgument", "1.16");
	}

	@Override
	public ArgumentType<?> _ArgumentVec2(boolean centerPosition) {
		return new ArgumentVec2(centerPosition);
	}

	@Override
	public ArgumentType<?> _ArgumentVec3(boolean centerPosition) {
		return ArgumentVec3.a(centerPosition);
	}

	@Override
	public Map<String, HelpTopic> getHelpMap() {
		return helpMapTopics.get((SimpleHelpMap) Bukkit.getHelpMap());
	}

	@Override
	public String[] compatibleVersions() {
		return new String[] { "1.15", "1.15.1", "1.15.2" };
	}

	@Override
	public String convert(org.bukkit.inventory.ItemStack is) {
		return is.getType().getKey().toString() + CraftItemStack.asNMSCopy(is).getOrCreateTag().toString();
	}

	@Override
	public String convert(ParticleData<?> particle) {
		return CraftParticle.toNMS(particle.particle(), particle.data()).a();
	}

	@Override
	public String convert(PotionEffectType potion) {
		return potion.getName().toLowerCase(Locale.ENGLISH);
	}

	@Override
	public String convert(Sound sound) {
		return CraftSound.getSound(sound);
	}

	// Converts NMS function to SimpleFunctionWrapper
	private SimpleFunctionWrapper convertFunction(CustomFunction customFunction) {
		ToIntFunction<CommandListenerWrapper> appliedObj = clw -> this.<MinecraftServer>getMinecraftServer().getFunctionData().a(customFunction, clw);

		Object[] cArr = customFunction.b();
		String[] result = new String[cArr.length];
		for (int i = 0, size = cArr.length; i < size; i++) {
			result[i] = cArr[i].toString();
		}
		return new SimpleFunctionWrapper(fromMinecraftKey(customFunction.a()), appliedObj, result);
	}

	@Override
	public void createDispatcherFile(File file, CommandDispatcher<CommandListenerWrapper> dispatcher) throws IOException {
		Files.write(new GsonBuilder().setPrettyPrinting().create()
				.toJson(ArgumentRegistry.a(dispatcher, dispatcher.getRoot())), file, StandardCharsets.UTF_8);
	}

	@Override
	public HelpTopic generateHelpTopic(String commandName, String shortDescription, String fullDescription, String permission) {
		return new CustomHelpTopic(commandName, shortDescription, fullDescription, permission);
	}

	@Override
	public org.bukkit.advancement.Advancement getAdvancement(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return ArgumentMinecraftKeyRegistered.a(cmdCtx, key).bukkit;
	}

	@Override
	public float getAngle(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		throw new UnimplementedArgumentException("AngleArgument", "1.16.2");
	}

	@Override
	public EnumSet<Axis> getAxis(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		EnumSet<Axis> set = EnumSet.noneOf(Axis.class);
		EnumSet<EnumAxis> parsedEnumSet = ArgumentRotationAxis.a(cmdCtx, key);
		for (EnumAxis element : parsedEnumSet) {
			set.add(switch (element) {
				case X -> Axis.X;
				case Y -> Axis.Y;
				case Z -> Axis.Z;
			});
		}
		return set;
	}

	@Override
	public Object getBiome(CommandContext<CommandListenerWrapper> cmdCtx, String key, ArgumentSubType subType) throws CommandSyntaxException {
		throw new UnimplementedArgumentException("BiomeArgument", "1.16");
	}

	@Override
	public Predicate<Block> getBlockPredicate(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		Predicate<ShapeDetectorBlock> predicate = ArgumentBlockPredicate.a(cmdCtx, key);
		return (Block block) -> predicate.test(new ShapeDetectorBlock(cmdCtx.getSource().getWorld(),
				new BlockPosition(block.getX(), block.getY(), block.getZ()), true));
	}

	@Override
	public BlockData getBlockState(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return CraftBlockData.fromData(ArgumentTile.a(cmdCtx, key).a());
	}

	@Override
	public com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> getBrigadierDispatcher() {
		return this.<MinecraftServer>getMinecraftServer().vanillaCommandDispatcher.a();
	}

	@Override
	public CommandDispatcher<CommandListenerWrapper> getResourcesDispatcher() {
		return this.<MinecraftServer>getMinecraftServer().getCommandDispatcher().a();
	}

	@Override
	public CommandListenerWrapper getBrigadierSourceFromCommandSender(AbstractCommandSender<? extends CommandSender> senderWrapper) {
		return VanillaCommandWrapper.getListener(senderWrapper.getSource());
	}

	@Override
	public BukkitCommandSender<? extends CommandSender> getCommandSenderFromCommandSource(CommandListenerWrapper clw) {
		try {
			return wrapCommandSender(clw.getBukkitSender());
		} catch (UnsupportedOperationException e) {
			return null;
		}
	}

	@Override
	public World getDimension(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return this.<MinecraftServer>getMinecraftServer().getWorldServer(ArgumentDimension.a(cmdCtx, key)).getWorld();
	}

	@Override
	public Enchantment getEnchantment(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return new CraftEnchantment(ArgumentEnchantment.a(cmdCtx, key));
	}

	@Override
	public Object getEntitySelector(CommandContext<CommandListenerWrapper> cmdCtx, String str, ArgumentSubType subType, boolean allowEmpty) throws CommandSyntaxException {
		EntitySelector argument = cmdCtx.getArgument(str, EntitySelector.class);
		try {
			entitySelectorCheckPermissions.set(argument, false);
		} catch (IllegalAccessException e) {
			// Shouldn't happen, CommandAPIHandler#getField makes it accessible
		}

		return switch (subType) {
			case ENTITYSELECTOR_MANY_ENTITIES:
				// ArgumentEntity.c -> EntitySelector.getEntities
				try {
					List<org.bukkit.entity.Entity> result = new ArrayList<>();
					for (Entity entity : argument.getEntities(cmdCtx.getSource())) {
						result.add(entity.getBukkitEntity());
					}
					if (result.isEmpty() && !allowEmpty) {
						throw ArgumentEntity.d.create();
					} else {
						yield result;
					}
				} catch (CommandSyntaxException e) {
					if (allowEmpty) {
						yield new ArrayList<org.bukkit.entity.Entity>();
					} else {
						throw e;
					}
				}
			case ENTITYSELECTOR_MANY_PLAYERS:
				// ArgumentEntity.d -> EntitySelector.d
				try {
					List<Player> result = new ArrayList<>();
					for (EntityPlayer player : argument.d(cmdCtx.getSource())) {
						result.add(player.getBukkitEntity());
					}
					if (result.isEmpty() && !allowEmpty) {
						throw ArgumentEntity.e.create();
					} else {
						yield result;
					}
				} catch (CommandSyntaxException e) {
					if (allowEmpty) {
						yield new ArrayList<Player>();
					} else {
						throw e;
					}
				}
			case ENTITYSELECTOR_ONE_ENTITY:
				// ArgumentEntity.a -> EntitySelector.a
				yield argument.a(cmdCtx.getSource()).getBukkitEntity();
			case ENTITYSELECTOR_ONE_PLAYER:
				// ArgumentEntity.e -> EntitySelector.c
				yield argument.c(cmdCtx.getSource()).getBukkitEntity();
			default:
				throw new IllegalArgumentException("Unexpected value: " + subType);
		};
	}

	@Override
	public EntityType getEntityType(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return IRegistry.ENTITY_TYPE.get(ArgumentEntitySummon.a(cmdCtx, key))
				.a(((CraftWorld) getWorldForCSS(cmdCtx.getSource())).getHandle()).getBukkitEntity().getType();
	}

	@Override
	public FloatRange getFloatRange(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		CriterionConditionValue.FloatRange range = cmdCtx.getArgument(key,
				CriterionConditionValue.FloatRange.class);
		float low = range.a() == null ? -Float.MAX_VALUE : range.a();
		float high = range.b() == null ? Float.MAX_VALUE : range.b();
		return new FloatRange(low, high);
	}

	@Override
	public FunctionWrapper[] getFunction(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		List<FunctionWrapper> result = new ArrayList<>();
		CommandListenerWrapper commandListenerWrapper = cmdCtx.getSource().a().b(2);

		for (CustomFunction customFunction : ArgumentTag.a(cmdCtx, key)) {
			result.add(FunctionWrapper.fromSimpleFunctionWrapper(convertFunction(customFunction),
					commandListenerWrapper, e -> cmdCtx.getSource().a(((CraftEntity) e).getHandle())));
		}

		return result.toArray(new FunctionWrapper[0]);
	}

	@Override
	public SimpleFunctionWrapper getFunction(NamespacedKey key) {
		return convertFunction(this.<MinecraftServer>getMinecraftServer().getFunctionData().a(new MinecraftKey(key.getNamespace(), key.getKey())).get());
	}

	@Override
	public Set<NamespacedKey> getFunctions() {
		Set<NamespacedKey> functions = new HashSet<>();
		for (MinecraftKey key : this.<MinecraftServer>getMinecraftServer().getFunctionData().c().keySet()) {
			functions.add(fromMinecraftKey(key));
		}
		return functions;
	}

	@Override
	public IntegerRange getIntRange(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		CriterionConditionValue.IntegerRange range = ArgumentCriterionValue.b.a(cmdCtx, key);
		int low = range.a() == null ? Integer.MIN_VALUE : range.a();
		int high = range.b() == null ? Integer.MAX_VALUE : range.b();
		return new IntegerRange(low, high);
	}

	@Override
	public org.bukkit.inventory.ItemStack getItemStack(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		ArgumentPredicateItemStack input = ArgumentItemStack.a(cmdCtx, key);

		// Create the basic ItemStack with an amount of 1
		ItemStack itemWithMaybeTag = input.a(1, false);

		// Try and find the amount from the CompoundTag (if present)
		final NBTTagCompound tag = itemStackPredicateArgument.get(input);
		if(tag != null) {
			// The tag has some extra metadata we need! Get the Count (amount)
			// and create the ItemStack with the correct metadata
			int count = (int) tag.getByte("Count");
			itemWithMaybeTag = input.a(count == 0 ? 1 : count, false);
		}

		org.bukkit.inventory.ItemStack result = CraftItemStack.asBukkitCopy(itemWithMaybeTag);
		result.setItemMeta(CraftItemStack.getItemMeta(itemWithMaybeTag));
		return result;
	}

	@Override
	public Predicate<org.bukkit.inventory.ItemStack> getItemStackPredicate(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		Predicate<ItemStack> predicate = ArgumentItemPredicate.a(cmdCtx, key);
		return item -> predicate.test(CraftItemStack.asNMSCopy(item));
	}

	@Override
	public Location2D getLocation2DBlock(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		BlockPosition2D blockPos = ArgumentVec2I.a(cmdCtx, key);
		return new Location2D(getWorldForCSS(cmdCtx.getSource()), blockPos.a, blockPos.b);
	}

	@Override
	public Location2D getLocation2DPrecise(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		Vec2F vecPos = ArgumentVec2.a(cmdCtx, key);
		return new Location2D(getWorldForCSS(cmdCtx.getSource()), vecPos.i, vecPos.j);
	}

	@Override
	public Location getLocationBlock(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		BlockPosition blockPos = ArgumentPosition.b(cmdCtx, key);
		return new Location(getWorldForCSS(cmdCtx.getSource()), blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}

	@Override
	public Location getLocationPrecise(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		Vec3D vecPos = ArgumentVec3.a(cmdCtx, key);
		return new Location(getWorldForCSS(cmdCtx.getSource()), vecPos.x, vecPos.y, vecPos.z);
	}

	@Differs(from = "1.14.4", by = "ArgumentMinecraftKeyRegistered.c() -> ArgumentMinecraftKeyRegistered.d()")
	@Override
	public org.bukkit.loot.LootTable getLootTable(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		MinecraftKey minecraftKey = ArgumentMinecraftKeyRegistered.d(cmdCtx, key);
		return new CraftLootTable(fromMinecraftKey(minecraftKey), this.<MinecraftServer>getMinecraftServer().getLootTableRegistry().getLootTable(minecraftKey));
	}

	@Override
	public MathOperation getMathOperation(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		// We run this to ensure the argument exists/parses properly
		ArgumentMathOperation.a(cmdCtx, key);
		return MathOperation.fromString(CommandAPIHandler.getRawArgumentInput(cmdCtx, key));
	}

	@Differs(from = "1.14.4", by = "ArgumentMinecraftKeyRegistered.c() -> ArgumentMinecraftKeyRegistered.d()")
	@SuppressWarnings("deprecation")
	@Override
	public NamespacedKey getMinecraftKey(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		MinecraftKey resourceLocation = ArgumentMinecraftKeyRegistered.d(cmdCtx, key);
		return new NamespacedKey(resourceLocation.getNamespace(), resourceLocation.getKey());
	}

	@Override
	public <NBTContainer> Object getNBTCompound(CommandContext<CommandListenerWrapper> cmdCtx, String key, Function<Object, NBTContainer> nbtContainerConstructor) {
		return nbtContainerConstructor.apply(ArgumentNBTTag.a(cmdCtx, key));
	}

	@Override
	public Objective getObjective(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws IllegalArgumentException, CommandSyntaxException {
		String objectiveName = ArgumentScoreboardObjective.a(cmdCtx, key).getName();
		return Bukkit.getScoreboardManager().getMainScoreboard().getObjective(objectiveName);
	}

	@Override
	public String getObjectiveCriteria(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return ArgumentScoreboardCriteria.a(cmdCtx, key).getName();
	}

	@Override
	public OfflinePlayer getOfflinePlayer(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		OfflinePlayer target = Bukkit
				.getOfflinePlayer((ArgumentProfile.a(cmdCtx, key).iterator().next()).getId());
		if (target == null) {
			throw ArgumentProfile.a.create();
		} else {
			return target;
		}
	}

	@Override
	public ParticleData<?> getParticle(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		final ParticleParam particleOptions = ArgumentParticle.a(cmdCtx, key);
		final Particle particle = CraftParticle.toBukkit(particleOptions);

		if (particleOptions instanceof ParticleType) {
			return new ParticleData<Void>(particle, null);
		}
		else if (particleOptions instanceof ParticleParamBlock options) {
			return new ParticleData<BlockData>(particle, CraftBlockData.fromData(particleParamBlockData.get(options)));
		}
		else if (particleOptions instanceof ParticleParamRedstone options) {
			return getParticleDataAsDustOptions(particle, options);
		}
		else if (particleOptions instanceof ParticleParamItem options) {
			return new ParticleData<org.bukkit.inventory.ItemStack>(particle, CraftItemStack.asBukkitCopy(particleParamItemStack.get(options)));
		}
		else {
			CommandAPI.getLogger().warning("Invalid particle data type for " + particle.getDataType().toString());
			return new ParticleData<Void>(particle, null);
		}
	}

	private ParticleData<DustOptions> getParticleDataAsDustOptions(Particle particle, ParticleParamRedstone options) {
		String optionsStr = options.a(); // Of the format "particle_type float float float"
		String[] optionsArr = optionsStr.split(" ");
		final float red = Float.parseFloat(optionsArr[1]);
		final float green = Float.parseFloat(optionsArr[2]);
		final float blue = Float.parseFloat(optionsArr[3]);

		final Color color = Color.fromRGB((int) (red * 255.0F), (int) (green * 255.0F), (int) (blue * 255.0F));
		return new ParticleData<DustOptions>(particle, new DustOptions(color, particleParamRedstoneSize.get(options)));
	}

	@Override
	public Player getPlayer(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		Player target = Bukkit.getPlayer((ArgumentProfile.a(cmdCtx, key).iterator().next()).getId());
		if (target == null) {
			throw ArgumentProfile.a.create();
		} else {
			return target;
		}
	}

	@Override
	public PotionEffectType getPotionEffect(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return new CraftPotionEffectType(ArgumentMobEffect.a(cmdCtx, key));
	}

	@Differs(from = "1.14.4", by = "Implementation of ComplexRecipe instead of Recipe")
	@Override
	public ComplexRecipe getRecipe(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		IRecipe<?> recipe = ArgumentMinecraftKeyRegistered.b(cmdCtx, key);
		return new ComplexRecipeImpl(fromMinecraftKey(recipe.getKey()), recipe.toBukkitRecipe());
	}

	@Override
	public Rotation getRotation(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		Vec2F vec = ArgumentRotation.a(cmdCtx, key).b(cmdCtx.getSource());
		return new Rotation(vec.j, vec.i);
	}

	@Override
	public ScoreboardSlot getScoreboardSlot(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return ScoreboardSlot.ofMinecraft(ArgumentScoreboardSlot.a(cmdCtx, key));
	}

	@Override
	public Collection<String> getScoreHolderMultiple(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return ArgumentScoreholder.b(cmdCtx, key);
	}

	@Override
	public String getScoreHolderSingle(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return ArgumentScoreholder.a(cmdCtx, key);
	}

	@Override
	public BukkitCommandSender<? extends CommandSender> getSenderForCommand(CommandContext<CommandListenerWrapper> cmdCtx, boolean isNative) {
		CommandListenerWrapper clw = cmdCtx.getSource();

		CommandSender sender = clw.getBukkitSender();
		if (sender == null) {
			// Sender CANNOT be null. This can occur when using a remote console
			// sender. You can access it directly using this.<MinecraftServer>getMinecraftServer().remoteConsole
			// however this may also be null, so delegate to the next most-meaningful sender.
			sender = Bukkit.getConsoleSender();
		}
		Vec3D pos = clw.getPosition();
		Vec2F rot = clw.i();
		World world = getWorldForCSS(clw);
		Location location = new Location(world, pos.getX(), pos.getY(), pos.getZ(), rot.j, rot.i);

		Entity proxyEntity = clw.getEntity();
		CommandSender proxy = proxyEntity == null ? null : proxyEntity.getBukkitEntity();
		if (isNative || (proxy != null && !sender.equals(proxy))) {
			if (proxy == null) {
				proxy = sender;
			}
			return new BukkitNativeProxyCommandSender(new NativeProxyCommandSender(sender, proxy, location, world));
		} else {
			return wrapCommandSender(sender);
		}
	}

	@Override
	public SimpleCommandMap getSimpleCommandMap() {
		return ((CraftServer) Bukkit.getServer()).getCommandMap();
	}

	@Differs(from = "1.14.4", by = "ArgumentMinecraftKeyRegistered.c() -> ArgumentMinecraftKeyRegistered.d()")
	@Override
	public final Object getSound(CommandContext<CommandListenerWrapper> cmdCtx, String key, ArgumentSubType subType) {
		final MinecraftKey soundResource = ArgumentMinecraftKeyRegistered.d(cmdCtx, key);
		return switch(subType) {
			case SOUND_SOUND -> {
				for (CraftSound sound : CraftSound.values()) {
					if(craftSoundMinecraftKey.get(sound).equals(soundResource.getKey())) {
						yield Sound.valueOf(sound.name());
					}
				}
				yield null;
			}
			case SOUND_NAMESPACEDKEY -> fromMinecraftKey(soundResource);
			default -> throw new IllegalArgumentException("Unexpected value: " + subType);
		};
	}

	@Differs(from = "1.14.4", by = "this.<MinecraftServer>getMinecraftServer().getAdvancementData().a() -> this.<MinecraftServer>getMinecraftServer().getAdvancementData().getAdvancements()")
	@Override
	public SuggestionProvider<CommandListenerWrapper> getSuggestionProvider(SuggestionProviders provider) {
		return switch (provider) {
			case FUNCTION -> (context, builder) -> {
				CustomFunctionData functionData = this.<MinecraftServer>getMinecraftServer().getFunctionData();
				ICompletionProvider.a(functionData.h().a(), builder, "#");
				return ICompletionProvider.a(functionData.c().keySet(), builder);
			};
			case RECIPES -> CompletionProviders.b;
			case SOUNDS -> CompletionProviders.c;
			case ADVANCEMENTS -> (cmdCtx, builder) -> ICompletionProvider.a(this.<MinecraftServer>getMinecraftServer().getAdvancementData().getAdvancements().stream().map(Advancement::getName), builder);
			case LOOT_TABLES -> (cmdCtx, builder) -> ICompletionProvider.a(this.<MinecraftServer>getMinecraftServer().getLootTableRegistry().a(), builder);
			case ENTITIES -> CompletionProviders.d;
			default -> (context, builder) -> Suggestions.empty();
		};
	}

	@Override
	public SimpleFunctionWrapper[] getTag(NamespacedKey key) {
		List<CustomFunction> customFunctions = new ArrayList<>(
				this.<MinecraftServer>getMinecraftServer().getFunctionData().h().b(new MinecraftKey(key.getNamespace(), key.getKey())).a());
		SimpleFunctionWrapper[] result = new SimpleFunctionWrapper[customFunctions.size()];
		for (int i = 0, size = customFunctions.size(); i < size; i++) {
			result[i] = convertFunction(customFunctions.get(i));
		}
		return result;
	}

	@Override
	public Set<NamespacedKey> getTags() {
		Set<NamespacedKey> functions = new HashSet<>();
		for (MinecraftKey key : this.<MinecraftServer>getMinecraftServer().getFunctionData().h().a()) {
			functions.add(fromMinecraftKey(key));
		}
		return functions;
	}

	@Override
	public Team getTeam(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		String teamName = ArgumentScoreboardTeam.a(cmdCtx, key).getName();
		return Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
	}

	@Override
	public int getTime(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return cmdCtx.getArgument(key, Integer.class);
	}

	@Override
	public UUID getUUID(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		throw new UnimplementedArgumentException("UUIDArgument", "1.16");
	}

	@Override
	public World getWorldForCSS(CommandListenerWrapper clw) {
		return (clw.getWorld() == null) ? null : clw.getWorld().getWorld();
	}

	@Override
	public boolean isVanillaCommandWrapper(Command command) {
		return command instanceof VanillaCommandWrapper;
	}

	@Override
	public Command wrapToVanillaCommandWrapper(CommandNode<CommandListenerWrapper> node) {
		return new VanillaCommandWrapper(this.<MinecraftServer>getMinecraftServer().vanillaCommandDispatcher, node);
	}

	@Override
	public boolean isBukkitCommandWrapper(CommandNode<CommandListenerWrapper> node) {
		return node.getCommand() instanceof BukkitCommandWrapper;
	}

	@Override
	public void reloadDataPacks() {
		// Datapacks don't need reloading in this version
	}

	@Override
	public Message generateMessageFromJson(String json) {
		return ChatSerializer.a(json);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getMinecraftServer() {
		if(Bukkit.getServer() instanceof CraftServer server) {
			return (T) server.getServer();
		} else {
			return null;
		}
	}

}
