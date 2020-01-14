package com.gmail.filoghost.quakecraft.runnables;

import org.bukkit.Bukkit;


public class TaskRemoverTask implements Runnable {
	
	final int id;
	
	public TaskRemoverTask(int id) {
		this.id = id;
	}
	
	public void run() {
		Bukkit.getScheduler().cancelTask(id);
	}
}
