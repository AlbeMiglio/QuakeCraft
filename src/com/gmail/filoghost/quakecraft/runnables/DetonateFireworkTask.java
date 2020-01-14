package com.gmail.filoghost.quakecraft.runnables;

import org.bukkit.entity.Firework;

public class DetonateFireworkTask implements Runnable {

	private Firework firework;

	public DetonateFireworkTask(Firework firework) {
		this.firework = firework;
	}

	@Override
	public void run() {
		firework.detonate();
	}

}
