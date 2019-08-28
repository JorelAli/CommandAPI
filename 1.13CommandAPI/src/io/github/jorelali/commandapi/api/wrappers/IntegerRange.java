package io.github.jorelali.commandapi.api.wrappers;

public class IntegerRange {

	private final int low;
	private final int high;
	
	public IntegerRange(int low, int high) {
		this.low = low;
		this.high = high;
	}
	
	public int getLowerBound() {
		return this.low;
	}
	
	public int getUpperBound() {
		return this.high;
	}
	
	public boolean isInRange(int i) {
		return i >= low && i <= high;
	}
	
}
