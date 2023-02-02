package dev.jorel.commandapi.nms;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.arguments.ExceptionHandlingArgumentType;
import net.minecraft.server.v1_16_R1.ArgumentRegistry;
import net.minecraft.server.v1_16_R1.ArgumentSerializer;
import net.minecraft.server.v1_16_R1.PacketDataSerializer;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

public class ExceptionHandlingArgumentSerializer_1_16_R1<T> implements ArgumentSerializer<ExceptionHandlingArgumentType<T>> {
	private static final VarHandle ArgumentRegistry_getInfo;

	// Compute all var handles all in one go so we don't do this during main server runtime
	static {
		VarHandle ar_a = null;
		try {
			ar_a = MethodHandles.privateLookupIn(ArgumentRegistry.class, MethodHandles.lookup())
				.findVarHandle(ArgumentRegistry.class, "a", ArgumentType.class);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
		ArgumentRegistry_getInfo = ar_a;
	}

	@Override
	// serializeToNetwork
	public void a(ExceptionHandlingArgumentType<T> argument, PacketDataSerializer packetDataSerializer) {
		try {
			Object myInfo = ArgumentRegistry_getInfo.get(argument);

			// TODO: This Field reflection (and others in this class) acts on the class ArgumentRegistry.a. This inner
			//  class is package-private, and the @RequireField annotation doesn't currently support that. We would like
			//  to check this reflection at compile-time though, but the preprocess needs to be expanded first
			Field keyField = CommandAPIHandler.getField(myInfo.getClass(), "c");
			String myKey = keyField.get(myInfo).toString();
			byte[] myKeyBytes = myKey.getBytes(StandardCharsets.UTF_8);
			// Removing length and size of string, assuming length is always written as 1 byte
			packetDataSerializer.writerIndex(packetDataSerializer.writerIndex() - myKeyBytes.length - 1);

			// Add baseType key instead
			ArgumentType<T> baseType = argument.baseType();
			Object baseInfo = ArgumentRegistry_getInfo.get(baseType);
			String baseKey = keyField.get(baseInfo).toString();
			packetDataSerializer.a(baseKey);

			// Serialize baseType
			Field subSerializerField = CommandAPIHandler.getField(baseInfo.getClass(), "b");
			ArgumentSerializer<ArgumentType<T>> subSerializer = (ArgumentSerializer<ArgumentType<T>>) subSerializerField.get(baseInfo);
			subSerializer.a(baseType, packetDataSerializer);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	@Override
	// serializeToJson
	public void a(ExceptionHandlingArgumentType<T> argument, JsonObject properties) {
		try {
			ArgumentType<T> baseType = argument.baseType();

			Object baseInfo = ArgumentRegistry_getInfo.get(baseType);

			Field keyField = CommandAPIHandler.getField(baseInfo.getClass(), "c");
			properties.addProperty("baseType", keyField.get(baseInfo).toString());

			Field subSerializerField = CommandAPIHandler.getField(baseInfo.getClass(), "b");
			ArgumentSerializer<ArgumentType<T>> subSerializer = (ArgumentSerializer<ArgumentType<T>>) subSerializerField.get(baseInfo);

			JsonObject subProperties = new JsonObject();
			subSerializer.a(baseType, subProperties);
			if (subProperties.size() > 0) {
				properties.add("baseProperties", subProperties);
			}
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	@Override
	// deserializeFromNetwork
	public ExceptionHandlingArgumentType<T> b(PacketDataSerializer packetDataSerializer) {
		// Since this class overrides its ArgumentRegistry key with the baseType's,
		// this class's key should never show up in a packet and this method should never
		// be called to deserialize the ArgumentType info that wasn't put into the packet
		// anyway. Also, the server shouldn't ever deserialize a PacketPlay*Out*Commands
		// either. If this method ever gets called, either you or I are doing something very wrong!
		throw new IllegalStateException("This shouldn't happen! See dev.jorel.commandapi.nms.ExceptionHandlingArgumentSerializer_1_16_R1#b for more information");
		// Including a mini-stacktrace here in case this exception shows up
		// on a client-disconnected screen, which is not very helpful
	}
}
