package com.gmail.filoghost.quakecraft.utils;

public class Validator {

	public static void notNull(Object o) {
		if (o == null) {
			throw new NullPointerException();
		}
	}
	
	public static void isTrue(boolean b) {
		if (!b) {
			throw new RuntimeException();
		}
	}
	
	public static void notNull(Object o, String msg) {
		if (o == null) {
			throw new NullPointerException(msg);
		}
	}
	
	public static void isTrue(boolean b, String msg) {
		if (!b) {
			throw new RuntimeException(msg);
		}
	}
}
