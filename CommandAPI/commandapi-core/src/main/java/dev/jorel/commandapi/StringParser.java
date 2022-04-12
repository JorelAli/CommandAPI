package dev.jorel.commandapi;

import java.io.Serializable;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public interface StringParser extends Serializable {

	public String parse(StringReader reader) throws CommandSyntaxException;
	
}
