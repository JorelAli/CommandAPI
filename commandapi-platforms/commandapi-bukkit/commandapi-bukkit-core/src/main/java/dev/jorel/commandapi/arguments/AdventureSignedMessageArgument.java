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

import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.exceptions.PaperAdventureNotFoundException;
import net.kyori.adventure.chat.SignedMessage;

/**
 * An argument that represents a signed message from a client
 * 
 * @apiNote Returns a {@link SignedMessage} object
 */
public class AdventureSignedMessageArgument extends Argument<SignedMessage> {

	/**
	 * Constructs a SignedMessage argument with a given node name. Represents
	 * messages that have been signed by the client. If the message was not signed
	 * by the client, this will return a "disguised" message instead - a message
	 * which has been signed explicitly by the server and redistributed to clients
	 * (and the server)
	 * 
	 * @param nodeName the name of the node for argument
	 */
	public AdventureSignedMessageArgument(String nodeName) {
		super(nodeName, CommandAPIBukkit.get()._ArgumentChat());

		try {
			Class.forName("net.kyori.adventure.chat.SignedMessage");
		} catch (ClassNotFoundException e) {
			throw new PaperAdventureNotFoundException(this.getClass());
		}
	}

	@Override
	public Class<SignedMessage> getPrimitiveType() {
		return SignedMessage.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.ADVENTURE_SIGNED_MESSAGE;
	}

	@Override
	public <CommandSourceStack> SignedMessage parseArgument(CommandContext<CommandSourceStack> cmdCtx, String key, Object[] previousArgs) throws CommandSyntaxException {
		return CommandAPIBukkit.<CommandSourceStack>get().getAdventureSignedMessage(cmdCtx, key);
	}
}
