package dev.jorel.commandapi.wrappers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

/**
 * A wrapper for the StringReader so other developers don't have to import Mojang's Brigadier
 */
public class WrapperStringReader {

    /**
     * The Brigadier StringReader this class wraps
     */
    private final StringReader stringReader;

    /**
     * Creates a WrapperStringReader
     *
     * @param stringReader the StringReader to wrap
     */
    public WrapperStringReader(StringReader stringReader) {
        this.stringReader = stringReader;
    }

    /**
     * Returns the wrapped StringReader
     *
     * @return the wrapped StringReader
     */
    public StringReader getStringReader() {
        return this.stringReader;
    }

    // Overrides - Pass everything to the wrapped StringReader
    // Also wrap CommandSyntaxExceptions in WrapperCommandSyntaxExceptions
    // Javadocs copied from https://github.com/JorelAli/brigadier-with-javadocs
    /**
     * Returns the full input string.
     *
     * @return the full input string
     */
    public String getString() {
        return stringReader.getString();
    }

    /**
     * Sets the cursor index position.
     *
     * @param cursor the new cursor position
     */
    public void setCursor(int cursor) {
        stringReader.setCursor(cursor);
    }

    /**
     * Returns the remaining length, so the distance from the cursor to the end of the input.
     *
     * @return the remaining length, so the distance from the cursor to the end of the input
     */
    public int getRemainingLength() {
        return stringReader.getRemainingLength();
    }

    /**
     * Returns the total length of the input.
     *
     * @return the total length of the input
     */
    public int getTotalLength() {
        return stringReader.getTotalLength();
    }

    /**
     * Returns the current cursor position.
     *
     * @return the current cursor position
     */
    public int getCursor() {
        return stringReader.getCursor();
    }

    /**
     * Returns the part of the input that was already read.
     *
     * @return the part of the input that was already read
     */
    public String getRead() {
        return stringReader.getRead();
    }

    /**
     * Returns the part of the input that was not yet read.
     *
     * @return the part of the input that was not yet read
     */
    public String getRemaining() {
        return stringReader.getRemaining();
    }

    /**
     * Checks if the reader has enough input to read {@code length} more characters.
     *
     * @param length the amount of characters to read
     * @return true if the reader has enough input to read {@code length} more characters
     */
    public boolean canRead(int length) {
        return stringReader.canRead(length);
    }

    /**
     * Checks if the reader can read at least one more character.
     *
     * @return true if the reader can read at least one more character
     * @see #canRead(int)
     */
    public boolean canRead() {
        return stringReader.canRead();
    }

    /**
     * Returns the next character but without consuming it (so the cursor stays at its current position).
     *
     * @return the next character
     * @see #peek(int)
     */
    public char peek() {
        return stringReader.peek();
    }

    /**
     * Returns the character {@code offset} places from the cursor position without consuming it
     * (so the cursor stays at its current position).
     *
     * @param offset the offset of the character to peek at
     * @return the character {@code offset} places from the current cursor position
     */
    public char peek(int offset) {
        return stringReader.peek(offset);
    }

    /**
     * Reads the next character.
     * <p>
     * Same as {@link #peek()}, but also consumes the character.
     *
     * @return the read character
     */
    public char read() {
        return stringReader.read();
    }

    /**
     * Skips a single character.
     */
    public void skip() {
        stringReader.skip();
    }

    /**
     * Checks if the character is allowed in a number.
     *
     * @param c the character to check
     * @return true if the character is allowed to appear in a number
     */
    public static boolean isAllowedNumber(char c) {
        return StringReader.isAllowedNumber(c);
    }

    /**
     * Checks if the character is a allowed as the start of a quoted string.
     *
     * <em>Currently</em> allowed characters are:<br>
     * {@code ["'"]} (as a regular expression set)
     *
     * @param c the character to check
     * @return true if the character is allowed as the start of a quoted string
     */
    public static boolean isQuotedStringStart(char c) {
        return StringReader.isQuotedStringStart(c);
    }

    /**
     * Skips all following whitespace characters as determined by {@link Character#isWhitespace(char)}.
     */
    public void skipWhitespace() {
        stringReader.skipWhitespace();
    }

    /**
     * Reads an integer.
     * <p>
     * The integer may only contain characters that match {@link #isAllowedNumber(char)}.
     *
     * @return the read integer
     * @throws WrapperCommandSyntaxException if the command is no proper int
     */
    public int readInt() throws WrapperCommandSyntaxException {
        try {
            return stringReader.readInt();
        } catch (CommandSyntaxException e) {
            throw new WrapperCommandSyntaxException(e);
        }
    }

    /**
     * Reads a long.
     * <p>
     * The long may only contain characters that match {@link #isAllowedNumber(char)}.
     *
     * @return the read long
     * @throws WrapperCommandSyntaxException if the command is no proper long
     */
    public long readLong() throws WrapperCommandSyntaxException {
        try {
            return stringReader.readLong();
        } catch (CommandSyntaxException e) {
            throw new WrapperCommandSyntaxException(e);
        }
    }

    /**
     * Reads a double.
     * <p>
     * The double may only contain characters that match {@link #isAllowedNumber(char)}.
     *
     * @return the read double
     * @throws WrapperCommandSyntaxException if the command is no proper double
     */
    public double readDouble() throws WrapperCommandSyntaxException {
        try {
            return stringReader.readDouble();
        } catch (CommandSyntaxException e) {
            throw new WrapperCommandSyntaxException(e);
        }
    }

    /**
     * Reads a float.
     * <p>
     * The float may only contain characters that match {@link #isAllowedNumber(char)}.
     *
     * @return the read float
     * @throws WrapperCommandSyntaxException if the command is no proper float
     */
    public float readFloat() throws WrapperCommandSyntaxException {
        try {
            return stringReader.readFloat();
        } catch (CommandSyntaxException e) {
            throw new WrapperCommandSyntaxException(e);
        }
    }

    /**
     * Checks if a character is allowed in an unquoted string or needs to be escaped.
     *
     * <em>Currently</em> allowed characters are:<br>
     * {@code [0-9A-Za-z_\-.+]} (as a regular expression set)
     *
     * @param c the character to check
     * @return true if the character is allowed to appear in an unquoted string
     */
    public static boolean isAllowedInUnquotedString(char c) {
        return StringReader.isAllowedInUnquotedString(c);
    }

    /**
     * Reads an unquoted string, {@literal i.e.} for as long as {@link #isAllowedInUnquotedString(char)} returns true.
     *
     * @return the read string
     */
    public String readUnquotedString() {
        return stringReader.readUnquotedString();
    }

    // @formatter:off
    /**
     * Returns a quoted string.
     *
     * The format of a quoted string is as follows:
     * <ul>
     *     <li>
     *         Starts and ends with {@code "} (quotation mark)
     *     </li>
     *     <li>
     *         Escape character {@code \} (backslash) has to be used for quotation marks and literal escape
     *         characters within the string
     *     </li>
     * </ul>
     * @return a quoted string or an empty string, if {@link #canRead} is false
     * @throws WrapperCommandSyntaxException if the next character is not a quote, an invalid escape character is encountered
     * or the closing quote was not found
     */
    // @formatter:on
    public String readQuotedString() throws WrapperCommandSyntaxException {
        try {
            return stringReader.readQuotedString();
        } catch (CommandSyntaxException e) {
            throw new WrapperCommandSyntaxException(e);
        }
    }

    // NOTE: This method does not have javadocs at https://github.com/JorelAli/brigadier-with-javadocs
    /**
     * Reads a String until the {@code terminator} character is found. Escape character {@code \} (backslash) has to be
     * used for the {@code terminator} and literal escape characters within the string.
     *
     * @param terminator The character that ends the String
     * @return the read string, which may be empty if the first character read was the {@code terminator}
     * @throws WrapperCommandSyntaxException if an invalid escape character is encountered or the closing {@code terminator}
     * character was not found.
     */
    public String readStringUntil(char terminator) throws WrapperCommandSyntaxException {
        try {
            return stringReader.readStringUntil(terminator);
        } catch (CommandSyntaxException e) {
            throw new WrapperCommandSyntaxException(e);
        }
    }

    /**
     * Reads a string, deciding between {@link #readQuotedString} and {@link #readUnquotedString} based on whether the
     * next character is a quote.
     *
     * @return the read string
     * @throws WrapperCommandSyntaxException if an error occurs parsing the string
     */
    public String readString() throws WrapperCommandSyntaxException {
        try {
            return stringReader.readString();
        } catch (CommandSyntaxException e) {
            throw new WrapperCommandSyntaxException(e);
        }
    }

    /**
     * Reads a single boolean.
     * <p>
     * A boolean can be either {@code true} or {@code false} and is case sensitive.
     *
     * @return the read boolean
     * @throws WrapperCommandSyntaxException if the input was empty or the read string is not a valid boolean
     */
    public boolean readBoolean() throws WrapperCommandSyntaxException {
        try {
            return stringReader.readBoolean();
        } catch (CommandSyntaxException e) {
            throw new WrapperCommandSyntaxException(e);
        }
    }

    /**
     * Peeks at the next char and consumes it, if it is the expected character{@literal .} If not, it throws an
     * exception.
     *
     * @param c the character that is expected to occur next
     * @throws WrapperCommandSyntaxException if the character was not the expected character or the end of the input was
     * reached
     */
    public void expect(char c) throws WrapperCommandSyntaxException {
        try {
            stringReader.expect(c);
        } catch (CommandSyntaxException e) {
            throw new WrapperCommandSyntaxException(e);
        }
    }

    // Comparison methods
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof WrapperStringReader wsr)) return false;
        return this.stringReader.equals(wsr.stringReader);
    }

    @Override
    public int hashCode() {
        return stringReader.hashCode();
    }

    @Override
    public String toString() {
        return stringReader.toString();
    }
}