package dev.jorel.commandapi.wrappers.arguments;

public class Time {

	private String time;
	
	private Time(String time) {
		this.time = time;
	}

	public static Time ticks(int ticks) {
		return new Time(ticks + "t");
	}
	
	public static Time days(int days) {
		return new Time(days + "d");
	}
	
	public static Time seconds(int seconds) {
		return new Time(seconds + "s");
	}
	
	@Override
	public String toString() {
		return this.time;
	}
	
}
