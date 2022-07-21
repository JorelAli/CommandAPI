package dev.jorel.commandapi.wrappers;

import dev.jorel.commandapi.arguments.PreviewInfo;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import net.kyori.adventure.text.Component;

@FunctionalInterface
public interface Preview {

	public Component generatePreview(PreviewInfo info) throws WrapperCommandSyntaxException;
	
}
