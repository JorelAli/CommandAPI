package dev.jorel.commandapi.annotations.parser;

public abstract class CommandElement implements Emittable {
	
	public int indentation;
	
	public String indentation() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < indentation; i++) {
			builder.append("    ");
		}
		return builder.toString();
	};
	
	public void indent() {
		indentation++;
	}
	
	public void dedent() {
		indentation--;
		if(indentation < 0) {
			indentation = 0;
		}
	}

}
