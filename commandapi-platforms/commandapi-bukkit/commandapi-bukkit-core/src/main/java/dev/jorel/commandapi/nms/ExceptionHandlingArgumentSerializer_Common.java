package dev.jorel.commandapi.nms;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import dev.jorel.commandapi.arguments.ExceptionHandlingArgumentType;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public abstract class ExceptionHandlingArgumentSerializer_Common<T, WRITER extends ByteBuf> {
	protected void commonSerializeToNetwork(ExceptionHandlingArgumentType<T> argument, WRITER packetWriter) {
		// REMOVE MY KEY FROM THE PACKET
		Object myInfo = getArgumentTypeInformation(argument);

		byte[] myKeyBytes = getSerializationKey(myInfo).getBytes(StandardCharsets.UTF_8);

		// Removing bytes for the key, assuming the length always takes up 1 byte
		packetWriter.writerIndex(packetWriter.writerIndex() - myKeyBytes.length - 1);

		// ADD BASE TYPE KEY INSTEAD
		ArgumentType<T> baseType = argument.baseType();
		Object baseInfo = getArgumentTypeInformation(baseType);
		byte[] baseKeyBytes = getSerializationKey(baseInfo).getBytes(StandardCharsets.UTF_8);

		// Assume length always takes up 1 byte
		packetWriter.writeByte(baseKeyBytes.length);
		packetWriter.writeBytes(baseKeyBytes);

		// SERIALIZE BASE TYPE
		serializeBaseTypeToNetwork(baseType, baseInfo, packetWriter);
	}

	protected void commonSerializeToJson(ExceptionHandlingArgumentType<T> argument, JsonObject properties) {
		ArgumentType<T> baseType = argument.baseType();

		Object baseInfo = getArgumentTypeInformation(baseType);

		properties.addProperty("baseType", getSerializationKey(baseInfo));

		JsonObject baseProperties = new JsonObject();
		serializeBaseTypeToJson(baseType, baseInfo, baseProperties);
		if (baseProperties.size() > 0) {
			properties.add("baseProperties", baseProperties);
		}
	}

	protected abstract Object getArgumentTypeInformation(ArgumentType<?> argumentType);

	protected abstract String getSerializationKey(Object info);

	protected abstract void serializeBaseTypeToNetwork(ArgumentType<T> baseType, Object baseInfo, WRITER packetWriter);

	protected abstract void serializeBaseTypeToJson(ArgumentType<T> baseType, Object baseInfo, JsonObject properties);
}
