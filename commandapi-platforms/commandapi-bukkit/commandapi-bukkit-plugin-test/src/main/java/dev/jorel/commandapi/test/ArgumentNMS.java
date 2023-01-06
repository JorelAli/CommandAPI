package dev.jorel.commandapi.test;
import java.util.EnumSet;
import java.util.function.Function;
import java.util.function.Predicate;

import org.bukkit.Axis;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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

import dev.jorel.commandapi.arguments.ArgumentSubType;
import dev.jorel.commandapi.arguments.SuggestionProviders;
import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.wrappers.FunctionWrapper;
import dev.jorel.commandapi.wrappers.Location2D;
import dev.jorel.commandapi.wrappers.MathOperation;
import dev.jorel.commandapi.wrappers.ParticleData;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.commands.CommandListenerWrapper;

/**
 * Argument related method implementations
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class ArgumentNMS extends BlankNMS {

	public ArgumentNMS(NMS<?> baseNMS) {
		super(baseNMS);
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
	public ArgumentType<?> _ArgumentChatFormat() {
		return BASE_NMS._ArgumentChatFormat();
	}
	
	@Override
	public ArgumentType<?> _ArgumentChatComponent() {
		return BASE_NMS._ArgumentChatComponent();
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
	public final ArgumentType<?> _ArgumentSyntheticBiome() {
		return BASE_NMS._ArgumentSyntheticBiome();
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
	public org.bukkit.advancement.Advancement getAdvancement(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getAdvancement((CommandContext) cmdCtx, key);
	}

	@Override
	public Component getAdventureChat(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		return BASE_NMS.getAdventureChat((CommandContext) cmdCtx, key);
	}

	@Override
	public Component getAdventureChatComponent(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return BASE_NMS.getAdventureChatComponent((CommandContext) cmdCtx, key);
	}

	@Override
	public EnumSet<Axis> getAxis(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return BASE_NMS.getAxis((CommandContext) cmdCtx, key);
	}

	@Override
	public Object getBiome(CommandContext<CommandListenerWrapper> cmdCtx, String key, ArgumentSubType subType) throws CommandSyntaxException {
		return BASE_NMS.getBiome((CommandContext) cmdCtx, key, subType);
	}

	@Override
	public Predicate<Block> getBlockPredicate(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		return BASE_NMS.getBlockPredicate((CommandContext) cmdCtx, key);
	}

	@Override
	public BlockData getBlockState(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return BASE_NMS.getBlockState((CommandContext) cmdCtx, key);
	}

	@Override
	public ChatColor getChatColor(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return BASE_NMS.getChatColor((CommandContext) cmdCtx, key);
	}
	
	@Override
	public BaseComponent[] getChatComponent(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return BASE_NMS.getChatComponent((CommandContext) cmdCtx, key);
	}

	@Override
	public Enchantment getEnchantment(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getEnchantment((CommandContext) cmdCtx, key);
	}

	@Override
	public Object getEntitySelector(CommandContext<CommandListenerWrapper> cmdCtx, String key, ArgumentSubType subType) throws CommandSyntaxException {
		return BASE_NMS.getEntitySelector((CommandContext) cmdCtx, key, subType);
	}

	@Override
	public EntityType getEntityType(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		return BASE_NMS.getEntityType((CommandContext) cmdCtx, key);
	}

	@Override
	public FunctionWrapper[] getFunction(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		return BASE_NMS.getFunction((CommandContext) cmdCtx, key);
	}

	@Override
	public ItemStack getItemStack(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		return BASE_NMS.getItemStack((CommandContext) cmdCtx, key);
	}

	@Override
	public Predicate<ItemStack> getItemStackPredicate(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		return BASE_NMS.getItemStackPredicate((CommandContext) cmdCtx, key);
	}

	@Override
	public Location2D getLocation2DBlock(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getLocation2DBlock((CommandContext) cmdCtx, key);
	}

	@Override
	public Location2D getLocation2DPrecise(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getLocation2DPrecise((CommandContext) cmdCtx, key);
	}

	@Override
	public Location getLocationBlock(CommandContext<CommandListenerWrapper> cmdCtx, String str) throws CommandSyntaxException {
		return BASE_NMS.getLocationBlock((CommandContext) cmdCtx, str);

	}

	@Override
	public final Location getLocationPrecise(CommandContext<CommandListenerWrapper> cmdCtx, String str) throws CommandSyntaxException {
		return BASE_NMS.getLocationPrecise((CommandContext) cmdCtx, str);
	}

	@Override
	public LootTable getLootTable(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return BASE_NMS.getLootTable((CommandContext) cmdCtx, key);
	}

	@Override
	public <NBTContainer> Object getNBTCompound(CommandContext<CommandListenerWrapper> cmdCtx, String key,
			Function<Object, NBTContainer> nbtContainerConstructor) {
		return BASE_NMS.getNBTCompound((CommandContext) cmdCtx, key, nbtContainerConstructor);
	}

	@Override
	public MathOperation getMathOperation(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getMathOperation((CommandContext) cmdCtx, key);
	}

	@Override
	public String getObjective(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws IllegalArgumentException, CommandSyntaxException {
		return BASE_NMS.getObjective((CommandContext) cmdCtx, key);
	}

	@Override
	public String getObjectiveCriteria(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return BASE_NMS.getObjectiveCriteria((CommandContext) cmdCtx, key);
	}

	@Override
	public ParticleData<?> getParticle(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return BASE_NMS.getParticle((CommandContext) cmdCtx, key);
	}
	
	@Override
	public Player getPlayer(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getPlayer((CommandContext) cmdCtx, key);
	}

	@Override
	public PotionEffectType getPotionEffect(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		return BASE_NMS.getPotionEffect((CommandContext) cmdCtx, key);
	}

	@Override
	public SuggestionProvider getSuggestionProvider(SuggestionProviders provider) {
		return BASE_NMS.getSuggestionProvider(provider);
	}
	
	@Override
	public Recipe getRecipe(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getRecipe((CommandContext) cmdCtx, key);
	}

	@Override
	public Object getSound(CommandContext<CommandListenerWrapper> cmdCtx, String key, ArgumentSubType subType) {
		return BASE_NMS.getSound((CommandContext) cmdCtx, key, subType);
	}

	@Override
	public String getTeam(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getTeam((CommandContext) cmdCtx, key);
	}

	@Override
	public Message generateMessageFromJson(final String json) {
		return BASE_NMS.generateMessageFromJson(json);
	}

}
