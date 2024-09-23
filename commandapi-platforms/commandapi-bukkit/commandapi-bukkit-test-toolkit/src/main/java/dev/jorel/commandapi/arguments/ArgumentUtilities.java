package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.jorel.commandapi.BukkitTooltip;
import dev.jorel.commandapi.arguments.parser.Parser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.function.Function;

/**
 * Utilities for creating mock argument parsers
 */
public class ArgumentUtilities {
	private ArgumentUtilities() {
	}

	// Translatable messages
	public static Message translatedMessage(String key) {
		return BukkitTooltip.messageFromAdventureComponent(Component.translatable(key));
	}


	public static Message translatedMessage(String key, Object... args) {
		TextComponent[] argsComponents = new TextComponent[args.length];
		for (int i = 0; i < args.length; i++) {
			Object arg = args[i];
			String text = arg.toString();
			argsComponents[i] = Component.text(text);
		}
		return BukkitTooltip.messageFromAdventureComponent(Component.translatable(key, argsComponents));
	}

	// Parser utilities
	public static final CommandSyntaxException NEXT_BRANCH = new SimpleCommandExceptionType(
		() -> "This branch did not match"
	).create();


	public static Parser.Literal assertCanRead(Function<StringReader, CommandSyntaxException> exception) {
		return reader -> {
			if (!reader.canRead()) throw exception.apply(reader);
		};
	}

	public static Parser.Literal literal(String literal) {
		return reader -> {
			if (reader.canRead(literal.length())) {
				int start = reader.getCursor();
				int end = start + literal.length();

				if (reader.getString().substring(start, end).equals(literal)) {
					reader.setCursor(end);
					return;
				}
			}
			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect().createWithContext(reader, literal);
		};
	}

	public static Parser.Argument<String> readUntilWithoutEscapeCharacter(char terminator) {
		return reader -> {
			int start = reader.getCursor();
			while (reader.canRead() && reader.peek() != terminator) {
				reader.skip();
			}
			return reader.getString().substring(start, reader.getCursor());
		};
	}
}
