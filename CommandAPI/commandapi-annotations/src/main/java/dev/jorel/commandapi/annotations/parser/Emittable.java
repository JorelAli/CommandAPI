package dev.jorel.commandapi.annotations.parser;

import java.io.PrintWriter;

public interface Emittable {

	/**
	 * Emits the current ADT.
	 * @param out the print writer to write to
	 */
	public void emit(PrintWriter out);

}
