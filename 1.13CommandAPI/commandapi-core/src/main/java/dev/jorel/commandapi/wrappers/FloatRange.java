package dev.jorel.commandapi.wrappers;

public class FloatRange {

	private final float low;
	private final float high;
	
	public FloatRange(float low, float high) {
		this.low = low;
		this.high = high;
	}
	
	/**
	 * The lower bound of this range.
	 * @return the lower bound of this range
	 */
	public float getLowerBound() {
		return this.low;
	}
	
	/**
	 * The upper bound of this range.
	 * @return the upper bound of this range
	 */
	public float getUpperBound() {
		return this.high;
	}
	
	/**
	 * Determines if a float is within range of the lower bound (inclusive) and the upper bound (inclusive). 
	 * @param f the float to check within range
	 * @return true if the given float is within the declared range
	 */
	public boolean isInRange(float f) {
		return f >= low && f <= high;
	}
	
}
