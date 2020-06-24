package com.gmail.filoghost.quakecraft.utils;

import java.util.HashMap;
import java.util.Map;

public class Monospace {
	
	//private static final int SPACE_BETWEEN_CHARS = 1;
	private static final int DEFAULT_CHAR_WIDTH = 5;
	
	private static final char SMALLEST = '´';
	private static final char SPACE = ' ';
	
	private static Map<Character, Integer> characters = new HashMap<Character, Integer>();
	static {
		register("#$%&+-/0123456789=?ABCDEFGHJKLMNOPQRSTUVWXYZ\\^_abcdeghjmnopqrsuvwxyzñÑáéóúü", 5);
		register("\"()*<>fk{}", 4);
		register(" I[]t", 3);
		register("'´lí", 2);
	}
	
	
	public static String fill(String input, final int width) {
		int currentLength = 0;
		for (char c : input.toCharArray()) {
			if (characters.containsKey(Character.valueOf(c))) {
				currentLength += characters.get(Character.valueOf(c));
			} else {
				currentLength += DEFAULT_CHAR_WIDTH;
			}
		}
		
		if (currentLength < width) {
			int spacesToAdd = (width - currentLength) / 3;
			currentLength += spacesToAdd * 3;
			input += createStringOfChar(SPACE, spacesToAdd);
			if (currentLength < width) {
				input += createStringOfChar(SMALLEST, width - currentLength);
			}
		}
		
		return input;
	}
	
	private static void register(String chars, int length) {
		for (char c : chars.toCharArray()) {
			characters.put(c, length);
		}
	}
	
	private static String createStringOfChar(char c, int length) {
		char[] array = new char[length];
		for (int i = 0; i < length; i++) {
			array[i] = c;
		}
		return new String(array);
	}
}
