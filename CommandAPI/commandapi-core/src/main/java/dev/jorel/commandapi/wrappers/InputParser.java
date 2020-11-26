package dev.jorel.commandapi.wrappers;

import dev.jorel.commandapi.exceptions.ParseException;

@FunctionalInterface
public interface InputParser {

	public void parse(String input, StringRangeWrapper range) throws ParseException;
	
}
