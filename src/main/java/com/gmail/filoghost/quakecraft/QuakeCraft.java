package com.gmail.filoghost.quakecraft;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import wild.api.WildCommons;
import wild.api.util.CaseInsensitiveMap;

import com.gmail.filoghost.quakecraft.commands.ArenaCommand;
import com.gmail.filoghost.quakecraft.commands.ClassificaCommand;
import com.gmail.filoghost.quakecraft.commands.CoinsCommand;
import com.gmail.filoghost.quakecraft.commands.KillallCommand;
import com.gmail.filoghost.quakecraft.commands.QuakeCommand;
import com.gmail.filoghost.quakecraft.commands.RankCommand;
import com.gmail.filoghost.quakecraft.commands.SpawnCommand;
import com.gmail.filoghost.quakecraft.commands.StartCommand;
import com.gmail.filoghost.quakecraft.commands.StatsCommand;
import com.gmail.filoghost.quakecraft.commands.TestCommand;
import com.gmail.filoghost.quakecraft.constants.ConfigNodes;
import com.gmail.filoghost.quakecraft.constants.Lang;
import com.gmail.filoghost.quakecraft.enums.GameState;
import com.gmail.filoghost.quakecraft.listeners.GuiListener;
import com.gmail.filoghost.quakecraft.listeners.PlayerListener;
import com.gmail.filoghost.quakecraft.listeners.RestrictionsListener;
import com.gmail.filoghost.quakecraft.objects.PowerUp;
import com.gmail.filoghost.quakecraft.objects.arenas.Arena;
import com.gmail.filoghost.quakecraft.objects.arenas.ArenaPVP;
import com.gmail.filoghost.quakecraft.objects.arenas.ArenaTeam;
import com.gmail.filoghost.quakecraft.objects.player.QuakePlayer;
import com.gmail.filoghost.quakecraft.objects.upgrades.Upgrade;
import com.gmail.filoghost.quakecraft.timers.AutosaveTimer;
import com.gmail.filoghost.quakecraft.timers.FlameEffectTimer;
import com.gmail.filoghost.quakecraft.timers.MenuUpdateTimer;
import com.gmail.filoghost.quakecraft.utils.Debug;
import com.gmail.filoghost.quakecraft.utils.Ranking;
import com.gmail.filoghost.quakecraft.utils.SettingsChecker;
import com.gmail.filoghost.quakecraft.utils.Utils;

public class QuakeCraft extends JavaPlugin {
	
	public static final String PLUGIN_ID = "quake";
	
	public static Plugin plugin;
	public static World mainWorld;
	public static Logger logger;
	public static FlameEffectTimer flameEffectTimer;
	public static MenuUpdateTimer menuUpdateTimer;
	public static AutosaveTimer autosaveTimer;
	
	public static Map<String, Arena> arenaMap = new CaseInsensitiveMap<>();
	public static Map<Player, String> playerMap = new HashMap<Player, String>();
	
	@Override
	public void onEnable() {
		if (!Bukkit.getPluginManager().isPluginEnabled("WildCommons")) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + this.getName() + "] Richiesto WildCommons!");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {}
			setEnabled(false);
			Bukkit.shutdown();
			return;
		}
		
		if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + this.getName() + "] Richiesto HolographicDisplays!");
			WildCommons.pauseThread(10000);
			setEnabled(false);
			Bukkit.shutdown();
			return;
		}
		
		logger = this.getLogger();
		plugin = this;
		mainWorld = Bukkit.getWorld("world");

		SettingsChecker.run();
		
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		
		Utils.loadFile("config.yml");
		Utils.loadFile("book.yml");
		
		File arenasFolder = new File(getDataFolder(), File.separator + Lang.ARENAS_FOLDER);
		if (!arenasFolder.exists())	arenasFolder.mkdir();
		
		File playersFolder = new File(getDataFolder(), File.separator + Lang.PLAYERS_FOLDER);
		if (!playersFolder.exists()) playersFolder.mkdir();
		
		Configuration.setup();
		
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
			autosaveTimer.runSync();
			
			Ranking.loadFiles();
			final List<Entry<String, Integer>> topKillers = Ranking.getTopKills(3);
			
			if (topKillers.size() >= 3) {
				final String first = topKillers.get(0).getKey();
				final String second = topKillers.get(1).getKey();
				final String third = topKillers.get(2).getKey();
				
				new BukkitRunnable() { @Override
				public void run() {
					Utils.setHeadName(Configuration.firstKillerHead, first);
					Utils.setSign(Configuration.firstKillerSign, "§1§l§nPRIMO", "", first, topKillers.get(0).getValue().toString());
					
					Utils.setHeadName(Configuration.secondKillerHead, second);
					Utils.setSign(Configuration.secondKillerSign, "§1§l§nSECONDO", "", second, topKillers.get(1).getValue().toString());
					
					Utils.setHeadName(Configuration.thirdKillerHead, third);
					Utils.setSign(Configuration.thirdKillerSign, "§1§l§nTERZO", "", third, topKillers.get(2).getValue().toString());
				}}.runTask(plugin);
			} else {
				Debug.color("§cNon posso fare la classifica uccisioni: lista troppo piccola (" + topKillers.size() + ").");
			}
			
			final List<Entry<String, Integer>> topWinners = Ranking.getTopWins(3);
			
			if (topWinners.size() >= 3) {
				final String first = topWinners.get(0).getKey();
				final String second = topWinners.get(1).getKey();
				final String third = topWinners.get(2).getKey();
				
				new BukkitRunnable() { @Override
				public void run() {
					Utils.setHeadName(Configuration.firstWinnerHead, first);
					Utils.setSign(Configuration.firstWinnerSign, "§1§l§nPRIMO", "", first, topWinners.get(0).getValue().toString());
					
					Utils.setHeadName(Configuration.secondWinnerHead, second);
					Utils.setSign(Configuration.secondWinnerSign, "§1§l§nSECONDO", "", second, topWinners.get(1).getValue().toString());
					
					Utils.setHeadName(Configuration.thirdWinnerHead, third);
					Utils.setSign(Configuration.thirdWinnerSign, "§1§l§nTERZO", "", third, topWinners.get(2).getValue().toString());
				}}.runTask(plugin);
			} else {
				Debug.color("§cNon posso fare la classifica vittorie: lista troppo piccola (" + topWinners.size() + ").");
			}
		}, 0L, 5 * 60 * 20L);
		
		
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
		Bukkit.getPluginManager().registerEvents(new RestrictionsListener(), this);
//		Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
		Bukkit.getPluginManager().registerEvents(new GuiListener(), this);
		
		
		getCommand("quake").setExecutor(new QuakeCommand());
		getCommand("start").setExecutor(new StartCommand());
		getCommand("spawn").setExecutor(new SpawnCommand());
		getCommand("arena").setExecutor(new ArenaCommand());
		getCommand("coins").setExecutor(new CoinsCommand());
		getCommand("rank").setExecutor(new RankCommand());
		getCommand("killall").setExecutor(new KillallCommand());
		getCommand("test").setExecutor(new TestCommand());
		getCommand("stats").setExecutor(new StatsCommand());
		getCommand("classifica").setExecutor(new ClassificaCommand());
		
		Utils.removeNonPlayers();
		Upgrade.setupItems();

		for (File arenaFile : arenasFolder.listFiles()) {
			if (arenaFile.isFile() && arenaFile.getName().endsWith(".yml")) {
				try {
					loadArena(arenaFile, Bukkit.getConsoleSender());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			QuakePlayer.load(player);
		}
		
		flameEffectTimer = new FlameEffectTimer();
		flameEffectTimer.startNewTask();
		
		menuUpdateTimer = new MenuUpdateTimer();
		menuUpdateTimer.startNewTask();
		
		autosaveTimer = new AutosaveTimer();
		autosaveTimer.startNewTask();
	}
	
	@Override
	public void onDisable() {
		
		getLogger().info("Saving all the players, please wait...");
		
		for (Arena arena : arenaMap.values()) {
			
			if (arena.getState() == GameState.GAME) {
				getLogger().info("Forced arena end: " + arena.getName());
				arena.giveRewards(null, true);
			}
		}
		
		getLogger().info("Saved!");
		
		getLogger().info("Removing powerups...");
		PowerUp.removeAll();
	}
	
	
	public static void loadArena(File file, CommandSender sender) throws Exception {
		if (!file.exists()) throw new Exception("File must exist");
		if (!file.isFile()) throw new Exception("The file can't be a folder");
		if (!file.getName().endsWith(".yml")) throw new Exception("The file must end with .yml");
		
		FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);
		Arena arena;
		
		String type = yaml.getString(ConfigNodes.ARENA_TYPE);
		if (type == null) type = "";
		
		if (type.equalsIgnoreCase("pvp"))			arena = new ArenaPVP(yaml, sender);
		else if (type.equalsIgnoreCase("team"))		arena = new ArenaTeam(yaml, sender);
		else										arena = new Arena(yaml, sender);
		
		if (arena != null && arena.creationSuccessful) {
			arenaMap.put(arena.getName(), arena);
		}
	}
	
	
	public static boolean isPlaying(Player player) {
		return playerMap.containsKey(player);
	}
	
	public static Arena getArenaByName(String name) {
		return arenaMap.get(name);
	}
	
	/**
	 * @return the arena of the player, null if he's not playing.
	 */
	public static Arena getArenaByPlayer(Player player) {
		String arenaName = playerMap.get(player);
		if (arenaName == null) {
			return null;
		}
		return arenaMap.get(arenaName);
	}
	
}
