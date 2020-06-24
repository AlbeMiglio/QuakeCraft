package com.gmail.filoghost.quakecraft.enums;

public enum StainedClayColor {

	DARK_GREEN(13),
	GREEN(5),
	YELLOW(4),
	ORANGE(1),
	RED(14);
	
	private short data;
	
	private StainedClayColor(int data) {
		this.data = (short) data;
	}
	
	public short getData() {
		return data;
	}
}
