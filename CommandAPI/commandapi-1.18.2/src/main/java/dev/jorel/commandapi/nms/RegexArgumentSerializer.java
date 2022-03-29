package dev.jorel.commandapi.nms;

import com.google.gson.JsonObject;

import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.network.FriendlyByteBuf;

public class RegexArgumentSerializer implements ArgumentSerializer<RegexArgumentType> {

	@Override
	public void serializeToNetwork(RegexArgumentType argument, FriendlyByteBuf packetByteBuf) {
		packetByteBuf.writeByteArray(argument.pattern.pattern().getBytes());
	}

	@Override
	public RegexArgumentType deserializeFromNetwork(FriendlyByteBuf packetByteBuf) {
		String result = new String(packetByteBuf.readByteArray());
		return new RegexArgumentType(result);
	}

	@Override
	public void serializeToJson(RegexArgumentType argument, JsonObject jsonObject) {
		jsonObject.addProperty("pattern", argument.pattern.pattern());
	}

}