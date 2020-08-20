package dev.jorel.commandapi.wrappers;

/**
 * A class that represents time suggestions for the TimeArgument
 */
public class Time {

	private String time;
	
	/**
	 * Constructs a Time object from a given String
	 * @param time the String to use 
	 */
	private Time(String time) {
		this.time = time;
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
	 * Returns the Minecraft argument representation of this Time object
	 * @return the Minecraft argument representation of this Time object
	 */
	@Override
	public String toString() {
		return this.time;
	}
	
}
