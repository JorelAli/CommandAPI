package dev.jorel.commandapi;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class Impl implements StringParser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5716618161211996120L;

	@Override
	public String parse(StringReader reader) throws CommandSyntaxException {

		final int start = reader.getCursor();

		// Read exactly 5 characters
		int i = 0;
		while (reader.canRead()) {
			reader.skip();
			i++;
			if (i == 5) {
				break;
			}
		}

		if (reader.getString().substring(start, reader.getCursor()).equals("Hello")) {
			reader.skipWhitespace();
			reader.readBoolean();
			return reader.getString().substring(start, reader.getCursor());
		} else {
			throw new SimpleCommandExceptionType(new LiteralMessage("You didn't write 'Hello'"))
					.createWithContext(reader);
		}
	}

}
