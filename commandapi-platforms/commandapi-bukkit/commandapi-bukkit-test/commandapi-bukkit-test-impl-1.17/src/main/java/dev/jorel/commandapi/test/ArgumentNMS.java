package dev.jorel.commandapi.test;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

import org.bukkit.Axis;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.loot.LootTable;
import org.bukkit.potion.PotionEffectType;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.arguments.ArgumentSubType;
import dev.jorel.commandapi.arguments.SuggestionProviders;
import dev.jorel.commandapi.wrappers.FloatRange;
import dev.jorel.commandapi.wrappers.FunctionWrapper;
import dev.jorel.commandapi.wrappers.IntegerRange;
import dev.jorel.commandapi.wrappers.Location2D;
import dev.jorel.commandapi.wrappers.MathOperation;
import dev.jorel.commandapi.wrappers.ParticleData;
import dev.jorel.commandapi.wrappers.Rotation;
import dev.jorel.commandapi.wrappers.ScoreboardSlot;
import dev.jorel.commandapi.wrappers.SimpleFunctionWrapper;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

/**
 * Argument related method implementations
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class ArgumentNMS extends MockPlatform<CommandSourceStack> {

	public CommandAPIBukkit<?> BASE_NMS;

	public ArgumentNMS(CommandAPIBukkit<?> baseNMS) {
		this.BASE_NMS = baseNMS;
	}

	@Override
	public ArgumentType<?> _ArgumentAngle() {
		return BASE_NMS._ArgumentAngle();
	}

	@Override
	public ArgumentType<?> _ArgumentAxis() {
		return BASE_NMS._ArgumentAxis();
	}

	@Override
	public final ArgumentType<?> _ArgumentBlockPredicate() {
		return BASE_NMS._ArgumentBlockPredicate();
	}

	@Override
	public final ArgumentType<?> _ArgumentBlockState() {
		return BASE_NMS._ArgumentBlockState();
	}

	@Override
	public ArgumentType<?> _ArgumentChat() {
		return BASE_NMS._ArgumentChat();
	}

	@Override
	public ArgumentType<?> _ArgumentChatComponent() {
		return BASE_NMS._ArgumentChatComponent();
	}

	@Override
	public ArgumentType<?> _ArgumentChatFormat() {
		return BASE_NMS._ArgumentChatFormat();
	}

	@Override
	public ArgumentType<?> _ArgumentDimension() {
		return BASE_NMS._ArgumentDimension();
	}

	@Override
	public ArgumentType<?> _ArgumentEnchantment() {
		return BASE_NMS._ArgumentEnchantment();
	}

	@Override
	public ArgumentType<?> _ArgumentEntity(ArgumentSubType subType) {
		return BASE_NMS._ArgumentEntity(subType);
	}

	@Override
	public ArgumentType<?> _ArgumentEntitySummon() {
		return BASE_NMS._ArgumentEntitySummon();
	}

	@Override
	public ArgumentType<?> _ArgumentFloatRange() {
		return BASE_NMS._ArgumentFloatRange();
	}

	@Override
	public ArgumentType<?> _ArgumentIntRange() {
		return BASE_NMS._ArgumentIntRange();
	}

	@Override
	public final ArgumentType<?> _ArgumentItemPredicate() {
		return BASE_NMS._ArgumentItemPredicate();
	}

	@Override
	public final ArgumentType<?> _ArgumentItemStack() {
		return BASE_NMS._ArgumentItemStack();
	}

	@Override
	public ArgumentType<?> _ArgumentMathOperation() {
		return BASE_NMS._ArgumentMathOperation();
	}

	@Override
	public ArgumentType<?> _ArgumentMinecraftKeyRegistered() {
		return BASE_NMS._ArgumentMinecraftKeyRegistered();
	}

	@Override
	public ArgumentType<?> _ArgumentMobEffect() {
		return BASE_NMS._ArgumentMobEffect();
	}

	@Override
	public ArgumentType<?> _ArgumentNBTCompound() {
		return BASE_NMS._ArgumentNBTCompound();
	}

	@Override
	public ArgumentType<?> _ArgumentParticle() {
		return BASE_NMS._ArgumentParticle();
	}

	@Override
	public ArgumentType<?> _ArgumentPosition() {
		return BASE_NMS._ArgumentPosition();
	}

	@Override
	public ArgumentType<?> _ArgumentPosition2D() {
		return BASE_NMS._ArgumentPosition2D();
	}

	@Override
	public ArgumentType<?> _ArgumentProfile() {
		return BASE_NMS._ArgumentProfile();
	}

	@Override
	public ArgumentType<?> _ArgumentRotation() {
		return BASE_NMS._ArgumentRotation();
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardCriteria() {
		return BASE_NMS._ArgumentScoreboardCriteria();
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardObjective() {
		return BASE_NMS._ArgumentScoreboardObjective();
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardSlot() {
		return BASE_NMS._ArgumentScoreboardSlot();
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardTeam() {
		return BASE_NMS._ArgumentScoreboardTeam();
	}

	@Override
	public ArgumentType<?> _ArgumentScoreholder(ArgumentSubType subType) {
		return BASE_NMS._ArgumentScoreholder(subType);
	}

	@Override
	public final ArgumentType<?> _ArgumentSyntheticBiome() {
		return BASE_NMS._ArgumentSyntheticBiome();
	}

	@Override
	public ArgumentType<?> _ArgumentTag() {
		return BASE_NMS._ArgumentTag();
	}

	@Override
	public ArgumentType<?> _ArgumentTime() {
		return BASE_NMS._ArgumentTime();
	}

	@Override
	public ArgumentType<?> _ArgumentUUID() {
		return BASE_NMS._ArgumentUUID();
	}

	@Override
	public ArgumentType<?> _ArgumentVec2() {
		return BASE_NMS._ArgumentVec2();
	}

	@Override
	public ArgumentType<?> _ArgumentVec3() {
		return BASE_NMS._ArgumentVec3();
	}

	@Override
	public Message generateMessageFromJson(final String json) {
		return BASE_NMS.generateMessageFromJson(json);
	}

	@Override
	public org.bukkit.advancement.Advancement getAdvancement(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getAdvancement(cmdCtx, key);
	}

	@Override
	public Component getAdventureChat(CommandContext cmdCtx, String key)
		throws CommandSyntaxException {
		return BASE_NMS.getAdventureChat(cmdCtx, key);
	}

	@Override
	public Component getAdventureChatComponent(CommandContext cmdCtx, String key) {
		return BASE_NMS.getAdventureChatComponent(cmdCtx, key);
	}

	@Override
	public float getAngle(CommandContext cmdCtx, String key) {
		return BASE_NMS.getAngle(cmdCtx, key);
	}

	@Override
	public EnumSet<Axis> getAxis(CommandContext cmdCtx, String key) {
		return BASE_NMS.getAxis(cmdCtx, key);
	}

	@Override
	public Object getBiome(CommandContext cmdCtx, String key, ArgumentSubType subType) throws CommandSyntaxException {
		return BASE_NMS.getBiome(cmdCtx, key, subType);
	}

	@Override
	public Predicate<Block> getBlockPredicate(CommandContext cmdCtx, String key)
		throws CommandSyntaxException {
		return BASE_NMS.getBlockPredicate(cmdCtx, key);
	}

	@Override
	public BlockData getBlockState(CommandContext cmdCtx, String key) {
		return BASE_NMS.getBlockState(cmdCtx, key);
	}

	@Override
	public BaseComponent[] getChat(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getChat(cmdCtx, key);
	}

	@Override
	public ChatColor getChatColor(CommandContext cmdCtx, String key) {
		return BASE_NMS.getChatColor(cmdCtx, key);
	}

	@Override
	public BaseComponent[] getChatComponent(CommandContext cmdCtx, String key) {
		return BASE_NMS.getChatComponent(cmdCtx, key);
	}

	@Override
	public World getDimension(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getDimension(cmdCtx, key);
	}

	@Override
	public Enchantment getEnchantment(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getEnchantment(cmdCtx, key);
	}

	@Override
	public Object getEntitySelector(CommandContext cmdCtx, String key, ArgumentSubType subType) throws CommandSyntaxException {
		return BASE_NMS.getEntitySelector(cmdCtx, key, subType);
	}

	@Override
	public EntityType getEntityType(CommandContext cmdCtx, String key)
		throws CommandSyntaxException {
		return BASE_NMS.getEntityType(cmdCtx, key);
	}

	@Override
	public Environment getEnvironment(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getEnvironment(cmdCtx, key);
	}

	@Override
	public FloatRange getFloatRange(CommandContext cmdCtx, String key) {
		return BASE_NMS.getFloatRange(cmdCtx, key);
	}

	@Override
	public FunctionWrapper[] getFunction(CommandContext cmdCtx, String key)
		throws CommandSyntaxException {
		return BASE_NMS.getFunction(cmdCtx, key);
	}

	@Override
	public SimpleFunctionWrapper getFunction(NamespacedKey key) {
		return getFunction(key);
	}

	@Override
	public Set<NamespacedKey> getFunctions() {
		return BASE_NMS.getFunctions();
	}

	@Override
	public IntegerRange getIntRange(CommandContext cmdCtx, String key) {
		return BASE_NMS.getIntRange(cmdCtx, key);
	}

	@Override
	public ItemStack getItemStack(CommandContext cmdCtx, String key)
		throws CommandSyntaxException {
		return BASE_NMS.getItemStack(cmdCtx, key);
	}

	@Override
	public Predicate<ItemStack> getItemStackPredicate(CommandContext cmdCtx, String key)
		throws CommandSyntaxException {
		return BASE_NMS.getItemStackPredicate(cmdCtx, key);
	}

	@Override
	public Location2D getLocation2DBlock(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getLocation2DBlock(cmdCtx, key);
	}

	@Override
	public Location2D getLocation2DPrecise(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getLocation2DPrecise(cmdCtx, key);
	}

	@Override
	public Location getLocationBlock(CommandContext cmdCtx, String str) throws CommandSyntaxException {
		return BASE_NMS.getLocationBlock(cmdCtx, str);
	}

	@Override
	public final Location getLocationPrecise(CommandContext cmdCtx, String str) throws CommandSyntaxException {
		return BASE_NMS.getLocationPrecise(cmdCtx, str);
	}

	@Override
	public LootTable getLootTable(CommandContext cmdCtx, String key) {
		return BASE_NMS.getLootTable(cmdCtx, key);
	}

	@Override
	public MathOperation getMathOperation(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getMathOperation(cmdCtx, key);
	}

	@Override
	public NamespacedKey getMinecraftKey(CommandContext cmdCtx, String key) {
		return BASE_NMS.getMinecraftKey(cmdCtx, key);
	}

	@Override
	public <NBTContainer> Object getNBTCompound(CommandContext<CommandSourceStack> cmdCtx, String key,
		Function<Object, NBTContainer> nbtContainerConstructor) {
		return BASE_NMS.getNBTCompound((CommandContext) cmdCtx, key, nbtContainerConstructor);
	}

	@Override
	public Objective getObjective(CommandContext cmdCtx, String key)
		throws IllegalArgumentException, CommandSyntaxException {
		return BASE_NMS.getObjective(cmdCtx, key);
	}

	@Override
	public String getObjectiveCriteria(CommandContext cmdCtx, String key) {
		return BASE_NMS.getObjectiveCriteria(cmdCtx, key);
	}

	@Override
	public OfflinePlayer getOfflinePlayer(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getOfflinePlayer(cmdCtx, key);
	}

	@Override
	public ParticleData<?> getParticle(CommandContext cmdCtx, String key) {
		return BASE_NMS.getParticle(cmdCtx, key);
	}

	@Override
	public Player getPlayer(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getPlayer(cmdCtx, key);
	}

	@Override
	public PotionEffectType getPotionEffect(CommandContext cmdCtx, String key)
		throws CommandSyntaxException {
		return BASE_NMS.getPotionEffect(cmdCtx, key);
	}

	@Override
	public Recipe getRecipe(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getRecipe(cmdCtx, key);
	}

	@Override
	public Rotation getRotation(CommandContext cmdCtx, String key) {
		return BASE_NMS.getRotation(cmdCtx, key);
	}

	@Override
	public ScoreboardSlot getScoreboardSlot(CommandContext cmdCtx, String key) {
		return BASE_NMS.getScoreboardSlot(cmdCtx, key);
	}

	@Override
	public Collection<String> getScoreHolderMultiple(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getScoreHolderMultiple(cmdCtx, key);
	}

	@Override
	public String getScoreHolderSingle(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getScoreHolderSingle(cmdCtx, key);
	}

	@Override
	public Object getSound(CommandContext cmdCtx, String key, ArgumentSubType subType) {
		return BASE_NMS.getSound(cmdCtx, key, subType);
	}

	@Override
	public SuggestionProvider getSuggestionProvider(SuggestionProviders provider) {
		return BASE_NMS.getSuggestionProvider(provider);
	}

	@Override
	public SimpleFunctionWrapper[] getTag(NamespacedKey key) {
		return BASE_NMS.getTag(key);
	}

	@Override
	public Set<NamespacedKey> getTags() {
		return BASE_NMS.getTags();
	}

	@Override
	public Team getTeam(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getTeam(cmdCtx, key);
	}

	@Override
	public int getTime(CommandContext cmdCtx, String key) {
		return BASE_NMS.getTime(cmdCtx, key);
	}

	@Override
	public UUID getUUID(CommandContext cmdCtx, String key) {
		return BASE_NMS.getUUID(cmdCtx, key);
	}

}
