package dev.jorel.commandapi.wrappers;

import com.mojang.brigadier.context.StringRange;

public class StringRangeWrapper extends StringRange {

	public StringRangeWrapper(int start, int end) {
		super(start, end);
	}
	
	@Override
	public int getEnd() {
		return super.getEnd();
	}
	
	@Override
	public int getLength() {
		return super.getLength();
	}
	
	@Override
	public String get(String string) {
		return super.get(string);
	}
	
	@Override
	public int getStart() {
		return super.getStart();
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
	@Override
	public boolean isEmpty() {
		return super.isEmpty();
	}
}
