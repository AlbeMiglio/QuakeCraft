package com.gmail.filoghost.quakecraft.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gmail.filoghost.quakecraft.QuakeCraft;
import com.gmail.filoghost.quakecraft.constants.ConfigNodes;
import com.gmail.filoghost.quakecraft.constants.Lang;

public class Purge {
	
	public static void purgeEmptyAccounts() {
		
		QuakeCraft.logger.info("Sto pulendo gli account vuoti...");
		
		File playerFolder = new File(QuakeCraft.plugin.getDataFolder(), File.separator + Lang.PLAYERS_FOLDER);
		if (!playerFolder.exists()) return;
		File[] files = playerFolder.listFiles();
		
		if (files != null && files.length > 0) {

			FileConfiguration config;
			int purgedFiles = 0;
			
			for (File f : files) {
				config = YamlConfiguration.loadConfiguration(f);
				
				if (	config.getInt(ConfigNodes.COINS) == 0 &&
						config.getInt(ConfigNodes.KILLS) == 0 &&
						config.getInt(ConfigNodes.WINS) == 0 &&
						config.getList(ConfigNodes.UPGRADES, new ArrayList<String>()).size() == 0) {
					
					f.delete();
					purgedFiles++;
				}
			}
			
			QuakeCraft.logger.info("Eliminati " + purgedFiles + " files.");
		}
		
	}
	
	public static void cleanKillsDeathsWins() {
		
		QuakeCraft.logger.info("Sto pulendo le uccisioni e le vittorie...");
		
		File playerFolder = new File(QuakeCraft.plugin.getDataFolder(), File.separator + Lang.PLAYERS_FOLDER);
		if (!playerFolder.exists()) return;
		File[] files = playerFolder.listFiles();
		
		if (files != null && files.length > 0) {

			FileConfiguration config;
			
			for (File f : files) {
				config = YamlConfiguration.loadConfiguration(f);
				config.set(ConfigNodes.KILLS, 0);
				config.set(ConfigNodes.DEATHS, 0);
				config.set(ConfigNodes.WINS, 0);
				
				try {
					config.save(f);
				} catch (IOException e) {
					QuakeCraft.logger.warning("Impossibile pulire il file: " + f.getName());
					e.printStackTrace();
				}
			}
			
			QuakeCraft.logger.info("Puliti " + files.length + " files.");
		}
		
	}
}
