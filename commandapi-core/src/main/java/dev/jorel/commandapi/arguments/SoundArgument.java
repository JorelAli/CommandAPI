/*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
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
package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.nms.NMS;

/**
 * An argument that represents the Bukkit Sound object
 */
public class SoundArgument extends SafeOverrideableArgument<org.bukkit.Sound, org.bukkit.Sound> implements ICustomProvidedArgument {
	
	/**
	 * A Sound argument. Represents Bukkit's Sound object
	 * @param nodeName the name of the node for this argument
	 */
	public SoundArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentMinecraftKeyRegistered(), CommandAPIHandler.getInstance().getNMS()::convert);
	}

	@Override
	public Class<org.bukkit.Sound> getPrimitiveType() {
		return org.bukkit.Sound.class;
	}

	@Override
	public SuggestionProviders getSuggestionProvider() {
		return SuggestionProviders.SOUNDS;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SOUND;
	}
	
	@Override
	public <CommandListenerWrapper> org.bukkit.Sound parseArgument(NMS<CommandListenerWrapper> nms,
			CommandContext<CommandListenerWrapper> cmdCtx, String key, Object[] previousArgs) throws CommandSyntaxException {
		return (org.bukkit.Sound) nms.getSound(cmdCtx, key, ArgumentSubType.SOUND_SOUND);
	}

	/**
	 * An argument that represents the Bukkit Sound object
	 * 
	 * @apiNote Returns a {@link NamespacedKey} object
	 */
	public static class NamespacedKey extends SafeOverrideableArgument<org.bukkit.NamespacedKey, org.bukkit.NamespacedKey> implements ICustomProvidedArgument {

		/**
		 * Constructs a SoundArgument with a given node name. This SoundArgument will
		 * return a {@link NamespacedKey}
		 * 
		 * @param nodeName the name of the node for argument
		 */
		public NamespacedKey(String nodeName) {
			super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentMinecraftKeyRegistered(), org.bukkit.NamespacedKey::toString);
		}

		@Override
		public SuggestionProviders getSuggestionProvider() {
			return SuggestionProviders.SOUNDS;
		}

		@Override
		public Class<org.bukkit.NamespacedKey> getPrimitiveType() {
			return org.bukkit.NamespacedKey.class;
		}

		@Override
		public CommandAPIArgumentType getArgumentType() {
			return CommandAPIArgumentType.SOUND;
		}

		@Override
		public <CommandSourceStack> org.bukkit.NamespacedKey parseArgument(NMS<CommandSourceStack> nms, CommandContext<CommandSourceStack> cmdCtx, String key,
			Object[] previousArgs) throws CommandSyntaxException {
			return (org.bukkit.NamespacedKey) nms.getSound(cmdCtx, key, ArgumentSubType.SOUND_NAMESPACEDKEY);
		}

	}
}
