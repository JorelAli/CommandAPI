package dev.jorel.commandapi.test;

import static org.mockito.ArgumentMatchers.any;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

import org.bukkit.Axis;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_20_R4.CraftParticle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.loot.LootTable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

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
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemPredicateArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;

/**
 * Argument related method implementations
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class ArgumentNMS extends MockPlatform<CommandSourceStack> {

	CommandAPIBukkit<CommandSourceStack> baseNMS;

	protected ArgumentNMS(CommandAPIBukkit<?> baseNMS) {
		this.baseNMS = (CommandAPIBukkit<CommandSourceStack>) baseNMS;
	}

	@Override
	public ArgumentType<?> _ArgumentAngle() {
		return baseNMS._ArgumentAngle();
	}

	@Override
	public ArgumentType<?> _ArgumentAxis() {
		return baseNMS._ArgumentAxis();
	}

	@Override
	public final ArgumentType<?> _ArgumentBlockPredicate() {
		CommandBuildContext buildContextMock = Mockito.mock(CommandBuildContext.class);
		Mockito
			.when(buildContextMock.lookup(any(ResourceKey.class)))
			.thenReturn(Optional.of(BuiltInRegistries.BLOCK.asLookup())); // Registry.BLOCK
		Mockito
			.when(buildContextMock.lookupOrThrow(any(ResourceKey.class)))
			.thenReturn(Optional.of(BuiltInRegistries.BLOCK.asLookup()).orElseThrow()); // Registry.BLOCK
		return BlockPredicateArgument.blockPredicate(buildContextMock);
	}

	@Override
	public final ArgumentType<?> _ArgumentBlockState() {
		CommandBuildContext buildContextMock = Mockito.mock(CommandBuildContext.class);
		Mockito
			.when(buildContextMock.lookup(any(ResourceKey.class)))
			.thenReturn(Optional.of(BuiltInRegistries.BLOCK.asLookup())); // Registry.BLOCK
		Mockito
			.when(buildContextMock.lookupOrThrow(any(ResourceKey.class)))
			.thenReturn(Optional.of(BuiltInRegistries.BLOCK.asLookup()).orElseThrow()); // Registry.BLOCK
		return BlockStateArgument.block(buildContextMock);
	}

	@Override
	public ArgumentType<?> _ArgumentChat() {
		return baseNMS._ArgumentChat();
	}

	@Override
	public ArgumentType<?> _ArgumentChatComponent() {
		return baseNMS._ArgumentChatComponent();
	}

	@Override
	public ArgumentType<?> _ArgumentChatFormat() {
		return baseNMS._ArgumentChatFormat();
	}

	@Override
	public ArgumentType<?> _ArgumentDimension() {
		return baseNMS._ArgumentDimension();
	}

	@Override
	public ArgumentType<?> _ArgumentEnchantment() {
		CommandBuildContext buildContextMock = Mockito.mock(CommandBuildContext.class);
		Mockito
			.when(buildContextMock.lookup(any(ResourceKey.class)))
			.thenReturn(Optional.of(BuiltInRegistries.ENCHANTMENT.asLookup())); // Registry.ENCHANTMENT
		Mockito
			.when(buildContextMock.lookupOrThrow(any(ResourceKey.class)))
			.thenReturn(Optional.of(BuiltInRegistries.ENCHANTMENT.asLookup()).orElseThrow()); // Registry.ENCHANTMENT
		return ResourceArgument.resource(buildContextMock, Registries.ENCHANTMENT);
	}

	@Override
	public ArgumentType<?> _ArgumentEntity(ArgumentSubType subType) {
		return baseNMS._ArgumentEntity(subType);
	}

	@Override
	public ArgumentType<?> _ArgumentEntitySummon() {
		// We can't use BASE_NMS for this, because that requires a
		// COMMAND_BUILD_CONTEXT. The COMMAND_BUILD_CONTEXT is only defined for
		// CraftServer instances, otherwise it'll return null.
		CommandBuildContext buildContextMock = Mockito.mock(CommandBuildContext.class);
		Mockito
			.when(buildContextMock.lookup(any(ResourceKey.class)))
			.thenReturn(Optional.of(BuiltInRegistries.ENTITY_TYPE.asLookup())); // Registry.ENTITY_TYPE
		Mockito
			.when(buildContextMock.lookupOrThrow(any(ResourceKey.class)))
			.thenReturn(Optional.of(BuiltInRegistries.ENTITY_TYPE.asLookup()).orElseThrow()); // Registry.ENTITY_TYPE
		return ResourceArgument.resource(buildContextMock, Registries.ENTITY_TYPE);
	}

	@Override
	public ArgumentType<?> _ArgumentFloatRange() {
		return baseNMS._ArgumentFloatRange();
	}

	@Override
	public ArgumentType<?> _ArgumentIntRange() {
		return baseNMS._ArgumentIntRange();
	}

	@Override
	public final ArgumentType<?> _ArgumentItemPredicate() {
		CommandBuildContext buildContextMock = Mockito.mock(CommandBuildContext.class);
		Mockito
			.when(buildContextMock.lookup(any(ResourceKey.class)))
			.thenReturn(Optional.of(BuiltInRegistries.ITEM.asLookup())); // Registry.ITEM
		Mockito
			.when(buildContextMock.lookupOrThrow(any(ResourceKey.class)))
			.thenReturn(Optional.of(BuiltInRegistries.ITEM.asLookup()).orElseThrow()); // Registry.ITEM
		return ItemPredicateArgument.itemPredicate(buildContextMock);
	}

	@Override
	public final ArgumentType<?> _ArgumentItemStack() {
		// We can't use BASE_NMS for this, because that requires a
		// COMMAND_BUILD_CONTEXT.
		// The COMMAND_BUILD_CONTEXT is only defined for CraftServer instances,
		// otherwise
		// it'll return null.
		CommandBuildContext buildContextMock = Mockito.mock(CommandBuildContext.class);
		Mockito
			.when(buildContextMock.lookup(any(ResourceKey.class)))
			.thenReturn(Optional.of(BuiltInRegistries.ITEM.asLookup())); // Registry.ITEM
		Mockito
			.when(buildContextMock.lookupOrThrow(any(ResourceKey.class)))
			.thenReturn(Optional.of(BuiltInRegistries.ITEM.asLookup()).orElseThrow()); // Registry.ITEM
		return ItemArgument.item(buildContextMock);
	}

	@Override
	public ArgumentType<?> _ArgumentMathOperation() {
		return baseNMS._ArgumentMathOperation();
	}

	@Override
	public ArgumentType<?> _ArgumentMinecraftKeyRegistered() {
		return baseNMS._ArgumentMinecraftKeyRegistered();
	}

	@Override
	public ArgumentType<?> _ArgumentMobEffect() {
		// We can't use BASE_NMS for this, because that requires a
		// COMMAND_BUILD_CONTEXT. The COMMAND_BUILD_CONTEXT is only defined for
		// CraftServer instances, otherwise it'll return null.
		CommandBuildContext buildContextMock = Mockito.mock(CommandBuildContext.class);
		Mockito
			.when(buildContextMock.lookup(any(ResourceKey.class)))
			.thenReturn(Optional.of(BuiltInRegistries.MOB_EFFECT.asLookup())); // Registry.MOB_EFFECT
		Mockito
			.when(buildContextMock.lookupOrThrow(any(ResourceKey.class)))
			.thenReturn(Optional.of(BuiltInRegistries.MOB_EFFECT.asLookup()).orElseThrow()); // Registry.MOB_EFFECT
		return ResourceArgument.resource(buildContextMock, Registries.MOB_EFFECT);
	}

	@Override
	public ArgumentType<?> _ArgumentNBTCompound() {
		return baseNMS._ArgumentNBTCompound();
	}

	@Override
	public ArgumentType<?> _ArgumentParticle() {
		CommandBuildContext buildContextMock = Mockito.mock(CommandBuildContext.class);
		Mockito
			.when(buildContextMock.lookup(any(ResourceKey.class)))
			.thenReturn(Optional.of(BuiltInRegistries.PARTICLE_TYPE.asLookup())); // Registry.PARTICLE_TYPE
		Mockito
			.when(buildContextMock.lookupOrThrow(any(ResourceKey.class)))
			.thenReturn(Optional.of(BuiltInRegistries.PARTICLE_TYPE.asLookup()).orElseThrow()); // Registry.PARTICLE_TYPE
		Mockito
			.when(buildContextMock.createSerializationContext(any()))
			.thenAnswer(a -> RegistryOps.create(a.getArgument(0), buildContextMock));
		return ParticleArgument.particle(buildContextMock);
	}

	@Override
	public ArgumentType<?> _ArgumentPosition() {
		return baseNMS._ArgumentPosition();
	}

	@Override
	public ArgumentType<?> _ArgumentPosition2D() {
		return baseNMS._ArgumentPosition2D();
	}

	@Override
	public ArgumentType<?> _ArgumentProfile() {
		return baseNMS._ArgumentProfile();
	}

	@Override
	public ArgumentType<?> _ArgumentRotation() {
		return baseNMS._ArgumentRotation();
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardCriteria() {
		return baseNMS._ArgumentScoreboardCriteria();
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardObjective() {
		return baseNMS._ArgumentScoreboardObjective();
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardSlot() {
		return baseNMS._ArgumentScoreboardSlot();
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardTeam() {
		return baseNMS._ArgumentScoreboardTeam();
	}

	@Override
	public ArgumentType<?> _ArgumentScoreholder(ArgumentSubType subType) {
		return baseNMS._ArgumentScoreholder(subType);
	}

	@Override
	public final ArgumentType<?> _ArgumentSyntheticBiome() {
		// Unpack Biomes.class
//		Set<ResourceKey<Biome>> biomes = new HashSet<>();
//		for(Field f : Biomes.class.getDeclaredFields()) {
//			try {
//				biomes.add((ResourceKey<Biome>) f.get(null));
//			} catch (IllegalArgumentException | IllegalAccessException e) {
//				e.printStackTrace();
//			}
//		}
		
		CommandBuildContext buildContextMock = Mockito.mock(CommandBuildContext.class);
		Mockito
			.when(buildContextMock.lookup(any(ResourceKey.class)))
			.thenAnswer(invocation -> {
				HolderLookup hl = Mockito.mock(HolderLookup.class);
				Mockito.when(hl.get((ResourceKey)any())).thenAnswer(i -> {
					ResourceKey rk = i.getArgument(0, ResourceKey.class);
//					if(biomes.contains(rk)) {
//						return Optional.of(Holder.Reference.createStandAlone(new HolderOwner() { }, rk));
//					} else {
//						return Optional.empty();
//					}
					// We'll return the thing anyway. Bukkit will handle if the thing exists or not...
					return Optional.of(Holder.Reference.createStandAlone(new HolderOwner() { }, rk));
				});
				return hl;
			});
		return ResourceArgument.resource(buildContextMock, Registries.BIOME);
	}

	@Override
	public ArgumentType<?> _ArgumentTag() {
		return baseNMS._ArgumentTag();
	}

	@Override
	public ArgumentType<?> _ArgumentTime() {
		return baseNMS._ArgumentTime();
	}

	@Override
	public ArgumentType<?> _ArgumentUUID() {
		return baseNMS._ArgumentUUID();
	}

	@Override
	public ArgumentType<?> _ArgumentVec2(boolean centerPosition) {
		return baseNMS._ArgumentVec2(centerPosition);
	}

	@Override
	public ArgumentType<?> _ArgumentVec3(boolean centerPosition) {
		return baseNMS._ArgumentVec3(centerPosition);
	}

	@Override
	public Message generateMessageFromJson(final String json) {
		return baseNMS.generateMessageFromJson(json);
	}

	@Override
	public org.bukkit.advancement.Advancement getAdvancement(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return baseNMS.getAdvancement(cmdCtx, key);
	}

	@Override
	public Component getAdventureChat(CommandContext cmdCtx, String key)
		throws CommandSyntaxException {
		return baseNMS.getAdventureChat(cmdCtx, key);
	}

	@Override
	public NamedTextColor getAdventureChatColor(CommandContext cmdCtx, String key) {
		return baseNMS.getAdventureChatColor(cmdCtx, key);
	}

	@Override
	public Component getAdventureChatComponent(CommandContext cmdCtx, String key) {
		return baseNMS.getAdventureChatComponent(cmdCtx, key);
	}

	@Override
	public float getAngle(CommandContext cmdCtx, String key) {
		return baseNMS.getAngle(cmdCtx, key);
	}

	@Override
	public EnumSet<Axis> getAxis(CommandContext cmdCtx, String key) {
		return baseNMS.getAxis(cmdCtx, key);
	}

	@Override
	public Object getBiome(CommandContext cmdCtx, String key, ArgumentSubType subType) throws CommandSyntaxException {
		return baseNMS.getBiome(cmdCtx, key, subType);
	}

	@Override
	public Predicate<Block> getBlockPredicate(CommandContext cmdCtx, String key)
		throws CommandSyntaxException {
		return baseNMS.getBlockPredicate(cmdCtx, key);
	}

	@Override
	public BlockData getBlockState(CommandContext cmdCtx, String key) {
		return baseNMS.getBlockState(cmdCtx, key);
	}

	@Override
	public BaseComponent[] getChat(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return baseNMS.getChat(cmdCtx, key);
	}

	@Override
	public ChatColor getChatColor(CommandContext cmdCtx, String key) {
		return baseNMS.getChatColor(cmdCtx, key);
	}

	@Override
	public BaseComponent[] getChatComponent(CommandContext cmdCtx, String key) {
		return baseNMS.getChatComponent(cmdCtx, key);
	}

	@Override
	public World getDimension(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return baseNMS.getDimension(cmdCtx, key);
	}

	@Override
	public Enchantment getEnchantment(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return baseNMS.getEnchantment(cmdCtx, key);
	}

	@Override
	public Object getEntitySelector(CommandContext cmdCtx, String key, ArgumentSubType subType, boolean allowEmpty) throws CommandSyntaxException {
		return baseNMS.getEntitySelector(cmdCtx, key, subType, allowEmpty);
	}

	@Override
	public EntityType getEntityType(CommandContext cmdCtx, String key)
		throws CommandSyntaxException {
		return baseNMS.getEntityType(cmdCtx, key);
	}

	@Override
	public FloatRange getFloatRange(CommandContext cmdCtx, String key) {
		return baseNMS.getFloatRange(cmdCtx, key);
	}

	@Override
	public FunctionWrapper[] getFunction(CommandContext cmdCtx, String key)
		throws CommandSyntaxException {
		return baseNMS.getFunction(cmdCtx, key);
	}

	@Override
	public SimpleFunctionWrapper getFunction(NamespacedKey key) {
		return baseNMS.getFunction(key);
	}

	@Override
	public Set<NamespacedKey> getFunctions() {
		return baseNMS.getFunctions();
	}

	@Override
	public IntegerRange getIntRange(CommandContext cmdCtx, String key) {
		return baseNMS.getIntRange(cmdCtx, key);
	}

	@Override
	public ItemStack getItemStack(CommandContext cmdCtx, String key)
		throws CommandSyntaxException {
		return baseNMS.getItemStack(cmdCtx, key);
	}

	@Override
	public Predicate<ItemStack> getItemStackPredicate(CommandContext cmdCtx, String key)
		throws CommandSyntaxException {
		return baseNMS.getItemStackPredicate(cmdCtx, key);
	}

	@Override
	public Location2D getLocation2DBlock(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return baseNMS.getLocation2DBlock(cmdCtx, key);
	}

	@Override
	public Location2D getLocation2DPrecise(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return baseNMS.getLocation2DPrecise(cmdCtx, key);
	}

	@Override
	public Location getLocationBlock(CommandContext cmdCtx, String str) throws CommandSyntaxException {
		return baseNMS.getLocationBlock(cmdCtx, str);
	}

	@Override
	public final Location getLocationPrecise(CommandContext cmdCtx, String str) throws CommandSyntaxException {
		return baseNMS.getLocationPrecise(cmdCtx, str);
	}

	@Override
	public LootTable getLootTable(CommandContext cmdCtx, String key) {
		return baseNMS.getLootTable(cmdCtx, key);
	}

	@Override
	public MathOperation getMathOperation(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return baseNMS.getMathOperation(cmdCtx, key);
	}

	@Override
	public NamespacedKey getMinecraftKey(CommandContext cmdCtx, String key) {
		return baseNMS.getMinecraftKey(cmdCtx, key);
	}

	@Override
	public <NBTContainer> Object getNBTCompound(CommandContext<CommandSourceStack> cmdCtx, String key,
		Function<Object, NBTContainer> nbtContainerConstructor) {
		return baseNMS.getNBTCompound((CommandContext) cmdCtx, key, nbtContainerConstructor);
	}

	@Override
	public Objective getObjective(CommandContext cmdCtx, String key)
		throws IllegalArgumentException, CommandSyntaxException {
		return baseNMS.getObjective(cmdCtx, key);
	}

	@Override
	public String getObjectiveCriteria(CommandContext cmdCtx, String key) {
		return baseNMS.getObjectiveCriteria(cmdCtx, key);
	}

	@Override
	public OfflinePlayer getOfflinePlayer(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return baseNMS.getOfflinePlayer(cmdCtx, key);
	}

	@Override
	public ParticleData<?> getParticle(CommandContext cmdCtx, String key) {
		final ParticleData<?> result;
		try (MockedStatic<CraftParticle> craftParticle = Mockito.mockStatic(CraftParticle.class)) {
			craftParticle.when(() -> CraftParticle.minecraftToBukkit(any()))
			.thenAnswer(args -> {
				ParticleType minecraft = args.getArgument(0, ParticleType.class);
				
				
				Registry<ParticleType<?>> registry = BuiltInRegistries.PARTICLE_TYPE; //CraftRegistry.getMinecraftRegistry(Registries.PARTICLE_TYPE);
//				org.bukkit.Registry.PARTICLE_TYPE
//				// new SimpleRegistry(Particle.class, (par) -> par.register);
//				
				// We can't actually implement this until we have MockBukkit
				// linked against 1.20.5, which is super annoying...
				
				// Do we wait for MockBukkit to update its paper dependencies, 
				// or do we just fork MockBukkit at this point?
//				
//				Particle bukkit = (Particle) org.bukkit.Registry.PARTICLE_TYPE.get(CraftNamespacedKey
//						.fromMinecraft(((ResourceKey) registry.getResourceKey(minecraft).orElseThrow()).location()));
				
				
				
				return Particle.CRIT;
				
				// return bukkit;
			});
			
			Map<String, Particle> map = new HashMap<>();
//			map.put("poof", Particle.POOF);
//			map.put("explosion", Particle.EXPLOSION);
//			map.put("explosion_emitter", Particle.EXPLOSION_EMITTER);
//			map.put("firework", Particle.FIREWORK);
//			map.put("bubble", Particle.BUBBLE);
//			map.put("splash", Particle.SPLASH);
//			map.put("fishing", Particle.FISHING);
//			map.put("underwater", Particle.UNDERWATER);
//			map.put("crit", Particle.CRIT);
//			map.put("enchanted_hit", Particle.ENCHANTED_HIT);
//			map.put("smoke", Particle.SMOKE);
//			map.put("large_smoke", Particle.LARGE_SMOKE);
//			map.put("effect", Particle.EFFECT);
//			map.put("instant_effect", Particle.INSTANT_EFFECT);
//			map.put("entity_effect", Particle.ENTITY_EFFECT);
//			map.put("witch", Particle.WITCH);
//			map.put("dripping_water", Particle.DRIPPING_WATER);
//			map.put("dripping_lava", Particle.DRIPPING_LAVA);
//			map.put("angry_villager", Particle.ANGRY_VILLAGER);
//			map.put("happy_villager", Particle.HAPPY_VILLAGER);
//			map.put("mycelium", Particle.MYCELIUM);
//			map.put("note", Particle.NOTE);
//			map.put("portal", Particle.PORTAL);
//			map.put("enchant", Particle.ENCHANT);
//			map.put("flame", Particle.FLAME);
//			map.put("lava", Particle.LAVA);
//			map.put("cloud", Particle.CLOUD);
//			map.put("dust", Particle.DUST);
//			map.put("item_snowball", Particle.ITEM_SNOWBALL);
//			map.put("item_slime", Particle.ITEM_SLIME);
//			map.put("heart", Particle.HEART);
//			map.put("item", Particle.ITEM);
//			map.put("block", Particle.BLOCK);
//			map.put("rain", Particle.RAIN);
//			map.put("elder_guardian", Particle.ELDER_GUARDIAN);
//			map.put("dragon_breath", Particle.DRAGON_BREATH);
//			map.put("end_rod", Particle.END_ROD);
//			map.put("damage_indicator", Particle.DAMAGE_INDICATOR);
//			map.put("sweep_attack", Particle.SWEEP_ATTACK);
//			map.put("falling_dust", Particle.FALLING_DUST);
//			map.put("totem_of_undying", Particle.TOTEM_OF_UNDYING);
//			map.put("spit", Particle.SPIT);
//			map.put("squid_ink", Particle.SQUID_INK);
//			map.put("bubble_pop", Particle.BUBBLE_POP);
//			map.put("current_down", Particle.CURRENT_DOWN);
//			map.put("bubble_column_up", Particle.BUBBLE_COLUMN_UP);
//			map.put("nautilus", Particle.NAUTILUS);
//			map.put("dolphin", Particle.DOLPHIN);
//			map.put("sneeze", Particle.SNEEZE);
//			map.put("campfire_cosy_smoke", Particle.CAMPFIRE_COSY_SMOKE);
//			map.put("campfire_signal_smoke", Particle.CAMPFIRE_SIGNAL_SMOKE);
//			map.put("composter", Particle.COMPOSTER);
//			map.put("flash", Particle.FLASH);
//			map.put("falling_lava", Particle.FALLING_LAVA);
//			map.put("landing_lava", Particle.LANDING_LAVA);
//			map.put("falling_water", Particle.FALLING_WATER);
//			map.put("dripping_honey", Particle.DRIPPING_HONEY);
//			map.put("falling_honey", Particle.FALLING_HONEY);
//			map.put("landing_honey", Particle.LANDING_HONEY);
//			map.put("falling_nectar", Particle.FALLING_NECTAR);
//			map.put("soul_fire_flame", Particle.SOUL_FIRE_FLAME);
//			map.put("ash", Particle.ASH);
//			map.put("crimson_spore", Particle.CRIMSON_SPORE);
//			map.put("warped_spore", Particle.WARPED_SPORE);
//			map.put("soul", Particle.SOUL);
//			map.put("dripping_obsidian_tear", Particle.DRIPPING_OBSIDIAN_TEAR);
//			map.put("falling_obsidian_tear", Particle.FALLING_OBSIDIAN_TEAR);
//			map.put("landing_obsidian_tear", Particle.LANDING_OBSIDIAN_TEAR);
//			map.put("reverse_portal", Particle.REVERSE_PORTAL);
//			map.put("white_ash", Particle.WHITE_ASH);
//			map.put("dust_color_transition", Particle.DUST_COLOR_TRANSITION);
//			map.put("vibration", Particle.VIBRATION);
//			map.put("falling_spore_blossom", Particle.FALLING_SPORE_BLOSSOM);
//			map.put("spore_blossom_air", Particle.SPORE_BLOSSOM_AIR);
//			map.put("small_flame", Particle.SMALL_FLAME);
//			map.put("snowflake", Particle.SNOWFLAKE);
//			map.put("dripping_dripstone_lava", Particle.DRIPPING_DRIPSTONE_LAVA);
//			map.put("falling_dripstone_lava", Particle.FALLING_DRIPSTONE_LAVA);
//			map.put("dripping_dripstone_water", Particle.DRIPPING_DRIPSTONE_WATER);
//			map.put("falling_dripstone_water", Particle.FALLING_DRIPSTONE_WATER);
//			map.put("glow_squid_ink", Particle.GLOW_SQUID_INK);
//			map.put("glow", Particle.GLOW);
//			map.put("wax_on", Particle.WAX_ON);
//			map.put("wax_off", Particle.WAX_OFF);
//			map.put("electric_spark", Particle.ELECTRIC_SPARK);
//			map.put("scrape", Particle.SCRAPE);
//			map.put("sonic_boom", Particle.SONIC_BOOM);
//			map.put("sculk_soul", Particle.SCULK_SOUL);
//			map.put("sculk_charge", Particle.SCULK_CHARGE);
//			map.put("sculk_charge_pop", Particle.SCULK_CHARGE_POP);
//			map.put("shriek", Particle.SHRIEK);
//			map.put("cherry_leaves", Particle.CHERRY_LEAVES);
//			map.put("egg_crack", Particle.EGG_CRACK);
//			map.put("dust_plume", Particle.DUST_PLUME);
//			map.put("white_smoke", Particle.WHITE_SMOKE);
//			map.put("gust", Particle.GUST);
//			map.put("small_gust", Particle.SMALL_GUST);
//			map.put("gust_emitter_large", Particle.GUST_EMITTER_LARGE);
//			map.put("gust_emitter_small", Particle.GUST_EMITTER_SMALL);
//			map.put("trial_spawner_detection", Particle.TRIAL_SPAWNER_DETECTION);
//			map.put("trial_spawner_detection_ominous", Particle.TRIAL_SPAWNER_DETECTION_OMINOUS);
//			map.put("vault_connection", Particle.VAULT_CONNECTION);
//			map.put("infested", Particle.INFESTED);
//			map.put("item_cobweb", Particle.ITEM_COBWEB);
//			map.put("dust_pillar", Particle.DUST_PILLAR);
//			map.put("ominous_spawning", Particle.OMINOUS_SPAWNING);
//			map.put("raid_omen", Particle.RAID_OMEN);
//			map.put("trial_omen", Particle.TRIAL_OMEN);
//			map.put("block_marker", Particle.BLOCK_MARKER);
			
			map.put("poof", Particle.valueOf("EXPLOSION_NORMAL"));
			map.put("explosion", Particle.valueOf("EXPLOSION_LARGE"));
			map.put("explosion_emitter", Particle.valueOf("EXPLOSION_HUGE"));
			map.put("firework", Particle.valueOf("FIREWORKS_SPARK"));
			map.put("bubble", Particle.valueOf("WATER_BUBBLE"));
			map.put("splash", Particle.valueOf("WATER_SPLASH"));
			map.put("fishing", Particle.valueOf("WATER_WAKE"));
			map.put("underwater", Particle.valueOf("SUSPENDED"));
			// map.put("???", Particle.valueOf("SUSPENDED_DEPTH"));
			map.put("crit", Particle.valueOf("CRIT"));
			map.put("enchanted_hit", Particle.valueOf("CRIT_MAGIC"));
			map.put("smoke", Particle.valueOf("SMOKE_NORMAL"));
			map.put("large_smoke", Particle.valueOf("SMOKE_LARGE"));
			map.put("effect", Particle.valueOf("SPELL"));
			map.put("instant_effect", Particle.valueOf("SPELL_INSTANT"));
			map.put("entity_effect", Particle.valueOf("SPELL_MOB"));
			// map.put("???", Particle.valueOf("SPELL_MOB_AMBIENT"));
			map.put("witch", Particle.valueOf("SPELL_WITCH"));
			map.put("dripping_water", Particle.valueOf("DRIP_WATER"));
			map.put("dripping_lava", Particle.valueOf("DRIP_LAVA"));
			map.put("angry_villager", Particle.valueOf("VILLAGER_ANGRY"));
			map.put("happy_villager", Particle.valueOf("VILLAGER_HAPPY"));
			map.put("mycelium", Particle.valueOf("TOWN_AURA"));
			map.put("note", Particle.valueOf("NOTE"));
			map.put("portal", Particle.valueOf("PORTAL"));
			map.put("enchant", Particle.valueOf("ENCHANTMENT_TABLE"));
			map.put("flame", Particle.valueOf("FLAME"));
			map.put("lava", Particle.valueOf("LAVA"));
			map.put("cloud", Particle.valueOf("CLOUD"));
			map.put("dust", Particle.valueOf("REDSTONE"));
			map.put("item_snowball", Particle.valueOf("SNOWBALL"));
			map.put("item_slime", Particle.valueOf("SNOW_SHOVEL"));
			// map.put("???", Particle.valueOf("SLIME"));
			map.put("heart", Particle.valueOf("HEART"));
			map.put("item", Particle.valueOf("ITEM_CRACK"));
			map.put("block", Particle.valueOf("BLOCK_CRACK"));
			map.put("rain", Particle.valueOf("BLOCK_DUST"));
			// map.put("???", Particle.valueOf("WATER_DROP"));
			map.put("elder_guardian", Particle.valueOf("MOB_APPEARANCE"));
			map.put("dragon_breath", Particle.valueOf("DRAGON_BREATH"));
			map.put("end_rod", Particle.valueOf("END_ROD"));
			map.put("damage_indicator", Particle.valueOf("DAMAGE_INDICATOR"));
			map.put("sweep_attack", Particle.valueOf("SWEEP_ATTACK"));
			map.put("falling_dust", Particle.valueOf("FALLING_DUST"));
			map.put("totem_of_undying", Particle.valueOf("TOTEM"));
			map.put("spit", Particle.valueOf("SPIT"));
			map.put("squid_ink", Particle.valueOf("SQUID_INK"));
			map.put("bubble_pop", Particle.valueOf("BUBBLE_POP"));
			map.put("current_down", Particle.valueOf("CURRENT_DOWN"));
			map.put("bubble_column_up", Particle.valueOf("BUBBLE_COLUMN_UP"));
			map.put("nautilus", Particle.valueOf("NAUTILUS"));
			map.put("dolphin", Particle.valueOf("DOLPHIN"));
			map.put("sneeze", Particle.valueOf("SNEEZE"));
			map.put("campfire_cosy_smoke", Particle.valueOf("CAMPFIRE_COSY_SMOKE"));
			map.put("campfire_signal_smoke", Particle.valueOf("CAMPFIRE_SIGNAL_SMOKE"));
			map.put("composter", Particle.valueOf("COMPOSTER"));
			map.put("flash", Particle.valueOf("FLASH"));
			map.put("falling_lava", Particle.valueOf("FALLING_LAVA"));
			map.put("landing_lava", Particle.valueOf("LANDING_LAVA"));
			map.put("falling_water", Particle.valueOf("FALLING_WATER"));
			map.put("dripping_honey", Particle.valueOf("DRIPPING_HONEY"));
			map.put("falling_honey", Particle.valueOf("FALLING_HONEY"));
			map.put("landing_honey", Particle.valueOf("LANDING_HONEY"));
			map.put("falling_nectar", Particle.valueOf("FALLING_NECTAR"));
			map.put("soul_fire_flame", Particle.valueOf("SOUL_FIRE_FLAME"));
			map.put("ash", Particle.valueOf("ASH"));
			map.put("crimson_spore", Particle.valueOf("CRIMSON_SPORE"));
			map.put("warped_spore", Particle.valueOf("WARPED_SPORE"));
			map.put("soul", Particle.valueOf("SOUL"));
			map.put("dripping_obsidian_tear", Particle.valueOf("DRIPPING_OBSIDIAN_TEAR"));
			map.put("falling_obsidian_tear", Particle.valueOf("FALLING_OBSIDIAN_TEAR"));
			map.put("landing_obsidian_tear", Particle.valueOf("LANDING_OBSIDIAN_TEAR"));
			map.put("reverse_portal", Particle.valueOf("REVERSE_PORTAL"));
			map.put("white_ash", Particle.valueOf("WHITE_ASH"));
			map.put("dust_color_transition", Particle.valueOf("DUST_COLOR_TRANSITION"));
			map.put("vibration", Particle.valueOf("VIBRATION"));
			map.put("falling_spore_blossom", Particle.valueOf("FALLING_SPORE_BLOSSOM"));
			map.put("spore_blossom_air", Particle.valueOf("SPORE_BLOSSOM_AIR"));
			map.put("small_flame", Particle.valueOf("SMALL_FLAME"));
			map.put("snowflake", Particle.valueOf("SNOWFLAKE"));
			map.put("dripping_dripstone_lava", Particle.valueOf("DRIPPING_DRIPSTONE_LAVA"));
			map.put("falling_dripstone_lava", Particle.valueOf("FALLING_DRIPSTONE_LAVA"));
			map.put("dripping_dripstone_water", Particle.valueOf("DRIPPING_DRIPSTONE_WATER"));
			map.put("falling_dripstone_water", Particle.valueOf("FALLING_DRIPSTONE_WATER"));
			map.put("glow_squid_ink", Particle.valueOf("GLOW_SQUID_INK"));
			map.put("glow", Particle.valueOf("GLOW"));
			map.put("wax_on", Particle.valueOf("WAX_ON"));
			map.put("wax_off", Particle.valueOf("WAX_OFF"));
			map.put("electric_spark", Particle.valueOf("ELECTRIC_SPARK"));
			map.put("scrape", Particle.valueOf("SCRAPE"));
			map.put("sonic_boom", Particle.valueOf("SONIC_BOOM"));
			map.put("sculk_soul", Particle.valueOf("SCULK_SOUL"));
			map.put("sculk_charge", Particle.valueOf("SCULK_CHARGE"));
			map.put("sculk_charge_pop", Particle.valueOf("SCULK_CHARGE_POP"));
			map.put("shriek", Particle.valueOf("SHRIEK"));
			map.put("cherry_leaves", Particle.valueOf("CHERRY_LEAVES"));
			map.put("egg_crack", Particle.valueOf("EGG_CRACK"));
			map.put("dust_plume", Particle.valueOf("BLOCK_MARKER"));
			
			Registry<ParticleType<?>> registry = BuiltInRegistries.PARTICLE_TYPE; //CraftRegistry.getMinecraftRegistry(Registries.PARTICLE_TYPE);
			
			final ParticleOptions particleOptions = ParticleArgument.getParticle(cmdCtx, key);
			String nmsKey = registry.getResourceKey(particleOptions.getType()).get().location().getPath();
			result = new ParticleData(map.get(nmsKey), baseNMS.getParticle(cmdCtx, key).data());
		}

		return result;
	}

	@Override
	public Player getPlayer(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return baseNMS.getPlayer(cmdCtx, key);
	}

	@Override
	public Object getPotionEffect(CommandContext cmdCtx, String key, ArgumentSubType subType)
		throws CommandSyntaxException {
		return baseNMS.getPotionEffect(cmdCtx, key, subType);
	}

	@Override
	public Recipe getRecipe(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return baseNMS.getRecipe(cmdCtx, key);
	}

	@Override
	public Rotation getRotation(CommandContext cmdCtx, String key) {
		return baseNMS.getRotation(cmdCtx, key);
	}

	@Override
	public ScoreboardSlot getScoreboardSlot(CommandContext cmdCtx, String key) {
		return baseNMS.getScoreboardSlot(cmdCtx, key);
	}

	@Override
	public Collection<String> getScoreHolderMultiple(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return baseNMS.getScoreHolderMultiple(cmdCtx, key);
	}

	@Override
	public String getScoreHolderSingle(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return baseNMS.getScoreHolderSingle(cmdCtx, key);
	}

	@Override
	public Object getSound(CommandContext cmdCtx, String key, ArgumentSubType subType) {
		return baseNMS.getSound(cmdCtx, key, subType);
	}

	@Override
	public SuggestionProvider getSuggestionProvider(SuggestionProviders provider) {
		if(provider == SuggestionProviders.BIOMES) {
			return _ArgumentSyntheticBiome()::listSuggestions;
		} else {
			return baseNMS.getSuggestionProvider(provider);
		}
	}

	@Override
	public SimpleFunctionWrapper[] getTag(NamespacedKey key) {
		return baseNMS.getTag(key);
	}

	@Override
	public Set<NamespacedKey> getTags() {
		return baseNMS.getTags();
	}

	@Override
	public Team getTeam(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return baseNMS.getTeam(cmdCtx, key);
	}

	@Override
	public int getTime(CommandContext cmdCtx, String key) {
		return baseNMS.getTime(cmdCtx, key);
	}

	@Override
	public UUID getUUID(CommandContext cmdCtx, String key) {
		return baseNMS.getUUID(cmdCtx, key);
	}

}
