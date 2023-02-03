/*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi.wrappers;

import java.util.Objects;

/**
 * A class representing a range of integers
 */
public class IntegerRange {

	private final int low;
	private final int high;
	
	/**
	 * Constructs an IntegerRange with a lower bound and an upper bound
	 * @param low the lower bound of this range
	 * @param high the upper bound of this range
	 */
	public IntegerRange(int low, int high) {
		this.low = low;
		this.high = high;
	}
	
	/**
	 * Constructs an IntegerRange with a given lower bound and no upper bound
	 * @param min the lower bound of this range
	 * @return an IntegerRange min..
	 */
	public static IntegerRange integerRangeGreaterThanOrEq(int min) {
		return new IntegerRange(min, Integer.MAX_VALUE);
	}
	
	/**
	 * Constructs a IntegerRange with a given upper bound and no lower bound
	 * @param max the upper bound of this range
	 * @return a IntegerRange ..max
	 */
	public static IntegerRange integerRangeLessThanOrEq(int max) {
		return new IntegerRange(Integer.MIN_VALUE, max);
	}
	
	/**
	 * The lower bound of this range.
	 * @return the lower bound of this range
	 */
	public int getLowerBound() {
		return this.low;
	}
	
	/**
	 * The upper bound of this range.
	 * @return the upper bound of this range
	 */
	public int getUpperBound() {
		return this.high;
	}
	
	/**
	 * Determines if an int is within range of the lower bound (inclusive) and the upper bound (inclusive). 
	 * @param i the int to check within range
	 * @return true if the given int is within the declared range
	 */
	public boolean isInRange(int i) {
		return i >= low && i <= high;
	}
	
	/**
	 * Converts this IntegerRange to a Minecraft string for use with arguments
	 * @return a Minecraft string for use with arguments
	 */
	@Override
	public String toString() {
		if(this.high == Integer.MAX_VALUE) {
			return this.low + "..";
		} else if(this.low == Integer.MIN_VALUE) {
			return ".." + this.high;
		} else {
			return this.low + ".." + this.high;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(high, low);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof IntegerRange other)) {
			return false;
		}
		return high == other.high && low == other.low;
	}
	
}
