package dev.jorel.commandapi.annotations.parser;

import java.io.PrintWriter;

public abstract class Emittable {
	
	public int indentation;
	
	/**
	 * Emits the current ADT.
	 * @param out the print writer to write to
	 */
	public abstract void emit(PrintWriter out);
	
	public String indentation() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < indentation; i++) {
			builder.append("    ");
		}
		return builder.toString();
	};
	
	public void indent() {
		indentation++;
	}
	
	public void dedent() {
		indentation--;
	}

}
