package com.gmail.filoghost.quakecraft.enums;

public enum GameState {
	
	PREGAME("§a[Entra]"),
	GAME("§4[In corso]"),
	END("§4[In corso]");
	
	private final String name;
	
	private GameState(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
