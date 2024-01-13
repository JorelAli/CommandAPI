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

import static dev.jorel.commandapi.preprocessor.Unimplemented.REASON.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.ToIntFunction;

import org.bukkit.NamespacedKey;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.jorel.commandapi.preprocessor.Differs;
import dev.jorel.commandapi.preprocessor.Unimplemented;
import dev.jorel.commandapi.wrappers.FunctionWrapper;
import dev.jorel.commandapi.wrappers.SimpleFunctionWrapper;
import net.minecraft.commands.CommandFunction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

/**
 * Common NMS code To ensure that this code actually works across all versions
 * of Minecraft that this is supposed to support (1.17+), you should be
 * compiling this code against all of the declared Maven profiles specified in
 * this submodule's pom.xml file, by running the following commands:
 * <ul>
 * <li><code>mvn clean package -pl :commandapi-bukkit-nms-common -P Platform.Bukkit,Spigot_1_19_3_R2</code></li>
 * <li><code>mvn clean package -pl :commandapi-bukkit-nms-common -P Platform.Bukkit,Spigot_1_19_R1</code></li>
 * <li><code>mvn clean package -pl :commandapi-bukkit-nms-common -P Platform.Bukkit,Spigot_1_18_2_R2</code></li>
 * <li><code>mvn clean package -pl :commandapi-bukkit-nms-common -P Platform.Bukkit,Spigot_1_18_R1</code></li>
 * <li><code>mvn clean package -pl :commandapi-bukkit-nms-common -P Platform.Bukkit,Spigot_1_17_R1</code></li>
 * </ul>
 * Any of these that do not work should be removed or implemented otherwise
 * (introducing another NMS_Common module perhaps?
 */
public abstract class NMS_CommonWithFunctions extends NMS_Common {

	private static NamespacedKey fromResourceLocation(ResourceLocation key) {
		return NamespacedKey.fromString(key.getNamespace() + ":" + key.getPath());
	}

	// Converts NMS function to SimpleFunctionWrapper
	private SimpleFunctionWrapper convertFunction(CommandFunction commandFunction) {
		ToIntFunction<CommandSourceStack> appliedObj = (CommandSourceStack css) -> this.<MinecraftServer>getMinecraftServer().getFunctions()
			.execute(commandFunction, css);

		CommandFunction.Entry[] cArr = commandFunction.getEntries();
		String[] result = new String[cArr.length];
		for (int i = 0, size = cArr.length; i < size; i++) {
			result[i] = cArr[i].toString();
		}
		return new SimpleFunctionWrapper(fromResourceLocation(commandFunction.getId()), appliedObj, result);
	}
	
	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "CraftEntity")
	public abstract FunctionWrapper[] getFunction(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	// TODO: This has its own implementation for 1.17, 1.18 and 1.18.2
	public SimpleFunctionWrapper getFunction(NamespacedKey key) {
		final ResourceLocation resourceLocation = new ResourceLocation(key.getNamespace(), key.getKey());
		Optional<CommandFunction> commandFunctionOptional = this.<MinecraftServer>getMinecraftServer().getFunctions().get(resourceLocation);
		if(commandFunctionOptional.isPresent()) {
			return convertFunction(commandFunctionOptional.get());
		} else {
			throw new IllegalStateException("Failed to get defined function " + key
				+ "! This should never happen - please report this to the CommandAPI"
				+ "developers, we'd love to know how you got this error message!");
		}
	}

	@Override
	// TODO: This has its own implementation for 1.17, 1.18 and 1.18.2
	public Set<NamespacedKey> getFunctions() {
		Set<NamespacedKey> result = new HashSet<>();
		for (ResourceLocation resourceLocation : this.<MinecraftServer>getMinecraftServer().getFunctions().getFunctionNames()) {
			result.add(fromResourceLocation(resourceLocation));
		}
		return result;
	}
	
	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, from = "1.18.2", to = "1.19")
	@Differs(from = "1.18.2", by = "getTag() now returns a Collection<> instead of a Tag<>, so don't have to call .getValues()")
	public abstract SimpleFunctionWrapper[] getTag(NamespacedKey key);
	
}
