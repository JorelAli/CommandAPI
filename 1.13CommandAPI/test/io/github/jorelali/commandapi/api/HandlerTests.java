package io.github.jorelali.commandapi.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.github.jorelali.commandapi.api.CommandAPIHandler.Version;


public class HandlerTests {

	@Test
	public void testVersion() {
		assertEquals(new Version("v1_13_R2"), new Version(13, 2));
	}
	
}
