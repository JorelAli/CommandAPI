import java.util.function.Function;
import java.util.function.Predicate;

import org.bukkit.ChatColor;
import org.bukkit.Location;
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
	public ArgumentType<?> _ArgumentChat() {
		// TODO Auto-generated method stub
		return super._ArgumentChat();
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
		return BASE_NMS.getAdventureChatComponent((CommandContext) cmdCtx, key);
	}

	@Override
	public Biome getBiome(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getBiome((CommandContext) cmdCtx, key);
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
	public Enchantment getEnchantment(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return BASE_NMS.getEnchantment((CommandContext) cmdCtx, key);
	}

	@Override
	public Object getEntitySelector(CommandContext<CommandListenerWrapper> cmdCtx, String key, EntitySelector selector)
			throws CommandSyntaxException {
		return BASE_NMS.getEntitySelector((CommandContext) cmdCtx, key, selector);
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
		return BASE_NMS.getParticle((CommandContext) cmdCtx, key);
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
	public PotionEffectType getPotionEffect(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException {
		return BASE_NMS.getPotionEffect((CommandContext) cmdCtx, key);
	}

	@Override
	public Recipe getRecipe(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getRecipe((CommandContext) cmdCtx, key);
	}

	@Override
	public Sound getSound(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return BASE_NMS.getSound((CommandContext) cmdCtx, key);
	}

	@Override
	public String getTeam(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return BASE_NMS.getTeam((CommandContext) cmdCtx, key);
	}

}
