package com.gmail.filoghost.quakecraft.objects;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class KillStreak {

	private static Map<Player, Integer> consecutiveKills = new HashMap<Player, Integer>();
	
	public static void addKill(Player player) {
		int kills = consecutiveKills.containsKey(player) ? consecutiveKills.get(player) : 0;
		consecutiveKills.put(player, kills + 1);
	}
	
	public static void resetConsecutiveKills(Player player) {
		consecutiveKills.remove(player);
	}
	
	public static int getConsecutiveKills(Player player) {
		return consecutiveKills.containsKey(player) ? consecutiveKills.get(player) : 0;
	}
	
}
