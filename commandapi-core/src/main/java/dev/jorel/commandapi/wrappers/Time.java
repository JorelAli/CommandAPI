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
 * A class that represents time suggestions for the TimeArgument
 */
public class Time {

	private String timeString;
	
	/**
	 * Constructs a Time object from a given String
	 * @param time the String to use 
	 */
	private Time(String time) {
		this.timeString = time;
	}

	/**
	 * Create a Time object with a given number of ticks
	 * @param ticks the number of ticks to use
	 * @return a Time object representing ticks (t)
	 */
	public static Time ticks(int ticks) {
		return new Time(ticks + "t");
	}
	
	/**
	 * Create a Time object with a given number of days
	 * @param days the number of days to use
	 * @return a Time object representing days (d)
	 */
	public static Time days(int days) {
		return new Time(days + "d");
	}
	
	/**
	 * Create a Time object with a given number of seconds
	 * @param seconds the number of seconds to use
	 * @return a Time object representing seconds (s)
	 */
	public static Time seconds(int seconds) {
		return new Time(seconds + "s");
	}
	
	/**
	 * @return the Minecraft argument representation of this Time object
	 */
	@Override
	public String toString() {
		return this.timeString;
	}

	@Override
	public int hashCode() {
		return Objects.hash(timeString);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Time other)) {
			return false;
		}
		return Objects.equals(timeString, other.timeString);
	}
	
}
