package dev.jorel.commandapi.nms;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import dev.jorel.commandapi.arguments.ExceptionHandlingArgumentType;
import dev.jorel.commandapi.preprocessor.Differs;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;

@Differs(from = {"1.19", "1.19.1", "1.19.2"},
	by = "Registry.COMMAND_ARGUMENT_TYPE -> BuiltInRegistries.COMMAND_ARGUMENT_TYPE")
public class ExceptionHandlingArgumentInfo_1_19_4<T> implements ArgumentTypeInfo<ExceptionHandlingArgumentType<T>, ExceptionHandlingArgumentInfo_1_19_4<T>.Template> {
	@Override
	public void serializeToNetwork(Template template, FriendlyByteBuf friendlyByteBuf) {
		ArgumentType<T> baseType = template.baseType;
		ArgumentTypeInfo<ArgumentType<T>, ArgumentTypeInfo.Template<ArgumentType<T>>> baseInfo =
			(ArgumentTypeInfo<ArgumentType<T>, ArgumentTypeInfo.Template<ArgumentType<T>>>) ArgumentTypeInfos.byClass(baseType);

		// Overwrite my id with the base type's. Since there are less than
		// 128 argument types by default, assume index will always fill 1 byte.
		// If you get a garbage packet, check this assumption
		int baseId = BuiltInRegistries.COMMAND_ARGUMENT_TYPE.getId(baseInfo);
		friendlyByteBuf.writerIndex(friendlyByteBuf.writerIndex() - 1);
		friendlyByteBuf.writeVarInt(baseId);

		// Add the base type to the packet
		baseInfo.serializeToNetwork(baseInfo.unpack(baseType), friendlyByteBuf);
	}

	@Override
	public void serializeToJson(Template template, JsonObject properties) {
		ArgumentType<T> baseType = template.baseType;
		ArgumentTypeInfo<ArgumentType<T>, ArgumentTypeInfo.Template<ArgumentType<T>>> baseInfo =
			(ArgumentTypeInfo<ArgumentType<T>, ArgumentTypeInfo.Template<ArgumentType<T>>>) ArgumentTypeInfos.byClass(baseType);
		properties.addProperty("baseType", BuiltInRegistries.COMMAND_ARGUMENT_TYPE.getKey(baseInfo).toString());
		JsonObject subProperties = new JsonObject();
		baseInfo.serializeToJson(baseInfo.unpack(baseType), subProperties);
		if(subProperties.size() > 0) {
			properties.add("baseProperties", subProperties);
		}
	}

	@Override
	public Template unpack(ExceptionHandlingArgumentType<T> exceptionHandlingArgumentType) {
		ArgumentType<T> baseType = exceptionHandlingArgumentType.baseType();
		return new Template(baseType);
	}

	@Override
	public Template deserializeFromNetwork(FriendlyByteBuf friendlyByteBuf) {
		// Since this class overrides its COMMAND_ARGUMENT_TYPE id with the baseType's,
		// this class's id should never show up in a packet and this method should never
		// be called to deserialize the ArgumentType info that wasn't put into the packet
		// anyway. Also, the server shouldn't ever deserialize a *ClientBound*CommandPacket
		// either. If this method ever gets called, either you or I are doing something very wrong!
		throw new IllegalStateException("This shouldn't happen! See dev.jorel.commandapi.nms.ExceptionHandlingArgumentInfo_1_19_3#deserializeFromNetwork for more information");
		// Including a mini-stacktrace here in case this exception shows up
		// on a client-disconnected screen, which is not very helpful
	}

	public final class Template implements ArgumentTypeInfo.Template<ExceptionHandlingArgumentType<T>> {
		final ArgumentType<T> baseType;

		public Template(ArgumentType<T> baseType) {
			this.baseType = baseType;
		}

		@Override
		public ArgumentTypeInfo<ExceptionHandlingArgumentType<T>, ?> type() {
			return ExceptionHandlingArgumentInfo_1_19_4.this;
		}

		@Override
		public ExceptionHandlingArgumentType<T> instantiate(CommandBuildContext commandBuildContext) {
			// Same as ExceptionHandlingArgumentInfo_1_19_3#deserializeFromNetwork.
			// An ExceptionHandlingArgumentType should never be built from a packet,
			// so this method shouldn't be used
			throw new IllegalStateException("This shouldn't happen! See dev.jorel.commandapi.nms.ExceptionHandlingArgumentInfo_1_19_3.Template#instantiate for more information");
			// Including a mini-stacktrace here in case this exception shows up
			// on a client-disconnected screen, which is not very helpful
		}
	}
}
