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

import java.util.function.BinaryOperator;

/**
 * A representation of the math operations for the Minecraft scoreboard
 */
@SuppressWarnings("null")
public enum MathOperation {

	/**
	 * Addition of two values (+=)
	 */
	ADD("+=", (val1, val2) -> val1 + val2),

	/**
	 * Assignment of a value (=)
	 */
	ASSIGN("=", (val1, val2) -> val2),

	/**
	 * Division of a value by another value (/=)
	 */
	DIVIDE("/=", (val1, val2) -> val1 / val2),

	/**
	 * The maximum value of two values (&gt;)
	 */
	MAX(">", Math::max),

	/**
	 * The minimum value of two values (&lt;)
	 */
	MIN("<", Math::min),

	/**
	 * Modulo of a value by another value (%=)
	 */
	MOD("%=", (val1, val2) -> val1 % val2),

	/**
	 * Multiplication of a value by another value (*=)
	 */
	MULTIPLY("*=", (val1, val2) -> val1 * val2),

	/**
	 * Subtraction of a value by another value (-=)
	 */
	SUBTRACT("-=", (val1, val2) -> val1 - val2),

	/**
	 * Swap the results of two values (&gt;&lt;)
	 */
	SWAP("><", (val1, val2) -> val2);

	private String stringValue;
	private BinaryOperator<Float> application;

	/**
	 * Construct a MathOperation with its respective Minecraft string value
	 * 
	 * @param stringValue
	 */
	MathOperation(String stringValue, BinaryOperator<Float> application) {
		this.stringValue = stringValue;
		this.application = application;
	}

	/**
	 * Returns the Minecraft string value of this MathOperation
	 * 
	 * @return the Minecraft string value of this MathOperation
	 */
	@Override
	public String toString() {
		return this.stringValue;
	}

	/**
	 * Creates a MathOperation from the Minecraft string representation (e.g. "=" or
	 * "/=")
	 * 
	 * @param input the string to convert to
	 * @return a MathOperation instance which represents the provided string
	 * @throws IllegalArgumentException if the input is not a valid MathOperation
	 */
	public static MathOperation fromString(String input) {
		for (MathOperation mathOp : MathOperation.values()) {
			if (mathOp.stringValue.equals(input)) {
				return mathOp;
			}
		}
		throw new IllegalArgumentException(input + " is not a valid MathOperation");
	}

	/**
	 * Applies the current MathOperation to two ints
	 * 
	 * @param val1 the base int to operate on
	 * @param val2 the new value to operate with
	 * @return a int that is the result of applying this math operation
	 */
	public int apply(int val1, int val2) {
		return (int) apply((float) val1, (float) val2);
	}

	/**
	 * Applies the current MathOperation to two floats
	 * 
	 * @param val1 the base float to operate on
	 * @param val2 the new value to operate with
	 * @return a float that is the result of applying this math operation
	 */
	public float apply(float val1, float val2) {
		return application.apply(val1, val2);
	}
}