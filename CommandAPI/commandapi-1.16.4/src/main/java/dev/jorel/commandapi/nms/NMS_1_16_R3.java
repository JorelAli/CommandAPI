package dev.jorel.commandapi.nms;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_16_R3.CraftLootTable;
import org.bukkit.craftbukkit.v1_16_R3.CraftParticle;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftSound;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_16_R3.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.v1_16_R3.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ComplexRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.potion.PotionEffectType;

import com.google.common.io.Files;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.arguments.ICustomProvidedArgument.SuggestionProviders;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.preprocessor.RequireField;
import dev.jorel.commandapi.wrappers.FloatRange;
import dev.jorel.commandapi.wrappers.FunctionWrapper;
import dev.jorel.commandapi.wrappers.IntegerRange;
import dev.jorel.commandapi.wrappers.Location2D;
import dev.jorel.commandapi.wrappers.MathOperation;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import dev.jorel.commandapi.wrappers.Rotation;
import dev.jorel.commandapi.wrappers.ScoreboardSlot;
import dev.jorel.commandapi.wrappers.SimpleFunctionWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_16_R3.Advancement;
import net.minecraft.server.v1_16_R3.ArgumentAngle;
import net.minecraft.server.v1_16_R3.ArgumentBlockPredicate;
import net.minecraft.server.v1_16_R3.ArgumentChat;
import net.minecraft.server.v1_16_R3.ArgumentChatComponent;
import net.minecraft.server.v1_16_R3.ArgumentChatFormat;
import net.minecraft.server.v1_16_R3.ArgumentCriterionValue;
import net.minecraft.server.v1_16_R3.ArgumentDimension;
import net.minecraft.server.v1_16_R3.ArgumentEnchantment;
import net.minecraft.server.v1_16_R3.ArgumentEntity;
import net.minecraft.server.v1_16_R3.ArgumentEntitySummon;
import net.minecraft.server.v1_16_R3.ArgumentItemPredicate;
import net.minecraft.server.v1_16_R3.ArgumentItemStack;
import net.minecraft.server.v1_16_R3.ArgumentMathOperation;
import net.minecraft.server.v1_16_R3.ArgumentMinecraftKeyRegistered;
import net.minecraft.server.v1_16_R3.ArgumentMobEffect;
import net.minecraft.server.v1_16_R3.ArgumentNBTTag;
import net.minecraft.server.v1_16_R3.ArgumentParticle;
import net.minecraft.server.v1_16_R3.ArgumentPosition;
import net.minecraft.server.v1_16_R3.ArgumentProfile;
import net.minecraft.server.v1_16_R3.ArgumentRegistry;
import net.minecraft.server.v1_16_R3.ArgumentRotation;
import net.minecraft.server.v1_16_R3.ArgumentRotationAxis;
import net.minecraft.server.v1_16_R3.ArgumentScoreboardCriteria;
import net.minecraft.server.v1_16_R3.ArgumentScoreboardObjective;
import net.minecraft.server.v1_16_R3.ArgumentScoreboardSlot;
import net.minecraft.server.v1_16_R3.ArgumentScoreboardTeam;
import net.minecraft.server.v1_16_R3.ArgumentScoreholder;
import net.minecraft.server.v1_16_R3.ArgumentTag;
import net.minecraft.server.v1_16_R3.ArgumentTile;
import net.minecraft.server.v1_16_R3.ArgumentTime;
import net.minecraft.server.v1_16_R3.ArgumentUUID;
import net.minecraft.server.v1_16_R3.ArgumentVec2;
import net.minecraft.server.v1_16_R3.ArgumentVec2I;
import net.minecraft.server.v1_16_R3.ArgumentVec3;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.BlockPosition2D;
import net.minecraft.server.v1_16_R3.CommandDispatcher;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import net.minecraft.server.v1_16_R3.CompletionProviders;
import net.minecraft.server.v1_16_R3.CriterionConditionValue;
import net.minecraft.server.v1_16_R3.CustomFunction;
import net.minecraft.server.v1_16_R3.CustomFunctionData;
import net.minecraft.server.v1_16_R3.CustomFunctionManager;
import net.minecraft.server.v1_16_R3.DataPackResources;
import net.minecraft.server.v1_16_R3.DedicatedServer;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntitySelector;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.EnumDirection.EnumAxis;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.ICompletionProvider;
import net.minecraft.server.v1_16_R3.IRecipe;
import net.minecraft.server.v1_16_R3.IRegistry;
import net.minecraft.server.v1_16_R3.IReloadableResourceManager;
import net.minecraft.server.v1_16_R3.IVectorPosition;
import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.LootTable;
import net.minecraft.server.v1_16_R3.LootTableRegistry;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.Scoreboard;
import net.minecraft.server.v1_16_R3.ScoreboardScore;
import net.minecraft.server.v1_16_R3.ShapeDetectorBlock;
import net.minecraft.server.v1_16_R3.SystemUtils;
import net.minecraft.server.v1_16_R3.Unit;
import net.minecraft.server.v1_16_R3.Vec2F;
import net.minecraft.server.v1_16_R3.Vec3D;

@RequireField(in = DataPackResources.class, name = "i", ofType = CustomFunctionManager.class)
@RequireField(in = DataPackResources.class, name = "b", ofType = IReloadableResourceManager.class)
@RequireField(in = DataPackResources.class, name = "a", ofType = CompletableFuture.class)
@RequireField(in = CustomFunctionManager.class, name = "g", ofType = int.class)
@RequireField(in = CraftSound.class, name = "minecraftKey", ofType = String.class)
@RequireField(in = EntitySelector.class, name = "checkPermissions", ofType = boolean.class)
public class NMS_1_16_R3 implements NMS<CommandListenerWrapper> {
	
	@Override
	public Component getAdventureChat(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException  {
		return GsonComponentSerializer.gson().deserialize(ChatSerializer.a(ArgumentChat.a(cmdCtx, key)));
	}
	
	@Override
	public ArgumentType<?> _ArgumentAngle() {
		return ArgumentAngle.a();
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
	public ArgumentType<?> _ArgumentEntity(dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector selector) {
		switch (selector) {
		case MANY_ENTITIES:
			return ArgumentEntity.multipleEntities();
		case MANY_PLAYERS:
			return ArgumentEntity.d();
		case ONE_ENTITY:
			return ArgumentEntity.a();
		case ONE_PLAYER:
			return ArgumentEntity.c();
		}
		return null;
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
	public ArgumentType<?> _ArgumentScoreholder(boolean single) {
		return single ? ArgumentScoreholder.a() : ArgumentScoreholder.b();
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
		return ArgumentUUID.a();
	}

	@Override
	public ArgumentType<?> _ArgumentVec2() {
		return ArgumentVec2.a();
	}

	@Override
	public ArgumentType<?> _ArgumentVec3() {
		return ArgumentVec3.a();
	}

	@Override
	public String[] compatibleVersions() {
		return new String[] { "1.16.4", "1.16.5" };
	}

	@Override
	public String convert(org.bukkit.inventory.ItemStack is) {
		return is.getType().getKey().toString() + CraftItemStack.asNMSCopy(is).getOrCreateTag().asString();
	}

	@Override
	public String convert(Particle particle) {
		return CraftParticle.toNMS(particle).a();
	}

	@SuppressWarnings("deprecation")
	@Override
	public String convert(PotionEffectType potion) {
		return IRegistry.MOB_EFFECT.getKey(IRegistry.MOB_EFFECT.fromId(potion.getId())).toString();
	}

	@Override
	public String convert(Sound sound) {
		return CraftSound.getSound(sound);
	}

	@Override
	public void createDispatcherFile(File file, com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> dispatcher) throws IOException {
		Files.write((new GsonBuilder()).setPrettyPrinting().create()
				.toJson(ArgumentRegistry.a(dispatcher, dispatcher.getRoot())), file, StandardCharsets.UTF_8);
	}

	@Override
	public org.bukkit.advancement.Advancement getAdvancement(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		return ArgumentMinecraftKeyRegistered.a(cmdCtx, key).bukkit;
	}

	@Override
	public float getAngle(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return ArgumentAngle.a(cmdCtx, key);
	}

	@Override
	public EnumSet<Axis> getAxis(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		EnumSet<Axis> set = EnumSet.noneOf(Axis.class);
		EnumSet<EnumAxis> parsedEnumSet = ArgumentRotationAxis.a(cmdCtx, key);
		for (EnumAxis element : parsedEnumSet) {
			switch (element) {
			case X:
				set.add(Axis.X);
				break;
			case Y:
				set.add(Axis.Y);
				break;
			case Z:
				set.add(Axis.Z);
				break;
			}
		}
		return set;
	}

	@Override
	public Biome getBiome(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return Biome.valueOf(cmdCtx.getArgument(key, MinecraftKey.class).getKey().toUpperCase());
	}

	@Override
	public Predicate<Block> getBlockPredicate(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		Predicate<ShapeDetectorBlock> predicate = ArgumentBlockPredicate.a(cmdCtx, key);
		return (Block block) -> {
			return predicate.test(new ShapeDetectorBlock(getCLW(cmdCtx).getWorld(),
					new BlockPosition(block.getX(), block.getY(), block.getZ()), true));
		};
	}

	@Override
	public BlockData getBlockState(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return CraftBlockData.fromData(ArgumentTile.a(cmdCtx, key).a());
	}

	@Override
	public com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> getBrigadierDispatcher() {
		return ((MinecraftServer) ((CraftServer) Bukkit.getServer()).getServer()).getCommandDispatcher().a();
	}

	@Override
	public BaseComponent[] getChat(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return ComponentSerializer.parse(ChatSerializer.a(ArgumentChat.a(cmdCtx, key)));
	}

	@Override
	public ChatColor getChatColor(CommandContext<CommandListenerWrapper> cmdCtx, String str) {
		return CraftChatMessage.getColor(ArgumentChatFormat.a(cmdCtx, str));
	}

	@Override
	public BaseComponent[] getChatComponent(CommandContext<CommandListenerWrapper> cmdCtx, String str) {
		return ComponentSerializer.parse(ChatSerializer.a(ArgumentChatComponent.a(cmdCtx, str)));
	}

	private CommandListenerWrapper getCLW(CommandContext<CommandListenerWrapper> cmdCtx) {
		return cmdCtx.getSource();
	}

	@Override
	public CommandSender getCommandSenderForCLW(CommandListenerWrapper clw) {
		try {
			return clw.getBukkitSender();
		} catch (UnsupportedOperationException e) {
			return null;
		}
	}

	@Override
	public Environment getDimension(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return ArgumentDimension.a(cmdCtx, key).getWorld().getEnvironment();
	}

	@Override
	public Enchantment getEnchantment(CommandContext<CommandListenerWrapper> cmdCtx, String str) {
		return new CraftEnchantment(ArgumentEnchantment.a(cmdCtx, str));
	}

	@Override
	public Object getEntitySelector(CommandContext<CommandListenerWrapper> cmdCtx, String str, dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector selector)
			throws CommandSyntaxException {
		
		EntitySelector argument = cmdCtx.getArgument(str, EntitySelector.class);
		try {
			CommandAPIHandler.getInstance().getField(EntitySelector.class, "checkPermissions").set(argument, false);
		} catch (IllegalArgumentException | IllegalAccessException e1) {
			e1.printStackTrace();
		}
		
		return switch (selector) {
		case MANY_ENTITIES:
			// ArgumentEntity.c -> EntitySelector.getEntities
			try {
				yield argument.getEntities(cmdCtx.getSource()).stream()
						.map(entity -> (org.bukkit.entity.Entity) ((Entity) entity).getBukkitEntity())
						.collect(Collectors.toList());
			} catch (CommandSyntaxException e) {
				yield new ArrayList<org.bukkit.entity.Entity>();
			}
		case MANY_PLAYERS:
			// ArgumentEntity.d -> EntitySelector.d
			try {
				yield argument.d(cmdCtx.getSource()).stream()
						.map(player -> (Player) ((Entity) player).getBukkitEntity()).collect(Collectors.toList());
			} catch (CommandSyntaxException e) {
				yield new ArrayList<Player>();
			}
		case ONE_ENTITY:
			// ArgumentEntity.a -> EntitySelector.a
			yield (org.bukkit.entity.Entity) argument.a(cmdCtx.getSource()).getBukkitEntity();
		case ONE_PLAYER:
			// ArgumentEntity.e -> EntitySelector.c
			yield (Player) argument.c(cmdCtx.getSource()).getBukkitEntity();
		};
	}

	@SuppressWarnings("deprecation")
	@Override
	public EntityType getEntityType(CommandContext<CommandListenerWrapper> cmdCtx, String str) throws CommandSyntaxException {
		return EntityType.fromName(EntityTypes.getName(IRegistry.ENTITY_TYPE.get(ArgumentEntitySummon.a(cmdCtx, str))).getKey());
	}

	@Override
	public FloatRange getFloatRange(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		CriterionConditionValue.FloatRange range = (CriterionConditionValue.FloatRange) cmdCtx.getArgument(key,
				CriterionConditionValue.FloatRange.class);
		float low = range.a() == null ? -Float.MAX_VALUE : range.a();
		float high = range.b() == null ? Float.MAX_VALUE : range.b();
		return new FloatRange(low, high);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public Set<NamespacedKey> getFunctions() {
		Set<NamespacedKey> functions = new HashSet<>();
		for(MinecraftKey key : ((CraftServer) Bukkit.getServer()).getServer().getFunctionData().f()) {
			functions.add(new NamespacedKey(key.getNamespace(), key.getKey()));
		}
		return functions;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public Set<NamespacedKey> getTags() {
		Set<NamespacedKey> functions = new HashSet<>();
		for(MinecraftKey key : ((CraftServer) Bukkit.getServer()).getServer().getFunctionData().g()) {
			functions.add(new NamespacedKey(key.getNamespace(), key.getKey()));
		}
		return functions;
	}
	
	@Override
	public CommandListenerWrapper getCLWFromCommandSender(CommandSender sender) {
		return VanillaCommandWrapper.getListener(sender);
	}
	
	//Converts NMS function to SimpleFunctionWrapper
	private SimpleFunctionWrapper convertFunction(CustomFunction customFunction) {
		@SuppressWarnings("deprecation")
		NamespacedKey minecraftKey = new NamespacedKey(customFunction.a().getNamespace(), customFunction.a().getKey());

		CustomFunctionData customFunctionData = ((CraftServer) Bukkit.getServer()).getServer().getFunctionData();

		ToIntBiFunction<CustomFunction, CommandListenerWrapper> obj = customFunctionData::a;
		ToIntFunction<CommandListenerWrapper> appliedObj = clw -> obj.applyAsInt(customFunction, clw);

		return new SimpleFunctionWrapper(minecraftKey, appliedObj,
				Arrays.stream(customFunction.b()).map(Object::toString).toArray(String[]::new));
	}
	
	@Override
	public SimpleFunctionWrapper[] getTag(NamespacedKey key) {
		MinecraftKey minecraftKey = new MinecraftKey(key.getNamespace(), key.getKey());
		CustomFunctionData functionData = ((CraftServer) Bukkit.getServer()).getServer().getFunctionData();
		return functionData.b(minecraftKey).getTagged().stream().map(this::convertFunction).toArray(SimpleFunctionWrapper[]::new);
	}
	
	@Override
	public SimpleFunctionWrapper getFunction(NamespacedKey key) {
		MinecraftKey minecraftKey = new MinecraftKey(key.getNamespace(), key.getKey());
		CustomFunctionData functionData = ((CraftServer) Bukkit.getServer()).getServer().getFunctionData();
		return convertFunction(functionData.a(minecraftKey).get());
	}

	@Override
	public FunctionWrapper[] getFunction(CommandContext<CommandListenerWrapper> cmdCtx, String str) throws CommandSyntaxException {
		Collection<CustomFunction> customFuncList = ArgumentTag.a(cmdCtx, str);
		FunctionWrapper[] result = new FunctionWrapper[customFuncList.size()];
		CommandListenerWrapper commandListenerWrapper = getCLW(cmdCtx).a().b(2);

		int count = 0;
		for (CustomFunction customFunction : customFuncList) { 
			result[count++] = FunctionWrapper.fromSimpleFunctionWrapper(convertFunction(customFunction), commandListenerWrapper, e -> {
				return getCLW(cmdCtx).a(((CraftEntity) e).getHandle());
			});
		}

		return result;
	}

	@Override
	public IntegerRange getIntRange(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		CriterionConditionValue.IntegerRange range = ArgumentCriterionValue.b.a(cmdCtx, key);
		int low = range.a() == null ? Integer.MIN_VALUE : range.a();
		int high = range.b() == null ? Integer.MAX_VALUE : range.b();
		return new IntegerRange(low, high);
	}

	@Override
	public org.bukkit.inventory.ItemStack getItemStack(CommandContext<CommandListenerWrapper> cmdCtx, String str)
			throws CommandSyntaxException {
		return CraftItemStack.asBukkitCopy(ArgumentItemStack.a(cmdCtx, str).a(1, false));
	}

	@Override
	public Predicate<org.bukkit.inventory.ItemStack> getItemStackPredicate(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		Predicate<ItemStack> predicate = ArgumentItemPredicate.a(cmdCtx, key);
		return item -> predicate.test(CraftItemStack.asNMSCopy(item));
	}

	@Override
	public String getKeyedAsString(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		MinecraftKey minecraftKey = ArgumentMinecraftKeyRegistered.e(cmdCtx, key);
		return minecraftKey.toString();
	}

	@Override
	public Location getLocation(CommandContext<CommandListenerWrapper> cmdCtx, String str, LocationType locationType)
			throws CommandSyntaxException {
		return switch (locationType) {
		case BLOCK_POSITION:
			BlockPosition blockPos = ArgumentPosition.a(cmdCtx, str);
			yield new Location(getWorldForCLW(getCLW(cmdCtx)), blockPos.getX(), blockPos.getY(),
					blockPos.getZ());
		case PRECISE_POSITION:
			Vec3D vecPos = ArgumentVec3.a(cmdCtx, str);
			yield new Location(getWorldForCLW(getCLW(cmdCtx)), vecPos.x, vecPos.y, vecPos.z);
		};
	}

	@Override
	public Location2D getLocation2D(CommandContext<CommandListenerWrapper> cmdCtx, String key, LocationType locationType2d)
			throws CommandSyntaxException {
		return switch (locationType2d) {
		case BLOCK_POSITION:
			BlockPosition2D blockPos = ArgumentVec2I.a(cmdCtx, key);
			yield new Location2D(getWorldForCLW(getCLW(cmdCtx)), blockPos.a, blockPos.b);
		case PRECISE_POSITION:
			Vec2F vecPos = ArgumentVec2.a(cmdCtx, key);
			yield new Location2D(getWorldForCLW(getCLW(cmdCtx)), vecPos.i, vecPos.j);
		};
	}

	@SuppressWarnings("deprecation")
	@Override
	public org.bukkit.loot.LootTable getLootTable(CommandContext<CommandListenerWrapper> cmdCtx, String str) {
		MinecraftKey minecraftKey = ArgumentMinecraftKeyRegistered.e(cmdCtx, str);
		String namespace = minecraftKey.getNamespace();
		String key = minecraftKey.getKey();

		LootTable lootTable = getCLW(cmdCtx).getServer().getLootTableRegistry().getLootTable(minecraftKey);
		return new CraftLootTable(new NamespacedKey(namespace, key), lootTable);
	}

	@Override
	public MathOperation getMathOperation(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		ArgumentMathOperation.a result = ArgumentMathOperation.a(cmdCtx, key);
		Scoreboard board = new Scoreboard();
		ScoreboardScore tester_left = new ScoreboardScore(board, null, null);
		ScoreboardScore tester_right = new ScoreboardScore(board, null, null);

		tester_left.setScore(6);
		tester_right.setScore(2);
		result.apply(tester_left, tester_right);

		switch (tester_left.getScore()) {
		case 8:
			return MathOperation.ADD;
		case 4:
			return MathOperation.SUBTRACT;
		case 12:
			return MathOperation.MULTIPLY;
		case 3:
			return MathOperation.DIVIDE;
		case 0:
			return MathOperation.MOD;
		case 6:
			return MathOperation.MAX;

		case 2: {
			if (tester_right.getScore() == 6)
				return MathOperation.SWAP;
			tester_left.setScore(2);
			tester_right.setScore(6);
			result.apply(tester_left, tester_right);
			if (tester_left.getScore() == 2)
				return MathOperation.MIN;
			return MathOperation.ASSIGN;
		}
		}
		return null;
	}

	@Override
	public NBTContainer getNBTCompound(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return new NBTContainer(ArgumentNBTTag.a(cmdCtx, key));
	}

	@Override
	public String getObjective(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws IllegalArgumentException, CommandSyntaxException {
		return ArgumentScoreboardObjective.a(cmdCtx, key).getName();
	}

	@Override
	public String getObjectiveCriteria(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return ArgumentScoreboardCriteria.a(cmdCtx, key).getName();
	}

	@Override
	public Particle getParticle(CommandContext<CommandListenerWrapper> cmdCtx, String str) {
		return CraftParticle.toBukkit(ArgumentParticle.a(cmdCtx, str));
	}

	@Override
	public Player getPlayer(CommandContext<CommandListenerWrapper> cmdCtx, String str) throws CommandSyntaxException {
		Player target = Bukkit.getPlayer(((GameProfile) ArgumentProfile.a(cmdCtx, str).iterator().next()).getId());
		if (target == null) {
			throw ArgumentProfile.a.create();
		} else {
			return target;
		}
	}

	@Override
	public PotionEffectType getPotionEffect(CommandContext<CommandListenerWrapper> cmdCtx, String str) throws CommandSyntaxException {
		return new CraftPotionEffectType(ArgumentMobEffect.a(cmdCtx, str));
	}

	@Override
	public ComplexRecipe getRecipe(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		IRecipe<?> recipe = ArgumentMinecraftKeyRegistered.b(cmdCtx, key);
		return new ComplexRecipe() {

			@SuppressWarnings("deprecation")
			@Override
			public NamespacedKey getKey() {
				return new NamespacedKey(recipe.getKey().getNamespace(), recipe.getKey().getKey());
			}

			@Override
			public org.bukkit.inventory.ItemStack getResult() {
				return recipe.toBukkitRecipe().getResult();
			}
		};
	}

	@Override
	public Rotation getRotation(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		IVectorPosition pos = ArgumentRotation.a(cmdCtx, key);
		Vec2F vec = pos.b(getCLW(cmdCtx));
		return new Rotation(vec.i, vec.j);
	}

	@Override
	public ScoreboardSlot getScoreboardSlot(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return new ScoreboardSlot(ArgumentScoreboardSlot.a(cmdCtx, key));
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
	public CommandSender getSenderForCommand(CommandContext<CommandListenerWrapper> cmdCtx, boolean isNative) {
		CommandListenerWrapper clw = getCLW(cmdCtx);

		CommandSender sender = clw.getBukkitSender();
		Vec3D pos = clw.getPosition();
		Vec2F rot = clw.i();
		World world = getWorldForCLW(clw);
		Location location = new Location(world, pos.getX(), pos.getY(), pos.getZ(), rot.j, rot.i);

		Entity proxyEntity = clw.getEntity();
		CommandSender proxy = proxyEntity == null ? null : ((Entity) proxyEntity).getBukkitEntity();
		if (isNative || (proxy != null && !sender.equals(proxy))) {
			sender = new NativeProxyCommandSender(sender, proxy, location, world);
		}

		return sender;
	}

	@Override
	public SimpleCommandMap getSimpleCommandMap() {
		return ((CraftServer) Bukkit.getServer()).getCommandMap();
	}

	@Override
	public Sound getSound(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		MinecraftKey minecraftKey = ArgumentMinecraftKeyRegistered.e(cmdCtx, key);
		for (CraftSound sound : CraftSound.values()) {
			try {
				if (CommandAPIHandler.getInstance().getField(CraftSound.class, "minecraftKey").get(sound)
						.equals(minecraftKey.getKey())) {
					return Sound.valueOf(sound.name());
				}
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public SuggestionProvider<CommandListenerWrapper> getSuggestionProvider(SuggestionProviders provider) {
		switch (provider) {
		case FUNCTION:
			return (context, builder) -> {
				CustomFunctionData functionData = getCLW(context).getServer().getFunctionData();
				ICompletionProvider.a(functionData.g(), builder, "#");
				return ICompletionProvider.a(functionData.f(), builder);
			};
		case RECIPES:
			return CompletionProviders.b;
		case SOUNDS:
			return CompletionProviders.c;
		case ADVANCEMENTS:
			return (cmdCtx, builder) -> {
				Collection<Advancement> advancements = ((CommandListenerWrapper) cmdCtx.getSource()).getServer()
						.getAdvancementData().getAdvancements();
				return ICompletionProvider.a(advancements.stream().map(Advancement::getName), builder);
			};
		case LOOT_TABLES:
			return (context, builder) -> {
				LootTableRegistry lootTables = getCLW(context).getServer().getLootTableRegistry();
				return ICompletionProvider.a(lootTables.a(), builder);
			};
		case BIOMES:
			return CompletionProviders.d;
		case ENTITIES:
			return CompletionProviders.e;
		default:
			return (context, builder) -> Suggestions.empty();
		}
	}

	@Override
	public String getTeam(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return ArgumentScoreboardTeam.a(cmdCtx, key).getName();
	}

	@Override
	public int getTime(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return (Integer) cmdCtx.getArgument(key, Integer.class);
	}

	@Override
	public UUID getUUID(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return ArgumentUUID.a(cmdCtx, key);
	}

	@Override
	public boolean isVanillaCommandWrapper(Command command) {
		return command instanceof VanillaCommandWrapper;
	}

	@Override
	public void reloadDataPacks()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		CommandAPI.getLog().info("Reloading datapacks...");

		// Get the NMS server
		DedicatedServer server = ((CraftServer) Bukkit.getServer()).getHandle().getServer();

		// Get previously declared recipes to be re-registered later
		Iterator<Recipe> recipes = Bukkit.recipeIterator();

		// Update the commandDispatcher with the current server's commandDispatcher
		DataPackResources datapackResources = server.dataPackResources;
		datapackResources.commandDispatcher = server.getCommandDispatcher();

		// Reflection doesn't need to be cached because this only executes once at
		// server startup
		Field i = DataPackResources.class.getDeclaredField("i"); // CustomFunctionManager
		i.setAccessible(true);

		Field gField = CustomFunctionManager.class.getDeclaredField("g"); // int
		gField.setAccessible(true);
		int g = (int) gField.get(datapackResources.a()); // Related to the permission required to run this function?

		// Update the CustomFunctionManager for the datapackResources which now has the
		// new commandDispatcher
		i.set(datapackResources, new CustomFunctionManager(g, datapackResources.commandDispatcher.a()));

		// Construct the new CompletableFuture that now uses datapackResources
		Field b = DataPackResources.class.getDeclaredField("b"); // IReloadableResourceManager
		b.setAccessible(true);
		IReloadableResourceManager reloadableResourceManager = (IReloadableResourceManager) b.get(datapackResources);
		Field a = DataPackResources.class.getDeclaredField("a"); // CompletableFuture<Unit>
		a.setAccessible(true);

		@SuppressWarnings("unchecked")
		CompletableFuture<Unit> unit = (CompletableFuture<Unit>) a.get(null);
		CompletableFuture<Unit> unitCompletableFuture = reloadableResourceManager.a(SystemUtils.f(), Runnable::run,
				server.getResourcePackRepository().f(), unit);

		CompletableFuture<DataPackResources> completablefuture = unitCompletableFuture
				.whenComplete((Unit u, Throwable t) -> {
					if (t != null) {
						datapackResources.close();
					}

				}).thenApply((Unit u) -> {
					return datapackResources;
				});

		// Run the completableFuture and bind tags
		try {
			((DataPackResources) completablefuture.get()).i();

			// Register recipes again because reloading datapacks removes all non-vanilla
			// recipes
			recipes.forEachRemaining(recipe -> {
				try {
					Bukkit.addRecipe(recipe);
					if (recipe instanceof Keyed keyedRecipe) {
						CommandAPI.logInfo("Re-registering recipe: " + keyedRecipe.getKey());
					}
				} catch (Exception e) {
					// Can't re-register registered recipes. Not an error.
				}
			});

			CommandAPI.getLog().info("Finished reloading datapacks");
		} catch (Exception e) {
			CommandAPI.getLog().log(Level.WARNING,
					"Failed to load datapacks, can't proceed with normal server load procedure. Try fixing your datapacks?",
					e);
		}
	}

	@Override
	public void resendPackets(Player player) {
		CraftPlayer craftPlayer = (CraftPlayer) player;
		CraftServer craftServer = (CraftServer) Bukkit.getServer();
		CommandDispatcher nmsDispatcher = craftServer.getServer().getCommandDispatcher();
		nmsDispatcher.a(craftPlayer.getHandle());
	}

	@Override
	public Component getAdventureChatComponent(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		String jsonString = ChatSerializer.a(ArgumentChatComponent.a(cmdCtx, key));
		return GsonComponentSerializer.gson().deserialize(jsonString);
	}

	@Override
	public World getWorldForCLW(CommandListenerWrapper clw) {
		return (clw.getWorld() == null) ? null : clw.getWorld().getWorld();
	}
}
