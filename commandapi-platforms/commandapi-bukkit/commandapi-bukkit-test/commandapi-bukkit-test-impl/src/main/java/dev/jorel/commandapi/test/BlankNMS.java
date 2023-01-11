package dev.jorel.commandapi.test;
import java.util.Collection;
import java.util.Set;

import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.World.Environment;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.arguments.ArgumentSubType;
import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.wrappers.FloatRange;
import dev.jorel.commandapi.wrappers.IntegerRange;
import dev.jorel.commandapi.wrappers.ScoreboardSlot;
import dev.jorel.commandapi.wrappers.SimpleFunctionWrapper;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSourceStack;

public abstract class BlankNMS extends CommandAPIBukkit<CommandSourceStack> {

	public final NMS<?> BASE_NMS;

	public BlankNMS(NMS<?> baseNMS) {
		this.BASE_NMS = baseNMS;
	}

	// TODO: All of this stuff needs to be implemented at some point

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
	public float getAngle(CommandContext<CommandSourceStack> cmdCtx, String key) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public BaseComponent[] getChat(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public Environment getEnvironment(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public FloatRange getFloatRange(CommandContext<CommandSourceStack> cmdCtx, String key) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public IntegerRange getIntRange(CommandContext<CommandSourceStack> cmdCtx, String key) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public NamespacedKey getMinecraftKey(CommandContext<CommandSourceStack> cmdCtx, String key) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public OfflinePlayer getOfflinePlayer(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public ScoreboardSlot getScoreboardSlot(CommandContext<CommandSourceStack> cmdCtx, String key) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public Collection<String> getScoreHolderMultiple(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public String getScoreHolderSingle(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

}
