import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Biome;
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
import net.md_5.bungee.api.chat.BaseComponent;

public class CustomNMS implements NMS {

	@Override
	public ArgumentType _ArgumentAngle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentAxis() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentBlockPredicate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentBlockState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentChat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentChatComponent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentChatFormat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentDimension() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentEnchantment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentEntity(EntitySelector selector) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentEntitySummon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentFloatRange() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentIntRange() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentItemPredicate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentItemStack() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentMathOperation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentMinecraftKeyRegistered() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentMobEffect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentNBTCompound() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentParticle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentPosition2D() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentProfile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentRotation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentScoreboardCriteria() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentScoreboardObjective() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentScoreboardSlot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentScoreboardTeam() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentScoreholder(boolean single) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentTag() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentUUID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentVec2() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentVec3() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType _ArgumentSyntheticBiome() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] compatibleVersions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String convert(ItemStack is) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String convert(ParticleData particle) {
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
	public void createDispatcherFile(File file, CommandDispatcher dispatcher) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Advancement getAdvancement(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Component getAdventureChat(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Component getAdventureChatComponent(CommandContext cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getAngle(CommandContext cmdCtx, String key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public EnumSet getAxis(CommandContext cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Biome getBiome(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Predicate getBlockPredicate(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlockData getBlockState(CommandContext cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommandDispatcher getBrigadierDispatcher() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseComponent[] getChat(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChatColor getChatColor(CommandContext cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseComponent[] getChatComponent(CommandContext cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getCLWFromCommandSender(CommandSender sender) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommandSender getCommandSenderFromCSS(Object clw) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Environment getDimension(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enchantment getEnchantment(CommandContext cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getEntitySelector(CommandContext cmdCtx, String key, EntitySelector selector)
			throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityType getEntityType(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FloatRange getFloatRange(CommandContext cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FunctionWrapper[] getFunction(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SimpleFunctionWrapper getFunction(NamespacedKey key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set getFunctions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegerRange getIntRange(CommandContext cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getItemStack(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Predicate getItemStackPredicate(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location2D getLocation2DBlock(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location2D getLocation2DPrecise(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location getLocationBlock(CommandContext cmdCtx, String str) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location getLocationPrecise(CommandContext cmdCtx, String str) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LootTable getLootTable(CommandContext cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MathOperation getMathOperation(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NamespacedKey getMinecraftKey(CommandContext cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getNBTCompound(CommandContext cmdCtx, String key, Function nbtContainerConstructor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getObjective(CommandContext cmdCtx, String key)
			throws IllegalArgumentException, CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getObjectiveCriteria(CommandContext cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParticleData getParticle(CommandContext cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Player getPlayer(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OfflinePlayer getOfflinePlayer(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PotionEffectType getPotionEffect(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Recipe getRecipe(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rotation getRotation(CommandContext cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScoreboardSlot getScoreboardSlot(CommandContext cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection getScoreHolderMultiple(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScoreHolderSingle(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommandSender getSenderForCommand(CommandContext cmdCtx, boolean forceNative) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SimpleCommandMap getSimpleCommandMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sound getSound(CommandContext cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SuggestionProvider getSuggestionProvider(SuggestionProviders provider) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SimpleFunctionWrapper[] getTag(NamespacedKey key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set getTags() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTeam(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTime(CommandContext cmdCtx, String key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UUID getUUID(CommandContext cmdCtx, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public World getWorldForCSS(Object clw) {
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
	public void addToHelpMap(Map helpTopicsToAdd) {
		// TODO Auto-generated method stub
		
	}

}
