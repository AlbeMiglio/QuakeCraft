package com.gmail.filoghost.quakecraft.runnables;

import org.bukkit.entity.Player;

import com.gmail.filoghost.quakecraft.utils.PotionUtils;

public class SpeedAndResistancePotionTask implements Runnable {

	final Player player;
	final int amplifier;
	
	public SpeedAndResistancePotionTask(Player player, int amplifier) {
		this.player = player;
		this.amplifier = amplifier;
	}
	
	public void run() {
		PotionUtils.giveSpeed(player, amplifier);
		PotionUtils.giveRespawnResistance(player);
	}
}
