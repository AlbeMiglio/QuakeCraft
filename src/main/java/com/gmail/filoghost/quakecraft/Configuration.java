package com.gmail.filoghost.quakecraft;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

import com.gmail.filoghost.quakecraft.constants.ConfigNodes;

public class Configuration {

	public static int countDown;
	public static String joinMessage;
	public static String gameMotd;
	public static String preGameMotd;
	public static Location lobby;
	
	public static Block firstKillerHead;
	public static Block firstKillerSign;
	public static Block secondKillerHead;
	public static Block secondKillerSign;
	public static Block thirdKillerHead;
	public static Block thirdKillerSign;
	
	public static Block firstWinnerHead;
	public static Block firstWinnerSign;
	public static Block secondWinnerHead;
	public static Block secondWinnerSign;
	public static Block thirdWinnerHead;
	public static Block thirdWinnerSign;
	
	private Configuration() {}
	
	public static void setup() {
		//legge tutte le impostazioni
		QuakeCraft.plugin.reloadConfig();
		FileConfiguration config = QuakeCraft.plugin.getConfig();
		
		countDown = config.getInt("countdown-seconds");
		preGameMotd = config.getString("countdown-message").replace("&", "§");
		
		lobby = new Location(QuakeCraft.mainWorld, config.getDouble("lobby.x"), config.getDouble("lobby.y"), config.getDouble("lobby.z"), (float) config.getDouble("lobby.yaw"), (float) config.getDouble("lobby.pitch"));
		
		// Killers
		if (config.isSet(ConfigNodes.FIRST_KILLER_HEAD)) {
			firstKillerHead = getBlockFromConfigSection(config, ConfigNodes.FIRST_KILLER_HEAD);
		}
		if (config.isSet(ConfigNodes.SECOND_KILLER_HEAD)) {
			secondKillerHead = getBlockFromConfigSection(config, ConfigNodes.SECOND_KILLER_HEAD);
		}
		if (config.isSet(ConfigNodes.THIRD_KILLER_HEAD)) {
			thirdKillerHead = getBlockFromConfigSection(config, ConfigNodes.THIRD_KILLER_HEAD);
		}
		
		if (config.isSet(ConfigNodes.FIRST_KILLER_SIGN)) {
			firstKillerSign = getBlockFromConfigSection(config, ConfigNodes.FIRST_KILLER_SIGN);
		}
		if (config.isSet(ConfigNodes.SECOND_KILLER_SIGN)) {
			secondKillerSign = getBlockFromConfigSection(config, ConfigNodes.SECOND_KILLER_SIGN);
		}
		if (config.isSet(ConfigNodes.THIRD_KILLER_SIGN)) {
			thirdKillerSign = getBlockFromConfigSection(config, ConfigNodes.THIRD_KILLER_SIGN);
		}
		
		
		// Winners
		if (config.isSet(ConfigNodes.FIRST_WINNER_HEAD)) {
			firstWinnerHead = getBlockFromConfigSection(config, ConfigNodes.FIRST_WINNER_HEAD);
		}
		if (config.isSet(ConfigNodes.SECOND_WINNER_HEAD)) {
			secondWinnerHead = getBlockFromConfigSection(config, ConfigNodes.SECOND_WINNER_HEAD);
		}
		if (config.isSet(ConfigNodes.THIRD_WINNER_HEAD)) {
			thirdWinnerHead = getBlockFromConfigSection(config, ConfigNodes.THIRD_WINNER_HEAD);
		}
		
		if (config.isSet(ConfigNodes.FIRST_WINNER_SIGN)) {
			firstWinnerSign = getBlockFromConfigSection(config, ConfigNodes.FIRST_WINNER_SIGN);
		}
		if (config.isSet(ConfigNodes.SECOND_WINNER_SIGN)) {
			secondWinnerSign = getBlockFromConfigSection(config, ConfigNodes.SECOND_WINNER_SIGN);
		}
		if (config.isSet(ConfigNodes.THIRD_WINNER_SIGN)) {
			thirdWinnerSign = getBlockFromConfigSection(config, ConfigNodes.THIRD_WINNER_SIGN);
		}
	}
	
	public static Block getBlockFromConfigSection(FileConfiguration config, String section) {
		return QuakeCraft.mainWorld.getBlockAt(config.getInt(section + ".x"), config.getInt(section + ".y"), config.getInt(section + ".z"));
	}
	
	public static void setBlockToConfigSection(FileConfiguration config, String section, Block block) {
		config.set(section + ".x", block.getX());
		config.set(section + ".y", block.getY());
		config.set(section + ".z", block.getZ());
	}
	
	public static void saveBlock(FileConfiguration config, String section, Block block) {
		setBlockToConfigSection(config, section, block);
		QuakeCraft.plugin.saveConfig();
	}
	
	public static void setLobby(Location loc) {
		FileConfiguration config = QuakeCraft.plugin.getConfig();
		Configuration.lobby = loc;
		config.set("lobby.x", lobby.getX());
		config.set("lobby.y", lobby.getY());
		config.set("lobby.z", lobby.getZ());
		config.set("lobby.yaw", lobby.getYaw());
		config.set("lobby.pitch", lobby.getPitch());
		QuakeCraft.plugin.saveConfig();
	}
	
	public static FileConfiguration getConfig() {
		return QuakeCraft.plugin.getConfig();
	}
}
