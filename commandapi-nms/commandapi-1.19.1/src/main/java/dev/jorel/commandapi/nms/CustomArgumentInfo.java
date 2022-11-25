package dev.jorel.commandapi.nms;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import dev.jorel.commandapi.arguments.ExceptionHandlingArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.nio.charset.Charset;

public class CustomArgumentInfo implements ArgumentTypeInfo<ExceptionHandlingArgumentType<?>, CustomArgumentInfo.CustomTemplate> {

	@Override
	public void serializeToNetwork(CustomTemplate template, FriendlyByteBuf friendlyByteBuf) {
		ArgumentType baseType = template.baseType;
		ArgumentTypeInfo baseInfo = ArgumentTypeInfos.byClass(baseType);
		String baseKey = Registry.COMMAND_ARGUMENT_TYPE.getKey(baseInfo).toString();
		friendlyByteBuf.writeInt(baseKey.length());
		friendlyByteBuf.writeCharSequence(baseKey, Charset.defaultCharset());
		baseInfo.serializeToNetwork(baseInfo.unpack(baseType), friendlyByteBuf);
	}

	@Override
	public CustomTemplate deserializeFromNetwork(FriendlyByteBuf friendlyByteBuf) {
		int keyLength = friendlyByteBuf.readInt();
		String baseKey = friendlyByteBuf.readCharSequence(keyLength, Charset.defaultCharset()).toString();
		ArgumentTypeInfo baseInfo = Registry.COMMAND_ARGUMENT_TYPE.get(ResourceLocation.of(baseKey, ':'));
		Template baseTemplate = baseInfo.deserializeFromNetwork(friendlyByteBuf);
		return new CustomTemplate(baseTemplate);
	}

	@Override
	public void serializeToJson(CustomTemplate template, JsonObject jsonObject) {
		jsonObject.addProperty("baseType", "TODO: Set this!");
	}

	@Override
	public CustomTemplate unpack(ExceptionHandlingArgumentType<?> exceptionHandlingArgumentType) {
		ArgumentType baseType = exceptionHandlingArgumentType.getBaseType();
		return new CustomTemplate(baseType);
	}

	public final class CustomTemplate implements ArgumentTypeInfo.Template<ExceptionHandlingArgumentType<?>> {
		final ArgumentType baseType;
		final Template baseTemplate;

		public CustomTemplate(ArgumentType<?> baseType) {
			this.baseType = baseType;
			baseTemplate = ArgumentTypeInfos.unpack(baseType);
		}

		public CustomTemplate(Template<?> baseTemplate) {
			this.baseTemplate = baseTemplate;
			baseType = null;
		}

		@Override
		public ExceptionHandlingArgumentType<?> instantiate(CommandBuildContext commandBuildContext) {
			return new ExceptionHandlingArgumentType<>(baseType == null ? baseTemplate.instantiate(commandBuildContext) : baseType);
		}

		@Override
		public ArgumentTypeInfo<ExceptionHandlingArgumentType<?>, ?> type() {
			return CustomArgumentInfo.this;
		}
	}
}
