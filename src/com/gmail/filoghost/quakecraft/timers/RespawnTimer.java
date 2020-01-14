package com.gmail.filoghost.quakecraft.timers;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import wild.api.world.Particle;

import com.gmail.filoghost.quakecraft.utils.ParticleUtils;

public class RespawnTimer extends TimerMaster {
	
	private Player player;

	public RespawnTimer(Player player) {
		this.player = player;
	}

	
	
	public long getDelayBeforeFirstRun() {
		return 0;
	}

	

	public long getDelayBetweenEachRun() {
		return 4; // un quinto di secondo
	}

	
	//funziona finché ha resistenza (che significa protezione)
	public void run() {
		if (!player.isOnline()) stopTask();
		if (!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) stopTask();
		Location loc = player.getLocation();
		ParticleUtils.regenEffect(loc);
		ParticleUtils.detailedParticle(loc, Particle.SMOKE, 0.3F, 1.2F, 0.0F, 60);
		ParticleUtils.detailedParticle(loc, Particle.HAPPY_VILLAGER, 0.3F, 1.2F, 0.0F, 4);
	}
}
