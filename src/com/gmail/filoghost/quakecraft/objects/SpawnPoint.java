package com.gmail.filoghost.quakecraft.objects;

import org.bukkit.Location;

import com.gmail.filoghost.quakecraft.QuakeCraft;

public class SpawnPoint {
	
	private int x;
	private int y;
	private int z;
	private int radius;
	
	public SpawnPoint(int x, int y, int z, int radius) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.radius = radius;
	}
	
	public Location getRandomLocation() {
		double angle = Math.random() * Math.PI * 2;
		double r = Math.random() * radius;
		return new Location(QuakeCraft.mainWorld,
							x + 0.5 + Math.cos(angle)*r,
							y,
							z + 0.5 + Math.sin(angle)*r,
							(float) ((Math.random() * 360.0) - 180.0),
							1F);
	}
}
