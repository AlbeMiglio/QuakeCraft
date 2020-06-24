package com.gmail.filoghost.quakecraft.utils;

public class MillisCounter {

	private long startMillis;
	private long endMillis;
	
	public void start() {
		startMillis = System.currentTimeMillis();
	}
	
	public void end() {
		endMillis = System.currentTimeMillis();
	}
	
	public long getDiff() {
		return endMillis - startMillis;
	}
}
