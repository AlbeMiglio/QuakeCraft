package com.gmail.filoghost.quakecraft.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gmail.filoghost.quakecraft.QuakeCraft;
import com.gmail.filoghost.quakecraft.constants.ConfigNodes;
import com.gmail.filoghost.quakecraft.constants.Lang;

public class Ranking {
	
	public static TreeMap<String, Integer> orderedCoins;
	public static TreeMap<String, Integer> orderedKills;
	public static TreeMap<String, Integer> orderedWins;
	
	public static void loadFiles() {

		QuakeCraft.logger.info("Sto leggendo la classifica...");
		
		File playerFolder = new File(QuakeCraft.plugin.getDataFolder(), File.separator + Lang.PLAYERS_FOLDER);
		if (!playerFolder.exists()) return;
		File[] files = playerFolder.listFiles();
		
		HashMap<String, Integer> coins = new HashMap<String, Integer>();
		HashMap<String, Integer> kills = new HashMap<String, Integer>();
		HashMap<String, Integer> wins = new HashMap<String, Integer>();

		if (files != null && files.length > 0) {
			
			int total = files.length;
			QuakeCraft.logger.info("[Classifica] Trovati " + total + " files.");
			long millis = System.currentTimeMillis();
			boolean[] donePercentuals = new boolean[11];
			int parsedFiles = 0;
			
			for (File f : files) {
				
				YamlConfiguration config = new YamlConfiguration();
				try {
					config.load(f);
				} catch (IOException e) {
					e.printStackTrace();
					QuakeCraft.logger.warning("[Classifica] Impossibile leggere il file " + f.getName() + " (errore I/O)!");
					continue;
				} catch (InvalidConfigurationException e) {
					e.printStackTrace();
					QuakeCraft.logger.warning("[Classifica] Impossibile leggere il file " + f.getName() + " (errore configurazione)!");
					continue;
				}
				
				String name = f.getName();
				name = name.substring(0, name.length() - 4);
				
				if (config.isString(ConfigNodes.FIXED_CASE_NAME)) {
					String fixed = config.getString(ConfigNodes.FIXED_CASE_NAME);
					
					if (fixed.equalsIgnoreCase(name)) {
						name = fixed;
					}
				}
				
				coins.put(name, config.getInt(ConfigNodes.COINS));
				kills.put(name, config.getInt(ConfigNodes.KILLS));
				wins.put(name, config.getInt(ConfigNodes.WINS));
				parsedFiles++;
				
				double percent = ((double)parsedFiles) / ((double) total)*10.0D;
				
				if (percent > 10.0D) percent = 10.0D;
				
				if (!donePercentuals[(int) percent]) {
					donePercentuals[(int) percent] = true;
					QuakeCraft.logger.info("[Classifica] Completamento al " + (((int) percent)*10) + "%");
				}
			}
			
			QuakeCraft.logger.info("[Classifica] Finito in " + (System.currentTimeMillis()-millis) + " millisecondi.");
		}
		
		ValueComparator coinsComparator =  new ValueComparator(coins);
		orderedCoins = new TreeMap<String, Integer>(coinsComparator);
		orderedCoins.putAll(coins);
		
		ValueComparator killsComparator =  new ValueComparator(kills);
		orderedKills = new TreeMap<String, Integer>(killsComparator);
		orderedKills.putAll(kills);
		
		ValueComparator winsComparator = new ValueComparator(wins);
		orderedWins = new TreeMap<String, Integer>(winsComparator);
		orderedWins.putAll(wins);
	}
	
	public static List<Entry<String, Integer>> getTopCoins(int amount) {
		List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>();
		
		for (Entry<String, Integer> entry : orderedCoins.entrySet()) {
			if (list.size() > amount) break;
			list.add(entry);
		}
		return list;
	}
	
	public static List<Entry<String, Integer>> getTopKills(int amount) {
		List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>();
		
		for (Entry<String, Integer> entry : orderedKills.entrySet()) {
			if (list.size() > amount) break;
			list.add(entry);
		}
		return list;
	}
	
	public static List<Entry<String, Integer>> getTopWins(int amount) {
		List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>();
		
		for (Entry<String, Integer> entry : orderedWins.entrySet()) {
			if (list.size() > amount) break;
			list.add(entry);
		}
		return list;
	}
	
	static class ValueComparator implements Comparator<String> {

	    Map<String, Integer> base;
	    
	    public ValueComparator(Map<String, Integer> base) {
	        this.base = base;
	    }

	    @Override
		public int compare(String a, String b) {
	    	int c = base.get(b) - base.get(a);
	    	
	    	if (c == 0) {
	    		c = a.compareToIgnoreCase(b);
	    	}
	    	
	    	return c;
	    }
	}
}
