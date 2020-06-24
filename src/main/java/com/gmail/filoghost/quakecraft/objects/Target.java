package com.gmail.filoghost.quakecraft.objects;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Target {

	public Location location;
	public Player player;
	
	/**
	 * A target is made of a location (never null) and a player (that can be null)
	 */
	public Target(Location location, Player player) {
		this.location = location;
		this.player = player;
	}
}
