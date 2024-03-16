/*******************************************************************************
 * Copyright 2018, 2021 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi.nms;

import java.util.Map;

import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.help.SimpleHelpMap;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.jorel.commandapi.preprocessor.Differs;
import dev.jorel.commandapi.preprocessor.NMSMeta;
import dev.jorel.commandapi.preprocessor.RequireField;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.server.v1_16_R3.ArgumentChat;
import net.minecraft.server.v1_16_R3.ArgumentChatComponent;
import net.minecraft.server.v1_16_R3.ArgumentChatFormat;
import net.minecraft.server.v1_16_R3.ArgumentPredicateItemStack;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import net.minecraft.server.v1_16_R3.CustomFunctionManager;
import net.minecraft.server.v1_16_R3.DataPackResources;
import net.minecraft.server.v1_16_R3.EntitySelector;
import net.minecraft.server.v1_16_R3.IBlockData;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.IReloadableResourceManager;
import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.ParticleParamBlock;
import net.minecraft.server.v1_16_R3.ParticleParamItem;
import net.minecraft.server.v1_16_R3.ParticleParamRedstone;

/**
 * NMS implementation for Minecraft 1.16.5
 */
@NMSMeta(compatibleWith = "1.16.5")
@RequireField(in = DataPackResources.class, name = "i", ofType = CustomFunctionManager.class)
@RequireField(in = DataPackResources.class, name = "b", ofType = IReloadableResourceManager.class)
@RequireField(in = CustomFunctionManager.class, name = "h", ofType = CommandDispatcher.class)
@RequireField(in = EntitySelector.class, name = "checkPermissions", ofType = boolean.class)
@RequireField(in = SimpleHelpMap.class, name = "helpTopics", ofType = Map.class)
@RequireField(in = ParticleParamBlock.class, name = "c", ofType = IBlockData.class)
@RequireField(in = ParticleParamItem.class, name = "c", ofType = ItemStack.class)
@RequireField(in = ParticleParamRedstone.class, name = "g", ofType = float.class)
@RequireField(in = ArgumentPredicateItemStack.class, name = "c", ofType = NBTTagCompound.class)
public class NMS_1_16_R3 extends NMS_1_16_4_R3 {

	@Differs(from = "1.16.4", by = "Use of non-deprecated NamespacedKey.fromString method")
	@Override
	protected NamespacedKey fromMinecraftKey(MinecraftKey key) {
		return NamespacedKey.fromString(key.getNamespace() + ":" + key.getKey());
	}

	@Override
	public String[] compatibleVersions() {
		return new String[] { "1.16.5" };
	}

}
