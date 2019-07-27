package io.github.jorelali.commandapi.api.nms;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.ToIntBiFunction;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_13_R1.CraftLootTable;
import org.bukkit.craftbukkit.v1_13_R1.CraftParticle;
import org.bukkit.craftbukkit.v1_13_R1.CraftServer;
import org.bukkit.craftbukkit.v1_13_R1.CraftSound;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R1.command.ProxiedNativeCommandSender;
import org.bukkit.craftbukkit.v1_13_R1.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.v1_13_R1.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_13_R1.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.v1_13_R1.util.CraftChatMessage;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.loot.LootTable;
import org.bukkit.potion.PotionEffectType;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;

import io.github.jorelali.commandapi.api.CommandAPIHandler;
import io.github.jorelali.commandapi.api.FunctionWrapper;
import io.github.jorelali.commandapi.api.arguments.CustomProvidedArgument.SuggestionProviders;
import io.github.jorelali.commandapi.api.arguments.EntitySelectorArgument.EntitySelector;
import io.github.jorelali.commandapi.api.arguments.LocationArgument.LocationType;
import io.github.jorelali.commandapi.safereflection.ReflectionType;
import io.github.jorelali.commandapi.safereflection.SafeReflection;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_13_R1.Advancement;
import net.minecraft.server.v1_13_R1.ArgumentChatComponent;
import net.minecraft.server.v1_13_R1.ArgumentChatFormat;
import net.minecraft.server.v1_13_R1.ArgumentEnchantment;
import net.minecraft.server.v1_13_R1.ArgumentEntity;
import net.minecraft.server.v1_13_R1.ArgumentEntitySummon;
import net.minecraft.server.v1_13_R1.ArgumentItemStack;
import net.minecraft.server.v1_13_R1.ArgumentMinecraftKeyRegistered;
import net.minecraft.server.v1_13_R1.ArgumentMobEffect;
import net.minecraft.server.v1_13_R1.ArgumentParticle;
import net.minecraft.server.v1_13_R1.ArgumentPosition;
import net.minecraft.server.v1_13_R1.ArgumentProfile;
import net.minecraft.server.v1_13_R1.ArgumentTag;
import net.minecraft.server.v1_13_R1.ArgumentVec3;
import net.minecraft.server.v1_13_R1.BlockPosition;
import net.minecraft.server.v1_13_R1.CommandListenerWrapper;
import net.minecraft.server.v1_13_R1.CompletionProviders;
import net.minecraft.server.v1_13_R1.CustomFunction;
import net.minecraft.server.v1_13_R1.CustomFunctionData;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_13_R1.ICompletionProvider;
import net.minecraft.server.v1_13_R1.LootTableRegistry;
import net.minecraft.server.v1_13_R1.MinecraftKey;
import net.minecraft.server.v1_13_R1.MinecraftServer;
import net.minecraft.server.v1_13_R1.Vec3D;

@SuppressWarnings({"unchecked", "rawtypes"})
@SafeReflection(type = ReflectionType.FIELD, target = CraftSound.class, name = "minecraftKey", returnType = String.class, versions = "1.13")
@SafeReflection(type = ReflectionType.FIELD, target = LootTableRegistry.class, name = "e", returnType = Map.class, versions = "1.13")
public class NMS_1_13_R1 implements NMS {
	
	private CommandListenerWrapper getCLW(CommandContext cmdCtx) {
		return (CommandListenerWrapper) cmdCtx.getSource();
	}

	@Override
	public ChatColor getChatColor(CommandContext cmdCtx, String str) {
		return CraftChatMessage.getColor(ArgumentChatFormat.a(cmdCtx, str));
	}

	@Override
	public BaseComponent[] getChatComponent(CommandContext cmdCtx, String str) {
		String resultantString = ChatSerializer.a(ArgumentChatComponent.a(cmdCtx, str));
		return ComponentSerializer.parse((String) resultantString);
	}

	@Override
	public Enchantment getEnchantment(CommandContext cmdCtx, String str) {
		return new CraftEnchantment(ArgumentEnchantment.a(cmdCtx, str));
	}

	@Override
	public ItemStack getItemStack(CommandContext cmdCtx, String str) throws CommandSyntaxException {
		return CraftItemStack.asBukkitCopy(ArgumentItemStack.a(cmdCtx, str).a(1, false));
	}

	@Override
	public Location getLocation(CommandContext cmdCtx, String str, LocationType locationType, CommandSender sender) throws CommandSyntaxException {
		switch(locationType) {
			case BLOCK_POSITION:
				BlockPosition blockPos = ArgumentPosition.a(cmdCtx, str);
				return new Location(getCommandSenderWorld(sender), blockPos.getX(), blockPos.getY(), blockPos.getZ());
			case PRECISE_POSITION:
				Vec3D vecPos = ArgumentVec3.a(cmdCtx, str);
				return new Location(getCommandSenderWorld(sender), vecPos.x, vecPos.y, vecPos.z);
		}
		return null;
	}

	@Override
	public Particle getParticle(CommandContext cmdCtx, String str) {
		return CraftParticle.toBukkit(ArgumentParticle.a(cmdCtx, str));
	}

	@Override
	public PotionEffectType getPotionEffect(CommandContext cmdCtx, String str) throws CommandSyntaxException {
		return new CraftPotionEffectType(ArgumentMobEffect.a(cmdCtx, str));
	}

	@Override
	public void createDispatcherFile(Object server, File file, CommandDispatcher dispatcher) {
		((MinecraftServer) server).commandDispatcher.a(file);
	}

	@Override
	public SuggestionProvider getSuggestionProvider(SuggestionProviders provider) {
		switch(provider) {
			case FUNCTION:
				return (context, builder) -> {
					CustomFunctionData functionData = getCLW(context).getServer().getFunctionData();
					ICompletionProvider.a(functionData.g().a(), builder, "#");
					return ICompletionProvider.a(functionData.c().keySet(), builder);
				};
			case RECIPES:
				return CompletionProviders.b;
			case SOUNDS:
				return CompletionProviders.c;
			case ADVANCEMENTS:
				return (cmdCtx, builder) -> {
					Collection<Advancement> advancements = ((CommandListenerWrapper) cmdCtx.getSource()).getServer().getAdvancementData().b();
					return ICompletionProvider.a(advancements.stream().map(Advancement::getName)::iterator, builder);
				};
			case LOOT_TABLES:
					return (context, builder) -> {
					try {
						Map<MinecraftKey, LootTable> map = (Map<MinecraftKey, LootTable>) CommandAPIHandler.getField(LootTableRegistry.class, "e").get(getCLW(context).getServer().aP());
						return ICompletionProvider.a((Iterable) map.keySet(), builder);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
					return Suggestions.empty();
					
				};		
			default:
				return (context, builder) -> Suggestions.empty();
		}
	}

	@Override
	public FunctionWrapper[] getFunction(CommandContext cmdCtx, String str) throws CommandSyntaxException {
		Collection<CustomFunction> customFuncList = ArgumentTag.a(cmdCtx, str);
		
		FunctionWrapper[] result = new FunctionWrapper[customFuncList.size()];
		
		CustomFunctionData customFunctionData = getCLW(cmdCtx).getServer().getFunctionData();
		CommandListenerWrapper commandListenerWrapper = getCLW(cmdCtx).a().b(2);
		
		int count = 0;
		Iterator<CustomFunction> it = customFuncList.iterator();
		while(it.hasNext()) {
			CustomFunction customFunction = it.next();
			@SuppressWarnings("deprecation")
			NamespacedKey minecraftKey = new NamespacedKey(customFunction.a().b(), customFunction.a().getKey());
			ToIntBiFunction<CustomFunction, CommandListenerWrapper> obj = customFunctionData::a;
			
			FunctionWrapper wrapper = new FunctionWrapper(minecraftKey, obj, customFunction, commandListenerWrapper, e -> {
				return (Object) getCLW(cmdCtx).a(((CraftEntity) e).getHandle());
			});
			
			result[count] = wrapper;
			count++;
		}
		
		return result;
	}

	@Override
	public CommandSender getSenderForCommand(CommandContext cmdCtx) {
		CommandSender sender = getCLW(cmdCtx).getBukkitSender();
		
		Entity proxyEntity = getCLW(cmdCtx).f();
		if(proxyEntity != null) {
			CommandSender proxy = ((Entity) proxyEntity).getBukkitEntity();
			
			if(!proxy.equals(sender)) {
				sender = new ProxiedNativeCommandSender(getCLW(cmdCtx), sender, proxy);
			}
		}
		
		return sender;
	}

	@Override
	public CommandDispatcher getBrigadierDispatcher(Object server) {
		return ((MinecraftServer) server).commandDispatcher.a();
	}

	@Override
	public CommandSender getCommandSenderForCLW(Object clw) {
		return ((CommandListenerWrapper) clw).getBukkitSender();
	}

	@Override
	public Player getPlayer(CommandContext cmdCtx, String str) throws CommandSyntaxException {
		Player target = Bukkit.getPlayer(((GameProfile) ArgumentProfile.a(cmdCtx, str).iterator().next()).getId());
		if(target == null) {
			throw ArgumentProfile.a.create();
		} else {
			return target;
		}
	}

	@Override
	public Object getEntitySelector(CommandContext cmdCtx, String str, EntitySelector selector) throws CommandSyntaxException {
		switch(selector) {
			case MANY_ENTITIES:
				try {
					return ArgumentEntity.c(cmdCtx, str).stream().map(entity -> (org.bukkit.entity.Entity) ((Entity) entity).getBukkitEntity()).collect(Collectors.toList());
				} catch(CommandSyntaxException e) {
					return new ArrayList<org.bukkit.entity.Entity>();
				}
			case MANY_PLAYERS:
				try {
					return ArgumentEntity.d(cmdCtx, str).stream().map(player -> (Player) ((Entity) player).getBukkitEntity()).collect(Collectors.toList());
				} catch(CommandSyntaxException e) {
					return new ArrayList<Player>();
				}
			case ONE_ENTITY:
				return (org.bukkit.entity.Entity) ArgumentEntity.a(cmdCtx, str).getBukkitEntity();
			case ONE_PLAYER:
				return (Player) ArgumentEntity.e(cmdCtx, str).getBukkitEntity();
		}
		return null;
	}

	@Override
	public EntityType getEntityType(CommandContext cmdCtx, String str, CommandSender sender) throws CommandSyntaxException {
		Entity entity = EntityTypes.a(((CraftWorld) getCommandSenderWorld(sender)).getHandle(), ArgumentEntitySummon.a(cmdCtx, str));
		return entity.getBukkitEntity().getType();
	}

	@SuppressWarnings("deprecation")
	@Override
	public LootTable getLootTable(CommandContext cmdCtx, String str) {
		MinecraftKey minecraftKey = ArgumentMinecraftKeyRegistered.c(cmdCtx, str);
		String namespace = minecraftKey.b();
		String key = minecraftKey.getKey();
		
		net.minecraft.server.v1_13_R1.LootTable lootTable = getCLW(cmdCtx).getServer().aP().a(minecraftKey);
		return new CraftLootTable(new NamespacedKey(namespace, key), lootTable);
	}
	
	@Override
	public Sound getSound(CommandContext cmdCtx, String key) {
		MinecraftKey minecraftKey = ArgumentMinecraftKeyRegistered.c(cmdCtx, key);
		Map<String, CraftSound> map = new HashMap<>(); 
		Arrays.stream(CraftSound.values()).forEach(val -> {
			try {
				map.put((String) CommandAPIHandler.getField(CraftSound.class, "minecraftKey").get(val), val);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		});
		return Sound.valueOf(map.get(minecraftKey.getKey()).name());
	}
	
	@Override
	public org.bukkit.advancement.Advancement getAdvancement(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return ArgumentMinecraftKeyRegistered.a(cmdCtx, key).bukkit;
	}
	
	@Override
	public Recipe getRecipe(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return ArgumentMinecraftKeyRegistered.b(cmdCtx, key).toBukkitRecipe();
	}

	@Override
	public SimpleCommandMap getSimpleCommandMap() {
		return ((CraftServer) Bukkit.getServer()).getCommandMap();
	}

	@Override
	public boolean isVanillaCommandWrapper(Command command) {
		return command instanceof VanillaCommandWrapper;
	}

	@Override
	public ArgumentType _ArgumentChatFormat() {
		return ArgumentChatFormat.a();
	}

	@Override
	public ArgumentType _ArgumentChatComponent() {
		return ArgumentChatComponent.a();
	}

	@Override
	public ArgumentType _ArgumentMinecraftKeyRegistered() {
		return ArgumentMinecraftKeyRegistered.a();
	}

	@Override
	public ArgumentType _ArgumentMobEffect() {
		return ArgumentMobEffect.a();
	}

	@Override
	public ArgumentType _ArgumentProfile() {
		return ArgumentProfile.a();
	}

	@Override
	public ArgumentType _ArgumentParticle() {
		return ArgumentParticle.a();
	}

	@Override
	public ArgumentType _ArgumentPosition() {
		return ArgumentPosition.a();
	}

	@Override
	public ArgumentType _ArgumentVec3() {
		return ArgumentVec3.a();
	}

	@Override
	public ArgumentType _ArgumentItemStack() {
		return ArgumentItemStack.a();
	}

	@Override
	public ArgumentType _ArgumentTag() {
		return ArgumentTag.a();
	}

	@Override
	public ArgumentType _ArgumentEntitySummon() {
		return ArgumentEntitySummon.a();
	}

	@Override
	public ArgumentType _ArgumentEntity(EntitySelector selector) {
		switch(selector) {
			case MANY_ENTITIES:
				return ArgumentEntity.b();
			case MANY_PLAYERS:
				return ArgumentEntity.d();
			case ONE_ENTITY:
				return ArgumentEntity.a();
			case ONE_PLAYER:
				return ArgumentEntity.c();
		}
		return null;
	}

	@Override
	public ArgumentType _ArgumentEnchantment() {
		return ArgumentEnchantment.a();
	}

	@Override
	public String[] compatibleVersions() {
		return new String[] {"1.13.2"};
	}

}
