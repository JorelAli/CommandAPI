package dev.jorel.commandapi.nms;

import java.util.Map;

import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_14_R1.CraftSound;
import org.bukkit.craftbukkit.v1_14_R1.help.SimpleHelpMap;

import com.mojang.brigadier.context.CommandContext;

import dev.jorel.commandapi.preprocessor.Differs;
import dev.jorel.commandapi.preprocessor.NMSMeta;
import dev.jorel.commandapi.preprocessor.RequireField;
import net.minecraft.server.v1_14_R1.ArgumentMinecraftKeyRegistered;
import net.minecraft.server.v1_14_R1.ArgumentPredicateItemStack;
import net.minecraft.server.v1_14_R1.CommandListenerWrapper;
import net.minecraft.server.v1_14_R1.EntitySelector;
import net.minecraft.server.v1_14_R1.IBlockData;
import net.minecraft.server.v1_14_R1.ItemStack;
import net.minecraft.server.v1_14_R1.MinecraftKey;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.ParticleParamBlock;
import net.minecraft.server.v1_14_R1.ParticleParamItem;
import net.minecraft.server.v1_14_R1.ParticleParamRedstone;

/**
 * NMS implementation for Minecraft 1.14.4
 */
@NMSMeta(compatibleWith = "1.14.4")
@RequireField(in = CraftSound.class, name = "minecraftKey", ofType = String.class)
@RequireField(in = EntitySelector.class, name = "checkPermissions", ofType = boolean.class)
@RequireField(in = SimpleHelpMap.class, name = "helpTopics", ofType = Map.class)
@RequireField(in = ParticleParamBlock.class, name = "c", ofType = IBlockData.class)
@RequireField(in = ParticleParamItem.class, name = "c", ofType = ItemStack.class)
@RequireField(in = ParticleParamRedstone.class, name = "f", ofType = float.class)
@RequireField(in = ArgumentPredicateItemStack.class, name = "c", ofType = NBTTagCompound.class)
public class NMS_1_14_4 extends NMS_1_14_3 {

	@Differs(from = "1.14.3", by = "MinecraftKey.b() -> MinecraftKey.getNamespace()")
	@SuppressWarnings("deprecation")
	@Override
	protected NamespacedKey fromMinecraftKey(MinecraftKey key) {
		return new NamespacedKey(key.getNamespace(), key.getKey());
	}

	@Override
	public String[] compatibleVersions() {
		return new String[] { "1.14.4" };
	}

	@Differs(from = "1.14.3", by = "MinecraftKey.b() -> MinecraftKey.getNamespace()")
	@SuppressWarnings("deprecation")
	@Override
	public NamespacedKey getMinecraftKey(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		MinecraftKey resourceLocation = ArgumentMinecraftKeyRegistered.c(cmdCtx, key);
		return new NamespacedKey(resourceLocation.getNamespace(), resourceLocation.getKey());
	}
}
