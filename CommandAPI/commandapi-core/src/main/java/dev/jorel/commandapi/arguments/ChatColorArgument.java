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

import java.util.function.Function;

import org.bukkit.ChatColor;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.nms.NMS;

/**
 * An argument that represents the Bukkit ChatColor object
 */
public class ChatColorArgument extends SafeOverrideableArgument<ChatColor> {

	/**
	 * Constructs a ChatColor argument with a given node name. Represents a color or
	 * formatting for chat
	 * 
	 * @param nodeName the name of the node for argument
	 */
	public ChatColorArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentChatFormat(), ((Function<ChatColor, String>) ChatColor::name).andThen(String::toLowerCase));
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return ChatColor.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.CHATCOLOR;
	}
	
	@Override
	public <CommandListenerWrapper> Object parseArgument(NMS<CommandListenerWrapper> nms,
			CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return nms.getChatColor(cmdCtx, key);
	}
}
