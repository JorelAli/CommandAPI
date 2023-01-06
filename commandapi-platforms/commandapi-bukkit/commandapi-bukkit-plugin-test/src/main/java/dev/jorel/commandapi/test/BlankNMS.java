package dev.jorel.commandapi.test;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.arguments.ArgumentSubType;

import org.bukkit.Axis;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

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
import net.minecraft.commands.arguments.ArgumentChatComponent;
import net.minecraft.network.chat.IChatBaseComponent;

public abstract class BlankNMS extends CommandAPIBukkit<CommandListenerWrapper> {

	public final NMS<?> BASE_NMS;

	public BlankNMS(NMS<?> baseNMS) {
		this.BASE_NMS = baseNMS;
	}

	// TODO: All of this stuff needs to be implemented at some point

	@Override
	public SimpleCommandMap getSimpleCommandMap() {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public void addToHelpMap(Map<String, HelpTopic> helpTopicsToAdd) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public String convert(ItemStack is) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public String convert(ParticleData<?> particle) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public HelpTopic generateHelpTopic(String commandName, String shortDescription, String fullDescription, String permission) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public boolean isVanillaCommandWrapper(Command command) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public void reloadDataPacks() {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public SimpleFunctionWrapper getFunction(NamespacedKey key) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public Set<NamespacedKey> getFunctions() {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public SimpleFunctionWrapper[] getTag(NamespacedKey key) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public Set<NamespacedKey> getTags() {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentAngle() {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentAxis() {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentChatFormat() {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}
	
	@Override
	public ArgumentType<?> _ArgumentDimension() {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentEntitySummon() {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentFloatRange() {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentIntRange() {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentNBTCompound() {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentRotation() {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardCriteria() {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardObjective() {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardSlot() {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardTeam() {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentScoreholder(ArgumentSubType subType) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentTag() {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentTime() {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ArgumentType<?> _ArgumentUUID() {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public String convert(PotionEffectType potion) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public String convert(Sound sound) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public float getAngle(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public EnumSet<Axis> getAxis(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public BaseComponent[] getChat(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public BaseComponent[] getChatComponent(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return ComponentSerializer.parse(IChatBaseComponent.ChatSerializer.a(ArgumentChatComponent.a(cmdCtx, key)));
	}

	@Override
	public World getDimension(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public Environment getEnvironment(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public FloatRange getFloatRange(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public IntegerRange getIntRange(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public NamespacedKey getMinecraftKey(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public OfflinePlayer getOfflinePlayer(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public Rotation getRotation(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ScoreboardSlot getScoreboardSlot(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public Collection<String> getScoreHolderMultiple(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public String getScoreHolderSingle(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public int getTime(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public UUID getUUID(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

}
