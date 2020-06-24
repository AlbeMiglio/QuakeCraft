package com.gmail.filoghost.quakecraft.runnables;

import org.bukkit.entity.Player;

public class CloseInventoryTask implements Runnable {

	final Player player;
	
	public CloseInventoryTask(Player player) {
		this.player = player;
	}
	
	public void run() {
		player.closeInventory();	
	}
}
