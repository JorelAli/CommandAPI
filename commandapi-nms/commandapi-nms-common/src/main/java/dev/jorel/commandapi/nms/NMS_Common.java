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

import java.util.Collection;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import de.tr7zw.nbtapi.NBTContainer;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.wrappers.FloatRange;
import dev.jorel.commandapi.wrappers.IntegerRange;
import dev.jorel.commandapi.wrappers.Location2D;
import dev.jorel.commandapi.wrappers.MathOperation;
import dev.jorel.commandapi.wrappers.Rotation;
import dev.jorel.commandapi.wrappers.ScoreboardSlot;
import net.minecraft.advancements.critereon.CriterionConditionValue;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.arguments.ArgumentAngle;
import net.minecraft.commands.arguments.ArgumentChat;
import net.minecraft.commands.arguments.ArgumentChatComponent;
import net.minecraft.commands.arguments.ArgumentChatFormat;
import net.minecraft.commands.arguments.ArgumentCriterionValue;
import net.minecraft.commands.arguments.ArgumentDimension;
import net.minecraft.commands.arguments.ArgumentEnchantment;
import net.minecraft.commands.arguments.ArgumentEntity;
import net.minecraft.commands.arguments.ArgumentEntitySummon;
import net.minecraft.commands.arguments.ArgumentMathOperation;
import net.minecraft.commands.arguments.ArgumentMinecraftKeyRegistered;
import net.minecraft.commands.arguments.ArgumentMobEffect;
import net.minecraft.commands.arguments.ArgumentNBTTag;
import net.minecraft.commands.arguments.ArgumentParticle;
import net.minecraft.commands.arguments.ArgumentProfile;
import net.minecraft.commands.arguments.ArgumentScoreboardCriteria;
import net.minecraft.commands.arguments.ArgumentScoreboardObjective;
import net.minecraft.commands.arguments.ArgumentScoreboardSlot;
import net.minecraft.commands.arguments.ArgumentScoreboardTeam;
import net.minecraft.commands.arguments.ArgumentScoreholder;
import net.minecraft.commands.arguments.ArgumentUUID;
import net.minecraft.commands.arguments.blocks.ArgumentBlockPredicate;
import net.minecraft.commands.arguments.coordinates.ArgumentPosition;
import net.minecraft.commands.arguments.coordinates.ArgumentRotation;
import net.minecraft.commands.arguments.coordinates.ArgumentRotationAxis;
import net.minecraft.commands.arguments.coordinates.ArgumentVec2;
import net.minecraft.commands.arguments.coordinates.ArgumentVec2I;
import net.minecraft.commands.arguments.coordinates.ArgumentVec3;
import net.minecraft.commands.arguments.item.ArgumentTag;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.world.level.block.state.pattern.ShapeDetectorBlock;
import net.minecraft.world.phys.Vec2F;

// Mojang-Mapped reflection
//@NMSMeta(compatibleWith = "1.19")
//@RequireField(in = ServerFunctionLibrary.class, name = "dispatcher", ofType = CommandDispatcher.class)
//@RequireField(in = EntitySelector.class, name = "usesSelector", ofType = boolean.class)
//@RequireField(in = SimpleHelpMap.class, name = "helpTopics", ofType = Map.class)
//@RequireField(in = EntityPositionSource.class, name = "entityOrUuidOrId", ofType = Either.class)
public abstract class NMS_Common implements NMS<CommandListenerWrapper> {

	@Override
	public final ArgumentType<?> _ArgumentAngle() {
		return ArgumentAngle.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentAxis() {
		return ArgumentRotationAxis.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentChat() {
		return ArgumentChat.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentChatComponent() {
		return ArgumentChatComponent.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentChatFormat() {
		return ArgumentChatFormat.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentDimension() {
		return ArgumentDimension.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentEnchantment() {
		return ArgumentEnchantment.a();
	}

	// TODO: Whaaaaaaaaaaaaaaat is going on here???
	// ArgumentEntity.multipleEntities() -> ArgumentEntity.b() from 1.16.5 to 1.19?
	@Override
	public final ArgumentType<?> _ArgumentEntity(
			dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector selector) {
		return switch (selector) {
			case MANY_ENTITIES -> ArgumentEntity.b();
			case MANY_PLAYERS -> ArgumentEntity.d();
			case ONE_ENTITY -> ArgumentEntity.a();
			case ONE_PLAYER -> ArgumentEntity.c();
		};
	}

	@Override
	public final ArgumentType<?> _ArgumentEntitySummon() {
		return ArgumentEntitySummon.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentFloatRange() {
		return ArgumentCriterionValue.b();
	}

	@Override
	public final ArgumentType<?> _ArgumentIntRange() {
		return ArgumentCriterionValue.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentMathOperation() {
		return ArgumentMathOperation.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentMinecraftKeyRegistered() {
		return ArgumentMinecraftKeyRegistered.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentMobEffect() {
		return ArgumentMobEffect.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentNBTCompound() {
		return ArgumentNBTTag.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentParticle() {
		return ArgumentParticle.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentPosition() {
		return ArgumentPosition.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentPosition2D() {
		return ArgumentVec2I.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentProfile() {
		return ArgumentProfile.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentRotation() {
		return ArgumentRotation.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentScoreboardCriteria() {
		return ArgumentScoreboardCriteria.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentScoreboardObjective() {
		return ArgumentScoreboardObjective.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentScoreboardSlot() {
		return ArgumentScoreboardSlot.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentScoreboardTeam() {
		return ArgumentScoreboardTeam.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentScoreholder(boolean single) {
		return single ? ArgumentScoreholder.a() : ArgumentScoreholder.b();
	}

	@Override
	public final ArgumentType<?> _ArgumentTag() {
		return ArgumentTag.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentTime() {
		return ArgumentTag.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentUUID() {
		return ArgumentUUID.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentVec2() {
		return ArgumentVec2.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentVec3() {
		return ArgumentVec3.a();
	}

	// TODO: Implement this
//	@Override
//	public final void addToHelpMap(Map<String, HelpTopic> helpTopicsToAdd) {
//		Map<String, HelpTopic> helpTopics = (Map<String, HelpTopic>) SimpleHelpMap_helpTopics
//				.get(Bukkit.getServer().getHelpMap());
//		// We have to use VarHandles to use helpTopics.put (instead of .addTopic)
//		// because we're updating an existing help topic, not adding a new help topic
//		for (Map.Entry<String, HelpTopic> entry : helpTopicsToAdd.entrySet()) {
//			helpTopics.put(entry.getKey(), entry.getValue());
//		}
//	}

	@Override
	public final String convert(PotionEffectType potion) {
		return potion.getName().toLowerCase(Locale.ENGLISH);
	}

	@Override
	public final String convert(Sound sound) {
		return sound.getKey().toString();
	}

	@Override
	public final org.bukkit.advancement.Advancement getAdvancement(CommandContext<CommandListenerWrapper> cmdCtx,
			String key) throws CommandSyntaxException {
		return ArgumentMinecraftKeyRegistered.a(cmdCtx, key).bukkit;
	}

//	@SuppressWarnings("removal")
//	@Override
//	public final Component getAdventureChat(CommandContext<CommandListenerWrapper> cmdCtx, String key)
//			throws CommandSyntaxException {
//		return PaperComponents.gsonSerializer().deserialize(Serializer.toJson(MessageArgument.getMessage(cmdCtx, key)));
//	}
//
//	@SuppressWarnings("removal")
//	@Override
//	public final Component getAdventureChatComponent(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
//		return PaperComponents.gsonSerializer()
//				.deserialize(Serializer.toJson(ComponentArgument.getComponent(cmdCtx, key)));
//	}

	@Override
	public final float getAngle(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return ArgumentAngle.a(cmdCtx, key);
	}

	@Override
	public final EnumSet<Axis> getAxis(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		EnumSet<Axis> set = EnumSet.noneOf(Axis.class);
		EnumSet<EnumDirection.EnumAxis> parsedEnumSet = ArgumentRotationAxis.a(cmdCtx, key);
		for (EnumDirection.EnumAxis element : parsedEnumSet) {
			set.add(switch (element) {
				case a -> Axis.X;
				case b -> Axis.Y;
				case c -> Axis.Z;
			});
		}
		return set;
	}

	@Override
	public final Predicate<Block> getBlockPredicate(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		// TODO: f <- getWorld
		Predicate<ShapeDetectorBlock> predicate = ArgumentBlockPredicate.a(cmdCtx, key);
		return block -> predicate.test(new ShapeDetectorBlock(cmdCtx.getSource().f(),
				new BlockPosition(block.getX(), block.getY(), block.getZ()), true));
	}

//	@Override
//	public final BaseComponent[] getChat(CommandContext<CommandListenerWrapper> cmdCtx, String key)
//			throws CommandSyntaxException {
//		return ComponentSerializer.parse(Serializer.toJson(MessageArgument.getMessage(cmdCtx, key)));
//	}

//	@Override
//	public final BaseComponent[] getChatComponent(CommandContext<CommandListenerWrapper> cmdCtx, String str) {
//		return ComponentSerializer.parse(Serializer.toJson(ComponentArgument.getComponent(cmdCtx, str)));
//	}

	@Override
	public final CommandSender getCommandSenderFromCSS(CommandListenerWrapper css) {
		try {
			return css.getBukkitSender();
		} catch (UnsupportedOperationException e) {
			return null;
		}
	}

	@Override
	public final Environment getDimension(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		return ArgumentDimension.a(cmdCtx, key).getWorld().getEnvironment();
	}

//	@Override
//	public final Object getEntitySelector(CommandContext<CommandListenerWrapper> cmdCtx, String str,
//			dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector selector)
//			throws CommandSyntaxException {
//
//		// We override the rule whereby players need "minecraft.command.selector" and
//		// have to have
//		// level 2 permissions in order to use entity selectors. We're trying to allow
//		// entity selectors
//		// to be used by anyone that registers a command via the CommandAPI.
//		EntitySelector argument = cmdCtx.getArgument(str, EntitySelector.class);
//		try {
//			CommandAPIHandler.getInstance().getField(EntitySelector.class, "o").set(argument, false);
//		} catch (IllegalArgumentException | IllegalAccessException e1) {
//			e1.printStackTrace();
//		}
//
//		return switch (selector) {
//			case MANY_ENTITIES:
//				try {
//					List<org.bukkit.entity.Entity> result = new ArrayList<>();
//					for (Entity entity : argument.findEntities(cmdCtx.getSource())) {
//						result.add(entity.getBukkitEntity());
//					}
//					yield result;
//				} catch (CommandSyntaxException e) {
//					yield new ArrayList<org.bukkit.entity.Entity>();
//				}
//			case MANY_PLAYERS:
//				try {
//					List<Player> result = new ArrayList<>();
//					for (ServerPlayer player : argument.findPlayers(cmdCtx.getSource())) {
//						result.add(player.getBukkitEntity());
//					}
//					yield result;
//				} catch (CommandSyntaxException e) {
//					yield new ArrayList<Player>();
//				}
//			case ONE_ENTITY:
//				yield argument.findSingleEntity(cmdCtx.getSource()).getBukkitEntity();
//			case ONE_PLAYER:
//				yield argument.findSinglePlayer(cmdCtx.getSource()).getBukkitEntity();
//		};
//	}

	@Override
	public final FloatRange getFloatRange(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		CriterionConditionValue.DoubleRange range = ArgumentCriterionValue.a.a(cmdCtx, key);
		double low = range.a() == null ? -Float.MAX_VALUE : range.a();
		double high = range.b() == null ? Float.MAX_VALUE : range.b();
		return new FloatRange((float) low, (float) high);
	}

	@Override
	public final IntegerRange getIntRange(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		CriterionConditionValue.IntegerRange range = ArgumentCriterionValue.b.a(cmdCtx, key);
		int low = (range.a() == null) ? Integer.MIN_VALUE : ((Integer) range.a()).intValue();
		int high = (range.b() == null) ? Integer.MAX_VALUE : ((Integer) range.b()).intValue();
		return new IntegerRange(low, high);
	}

	@Override
	public final String getKeyedAsString(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		return ArgumentMinecraftKeyRegistered.e(cmdCtx, key).toString();
	}

	@Override
	public final Location2D getLocation2DPrecise(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		Vec2F vecPos = ArgumentVec2.a(cmdCtx, key);
		return new Location2D(getWorldForCSS((CommandListenerWrapper) cmdCtx.getSource()), vecPos.i, vecPos.j);
	}

	@Override
	public final MathOperation getMathOperation(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		// We run this to ensure the argument exists/parses properly
		ArgumentMathOperation.a(cmdCtx, key);
		return MathOperation.fromString(CommandAPIHandler.getRawArgumentInput(cmdCtx, key));
	}

	@Override
	public final NBTContainer getNBTCompound(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return new NBTContainer(ArgumentNBTTag.a(cmdCtx, key));
	}

	@Override
	public final OfflinePlayer getOfflinePlayer(CommandContext<CommandListenerWrapper> cmdCtx, String str)
			throws CommandSyntaxException {
		OfflinePlayer target = Bukkit.getOfflinePlayer(ArgumentProfile.a(cmdCtx, str).iterator().next().getId());
		if (target == null) {
			throw ArgumentProfile.a.create();
		} else {
			return target;
		}
	}

	@Override
	public final Player getPlayer(CommandContext<CommandListenerWrapper> cmdCtx, String str)
			throws CommandSyntaxException {
		Player target = Bukkit.getPlayer(ArgumentProfile.a(cmdCtx, str).iterator().next().getId());
		if (target == null) {
			throw ArgumentProfile.a.create();
		} else {
			return target;
		}
	}

	// TODO: Whatever this is
//	@Override
//	public final ComplexRecipe getRecipe(CommandContext<CommandListenerWrapper> cmdCtx, String key)
//			throws CommandSyntaxException {
//		IRecipe<?> recipe = ArgumentMinecraftKeyRegistered.b(cmdCtx, key);
//		return new ComplexRecipeImpl(fromResourceLocation(recipe.f()), recipe.toBukkitRecipe());
//	}

	@Override
	public final Rotation getRotation(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		Vec2F rotation = ArgumentRotation.a(cmdCtx, key).b((CommandListenerWrapper) cmdCtx.getSource());
		return new Rotation(rotation.i, rotation.j);
	}

	@Override
	public final ScoreboardSlot getScoreboardSlot(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return new ScoreboardSlot(ArgumentScoreboardSlot.a(cmdCtx, key));
	}

	@Override
	public final Collection<String> getScoreHolderMultiple(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		return ArgumentScoreholder.b(cmdCtx, key);
	}

	@Override
	public final String getScoreHolderSingle(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		return ArgumentScoreholder.a(cmdCtx, key);
	}

	// TODO
//	@Override
//	public final CommandSender getSenderForCommand(CommandContext<CommandListenerWrapper> cmdCtx, boolean isNative) {
//		CommandListenerWrapper css = cmdCtx.getSource();
//
//		CommandSender sender = css.getBukkitSender();
//		Vec3 pos = css.getPosition();
//		Vec2 rot = css.getRotation();
//		World world = getWorldForCSS(css);
//		Location location = new Location(world, pos.x(), pos.y(), pos.z(), rot.x, rot.y);
//
//		Entity proxyEntity = css.getEntity();
//		CommandSender proxy = proxyEntity == null ? null : proxyEntity.getBukkitEntity();
//		if (isNative || (proxy != null && !sender.equals(proxy))) {
//			return new NativeProxyCommandSender(sender, proxy, location, world);
//		} else {
//			return sender;
//		}
//	}

//	@Override
//	public final String getTeam(CommandContext<CommandListenerWrapper> cmdCtx, String key)
//			throws CommandSyntaxException {
//		return TeamArgument.getTeam(cmdCtx, key).getName();
//	}

	@Override
	public final int getTime(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return (Integer) cmdCtx.getArgument(key, Integer.class);
	}

	@Override
	public final UUID getUUID(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return ArgumentUUID.a(cmdCtx, key);
	}
}
