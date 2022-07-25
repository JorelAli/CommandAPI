package dev.jorel.commandapi.wrappers;

import dev.jorel.commandapi.arguments.PreviewInfo;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

public interface PreviewableFunction<T> {

	public T generatePreview(PreviewInfo info) throws WrapperCommandSyntaxException;
	
}
