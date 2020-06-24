package com.gmail.filoghost.quakecraft.timers;

import org.bukkit.Bukkit;

import com.gmail.filoghost.quakecraft.QuakeCraft;

public abstract class TimerMaster implements Runnable {
	
	private Integer taskId = null;
	
	public abstract long getDelayBeforeFirstRun();
	
	public abstract long getDelayBetweenEachRun();
	
	public void startNewTask() {
		if (isStarted()) {
			stopTask();
		}
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(QuakeCraft.plugin, this, getDelayBeforeFirstRun(), getDelayBetweenEachRun());
	}
	
	public void stopTask() {
		if (taskId != null) {
			Bukkit.getScheduler().cancelTask(taskId);
			taskId = null;
		}
	}
	
	public boolean isStarted() {
		return taskId != null;
	}
	
}
