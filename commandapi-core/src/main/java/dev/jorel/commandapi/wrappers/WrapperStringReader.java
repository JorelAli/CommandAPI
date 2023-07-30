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
    public StringReader getException() {
        return this.stringReader;
    }

    // Overrides - Pass everything to the wrapped StringReader
    // Also wrap CommandSyntaxExceptions in WrapperCommandSyntaxExceptions
    public String getString() {
        return stringReader.getString();
    }

    public void setCursor(int cursor) {
        stringReader.setCursor(cursor);
    }

    public int getRemainingLength() {
        return stringReader.getRemainingLength();
    }

    public int getTotalLength() {
        return stringReader.getTotalLength();
    }

    public int getCursor() {
        return stringReader.getCursor();
    }

    public String getRead() {
        return stringReader.getRead();
    }

    public String getRemaining() {
        return stringReader.getRemaining();
    }

    public boolean canRead(int length) {
        return stringReader.canRead(length);
    }

    public boolean canRead() {
        return stringReader.canRead();
    }

    public char peek() {
        return stringReader.peek();
    }

    public char peek(int offset) {
        return stringReader.peek(offset);
    }

    public char read() {
        return stringReader.read();
    }

    public void skip() {
        stringReader.skip();
    }

    public static boolean isAllowedNumber(char c) {
        return StringReader.isAllowedNumber(c);
    }

    public static boolean isQuotedStringStart(char c) {
        return StringReader.isQuotedStringStart(c);
    }

    public void skipWhitespace() {
        stringReader.skipWhitespace();
    }

    public int readInt() throws WrapperCommandSyntaxException {
        try {
            return stringReader.readInt();
        } catch (CommandSyntaxException e) {
            throw new WrapperCommandSyntaxException(e);
        }
    }

    public long readLong() throws WrapperCommandSyntaxException {
        try {
            return stringReader.readLong();
        } catch (CommandSyntaxException e) {
            throw new WrapperCommandSyntaxException(e);
        }
    }

    public double readDouble() throws WrapperCommandSyntaxException {
        try {
            return stringReader.readDouble();
        } catch (CommandSyntaxException e) {
            throw new WrapperCommandSyntaxException(e);
        }
    }

    public float readFloat() throws WrapperCommandSyntaxException {
        try {
            return stringReader.readFloat();
        } catch (CommandSyntaxException e) {
            throw new WrapperCommandSyntaxException(e);
        }
    }

    public static boolean isAllowedInUnquotedString(char c) {
        return StringReader.isAllowedInUnquotedString(c);
    }

    public String readUnquotedString() {
        return stringReader.readUnquotedString();
    }

    public String readQuotedString() throws WrapperCommandSyntaxException {
        try {
            return stringReader.readQuotedString();
        } catch (CommandSyntaxException e) {
            throw new WrapperCommandSyntaxException(e);
        }
    }

    public String readStringUntil(char terminator) throws WrapperCommandSyntaxException {
        try {
            return stringReader.readStringUntil(terminator);
        } catch (CommandSyntaxException e) {
            throw new WrapperCommandSyntaxException(e);
        }
    }

    public String readString() throws WrapperCommandSyntaxException {
        try {
            return stringReader.readString();
        } catch (CommandSyntaxException e) {
            throw new WrapperCommandSyntaxException(e);
        }
    }

    public boolean readBoolean() throws WrapperCommandSyntaxException {
        try {
            return stringReader.readBoolean();
        } catch (CommandSyntaxException e) {
            throw new WrapperCommandSyntaxException(e);
        }
    }

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