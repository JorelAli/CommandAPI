package dev.jorel.commandapi;

/**
 * An exception thrown when a method is called that hasn't been implemented yet.
 */
// This is the first way I thought to highlight where methods are not implemented
//  Though all the warnings may be a little unnecessary ¯\_(ツ)_/¯
@Deprecated(since = "TODO: Implement this method")
public class UnimplementedMethodException extends RuntimeException {
	public UnimplementedMethodException() {
		super("This method has not been implemented - CommandAPI");
	}

	public UnimplementedMethodException(String reason) {
		super(reason + " ==> This method has not been implemented - CommandAPI");
	}
}
