package dev.jorel.commandapi.nms;

import java.util.regex.Pattern;

import com.google.gson.JsonObject;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.network.FriendlyByteBuf;

public class RegexArgumentType_1_18_R1 implements ArgumentType<String> {
	
	public static final void register() {
		ArgumentTypes.register("regex", RegexArgumentType_1_18_R1.class, new RegexArgumentSerializer());
	}

	public Pattern pattern;
	public String errorMessage;
	
	public RegexArgumentType_1_18_R1(String pattern, String errorMessage) {
		this.pattern = Pattern.compile(pattern);
		this.errorMessage = errorMessage;
	}
	
	public static String getString(final CommandContext<?> context, final String name) {
		return context.getArgument(name, String.class);
	}

	@Override
	public String parse(StringReader reader) throws CommandSyntaxException {
		StringBuilder input = new StringBuilder();
		String lastEntry = null;
		int lastEntryCursor = 0;
		while(reader.canRead()) {
			input.append(reader.read());
			if(this.pattern.matcher(input).matches()) {
				// We have a match! Let's keep going though, there may be more valid matches
				lastEntry = input.toString();
				lastEntryCursor = reader.getCursor();
			}
		}
		if(lastEntry == null) {
			throw new SimpleCommandExceptionType(new LiteralMessage(errorMessage)).createWithContext(reader);
		} else {
			reader.setCursor(lastEntryCursor);
			return lastEntry;
		}
	}

	@Override
	public String toString() {
		return "regex(" + this.pattern.pattern() + ")(" + errorMessage + ")";
	}
	
	static class RegexArgumentSerializer implements ArgumentSerializer<RegexArgumentType_1_18_R1> {

		@Override
		public void serializeToNetwork(RegexArgumentType_1_18_R1 argument, FriendlyByteBuf packetByteBuf) {
			packetByteBuf.writeByteArray(argument.pattern.pattern().getBytes());
			packetByteBuf.writeByteArray(argument.errorMessage.getBytes());
		}

		@Override
		public RegexArgumentType_1_18_R1 deserializeFromNetwork(FriendlyByteBuf packetByteBuf) {
			String pattern = new String(packetByteBuf.readByteArray());
			String errorMessage = new String(packetByteBuf.readByteArray());
			return new RegexArgumentType_1_18_R1(pattern, errorMessage);
		}

		@Override
		public void serializeToJson(RegexArgumentType_1_18_R1 argument, JsonObject jsonObject) {
			jsonObject.addProperty("pattern", argument.pattern.pattern());
			jsonObject.addProperty("message", argument.errorMessage);
		}

	}
	
}