package dev.jorel.commandapi.arguments;

import java.util.Optional;
import java.util.function.Function;

import net.kyori.adventure.text.Component;

/**
 * An interface representing that the argument can be previewed using
 * Minecraft's chat preview feature. To use this, the server must have
 * {@code previews-chat=true} set in its {@code server.properties} file
 */
public interface IPreviewable<T extends Argument<?>> {

	public T withPreview(Function<PreviewInfo, Component> preview);
	
	public Optional<Function<PreviewInfo, Component>> getPreview();
	
}
