package dev.jorel.commandapi.arguments;

import java.util.Optional;

import dev.jorel.commandapi.wrappers.Preview;

/**
 * An interface representing that the argument can be previewed using
 * Minecraft's chat preview feature. To use this, the server must have
 * {@code previews-chat=true} set in its {@code server.properties} file
 */
public interface IPreviewable<T extends Argument<?>> {

	public T withPreview(Preview preview);
	
	public Optional<Preview> getPreview();
	
}
