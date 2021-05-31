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

import java.util.Collection;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.nms.NMS;

/**
 * An argument that represents a scoreholder's name, or a collection of scoreholder names
 */
public class ScoreHolderArgument extends Argument {
		
	private final boolean single;
	
	/**
	 * A Score Holder argument. Represents a single score holder or a collection of score holders.
	 * Defaults to using ScoreHolderType.SINGLE
	 * @param nodeName the name of the node for this argument
	 */
	public ScoreHolderArgument(String nodeName) {
		this(nodeName, ScoreHolderType.SINGLE);
	}
	
	/**
	 * A Score Holder argument. Represents a single score holder or a collection of score holders
	 * @param nodeName the name of the node for this argument
	 * @param type whether this argument represents a single score holder or a collection of score holders
	 */
	public ScoreHolderArgument(String nodeName, ScoreHolderType type) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentScoreholder(type == ScoreHolderType.SINGLE));
		single = (type == ScoreHolderType.SINGLE);
	}
	
	/**
	 * Returns whether this argument represents a single score holder or a collection of score holders
	 * @return true if this argument represents a single score holder
	 */
	public boolean isSingle() {
		return this.single;
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return single ? String.class : Collection.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SCORE_HOLDER;
	}
	
	@Override
	public <CommandListenerWrapper> Object parseArgument(NMS<CommandListenerWrapper> nms,
			CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return single ? nms.getScoreHolderSingle(cmdCtx, key) : nms.getScoreHolderMultiple(cmdCtx, key);
	}

	/**
	 * An enum specifying whether a score holder consists of a single score holder or a collection of score holders
	 */
	public static enum ScoreHolderType {
		/**
		 * A single score holder name
		 */
		SINGLE, 
		
		/**
		 * A collection of score holder names
		 */
		MULTIPLE;
	}
}
