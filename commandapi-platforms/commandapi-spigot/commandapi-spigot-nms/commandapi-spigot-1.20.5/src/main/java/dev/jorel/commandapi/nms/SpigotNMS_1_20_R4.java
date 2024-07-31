package dev.jorel.commandapi.nms;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.preprocessor.Differs;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component.Serializer;

public class SpigotNMS_1_20_R4 extends NMS_1_20_R4 implements SpigotNMS_Common {
	@Override
	@Differs(from = "1.20.4 and earlier", by = "Serializer.toJson now needs a Provider")
	public BaseComponent[] getChat(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		// TODO: Figure out if an empty provider is suitable here and in `getChatComponent`
		return ComponentSerializer.parse(Serializer.toJson(MessageArgument.getMessage(cmdCtx, key), COMMAND_BUILD_CONTEXT));
	}

	@Override
	@Differs(from = "1.20.4 and earlier", by = "Serializer.toJson now needs a Provider")
	public BaseComponent[] getChatComponent(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return ComponentSerializer.parse(Serializer.toJson(ComponentArgument.getComponent(cmdCtx, key), COMMAND_BUILD_CONTEXT));
	}
}
