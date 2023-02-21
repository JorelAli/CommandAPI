package dev.jorel.commandapi.nms;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.arguments.ExceptionHandlingArgumentType;
import dev.jorel.commandapi.preprocessor.Differs;
import net.minecraft.server.v1_16_R2.ArgumentRegistry;
import net.minecraft.server.v1_16_R2.ArgumentSerializer;
import net.minecraft.server.v1_16_R2.MinecraftKey;
import net.minecraft.server.v1_16_R2.PacketDataSerializer;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

@Differs(from = {"1.15", "1.16.2"}, by = "Renamed ArgumentRegistry#a(ArgumentType) to ArgumentRegistry#b(ArgumentType)")
public class ExceptionHandlingArgumentSerializer_1_16_R2<T> implements ArgumentSerializer<ExceptionHandlingArgumentType<T>> {
	private static final MethodHandle getArgumentTypeInformation;

	// Compute all var handles all in one go so we don't do this during main server runtime
	static {
		// We need a reference to the class object for ArgumentTypes.a, but that inner class is private
		// We can get an object from ArgumentTypes#get(ResourceLocation), then take its class
		Class<?> entryClass = null;
		try {
			Method getInfoByResourceLocation = ArgumentRegistry.class.getDeclaredMethod("a", MinecraftKey.class);
			getInfoByResourceLocation.setAccessible(true);
			Object entryObject = getInfoByResourceLocation.invoke(null, new MinecraftKey("entity"));
			entryClass = entryObject.getClass();
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}

		MethodHandle argumentRegistryGet = null;
		try {
			argumentRegistryGet = MethodHandles.privateLookupIn(ArgumentRegistry.class, MethodHandles.lookup())
				.findStatic(ArgumentRegistry.class, "b", MethodType.methodType(entryClass, ArgumentType.class));
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
		getArgumentTypeInformation = argumentRegistryGet;
	}

	@Override
	@Differs(from = {"1.13", "1.14", "1.15", "1.16.1"}, by = "ArgumentRegistry.a -> ArgumentRegistry.b")
	// serializeToNetwork
	public void a(ExceptionHandlingArgumentType<T> argument, PacketDataSerializer packetDataSerializer) {
		try {
			// Remove this key from packet
			Object myInfo = getArgumentTypeInformation.invoke(argument);

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
			Object baseInfo = getArgumentTypeInformation.invoke(baseType);
			String baseKey = keyField.get(baseInfo).toString();
			packetDataSerializer.a(baseKey);

			// Serialize baseType
			Field subSerializerField = CommandAPIHandler.getField(baseInfo.getClass(), "b");
			ArgumentSerializer<ArgumentType<T>> subSerializer = (ArgumentSerializer<ArgumentType<T>>) subSerializerField.get(baseInfo);
			subSerializer.a(baseType, packetDataSerializer);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	@Differs(from = {"1.13", "1.14", "1.15", "1.16.1"}, by = "ArgumentRegistry.a -> ArgumentRegistry.b")
	// serializeToJson
	public void a(ExceptionHandlingArgumentType<T> argument, JsonObject properties) {
		try {
			ArgumentType<T> baseType = argument.baseType();

			Object baseInfo = getArgumentTypeInformation.invoke(baseType);

			Field keyField = CommandAPIHandler.getField(baseInfo.getClass(), "c");
			properties.addProperty("baseType", keyField.get(baseInfo).toString());

			Field subSerializerField = CommandAPIHandler.getField(baseInfo.getClass(), "b");
			ArgumentSerializer<ArgumentType<T>> subSerializer = (ArgumentSerializer<ArgumentType<T>>) subSerializerField.get(baseInfo);

			JsonObject subProperties = new JsonObject();
			subSerializer.a(baseType, subProperties);
			if (subProperties.size() > 0) {
				properties.add("baseProperties", subProperties);
			}
		} catch (Throwable e) {
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
		throw new IllegalStateException("This shouldn't happen! See dev.jorel.commandapi.nms.ExceptionHandlingArgumentSerializer_1_16_R2#b for more information");
		// Including a mini-stacktrace here in case this exception shows up
		// on a client-disconnected screen, which is not very helpful
	}
}
