package dev.jorel.commandapi.nms;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import dev.jorel.commandapi.SafeStaticOneParameterMethodHandle;
import dev.jorel.commandapi.SafeVarHandle;
import dev.jorel.commandapi.arguments.ExceptionHandlingArgumentType;
import dev.jorel.commandapi.preprocessor.Differs;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Method;

@Differs(from = {"1.15", "1.16", "1.17", "1.18", "1.18.1"},
	by = "ArgumentTypes.Entry.c -> ArgumentTypes.Entry.b, ArgumentTypes.Entry.b -> ArgumentTypes.Entry.a")
public class ExceptionHandlingArgumentSerializer_1_18_R2<T> extends ExceptionHandlingArgumentSerializer_Common<T, FriendlyByteBuf> implements ArgumentSerializer<ExceptionHandlingArgumentType<T>> {
	// All the ? here should actually be ArgumentTypes.Entry, but that is a private inner class. That makes everything really annoying.
	// TODO: We want to check this reflection, but we can't give ArgumentTypes.Entry to the @RequireField annotation
	//  Hopefully something works out, but the preprocessor needs to be expanded first
	private static final SafeStaticOneParameterMethodHandle<?, ArgumentType> getArgumentTypeInformation;
	private static final SafeVarHandle<?, ResourceLocation> serializationKey;
	private static final SafeVarHandle<?, ArgumentSerializer> serializer;

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

		getArgumentTypeInformation = SafeStaticOneParameterMethodHandle.ofOrNull(ArgumentTypes.class, "b", "get", entryClass, ArgumentType.class);
		serializationKey = SafeVarHandle.ofOrNull(entryClass, "b", "name", ResourceLocation.class);
		serializer = SafeVarHandle.ofOrNull(entryClass, "a", "serializer", ArgumentSerializer.class);
	}

	// Serializer_Common methods
	@Override
	protected Object getArgumentTypeInformation(ArgumentType<?> argumentType) {
		return getArgumentTypeInformation.invokeOrNull(argumentType);
	}

	@Override
	protected String getSerializationKey(Object info) {
		return serializationKey.getUnknownInstanceType(info).toString();
	}

	@Override
	protected void serializeBaseTypeToNetwork(ArgumentType<T> baseType, Object baseInfo, FriendlyByteBuf packetWriter) {
		serializer.getUnknownInstanceType(baseInfo).serializeToNetwork(baseType, packetWriter);
	}

	@Override
	protected void serializeBaseTypeToJson(ArgumentType<T> baseType, Object baseInfo, JsonObject properties) {
		serializer.getUnknownInstanceType(baseInfo).serializeToJson(baseType, properties);
	}

	// ArgumentSerializer methods
	@Override
	public void serializeToNetwork(ExceptionHandlingArgumentType<T> argument, FriendlyByteBuf friendlyByteBuf) {
		commonSerializeToNetwork(argument, friendlyByteBuf);
	}

	@Override
	public void serializeToJson(ExceptionHandlingArgumentType<T> argument, JsonObject properties) {
		commonSerializeToJson(argument, properties);
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
