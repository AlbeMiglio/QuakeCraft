package com.gmail.filoghost.quakecraft.runnables;

import com.gmail.filoghost.quakecraft.objects.player.QuakePlayer;

public class DisplayCoinsTask implements Runnable {

	private QuakePlayer player;

	public DisplayCoinsTask(QuakePlayer player) {
		this.player = player;
	}
	
	public void run() {
		if (player.getBase().isOnline()) {
			player.displayCoins();
		}
	}
}
