package com.gmail.filoghost.quakecraft.timers;

import com.gmail.filoghost.quakecraft.constants.Numbers;
import com.gmail.filoghost.quakecraft.objects.arenas.Arena;

public class PowerUpTimer extends TimerMaster {
	
	private Arena arena;

	
	
	public PowerUpTimer(Arena arena) {
		this.arena = arena;
	}

	
	
	public long getDelayBeforeFirstRun() {
		return Numbers.POWERUP_DELAY_TICKS;
	}

	

	public long getDelayBetweenEachRun() {
		return Numbers.POWERUP_DELAY_TICKS;
	}

	
	
	public void run() {
		arena.dropRandomPowerUp();
	}
}
