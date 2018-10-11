package io.github.jorelali.commandapi.api.exceptions;

public class CantFindPlayerException extends RuntimeException {
	
	private String player;
	
	public CantFindPlayerException(String player) {
		this.player = player;
	}
	
	@Override
    public String getMessage() {
		return "Cannot find target player " + player + "";
    }
	
}
