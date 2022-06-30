import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

import org.bukkit.Axis;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.loot.LootTable;
import org.bukkit.potion.PotionEffectType;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import dev.jorel.commandapi.arguments.EntitySelector;
import dev.jorel.commandapi.arguments.SuggestionProviders;
import dev.jorel.commandapi.nms.NMS;
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
import net.minecraft.commands.CommandListenerWrapper;

public class CustomNMS implements NMS<CommandListenerWrapper> {

	@Override
	public ArgumentType<?> _ArgumentAngle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentAxis() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentBlockPredicate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentBlockState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentChat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentChatComponent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentChatFormat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentDimension() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentEnchantment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentEntity(EntitySelector selector) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentEntitySummon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentFloatRange() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentIntRange() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentItemPredicate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentItemStack() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentMathOperation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentMinecraftKeyRegistered() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentMobEffect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentNBTCompound() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentParticle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentPosition2D() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentProfile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentRotation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardCriteria() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardObjective() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardSlot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardTeam() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentScoreholder(boolean single) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentTag() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentUUID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentVec2() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentVec3() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentSyntheticBiome() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] compatibleVersions() {
		return new String[] { "1.19" };
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
	public String convert(PotionEffectType potion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String convert(Sound sound) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createDispatcherFile(File file, CommandDispatcher<CommandListenerWrapper> dispatcher)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Advancement getAdvancement(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Component getAdventureChat(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Component getAdventureChatComponent(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getAngle(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public EnumSet<Axis> getAxis(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Biome getBiome(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Predicate<Block> getBlockPredicate(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlockData getBlockState(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}
	
	CommandDispatcher<CommandListenerWrapper> dispatcher;

	@Override
	public CommandDispatcher<CommandListenerWrapper> getBrigadierDispatcher() {
		if(this.dispatcher == null) {
			this.dispatcher = new CommandDispatcher<>();
		}
		return this.dispatcher;
	}

	@Override
	public BaseComponent[] getChat(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChatColor getChatColor(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseComponent[] getChatComponent(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommandListenerWrapper getCLWFromCommandSender(CommandSender sender) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommandSender getCommandSenderFromCSS(CommandListenerWrapper clw) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Environment getDimension(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enchantment getEnchantment(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getEntitySelector(CommandContext<CommandListenerWrapper> cmdCtx, String key, EntitySelector selector)
			throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityType getEntityType(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FloatRange getFloatRange(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FunctionWrapper[] getFunction(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SimpleFunctionWrapper getFunction(NamespacedKey key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<NamespacedKey> getFunctions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegerRange getIntRange(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getItemStack(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Predicate<ItemStack> getItemStackPredicate(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location2D getLocation2DBlock(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location2D getLocation2DPrecise(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location getLocationBlock(CommandContext<CommandListenerWrapper> cmdCtx, String str)
			throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location getLocationPrecise(CommandContext<CommandListenerWrapper> cmdCtx, String str)
			throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LootTable getLootTable(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MathOperation getMathOperation(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NamespacedKey getMinecraftKey(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <NBTContainer> Object getNBTCompound(CommandContext<CommandListenerWrapper> cmdCtx, String key,
			Function<Object, NBTContainer> nbtContainerConstructor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getObjective(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws IllegalArgumentException, CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getObjectiveCriteria(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParticleData<?> getParticle(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Player getPlayer(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OfflinePlayer getOfflinePlayer(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PotionEffectType getPotionEffect(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Recipe getRecipe(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rotation getRotation(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScoreboardSlot getScoreboardSlot(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getScoreHolderMultiple(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScoreHolderSingle(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommandSender getSenderForCommand(CommandContext<CommandListenerWrapper> cmdCtx, boolean forceNative) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SimpleCommandMap getSimpleCommandMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sound getSound(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SuggestionProvider<CommandListenerWrapper> getSuggestionProvider(SuggestionProviders provider) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SimpleFunctionWrapper[] getTag(NamespacedKey key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<NamespacedKey> getTags() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTeam(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTime(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UUID getUUID(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public World getWorldForCSS(CommandListenerWrapper clw) {
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

	@Override
	public HelpTopic generateHelpTopic(String commandName, String shortDescription, String fullDescription,
			String permission) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addToHelpMap(Map<String, HelpTopic> helpTopicsToAdd) {
		// TODO Auto-generated method stub

	}

}
