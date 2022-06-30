import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
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
import dev.jorel.commandapi.wrappers.FunctionWrapper;
import dev.jorel.commandapi.wrappers.ParticleData;
import dev.jorel.commandapi.wrappers.SimpleFunctionWrapper;
import net.kyori.adventure.text.Component;
import net.minecraft.commands.CommandListenerWrapper;

@SuppressWarnings("unchecked")
public abstract class ArgumentNMS extends BlankNMS {
	
	@Override
	public ArgumentType<?> _ArgumentBlockPredicate() {
		return BASE_NMS._ArgumentBlockPredicate();
	}

	@Override
	public ArgumentType<?> _ArgumentBlockState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgumentType<?> _ArgumentEntity(EntitySelector selector) {
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
	public ArgumentType<?> _ArgumentSyntheticBiome() {
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
	public String convert(ParticleData<?> particle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createDispatcherFile(File file, CommandDispatcher<CommandListenerWrapper> dispatcher)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component getAdventureChat(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		return BASE_NMS.getAdventureChat((CommandContext) cmdCtx, key);
	}

	@Override
	public Component getAdventureChatComponent(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
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

	@Override
	public CommandDispatcher<CommandListenerWrapper> getBrigadierDispatcher() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChatColor getChatColor(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
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
	public LootTable getLootTable(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
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

	@Override
	public Location getLocationPrecise(CommandContext<CommandListenerWrapper> cmdCtx, String str) throws CommandSyntaxException {
		return BASE_NMS.getLocationPrecise((CommandContext) cmdCtx, str);
	}
	
}
