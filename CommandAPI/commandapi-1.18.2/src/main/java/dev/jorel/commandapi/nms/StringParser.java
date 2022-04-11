package dev.jorel.commandapi.nms;

import java.io.Serializable;

import com.mojang.brigadier.StringReader;

public interface StringParser extends Serializable {

	public String parse(StringReader reader);
	
}
