import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;

import dev.jorel.commandapi.arguments.SuggestionProviders;
import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.wrappers.FloatRange;
import dev.jorel.commandapi.wrappers.IntegerRange;
import dev.jorel.commandapi.wrappers.MathOperation;
import dev.jorel.commandapi.wrappers.ParticleData;
import dev.jorel.commandapi.wrappers.Rotation;
import dev.jorel.commandapi.wrappers.ScoreboardSlot;
import dev.jorel.commandapi.wrappers.SimpleFunctionWrapper;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.arguments.ArgumentChat;
import net.minecraft.commands.arguments.ArgumentChatComponent;
import net.minecraft.commands.arguments.ArgumentMinecraftKeyRegistered;
import net.minecraft.commands.arguments.ArgumentMobEffect;
import net.minecraft.commands.arguments.ArgumentProfile;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.commands.arguments.coordinates.ArgumentPosition;
import net.minecraft.commands.arguments.coordinates.ArgumentVec2;
import net.minecraft.commands.arguments.coordinates.ArgumentVec2I;
import net.minecraft.commands.arguments.coordinates.ArgumentVec3;
import net.minecraft.network.chat.IChatBaseComponent;

/**
 * This class is effectively identical to NMS_Common, except we want to be able
 * to override some of its methods. Yes, we could remove the final modifiers on
 * the NMS_Common overridden methods, however we do that to ensure that we're
 * not re-overriding those methods in the underlying NMS implementations. The
 * choice between having safer runtime code compared to having safer testing
 * code is to have safer runtime code instead.
 */
public abstract class BlankNMS implements NMS<CommandListenerWrapper> {

	public final NMS<?> BASE_NMS;

	public BlankNMS(NMS<?> baseNMS) {
		this.BASE_NMS = baseNMS;
	}

	// TODO: All of this stuff needs to be implemented at some point

	@Override
	public SimpleCommandMap getSimpleCommandMap() {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public void addToHelpMap(Map<String, HelpTopic> helpTopicsToAdd) {
		// TODO Auto-generated method stub

	}

	@Override
	public String convert(ItemStack is) {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public String convert(ParticleData<?> particle) {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public HelpTopic generateHelpTopic(String commandName, String shortDescription, String fullDescription, String permission) {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public SuggestionProvider<CommandListenerWrapper> getSuggestionProvider(SuggestionProviders provider) {
		return (context, builder) -> Suggestions.empty();
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

	@Override
	public SimpleFunctionWrapper getFunction(NamespacedKey key) {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public Set<NamespacedKey> getFunctions() {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public SimpleFunctionWrapper[] getTag(NamespacedKey key) {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public Set<NamespacedKey> getTags() {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentAngle() {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentAxis() {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
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
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentDimension() {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentEnchantment() {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentEntitySummon() {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentFloatRange() {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentIntRange() {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentMathOperation() {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
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
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentParticle() {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
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
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardCriteria() {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardObjective() {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardSlot() {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardTeam() {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentScoreholder(boolean single) {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentTag() {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentTime() {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentUUID() {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
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
	public String convert(PotionEffectType potion) {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public String convert(Sound sound) {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public org.bukkit.advancement.Advancement getAdvancement(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return ArgumentMinecraftKeyRegistered.a(cmdCtx, key).bukkit;
	}

	@Override
	public float getAngle(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public EnumSet<Axis> getAxis(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public BaseComponent[] getChat(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public BaseComponent[] getChatComponent(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return ComponentSerializer.parse(IChatBaseComponent.ChatSerializer.a(ArgumentChatComponent.a(cmdCtx, key)));
	}

	@Override
	public Environment getDimension(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public FloatRange getFloatRange(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public IntegerRange getIntRange(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public MathOperation getMathOperation(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public NamespacedKey getMinecraftKey(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public Player getPlayer(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		Player target = Bukkit.getPlayer(ArgumentProfile.a(cmdCtx, key).iterator().next().getId());
		if (target == null) {
			throw GameProfileArgument.ERROR_UNKNOWN_PLAYER.create();
		} else {
			return target;
		}
	}

	@Override
	public OfflinePlayer getOfflinePlayer(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public Rotation getRotation(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ScoreboardSlot getScoreboardSlot(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public Collection<String> getScoreHolderMultiple(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public String getScoreHolderSingle(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

	@Override
	public int getTime(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UUID getUUID(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		throw new RuntimeException("unimplemented");
	}

}
