package com.gmail.filoghost.quakecraft.enums;

import java.util.Arrays;
import java.util.List;

public enum ArenaType {

	NORMAL("Normale", Arrays.asList("Uccidi gli avversari.")),
	TEAM("Team", Arrays.asList("Uccidi gli avversari.")),
	PVP("PvP", Arrays.asList("Uccidi gli avversari.")),
	FAST("Speciale", Arrays.asList("Uccidi gli avversari."));
	
	
	
	String name;
	List<String> description;
	
	private ArenaType(String name, List<String> description) {
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public List<String> getDescription() {
		return description;
	}
	
}
