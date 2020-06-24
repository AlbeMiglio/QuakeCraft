package com.gmail.filoghost.quakecraft.timers;

import wild.api.world.Particle;

import com.gmail.filoghost.quakecraft.constants.Numbers;
import com.gmail.filoghost.quakecraft.objects.PowerUp;
import com.gmail.filoghost.quakecraft.utils.ParticleUtils;

public class FlameEffectTimer extends TimerMaster {

	private static double[] sinValues = new double[]{0.0, 0.5, 0.866, 1.0, 0.866, 0.5, 0.0, -0.5, -0.866, -1.0, -0.866, -0.5};
	private static double[] cosValues = new double[]{1.0, 0.866, 0.5, 0.0, -0.5, -0.866, -1.0, -0.866, -0.5, 0.0, 0.5, 0.866};
	int index;
	
	public long getDelayBeforeFirstRun() {
		return 0;
	}

	

	public long getDelayBetweenEachRun() {
		return Numbers.FLAME_EFFECT_TICKS;
	}
	
	
	
	public void run() {
		for (PowerUp powerUp : PowerUp.active) {
			if (powerUp.exists()) {
				ParticleUtils.singleParticle(powerUp.hologram.getLocation().add(sinValues[index]*0.7, -0.2, cosValues[index]*0.7), Particle.FLAME);
			}
		}
		
		index++;
		if (index > 11) {
			index = 0;
		}
	}
}
