package com.gmail.filoghost.quakecraft.timers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.quakecraft.QuakeCraft;
import com.gmail.filoghost.quakecraft.objects.player.QuakePlayer;

public class AutosaveTimer extends TimerMaster {
	
	private List<QuakePlayer> playersToSave = Collections.synchronizedList(new ArrayList<QuakePlayer>());
	
	public void addPlayer(QuakePlayer quakePlayer) {
		if (!playersToSave.contains(quakePlayer)) {
			playersToSave.add(quakePlayer);
		}
	}
	
	public boolean needsSave(QuakePlayer quakePlayer) {
		return playersToSave.contains(quakePlayer);
	}
	
	public void removePlayer(QuakePlayer quakePlayer) {
		playersToSave.remove(quakePlayer);
	}
	
	public AutosaveTimer() { }
	
	@Override
	public long getDelayBeforeFirstRun() {
		return getDelayBetweenEachRun();
	}

	@Override
	public long getDelayBetweenEachRun() {
		return 10*60*20;
	}
	
	@Override
	public void run() {
		QuakeCraft.logger.info("Salvataggio di " + playersToSave.size() + " giocatori...");
		
		final List<QuakePlayer> copy = new ArrayList<>(playersToSave);
		playersToSave.clear();
		
		new BukkitRunnable() { @Override
		public void run() {
			for (QuakePlayer quakePlayer : copy) {
				quakePlayer.saveSync();
			}
		}}.runTaskAsynchronously(QuakeCraft.plugin);
	}
	
	public void runSync() {
		QuakeCraft.logger.info("Salvataggio di " + playersToSave.size() + " giocatori...");
		
		final List<QuakePlayer> copy = new ArrayList<>(playersToSave);
		playersToSave.clear();
		
		for (QuakePlayer quakePlayer : copy) {
			quakePlayer.saveSync();
		}
	}
}
