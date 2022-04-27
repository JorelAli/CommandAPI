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

/**
 * A representation of the math operations for the Minecraft scoreboard
 */
public enum MathOperation {
	
	/**
	 * Addition of two values (+=)
	 */
	ADD("+="), 
	
	/**
	 * Assignment of a value (=)
	 */
	ASSIGN("="), 
	
	/**
	 * Division of a value by another value (/=)
	 */
	DIVIDE("/="), 
	
	/**
	 * The maximum value of two values (&gt;)
	 */
	MAX(">"), 
	
	/**
	 * The minimum value of two values (&lt;)
	 */
	MIN("<"), 
	
	/**
	 * Modulo of a value by another value (%=)
	 */
	MOD("%="), 
	
	/**
	 * Multiplication of a value by another value (*=) 
	 */
	MULTIPLY("*="), 
	
	/**
	 * Subtraction of a value by another value (-=)
	 */
	SUBTRACT("-="), 
	
	/**
	 * Swap the results of two values (&gt;&lt;)
	 */
	SWAP("><");
	
	private String stringValue;
	
	/**
	 * Construct a MathOperation with its respective Minecraft string value
	 * @param stringValue
	 */
	MathOperation(String stringValue) {
		this.stringValue = stringValue;
	}
	
	/**
	 * Returns the Minecraft string value of this MathOperation
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
	 */
	public static MathOperation fromString(String input) {
		return switch(input) {
			case "="  -> MathOperation.ASSIGN;
			case "+=" -> MathOperation.ADD;
			case "-=" -> MathOperation.SUBTRACT;
			case "*=" -> MathOperation.MULTIPLY;
			case "/=" -> MathOperation.DIVIDE;
			case "%=" -> MathOperation.MOD;
			case "<"  -> MathOperation.MIN;
			case ">"  -> MathOperation.MAX;
			case "><" -> MathOperation.SWAP;
			default   -> null;
		};
	}
	
	/**
	 * Applies the current MathOperation to two ints
	 * @param val1 the base int to operate on
	 * @param val2 the new value to operate with
	 * @return a int that is the result of applying this math operation
	 */
	public int apply(int val1, int val2) {
		return switch(this) {
		case ADD      -> val1 + val2;
		case ASSIGN   -> val2;
		case DIVIDE   -> val1 / val2;
		case MAX      -> Math.max(val1, val2);
		case MIN      -> Math.min(val1, val2);
		case MOD      -> val1 % val2;
		case MULTIPLY -> val1 * val2;
		case SUBTRACT -> val1 - val2;
		case SWAP     -> val2;
		default       -> val2;
		};
	}
	
	/**
	 * Applies the current MathOperation to two floats
	 * @param val1 the base float to operate on
	 * @param val2 the new value to operate with
	 * @return a float that is the result of applying this math operation
	 */
	public float apply(float val1, float val2) {
		return switch(this) {
		case ADD      -> val1 + val2;
		case ASSIGN   -> val2;
		case DIVIDE   -> val1 / val2;
		case MAX      -> Math.max(val1, val2);
		case MIN      -> Math.min(val1, val2);
		case MOD      -> val1 % val2;
		case MULTIPLY -> val1 * val2;
		case SUBTRACT -> val1 - val2;
		case SWAP     -> val2;
		default       -> val2;
		};
	}
}