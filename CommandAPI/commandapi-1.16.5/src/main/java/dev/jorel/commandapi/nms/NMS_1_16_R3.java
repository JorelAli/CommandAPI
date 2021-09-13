/*******************************************************************************
 * Copyright 2018, 2021 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi.nms;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
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

import de.tr7zw.nbtapi.NBTContainer;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.arguments.SuggestionProviders;
import dev.jorel.commandapi.preprocessor.RequireField;
import dev.jorel.commandapi.wrappers.ComplexRecipeImpl;
import dev.jorel.commandapi.wrappers.FloatRange;
import dev.jorel.commandapi.wrappers.FunctionWrapper;
import dev.jorel.commandapi.wrappers.IntegerRange;
import dev.jorel.commandapi.wrappers.Location2D;
import dev.jorel.commandapi.wrappers.MathOperation;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import dev.jorel.commandapi.wrappers.Rotation;
import dev.jorel.commandapi.wrappers.ScoreboardSlot;
import dev.jorel.commandapi.wrappers.SimpleFunctionWrapper;
import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.text.Component;
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
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import net.minecraft.server.v1_16_R3.CompletionProviders;
import net.minecraft.server.v1_16_R3.CriterionConditionValue;
import net.minecraft.server.v1_16_R3.CustomFunction;
import net.minecraft.server.v1_16_R3.CustomFunctionData;
import net.minecraft.server.v1_16_R3.CustomFunctionManager;
import net.minecraft.server.v1_16_R3.DataPackResources;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EntitySelector;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.EnumDirection.EnumAxis;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.ICompletionProvider;
import net.minecraft.server.v1_16_R3.IRecipe;
import net.minecraft.server.v1_16_R3.IRegistry;
import net.minecraft.server.v1_16_R3.IReloadableResourceManager;
import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.ShapeDetectorBlock;
import net.minecraft.server.v1_16_R3.SystemUtils;
import net.minecraft.server.v1_16_R3.Vec2F;
import net.minecraft.server.v1_16_R3.Vec3D;

@RequireField(in = DataPackResources.class, name = "i", ofType = CustomFunctionManager.class)
@RequireField(in = DataPackResources.class, name = "b", ofType = IReloadableResourceManager.class)
@RequireField(in = CustomFunctionManager.class, name = "g", ofType = int.class)
@RequireField(in = EntitySelector.class, name = "checkPermissions", ofType = boolean.class)
public class NMS_1_16_R3 implements NMS<CommandListenerWrapper> {
	
	private static final MinecraftServer MINECRAFT_SERVER = ((CraftServer) Bukkit.getServer()).getServer();
	private static final VarHandle DATAPACKRESOURCES_B;
	private static final VarHandle CUSTOMFUNCTIONMANAGER_G;
	
	// Compute all var handles all in one go so we don't do this during main server runtime
	static {
		VarHandle dpr_b = null;
		VarHandle cfm_g = null;
		 try {
			 dpr_b = MethodHandles.privateLookupIn(DataPackResources.class, MethodHandles.lookup()).findVarHandle(DataPackResources.class, "b", IReloadableResourceManager.class);			 
			 cfm_g = MethodHandles.privateLookupIn(CustomFunctionManager.class, MethodHandles.lookup()).findVarHandle(CustomFunctionManager.class, "g", int.class);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
		 DATAPACKRESOURCES_B = dpr_b;
		 CUSTOMFUNCTIONMANAGER_G = cfm_g;
	}

	@SuppressWarnings("deprecation")
	private static NamespacedKey fromMinecrafKey(MinecraftKey key) {
		return new NamespacedKey(key.getNamespace(), key.getKey());
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
		return switch (selector) {
		case MANY_ENTITIES -> ArgumentEntity.multipleEntities();
		case MANY_PLAYERS  -> ArgumentEntity.d();
		case ONE_ENTITY    -> ArgumentEntity.a();
		case ONE_PLAYER    -> ArgumentEntity.c();
		};
	}

	@Override
	public ArgumentType<?> _ArgumentEntitySummon() {
		return ArgumentEntitySummon.a();
	}

	@Override
	public ArgumentType<?> _ArgumentFloatRange() {
		return ArgumentCriterionValue.a();
	}

	@Override
	public ArgumentType<?> _ArgumentIntRange() {
		return ArgumentCriterionValue.b();
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
		return new String[] { "1.16.5" };
	}

	@Override
	public String convert(org.bukkit.inventory.ItemStack is) {
		return is.getType().getKey().toString() + CraftItemStack.asNMSCopy(is).getOrCreateTag().asString();
	}

	@Override
	public String convert(Particle particle) {
		return CraftParticle.toNMS(particle).a();
	}

	@Override
	public String convert(PotionEffectType potion) {
		return potion.getName().toLowerCase(Locale.ENGLISH);
	}

	@Override
	public String convert(Sound sound) {
		return sound.getKey().toString();
	}

	//Converts NMS function to SimpleFunctionWrapper
	private SimpleFunctionWrapper convertFunction(CustomFunction customFunction) {
		ToIntFunction<CommandListenerWrapper> appliedObj = clw -> MINECRAFT_SERVER.getFunctionData().a(customFunction, clw);

		Object[] cArr = customFunction.b();
		String[] result = new String[cArr.length];
		for(int i = 0, size = cArr.length; i < size; i++) {
			result[i] = cArr[i].toString();
		}
		return new SimpleFunctionWrapper(fromMinecrafKey(customFunction.a()), appliedObj, result);
	}

	@Override
	public void createDispatcherFile(File file, com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> dispatcher) throws IOException {
		Files.write(new GsonBuilder().setPrettyPrinting().create()
				.toJson(ArgumentRegistry.a(dispatcher, dispatcher.getRoot())), file, StandardCharsets.UTF_8);
	}

	@Override
	public org.bukkit.advancement.Advancement getAdvancement(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		return ArgumentMinecraftKeyRegistered.a(cmdCtx, key).bukkit;
	}

	@Override
	public Component getAdventureChat(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException  {
		return PaperComponents.gsonSerializer().deserialize(ChatSerializer.a(ArgumentChat.a(cmdCtx, key)));
	}

	@Override
	public Component getAdventureChatComponent(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return PaperComponents.gsonSerializer().deserialize(ChatSerializer.a(ArgumentChatComponent.a(cmdCtx, key)));
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
			case X -> set.add(Axis.X);
			case Y -> set.add(Axis.Y);
			case Z -> set.add(Axis.Z);
			default -> throw new IllegalArgumentException("Unexpected value: " + element);
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
			return predicate.test(new ShapeDetectorBlock(cmdCtx.getSource().getWorld(),
					new BlockPosition(block.getX(), block.getY(), block.getZ()), true));
		};
	}

	@Override
	public BlockData getBlockState(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return CraftBlockData.fromData(ArgumentTile.a(cmdCtx, key).a());
	}

	@Override
	public com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> getBrigadierDispatcher() {
		return MINECRAFT_SERVER.vanillaCommandDispatcher.a();
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

	@Override
	public CommandListenerWrapper getCLWFromCommandSender(CommandSender sender) {
		return VanillaCommandWrapper.getListener(sender);
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
				List<org.bukkit.entity.Entity> result = new ArrayList<>();
				for(Entity entity : argument.getEntities(cmdCtx.getSource())) {
					result.add(entity.getBukkitEntity());
				}
				yield result;
			} catch (CommandSyntaxException e) {
				yield new ArrayList<org.bukkit.entity.Entity>();
			}
		case MANY_PLAYERS:
			// ArgumentEntity.d -> EntitySelector.d
			try {
				List<Player> result = new ArrayList<>();
				for(EntityPlayer player : argument.d(cmdCtx.getSource())) {
					result.add(player.getBukkitEntity());
				}
				yield result;
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
	
	@Override
	public FunctionWrapper[] getFunction(CommandContext<CommandListenerWrapper> cmdCtx, String str) throws CommandSyntaxException {
		List<FunctionWrapper> result = new ArrayList<>();
		CommandListenerWrapper commandListenerWrapper = cmdCtx.getSource().a().b(2);
		
		for(CustomFunction customFunction : ArgumentTag.a(cmdCtx, str)) {
			result.add(FunctionWrapper.fromSimpleFunctionWrapper(convertFunction(customFunction), commandListenerWrapper, e -> {
				return cmdCtx.getSource().a(((CraftEntity) e).getHandle());
			}));
		}
		return result.toArray(new FunctionWrapper[0]);
	}
	
	@Override
	public SimpleFunctionWrapper getFunction(NamespacedKey key) {
		return convertFunction(MINECRAFT_SERVER.getFunctionData().a(new MinecraftKey(key.getNamespace(), key.getKey())).get());
	}
	
	@Override
	public Set<NamespacedKey> getFunctions() {
		Set<NamespacedKey> result = new HashSet<>();
		for(MinecraftKey minecraftKey : MINECRAFT_SERVER.getFunctionData().f()) {
			result.add(fromMinecrafKey(minecraftKey));
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
		return ArgumentMinecraftKeyRegistered.e(cmdCtx, key).toString();
	}

	@Override
	public Location getLocationBlock(CommandContext<CommandListenerWrapper> cmdCtx, String str)
			throws CommandSyntaxException {
		BlockPosition blockPos = ArgumentPosition.a(cmdCtx, str);
		return new Location(getWorldForCSS(cmdCtx.getSource()), blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}
	
	@Override
	public Location getLocationPrecise(CommandContext<CommandListenerWrapper> cmdCtx, String str)
			throws CommandSyntaxException {
		Vec3D vecPos = ArgumentVec3.a(cmdCtx, str);
		return new Location(getWorldForCSS(cmdCtx.getSource()), vecPos.x, vecPos.y, vecPos.z);
	}
	
	@Override
	public Location2D getLocation2DPrecise(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		Vec2F vecPos = ArgumentVec2.a(cmdCtx, key);
		return new Location2D(getWorldForCSS(cmdCtx.getSource()), vecPos.i, vecPos.j);
	}

	@Override
	public Location2D getLocation2DBlock(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		BlockPosition2D blockPos = ArgumentVec2I.a(cmdCtx, key);
		return new Location2D(getWorldForCSS(cmdCtx.getSource()), blockPos.a, blockPos.b);
	}

	@Override
	public org.bukkit.loot.LootTable getLootTable(CommandContext<CommandListenerWrapper> cmdCtx, String str) {
		MinecraftKey minecraftKey = ArgumentMinecraftKeyRegistered.e(cmdCtx, str);
		return new CraftLootTable(fromMinecrafKey(minecraftKey), MINECRAFT_SERVER.getLootTableRegistry().getLootTable(minecraftKey));
	}

	@Override
	public MathOperation getMathOperation(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		// We run this to ensure the argument exists/parses properly 
		ArgumentMathOperation.a(cmdCtx, key);
		return MathOperation.fromString(CommandAPIHandler.getRawArgumentInput(cmdCtx, key));
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
	public OfflinePlayer getOfflinePlayer(CommandContext<CommandListenerWrapper> cmdCtx, String str) throws CommandSyntaxException {
		OfflinePlayer target = Bukkit.getOfflinePlayer(((GameProfile) ArgumentProfile.a(cmdCtx, str).iterator().next()).getId());
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
		return new ComplexRecipeImpl(fromMinecrafKey(recipe.getKey()), recipe.toBukkitRecipe());
	}

	@Override
	public Rotation getRotation(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		Vec2F vec = ArgumentRotation.a(cmdCtx, key).b(cmdCtx.getSource());
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
		CommandListenerWrapper clw = cmdCtx.getSource();

		CommandSender sender = clw.getBukkitSender();
		Vec3D pos = clw.getPosition();
		Vec2F rot = clw.i();
		World world = getWorldForCSS(clw);
		Location location = new Location(world, pos.getX(), pos.getY(), pos.getZ(), rot.j, rot.i);

		Entity proxyEntity = clw.getEntity();
		CommandSender proxy = proxyEntity == null ? null : proxyEntity.getBukkitEntity();
		if (isNative || (proxy != null && !sender.equals(proxy))) {
			return new NativeProxyCommandSender(sender, proxy, location, world);
		} else {
			return sender;
		}
	}

	@Override
	public SimpleCommandMap getSimpleCommandMap() {
		return ((CraftServer) Bukkit.getServer()).getCommandMap();
	}

	@Override
	public Sound getSound(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return CraftSound.getBukkit(IRegistry.SOUND_EVENT.get(ArgumentMinecraftKeyRegistered.e(cmdCtx, key)));
	}

	@Override
	public SuggestionProvider<CommandListenerWrapper> getSuggestionProvider(SuggestionProviders provider) {
		return switch (provider) {
		case FUNCTION -> 
			(context, builder) -> {
				CustomFunctionData functionData = MINECRAFT_SERVER.getFunctionData();
				ICompletionProvider.a(functionData.g(), builder, "#");
				return ICompletionProvider.a(functionData.f(), builder);
			};
		case RECIPES -> CompletionProviders.b;
		case SOUNDS -> CompletionProviders.c;
		case ADVANCEMENTS ->
			(cmdCtx, builder) -> {
				return ICompletionProvider.a(MINECRAFT_SERVER.getAdvancementData().getAdvancements().stream().map(Advancement::getName), builder);
			};
		case LOOT_TABLES ->
			(cmdCtx, builder) -> {
				return ICompletionProvider.a(MINECRAFT_SERVER.getLootTableRegistry().a(), builder);
			};
		case BIOMES -> CompletionProviders.d;
		case ENTITIES -> CompletionProviders.e;
		default -> (context, builder) -> Suggestions.empty();
		};
	}

	@Override
	public SimpleFunctionWrapper[] getTag(NamespacedKey key) {
		List<CustomFunction> customFunctions = MINECRAFT_SERVER.getFunctionData().b(new MinecraftKey(key.getNamespace(), key.getKey())).getTagged();
		SimpleFunctionWrapper[] result = new SimpleFunctionWrapper[customFunctions.size()];
		for(int i = 0, size = customFunctions.size(); i < size; i++) {
			result[i] = convertFunction(customFunctions.get(i));
		}
		return result;
	}

	@Override
	public Set<NamespacedKey> getTags() {
		Set<NamespacedKey> result = new HashSet<>();
		for(MinecraftKey minecraftKey : MINECRAFT_SERVER.getFunctionData().g()) {
			result.add(fromMinecrafKey(minecraftKey));
		}
		return result;
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
	public World getWorldForCSS(CommandListenerWrapper clw) {
		return (clw.getWorld() == null) ? null : clw.getWorld().getWorld();
	}

	@Override
	public boolean isVanillaCommandWrapper(Command command) {
		return command instanceof VanillaCommandWrapper;
	}

	@Override
	public void reloadDataPacks()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		CommandAPI.logNormal("Reloading datapacks...");

		// Get previously declared recipes to be re-registered later
		Iterator<Recipe> recipes = Bukkit.recipeIterator();

		// Update the commandDispatcher with the current server's commandDispatcher
		DataPackResources datapackResources = MINECRAFT_SERVER.dataPackResources;
		datapackResources.commandDispatcher = MINECRAFT_SERVER.getCommandDispatcher();

		// Update the CustomFunctionManager for the datapackResources which now has the new commandDispatcher
		try {
			CommandAPIHandler.getInstance().getField(DataPackResources.class, "i").set(datapackResources,
					new CustomFunctionManager((int) CUSTOMFUNCTIONMANAGER_G.get(datapackResources.a()),
							datapackResources.commandDispatcher.a()));
		} catch (IllegalArgumentException | IllegalAccessException e1) {
			e1.printStackTrace();
		}

		// Construct the new CompletableFuture that now uses our updated datapackResources
		CompletableFuture<?> unitCompletableFuture = ((IReloadableResourceManager) DATAPACKRESOURCES_B
				.get(datapackResources)).a(SystemUtils.f(), Runnable::run,
						MINECRAFT_SERVER.getResourcePackRepository().f(), CompletableFuture.completedFuture(null));

		CompletableFuture<DataPackResources> completablefuture = unitCompletableFuture
				.whenComplete((Object u, Throwable t) -> {
					if (t != null) {
						datapackResources.close();
					}
				}).thenApply((Object u) -> datapackResources);

		// Run the completableFuture and bind tags
		try {
			completablefuture.get().i();

			// Register recipes again because reloading datapacks removes all non-vanilla recipes
			Recipe recipe;
			while(recipes.hasNext()) {
				recipe = recipes.next();
				try {
					Bukkit.addRecipe(recipe);
					if (recipe instanceof Keyed keyedRecipe) {
						CommandAPI.logInfo("Re-registering recipe: " + keyedRecipe.getKey());
					}
				} catch (Exception e) {
					continue; // Can't re-register registered recipes. Not an error.
				}
			}

			CommandAPI.logNormal("Finished reloading datapacks");
		} catch (Exception e) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			
			CommandAPI.logError("Failed to load datapacks, can't proceed with normal server load procedure. Try fixing your datapacks?\n" + stringWriter.toString());
		}
	}

	@Override
	public void resendPackets(Player player) {
		MINECRAFT_SERVER.getCommandDispatcher().a(((CraftPlayer) player).getHandle());
	}
}
