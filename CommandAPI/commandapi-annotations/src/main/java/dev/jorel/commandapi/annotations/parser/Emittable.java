package dev.jorel.commandapi.annotations.parser;

import java.io.PrintWriter;

import dev.jorel.commandapi.CommandPermission;

public interface Emittable {

	/**
	 * Emits the current ADT.
	 * @param out the print writer to write to
	 */
	public void emit(PrintWriter out, int currentIndentation);
	
	public default void emitPermission(PrintWriter out, CommandPermission permission) {
		if (permission.equals(CommandPermission.NONE)) {
			// Do nothing
		} else if (permission.equals(CommandPermission.OP)) {
			out.print(".withPermission(CommandPermission.OP)");
		} else {
			if (permission.isNegated()) {
				out.print(".withoutPermission(\"");
			} else {
				out.print(".withPermission(\"");
			}
			out.print(permission.getPermission());
			out.println("\")");
		}
	}

}
