package dev.jorel.commandapi.nms;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.arguments.ExceptionHandlingArgumentType;
import dev.jorel.commandapi.preprocessor.Differs;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.network.FriendlyByteBuf;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

public class ExceptionHandlingArgumentSerializer_1_18_R2<T> implements ArgumentSerializer<ExceptionHandlingArgumentType<T>> {
	private static final VarHandle ArgumentTypes_getInfo;

	// Compute all var handles all in one go so we don't do this during main server runtime
	static {
		VarHandle ar_b = null;
		try {
			ar_b = MethodHandles.privateLookupIn(ArgumentTypes.class, MethodHandles.lookup())
				.findVarHandle(ArgumentTypes.class, "b", ArgumentType.class);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
		ArgumentTypes_getInfo = ar_b;
	}

	@Override
	@Differs(from = {"1.13", "1.14", "1.15", "1.16", "1.17", "1.18", "1.18.1"},
		by = "ArgumentType.Entry.c -> ArgumentType.Entry.b, ArgumentType.Entry.b -> ArgumentType.Entry.a")
	public void serializeToNetwork(ExceptionHandlingArgumentType<T> argument, FriendlyByteBuf friendlyByteBuf) {
		try {
			// Remove this key from packet
			Object myInfo = ArgumentTypes_getInfo.get(argument);

			// TODO: This Field reflection (and others in this class) acts on the class ArgumentTypes.Entry. This inner
			//  class is private, and the @RequireField annotation doesn't currently support that. We would like
			//  to check this reflection at compile-time though, but the preprocess needs to be expanded first
			Field keyField = CommandAPIHandler.getField(myInfo.getClass(), "b");
			String myKey = keyField.get(myInfo).toString();
			byte[] myKeyBytes = myKey.getBytes(StandardCharsets.UTF_8);
			// Removing length and size of string, assuming length is always written as 1 byte
			friendlyByteBuf.writerIndex(friendlyByteBuf.writerIndex() - myKeyBytes.length - 1);

			// Add baseType key instead
			ArgumentType<T> baseType = argument.baseType();
			Object baseInfo = ArgumentTypes_getInfo.get(baseType);
			String baseKey = keyField.get(baseInfo).toString();
			friendlyByteBuf.writeUtf(baseKey);

			// Serialize baseType
			Field subSerializerField = CommandAPIHandler.getField(baseInfo.getClass(), "a");
			ArgumentSerializer<ArgumentType<T>> subSerializer = (ArgumentSerializer<ArgumentType<T>>) subSerializerField.get(baseInfo);
			subSerializer.serializeToNetwork(baseType, friendlyByteBuf);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	@Override
	@Differs(from = {"1.13", "1.14", "1.15", "1.16", "1.17", "1.18", "1.18.1"},
		by = "ArgumentType.Entry.c -> ArgumentType.Entry.b, ArgumentType.Entry.b -> ArgumentType.Entry.a")
	public void serializeToJson(ExceptionHandlingArgumentType<T> argument, JsonObject properties) {
		try {
			ArgumentType<T> baseType = argument.baseType();

			Object baseInfo = ArgumentTypes_getInfo.get(baseType);

			Field keyField = CommandAPIHandler.getField(baseInfo.getClass(), "b");
			properties.addProperty("baseType", keyField.get(baseInfo).toString());

			Field subSerializerField = CommandAPIHandler.getField(baseInfo.getClass(), "a");
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
		throw new IllegalStateException("This shouldn't happen! See dev.jorel.commandapi.nms.ExceptionHandlingArgumentSerializer_1_18_R2#deserializeFromNetwork for more information");
		// Including a mini-stacktrace here in case this exception shows up
		// on a client-disconnected screen, which is not very helpful
	}
}
