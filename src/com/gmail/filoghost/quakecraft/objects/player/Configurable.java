package com.gmail.filoghost.quakecraft.objects.player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.gmail.filoghost.quakecraft.constants.ConfigNodes;
import com.gmail.filoghost.quakecraft.utils.Validator;

public class Configurable extends PlayerExtension {

	private FileConfiguration config;
	private File configFile;
	
	public static FileConfiguration DEFAULT_CONFIGURATION;
	static {
		DEFAULT_CONFIGURATION = new YamlConfiguration();
		DEFAULT_CONFIGURATION.set(ConfigNodes.COINS, 0);
		DEFAULT_CONFIGURATION.set(ConfigNodes.KILLS, 0);
		DEFAULT_CONFIGURATION.set(ConfigNodes.WINS, 0);
		DEFAULT_CONFIGURATION.set(ConfigNodes.UPGRADES, new ArrayList<Integer>());
	}
	
	public Configurable(Player base, File configFile) {
		super(base);
		
		Validator.notNull(configFile);
		
		this.configFile = configFile;
	}
	
	public FileConfiguration getConfig() {
		Validator.isTrue(config != null, "not loaded yet");
		return config;
	}
	
	public void load() throws IOException {
		
		if (!configFile.exists()) {
			configFile.createNewFile();
			DEFAULT_CONFIGURATION.save(configFile);
		}

		config = YamlConfiguration.loadConfiguration(configFile);
	}
	
	public void save() throws IOException {
		config.save(configFile);
	}

}
