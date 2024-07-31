package dev.jorel.commandapi.nms;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.CommandAPIPaper;
import dev.jorel.commandapi.preprocessor.Overridden;
import dev.jorel.commandapi.preprocessor.Unimplemented;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component.Serializer;

import static dev.jorel.commandapi.preprocessor.Unimplemented.REASON.VERSION_SPECIFIC_IMPLEMENTATION;

public interface PaperNMS_Common extends CommandAPIPaper<CommandSourceStack> {
	@Override
	@Overridden(in = "1.20.5", because = "Serializer.toJson now needs a Provider")
	default Component getChat(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		return GsonComponentSerializer.gson().deserialize(Serializer.toJson(MessageArgument.getMessage(cmdCtx, key)));
	}

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, from = "ofExact", to = "namedColor", in = "NamedTextColor",
		introducedIn = "Adventure 4.10.0", info = "1.18 uses Adventure 4.9.3. 1.18.2 uses Adventure 4.11.0")
	NamedTextColor getChatColor(CommandContext<CommandSourceStack> cmdCtx, String key);

	@Override
	@Overridden(in = "1.20.5", because = "Serializer.toJson now needs a Provider")
	default Component getChatComponent(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return GsonComponentSerializer.gson().deserialize(Serializer.toJson(ComponentArgument.getComponent(cmdCtx, key)));
	}
}
