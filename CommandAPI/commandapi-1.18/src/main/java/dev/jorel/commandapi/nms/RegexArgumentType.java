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

public class RegexArgumentType implements ArgumentType<String> {
	
	public static final void register() {
		ArgumentTypes.register("regex", RegexArgumentType.class, new RegexArgumentSerializer());
	}

	public Pattern pattern;
	public String errorMessage;
	
	public RegexArgumentType(String pattern, String errorMessage) {
		this.pattern = Pattern.compile(pattern);
		this.errorMessage = errorMessage;
	}
	
	public static String getString(final CommandContext<?> context, final String name) {
		return context.getArgument(name, String.class);
	}

	@Override
	public String parse(StringReader reader) throws CommandSyntaxException {
		String input = "";
		while(reader.canRead() && !this.pattern.matcher(input).matches()) {
			input = input + reader.read();
		}
		if(!this.pattern.matcher(input).matches()) {
			throw new SimpleCommandExceptionType(new LiteralMessage(errorMessage)).createWithContext(reader);
		}
		return input;
	}

	@Override
	public String toString() {
		return "regex(" + this.pattern.pattern() + ")(" + errorMessage + ")";
	}
	
	static class RegexArgumentSerializer implements ArgumentSerializer<RegexArgumentType> {

		@Override
		public void serializeToNetwork(RegexArgumentType argument, FriendlyByteBuf packetByteBuf) {
			packetByteBuf.writeByteArray(argument.pattern.pattern().getBytes());
			packetByteBuf.writeByteArray(argument.errorMessage.getBytes());
		}

		@Override
		public RegexArgumentType deserializeFromNetwork(FriendlyByteBuf packetByteBuf) {
			String pattern = new String(packetByteBuf.readByteArray());
			String errorMessage = new String(packetByteBuf.readByteArray());
			return new RegexArgumentType(pattern, errorMessage);
		}

		@Override
		public void serializeToJson(RegexArgumentType argument, JsonObject jsonObject) {
			jsonObject.addProperty("pattern", argument.pattern.pattern());
			jsonObject.addProperty("message", argument.errorMessage);
		}

	}
	
}