package com.gmail.filoghost.quakecraft.runnables;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.gmail.filoghost.quakecraft.QuakeCraft;
import com.gmail.filoghost.quakecraft.objects.CyclicIterator;
import com.gmail.filoghost.quakecraft.utils.ParticleUtils;

public class FireworksTask implements Runnable {
	
	CyclicIterator iter = new CyclicIterator(ParticleUtils.colors);
	final Player player;
	private boolean finished;
	
	public FireworksTask(Player player) {
		this.player = player;
	}
	
	@Override
	public void run() {
		if (!finished && player.isOnline() && QuakeCraft.isPlaying(player)) {
			ParticleUtils.winningFirework(getRandomAround(player), iter.next());
		} else {
			finished = true;
		}
	}
	
	public static Location getRandomAround(Player player) {
		Location playerLoc = player.getLocation();

		double x = playerLoc.getX();
		double y = playerLoc.getY();
		double z = playerLoc.getZ();

		double angle = Math.random() * Math.PI * 2;

		Location loc = new Location(player.getWorld(), x + Math.cos(angle) * 2.0, y, z + Math.sin(angle) * 2.0);
		while (loc.getY() < 250.0 && loc.getBlock().getType() != Material.AIR) {
			loc.setY(loc.getY() + 1.0);
		}
		
		return loc;
	}
}
