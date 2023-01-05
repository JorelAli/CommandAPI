package dev.jorel.commandapi.wrappers;

public class NBTPath {

	private final String path;
	
	public NBTPath(String path) {
		this.path = path;
	}
	
	@Override
	public String toString() {
		return path;
	}
	
}
