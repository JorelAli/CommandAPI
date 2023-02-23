package dev.jorel.commandapi.nms;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import dev.jorel.commandapi.arguments.ExceptionHandlingArgumentType;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class ExceptionHandlingArgumentSerializer_1_18_R1<T> implements ArgumentSerializer<ExceptionHandlingArgumentType<T>> {
	// All the ? here should actually be ArgumentTypes.Entry, but that is a private inner class. That makes everything really annoying.
	// TODO: We want to check this reflection, but we can't give ArgumentTypes.Entry to the @RequireField annotation
	//  Hopefully something works out, but the preprocessor needs to be expanded first
	private static final NMS.SafeStaticOneParameterMethodHandle<?, ArgumentType> getArgumentTypeInformation;
	private static final NMS.SafeVarHandle<?, ResourceLocation> serializationKey;
	private static final NMS.SafeVarHandle<?, ArgumentSerializer> serializer;

	// Compute all var handles all in one go so we don't do this during main server runtime
	static {
		// We need a reference to the class object for ArgumentTypes.Entry,
		// We can get an object from ArgumentTypes#get(ResourceLocation), then take its class
		Class<?> entryClass = null;
		try {
			Method getInfoByResourceLocation = ArgumentTypes.class.getDeclaredMethod("a", ResourceLocation.class);
			getInfoByResourceLocation.setAccessible(true);
			Object entryObject = getInfoByResourceLocation.invoke(null, new ResourceLocation("entity"));
			entryClass = entryObject.getClass();
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}

		getArgumentTypeInformation = NMS.SafeStaticOneParameterMethodHandle.ofOrNull(ArgumentType.class, "b", entryClass, ArgumentType.class);
		serializationKey = NMS.SafeVarHandle.ofOrNull(entryClass, "c", ResourceLocation.class);
		serializer = NMS.SafeVarHandle.ofOrNull(entryClass, "b", ArgumentSerializer.class);
	}

	@Override
	public void serializeToNetwork(ExceptionHandlingArgumentType<T> argument, FriendlyByteBuf friendlyByteBuf) {
		// Remove this key from packet
		Object myInfo = getArgumentTypeInformation.invokeOrNull(argument);

		byte[] myKeyBytes = serializationKey.getUnknownInstanceType(myInfo).toString().getBytes(StandardCharsets.UTF_8);
		// Removing length and size of string, assuming length is always written as 1 byte
		friendlyByteBuf.writerIndex(friendlyByteBuf.writerIndex() - myKeyBytes.length - 1);

		// Add baseType key instead
		ArgumentType<T> baseType = argument.baseType();
		Object baseInfo = getArgumentTypeInformation.invokeOrNull(baseType);
		String baseKey = serializationKey.getUnknownInstanceType(baseInfo).toString();
		friendlyByteBuf.writeUtf(baseKey);

		// Serialize baseType
		ArgumentSerializer<ArgumentType<T>> baseSerializer = (ArgumentSerializer<ArgumentType<T>>) serializer.getUnknownInstanceType(baseInfo);
		baseSerializer.serializeToNetwork(baseType, friendlyByteBuf);
	}

	@Override
	public void serializeToJson(ExceptionHandlingArgumentType<T> argument, JsonObject properties) {
		ArgumentType<T> baseType = argument.baseType();

		Object baseInfo = getArgumentTypeInformation.invokeOrNull(baseType);

		properties.addProperty("baseType", serializationKey.getUnknownInstanceType(baseInfo).toString());

		ArgumentSerializer<ArgumentType<T>> baseSerializer = (ArgumentSerializer<ArgumentType<T>>) serializer.getUnknownInstanceType(baseInfo);

		JsonObject baseProperties = new JsonObject();
		baseSerializer.serializeToJson(baseType, baseProperties);
		if (baseProperties.size() > 0) {
			properties.add("baseProperties", baseProperties);
		}
	}

	@Override
	public ExceptionHandlingArgumentType<T> deserializeFromNetwork(FriendlyByteBuf friendlyByteBuf) {
		// Since this class overrides its ArgumentRegistry key with the baseType's,
		// this class's key should never show up in a packet and this method should never
		// be called to deserialize the ArgumentType info that wasn't put into the packet
		// anyway. Also, the server shouldn't ever deserialize a PacketPlay*Out*Commands
		// either. If this method ever gets called, either you or I are doing something very wrong!
		throw new IllegalStateException("This shouldn't happen! See dev.jorel.commandapi.nms.ExceptionHandlingArgumentSerializer_1_18_R1#deserializeFromNetwork for more information");
		// Including a mini-stacktrace here in case this exception shows up
		// on a client-disconnected screen, which is not very helpful
	}
}
