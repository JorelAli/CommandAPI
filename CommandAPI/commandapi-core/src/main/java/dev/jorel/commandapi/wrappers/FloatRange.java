package dev.jorel.commandapi.wrappers;

/**
 * A class representing a range of floats
 */
public class FloatRange {

	private final float low;
	private final float high;
	
	/**
	 * Constructs a FloatRange with a lower bound and an upper bound
	 * @param low the lower bound of this range
	 * @param high the upper bound of this range
	 */
	public FloatRange(float low, float high) {
		this.low = low;
		this.high = high;
	}
	
	/**
	 * Constructs a FloatRange with a given lower bound and no upper bound
	 * @param min the lower bound of this range
	 * @return a FloatRange min..
	 */
	public static FloatRange floatRangeGreaterThanOrEq(float min) {
		return new FloatRange(min, Float.MAX_VALUE);
	}
	
	/**
	 * Constructs a FloatRange with a given upper bound and no lower bound
	 * @param max the upper bound of this range
	 * @return a FloatRange ..max
	 */
	public static FloatRange floatRangeLessThanOrEq(float max) {
		return new FloatRange(-Float.MAX_VALUE, max);
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
	
	/**
	 * Converts this FloatRange to a Minecraft string for use with arguments
	 * @return a Minecraft string for use with arguments
	 */
	@Override
	public String toString() {
		if(this.high == Float.MAX_VALUE) {
			return this.low + "..";
		} else if(this.low == -Float.MAX_VALUE) {
			return ".." + this.high;
		} else {
			return this.low + ".." + this.high;
		}
	}
	
}
