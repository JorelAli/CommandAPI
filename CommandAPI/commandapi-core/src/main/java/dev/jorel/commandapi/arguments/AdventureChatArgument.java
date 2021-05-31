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
package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.exceptions.PaperAdventureNotFoundException;
import dev.jorel.commandapi.nms.NMS;
import net.kyori.adventure.text.Component;

/**
 * An argument that represents chat with entity selectors
 */
public class AdventureChatArgument extends Argument implements IGreedyArgument {

	/**
	 * Constructs a Chat argument with a given node name. Represents fancy greedy
	 * strings that can parse entity selectors
	 * 
	 * @param nodeName the name of the node for argument
	 */
	public AdventureChatArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentChat());
		
		try {
			Class.forName("net.kyori.adventure.text.Component");
		} catch(ClassNotFoundException e) {
			throw new PaperAdventureNotFoundException(this.getClass());
		}
	}

	@Override
	public Class<?> getPrimitiveType() {
		return Component.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.ADVENTURE_CHAT;
	}
	
	@Override
	public <CommandListenerWrapper> Object parseArgument(NMS<CommandListenerWrapper> nms,
			CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return nms.getAdventureChat(cmdCtx, key);
	}
}
