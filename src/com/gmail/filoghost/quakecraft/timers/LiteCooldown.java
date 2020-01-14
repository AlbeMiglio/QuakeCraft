package com.gmail.filoghost.quakecraft.timers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import com.gmail.filoghost.quakecraft.QuakeCraft;

public class LiteCooldown {

	private static List<String> players = new ArrayList<String>();
	private static BukkitScheduler scheduler = Bukkit.getScheduler();
	
	public static void addPlayer(Player player, int ticks) {
		
		final String name = player.getName();
		players.add(name);
		scheduler.scheduleSyncDelayedTask(QuakeCraft.plugin, new Runnable(){
			public void run() {
				players.remove(name);
			}
		}, ticks);
	}
	
	public static boolean canShot(Player player) {
		return !players.contains(player.getName());
	}
	
	public static void removePlayer(String name) {
		players.remove(name);
	}
	
}
