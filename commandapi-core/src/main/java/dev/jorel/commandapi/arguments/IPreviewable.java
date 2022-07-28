package dev.jorel.commandapi.arguments;

import java.util.Optional;

import dev.jorel.commandapi.wrappers.PreviewableFunction;

/**
 * An interface representing that the argument can be previewed using
 * Minecraft's chat preview feature. To use this, the server must have
 * {@code previews-chat=true} set in its {@code server.properties} file
 */
public interface IPreviewable<T extends Argument<?>, A> {

	public T withPreview(PreviewableFunction<A> preview);
	
	public Optional<PreviewableFunction<A>> getPreview();
	
	public boolean isLegacy();
	
}
