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

import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.datafixers.util.Either;

import dev.jorel.commandapi.preprocessor.NMSMeta;
import dev.jorel.commandapi.preprocessor.RequireField;
import io.netty.channel.Channel;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.server.ServerFunctionLibrary;
import net.minecraft.world.level.gameevent.EntityPositionSource;

// Mojang-Mapped reflection
/**
 * NMS implementation for Minecraft 1.19
 */
@NMSMeta(compatibleWith = "1.19")
@RequireField(in = ServerFunctionLibrary.class, name = "dispatcher", ofType = CommandDispatcher.class)
@RequireField(in = EntitySelector.class, name = "usesSelector", ofType = boolean.class)
@RequireField(in = EntityPositionSource.class, name = "entityOrUuidOrId", ofType = Either.class)
public class NMS_1_19_R1 extends NMS_1_19_Common {
	
	@Override
	public String[] compatibleVersions() {
		return new String[] { "1.19" };
	}

}
