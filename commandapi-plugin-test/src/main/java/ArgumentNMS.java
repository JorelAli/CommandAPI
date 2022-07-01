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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.loot.LootTable;
import org.bukkit.potion.PotionEffectType;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.jorel.commandapi.arguments.EntitySelector;
import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.wrappers.FunctionWrapper;
import dev.jorel.commandapi.wrappers.Location2D;
import dev.jorel.commandapi.wrappers.ParticleData;
import dev.jorel.commandapi.wrappers.SimpleFunctionWrapper;
import net.kyori.adventure.text.Component;
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
	public final ArgumentType<?> _ArgumentBlockPredicate() {
		return BASE_NMS._ArgumentBlockPredicate();
	}

	@Override
	public final ArgumentType<?> _ArgumentBlockState() {
		return BASE_NMS._ArgumentBlockState();
	}

	@Override
	public ArgumentType<?> _ArgumentEntity(EntitySelector selector) {
		return BASE_NMS._ArgumentEntity(selector);
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
	public final ArgumentType<?> _ArgumentSyntheticBiome() {
		return BASE_NMS._ArgumentSyntheticBiome();
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
	public ChatColor getChatColor(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
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
		return BASE_NMS.getEntitySelector((CommandContext) cmdCtx, key, selector);
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
	public Sound getSound(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
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

}
