package com.gmail.filoghost.quakecraft.runnables;

import org.bukkit.entity.Player;

import com.gmail.filoghost.quakecraft.QuakeCraft;

public class GiveArmorAndHatTask implements Runnable {

	final Player player;
	
	public GiveArmorAndHatTask(Player player) {
		this.player = player;
	}
	
	@Override
	public void run() {
		if (!player.isOnline() || !QuakeCraft.isPlaying(player)) {
			return;
		}
		
		QuakeCraft.getArenaByPlayer(player).giveArmor(player);
		player.sendMessage("§7§oNon sei più invisibile.");
	}
}
