package com.gmail.filoghost.quakecraft.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import com.gmail.filoghost.quakecraft.QuakeCraft;

public class SettingsChecker {

	
	public static void run() {
		
		List<String> logs = new ArrayList<String>();
		
		if (Bukkit.getAllowEnd()) {
			logs.add("End is allowed. It's recommanded to disable it.");
		}
		
		if (Bukkit.getAllowNether()) {
			logs.add("Nether is allowed. It's recommanded to disable it.");
		}
		
		if (QuakeCraft.mainWorld.getAllowAnimals()) {
			logs.add("Animal spawning allowed. It's recommanded to disable it.");
		}
		
		if (QuakeCraft.mainWorld.getAllowMonsters()) {
			logs.add("Animal spawning allowed. It's recommanded to disable it.");
		}

		/*
		if (Bukkit.getServer().getDefaultGameMode() != GameMode.ADVENTURE) {
			logs.add("Adventure gamemode by default is recommanded.");
		}
		*/


		if (logs.size() > 0) {
			System.out.println("-");
			System.out.println("****************** QuakeCraft ******************");
			for (String log : logs) {
				System.out.println(log);
			}
			System.out.println("************************************************");
			System.out.println("-");
		}
	}
}
