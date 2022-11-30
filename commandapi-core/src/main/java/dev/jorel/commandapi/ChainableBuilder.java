package dev.jorel.commandapi;

/**
 *
 * @param <Impl> The class extending this class, used as the return type for chain calls
 */
public interface ChainableBuilder<Impl> {
	/**
	 * Returns the instance of this class with the class Impl. Used for chaining builder methods.
	 */
	Impl instance();
}
