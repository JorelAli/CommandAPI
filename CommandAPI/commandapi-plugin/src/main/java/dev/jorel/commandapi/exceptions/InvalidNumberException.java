package dev.jorel.commandapi.exceptions;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * An exception caused when a number is not a number
 */
@SuppressWarnings("serial")
public class InvalidNumberException extends Exception {
	
	public InvalidNumberException(String input, String command, int index) {
		super(format(input, command, index));
	}
	
	private static String format(String input, String command, int index) {
		String[] parts = command.split(" ");
		for(int i = 0; i < parts.length; i++) {
			if(i == index) {
				parts[i] = parts[i] + "<--[HERE]";
			}
		}
		return "Invalid number found in command '" + 
			Arrays.stream(parts).collect(Collectors.joining(" ")) + 
			"': '" + input + "' is not a valid number!";
	}
	
}
