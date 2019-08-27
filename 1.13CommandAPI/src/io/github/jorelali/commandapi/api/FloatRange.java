package io.github.jorelali.commandapi.api;

public class FloatRange {

	private float low;
	private float high;
	
	public FloatRange(float low, float high) {
		this.low = low;
		this.high = high;
	}
	
	public float getLowerBound() {
		return this.low;
	}
	
	public float getUpperBound() {
		return this.high;
	}
	
	public boolean isInRange(float i) {
		return i >= low && i <= high;
	}
	
}
