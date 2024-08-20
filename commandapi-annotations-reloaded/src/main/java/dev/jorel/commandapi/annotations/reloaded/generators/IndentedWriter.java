/*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi.annotations.reloaded.generators;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.function.Consumer;

/**
 * A writer for outputting indented lines of code
 */
public class IndentedWriter {
	/**
	 * The underlying writer to output to
	 */
	private final Writer out;
	/**
	 * The prefix to indent each line with
	 */
	private final String prefix;

	/**
	 * @see #IndentedWriter(Writer, String)
	 */
	public IndentedWriter(Writer out) {
		this(out, "");
	}

	/**
	 * @param out    The underlying writer that should be output to
	 * @param prefix The initial prefix to indent this code with
	 */
	public IndentedWriter(Writer out, String prefix) {
		this.out = out;
		this.prefix = prefix;
	}

	/**
	 * Writes directly to the underlying writer. Any {@link IOException} are rethrown unchecked. It is assumed that
	 * compilation should halt if this occurs.
	 *
	 * @param text Text to output to the underlying writer
	 */
	private void uncheckedWrite(String text) {
		try {
			out.write(text);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Writes a single new-line character to the output. This is intended for creating empty, non-indented lines in the
	 * generated code.
	 */
	public void printEmptyLine() {
		uncheckedWrite("\n");
	}

	/**
	 * Writes a new-line character, followed by the current prefix. This is intended for starting a new indented line
	 * in the generated code.
	 */
	public void startNewLine() {
		uncheckedWrite("\n%s".formatted(prefix));
	}

	/**
	 * Writes a new-line character, followed by the current prefix, then the given text. This is intended for writing
	 * a whole line in the generated code.
	 *
	 * @param line The text to write on a new line
	 */
	public void printOnNewLine(String line) {
		startNewLine();
		uncheckedWrite(line);
	}

	/**
	 * Writes the given text directly to the output. This is intended for apprending text to the end of the current
	 * line of code.
	 *
	 * @param text The text to append
	 */
	public void appendInLine(String text) {
		uncheckedWrite(text);
	}

	/**
	 * Write output to a buffer rather than the output writer. This is intended for situations where you need to do
	 * something with the text before it gets written to the output.
	 *
	 * @param withWriter Provides a new writer that outputs to a buffer rather than the output writer
	 * @return The buffered text
	 */
	public String buffer(Consumer<IndentedWriter> withWriter) {
		var buffer = new StringWriter();
		withWriter.accept(new IndentedWriter(buffer, prefix));
		return buffer.toString();
	}

	/**
	 * Write something with increased indentation.
	 *
	 * @param withWriter Provides a new writer with the indentation increased by one tab character
	 */
	public void indent(Consumer<IndentedWriter> withWriter) {
		withWriter.accept(new IndentedWriter(out, prefix + "\t"));
	}

	/**
	 * This is a combination of both {@link #buffer(Consumer)} and {@link #indent(Consumer)} at the same time.
	 *
	 * @param withWriter Provides a new writer that outputs to a buffer and increments the prefix by one tab
	 * @return The buffered text
	 * @see #buffer(Consumer)
	 * @see #indent(Consumer)
	 */
	public String indentToBuffer(Consumer<IndentedWriter> withWriter) {
		var buffer = new StringWriter();
		withWriter.accept(new IndentedWriter(buffer, prefix + "\t"));
		return buffer.toString();
	}
}
