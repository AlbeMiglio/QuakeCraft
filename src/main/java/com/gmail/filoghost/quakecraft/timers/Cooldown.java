package com.gmail.filoghost.quakecraft.timers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import wild.api.WildCommons;

import com.gmail.filoghost.quakecraft.QuakeCraft;

public class Cooldown {

	private static List<String> players = new ArrayList<String>();
	private static BukkitScheduler scheduler = Bukkit.getScheduler();
	
	public static void addPlayer(Player player, int ticks) {
		
		final String name = player.getName();
		players.add(name);
		expBar(player, ticks);
		scheduler.scheduleSyncDelayedTask(QuakeCraft.plugin, new Runnable(){
			@Override
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
	
	
	public static void expBar(final Player player, int duration) {
		
		final float step = 2.0F/(duration - 1);
		
		final int taskID = scheduler.scheduleSyncRepeatingTask(QuakeCraft.plugin, new Runnable(){
			
			float exp = 1F;
			
			@Override
			public void run() {
				WildCommons.sendExperiencePacket(player, exp, 0);
				exp -= step;
			}
			
		}, 0, 2);
		
		scheduler.scheduleSyncDelayedTask(QuakeCraft.plugin, new Runnable(){
			@Override
			public void run() {
				scheduler.cancelTask(taskID);
				WildCommons.sendExperiencePacket(player, 0f, 0);
			}
		}, duration);
		
	}
	
	public static int round(float f) {
		int cut = (int) f;
		if (f - cut > 0.5) {
			return cut + 1;
		} else {
			return cut;
		}
	}
}
