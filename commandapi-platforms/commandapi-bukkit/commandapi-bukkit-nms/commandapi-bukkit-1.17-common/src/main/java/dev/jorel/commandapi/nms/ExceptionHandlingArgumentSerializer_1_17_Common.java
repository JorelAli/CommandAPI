package dev.jorel.commandapi.nms;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.arguments.ExceptionHandlingArgumentType;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.network.FriendlyByteBuf;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class ExceptionHandlingArgumentSerializer_1_17_Common<T> implements ArgumentSerializer<ExceptionHandlingArgumentType<T>> {
	private static Method getInfo = null;

	@Override
	public void serializeToNetwork(ExceptionHandlingArgumentType<T> argument, FriendlyByteBuf friendlyByteBuf) {
		try {
			// Remove this key from packet
			if(getInfo == null) {
				getInfo = ArgumentTypes.class.getDeclaredMethod("b", ArgumentType.class);
				getInfo.setAccessible(true);
			}
			Object myInfo = getInfo.invoke(null, argument);

			Field keyField = CommandAPIHandler.getField(myInfo.getClass(), "c");
			String myKey = keyField.get(myInfo).toString();
			byte[] myKeyBytes = myKey.getBytes(StandardCharsets.UTF_8);
			// Removing length and size of string, assuming length is always written as 1 byte
			friendlyByteBuf.writerIndex(friendlyByteBuf.writerIndex() - myKeyBytes.length - 1);

			// Add baseType key instead
			ArgumentType<T> baseType = argument.baseType();
			Object baseInfo = getInfo.invoke(null, baseType);
			String baseKey = keyField.get(baseInfo).toString();
			friendlyByteBuf.writeUtf(baseKey);

			// Serialize baseType
			Field subSerializerField = CommandAPIHandler.getField(baseInfo.getClass(), "b");
			ArgumentSerializer<ArgumentType<T>> subSerializer = (ArgumentSerializer<ArgumentType<T>>) subSerializerField.get(baseInfo);
			subSerializer.serializeToNetwork(baseType, friendlyByteBuf);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void serializeToJson(ExceptionHandlingArgumentType<T> argument, JsonObject properties) {
		try {
			ArgumentType<T> baseType = argument.baseType();

			if(getInfo == null) {
				getInfo = ArgumentTypes.class.getDeclaredMethod("b", ArgumentType.class);
				getInfo.setAccessible(true);
			}
			Object baseInfo = getInfo.invoke(null, baseType);

			Field keyField = CommandAPIHandler.getField(baseInfo.getClass(), "c");
			properties.addProperty("baseType", keyField.get(baseInfo).toString());

			Field subSerializerField = CommandAPIHandler.getField(baseInfo.getClass(), "b");
			ArgumentSerializer<ArgumentType<T>> subSerializer = (ArgumentSerializer<ArgumentType<T>>) subSerializerField.get(baseInfo);

			JsonObject subProperties = new JsonObject();
			subSerializer.serializeToJson(baseType, subProperties);
			if (subProperties.size() > 0) {
				properties.add("baseProperties", subProperties);
			}
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ExceptionHandlingArgumentType<T> deserializeFromNetwork(FriendlyByteBuf friendlyByteBuf) {
		// Since this class overrides its ArgumentRegistry key with the baseType's,
		// this class's key should never show up in a packet and this method should never
		// be called to deserialize the ArgumentType info that wasn't put into the packet
		// anyway. Also, the server shouldn't ever deserialize a PacketPlay*Out*Commands
		// either. If this method ever gets called, either you or I are doing something very wrong!
		throw new IllegalStateException("This shouldn't happen! See dev.jorel.commandapi.nms.ExceptionHandlingArgumentSerializer_1_17_Common#deserializeFromNetwork for more information");
		// Including a mini-stacktrace here in case this exception shows up
		// on a client-disconnected screen, which is not very helpful
	}
}
