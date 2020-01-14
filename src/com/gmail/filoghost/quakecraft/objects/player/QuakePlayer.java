package com.gmail.filoghost.quakecraft.objects.player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import wild.api.WildConstants;
import wild.api.sound.EasySound;
import wild.api.util.FakeOfflinePlayer;

import com.gmail.filoghost.quakecraft.QuakeCraft;
import com.gmail.filoghost.quakecraft.constants.ConfigNodes;
import com.gmail.filoghost.quakecraft.constants.Lang;
import com.gmail.filoghost.quakecraft.enums.Gui;
import com.gmail.filoghost.quakecraft.enums.UpgradeType;
import com.gmail.filoghost.quakecraft.objects.upgrades.Upgrade;
import com.gmail.filoghost.quakecraft.utils.Debug;
import com.gmail.filoghost.quakecraft.utils.Validator;

public class QuakePlayer extends Equippable {
	
	private Integer coins;
	private Integer kills;
	private Integer deaths;
	private Integer wins;
	
	private int saveTries;

	public static Map<Player, QuakePlayer> playersMap = new HashMap<Player, QuakePlayer>();
	
	private QuakePlayer(Player base) {
		super(base, new File(QuakeCraft.plugin.getDataFolder(), Lang.PLAYERS_FOLDER + File.separator + base.getName().toLowerCase() + ".yml"));
		
		try {
			load();
		} catch (IOException e) {
			base.kickPlayer("§cErrore di lettura del tuo salvataggio! Riprova fra poco!");
			e.printStackTrace();
			return;
		}
		
		FileConfiguration config = getConfig();
		config.set(ConfigNodes.FIXED_CASE_NAME, base.getName());
		
		//loads upgrades
		List<Integer> configUpgradeList = getConfig().getIntegerList(ConfigNodes.UPGRADES);
		for (Upgrade upgrade : Upgrade.list) {
			if (configUpgradeList.contains(upgrade.getID())) {
				addUpgrade(upgrade);
			}
		}
		
		coins = config.getInt(ConfigNodes.COINS);
		kills = config.getInt(ConfigNodes.KILLS);
		deaths = config.getInt(ConfigNodes.DEATHS);
		wins = config.getInt(ConfigNodes.WINS);
		
		if (config.isSet(ConfigNodes.HAT)) {
			Upgrade hat = Upgrade.getByID(config.getInt(ConfigNodes.HAT));
			
			if (hat != null && !canUseVIPUpgrade(hat)) {
				hat = null;
			}
			
			setHat(hat);
		}
		
		if (config.isSet(ConfigNodes.ARMOR)) {
			Upgrade armor = Upgrade.getByID(config.getInt(ConfigNodes.ARMOR));
			
			if (armor != null && !canUseVIPUpgrade(armor)) {
				armor = null;
			}
			
			setArmor(armor);
		}
		
		if (config.isSet(ConfigNodes.WEAPON)) {
			Upgrade weapon = Upgrade.getByID(config.getInt(ConfigNodes.WEAPON));
			
			if (weapon != null && !canUseVIPUpgrade(weapon)) {
				weapon = null;
			}

			setWeapon(weapon);
		}
		
		if (config.isSet(ConfigNodes.TRAIL)) {
			Upgrade trail = Upgrade.getByID(config.getInt(ConfigNodes.TRAIL));
			
			if (trail != null && !canUseVIPUpgrade(trail)) {
				trail = null;
			}
			
			setTrail(trail);
		}
		
		if (config.isSet(ConfigNodes.EXPLOSION)) {
			Upgrade explosion = Upgrade.getByID(config.getInt(ConfigNodes.EXPLOSION));
			
			if (explosion != null && !canUseVIPUpgrade(explosion)) {
				explosion = null;
			}
			
			setExplosion(explosion);
		}
	}
	
	public static QuakePlayer load(Player base) {
		if (playersMap.containsKey(base)) {
			Debug.ln("User already loaded! (" + base.getName() + ")");
			return playersMap.get(base);
		}
		
		QuakePlayer loaded = new QuakePlayer(base);
		playersMap.put(base, loaded);
		
		return loaded;
	}
	
	public static QuakePlayer get(Player base) {
		return playersMap.get(base);
	}
	
	public static void unload(Player base) {
		playersMap.remove(base);
	}
	
	public void unequip(UpgradeType type) {
		super.unequip(type);
		
		if (type == UpgradeType.ARMOR) {
			PlayerInventory inv = getInventory();
			inv.setChestplate(null);
			inv.setLeggings(null);
			inv.setBoots(null);
		} else if (type == UpgradeType.HAT) {
			getInventory().setHelmet(null);
		}
	}
	
	public void equip(Upgrade upgrade) {
		super.equip(upgrade);
		
		if (upgrade.getType() == UpgradeType.ARMOR) {
			wearArmor();
		} else if (upgrade.getType() == UpgradeType.HAT) {
			wearHat();
		}
	}
	
	public void clickedOnUpgrade(Upgrade upgrade) {
		
		if (!canUseVIPUpgrade(upgrade)) {
			sendMessage("§cDevi essere VIP per usare questo oggetto.");
			sendMessage("§cVai su §6store.WildAdventure.it §cper maggiori informazioni.");
			EasySound.quickPlay(getBase(), Sound.BLOCK_NOTE_BASS);
			return;
		}
		
		if (ownsUpgrade(upgrade)) {
			clickOwned(upgrade);
		} else {
			clickNotOwned(upgrade);
		}
	}
	
	private void clickNotOwned(Upgrade upgrade) {
		
		if (ownsUpgrade(upgrade)) {
			sendMessage("§cPossiedi già questo oggetto.");
			return;
		}
		
		if (!canUseVIPUpgrade(upgrade)) {
			sendMessage("§cDevi essere VIP per usare questo oggetto.");
			sendMessage("§cVai su §6store.WildAdventure.it §cper maggiori informazioni.");
			return;
		}
		
		Integer ID = upgrade.getID();
			
		if (upgrade.getType() == UpgradeType.WEAPON && upgrade.getID() != 300) {
			if (!ownsUpgrade(Upgrade.getByID(ID - 1))) {
				sendMessage("§cDevi prima comprare l'arma precedente (" + Upgrade.getByID(ID - 1).getName() + ").");
				EasySound.quickPlay(getBase(), Sound.BLOCK_NOTE_BASS);
				return;
			}
		}
			
		if (coins >= upgrade.getPrice()) {
				
			removeCoins(upgrade.getPrice());
			displayCoins();
			buyUpgrade(upgrade);
			sendMessage(Lang.UPGRADE_BOUGHT);
			playSound(Sound.ENTITY_PLAYER_LEVELUP);
			saveAsync(); // Salva sempre quando uno compra un upgrade.
			
			clickOwned(upgrade);
		} else {
			EasySound.quickPlay(getBase(), Sound.BLOCK_NOTE_BASS);
			sendMessage(Lang.NO_MONEY);
		}
		
	}

	private void clickOwned(Upgrade upgrade) {
		
		Validator.isTrue(ownsUpgrade(upgrade), "upgrade not owned");
		
		if (!isEquipped(upgrade)) {
			
			equip(upgrade);
			
			// Refresh inventory
			getGuiByType(upgrade.getType()).open(getBase());
			
			sendMessage(Lang.UPGRADE_EQUIP);
			playSound(Sound.BLOCK_NOTE_SNARE, 1.3f);
			
		} else {
			
			unequip(upgrade.getType());
			
			// Refresh inventory
			getGuiByType(upgrade.getType()).open(getBase());
			
			sendMessage(Lang.UPGRADE_UNEQUIP);
			playSound(Sound.BLOCK_NOTE_SNARE, 0.7f);
		}
		
		QuakeCraft.autosaveTimer.addPlayer(this);
	}
	
	public Gui getGuiByType(UpgradeType type) {
		
		Validator.notNull(type);
		
		switch(type) {
			case ARMOR:
				return Gui.SHOP_ARMOR;
			case EXPLOSION:
				return Gui.SHOP_EXPLOSION;
			case HAT:
				return Gui.SHOP_HAT;
			case TRAIL:
				return Gui.SHOP_TRAIL;
			case WEAPON:
				return Gui.SHOP_WEAPON;
		}
		
		return null;
	}

	public void wearHat() {
		PlayerInventory inv = getInventory();
		
		if (getHat() != null) {
			inv.setHelmet(getHat().getHat());
		}
	}
	
	public void wearArmor() {
		if (getArmor() != null) {
			PlayerInventory inv = getInventory();
			inv.setChestplate(getArmor().getBody());
			inv.setLeggings(getArmor().getLeggings());
			inv.setBoots(getArmor().getBoots());
		}
	}
	
	public void wearWeapon() {
		PlayerInventory inv = getInventory();
		
		if (getWeapon() != null) {
			inv.addItem(getWeapon().getWeapon());
		} else {
			inv.addItem(Upgrade.DEFAULT_WEAPON.getWeapon());
		}
	}
	
	public FireworkEffect[] getExplosionEffects() {
		return getExplosion().getEffects();
	}
	
	public void wearEquippedUpgrades() {
		
		PlayerInventory inv = getInventory();
		
		if (getHat() != null) {
			inv.setHelmet(getHat().getHat());
		}
		
		if (getArmor() != null) {
			inv.setChestplate(getArmor().getBody());
			inv.setLeggings(getArmor().getLeggings());
			inv.setBoots(getArmor().getBoots());
		}
		
		if (getWeapon() != null) {
			inv.addItem(getWeapon().getWeapon());
		} else {
			inv.addItem(Upgrade.DEFAULT_WEAPON.getWeapon());
		}
	}
	
	@Deprecated
	public void setCoins(int amount) {
		coins = amount;
		getConfig().set(ConfigNodes.COINS, coins);
	}
	
	public int getWins() {
		return wins;
	}
	
	public int getCoins() {
		return coins;
	}
	
	public void addCoins(int amount) {
		coins = coins + amount;
		getConfig().set(ConfigNodes.COINS, coins);
	}
	
	public void removeCoins(int amount) {
		coins = coins - amount;
		getConfig().set(ConfigNodes.COINS, coins);
	}
	
	@Deprecated
	public void setKills(int amount) {
		kills = amount;
		getConfig().set(ConfigNodes.KILLS, kills);
	}
	
	public int getKills() {
		return kills;
	}
	
	public void addKills(int amount) {
		kills = kills + amount;
		getConfig().set(ConfigNodes.KILLS, kills);
	}
	
	public void addWin() {
		wins++;
		getConfig().set(ConfigNodes.WINS, wins);
	}
	
	public void addDeaths(int amount) {
		deaths = deaths + amount;
		getConfig().set(ConfigNodes.DEATHS, deaths);
	}
	
	public int getDeaths() {
		return deaths;
	}
	
	public void displayCoins() {
		Scoreboard stats = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = stats.registerNewObjective("stats", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(Lang.OBJECTIVE_STATISTICHE_NAME);
		
		setScore(obj, emptyLine(13), 13);
		setScore(obj, Lang.OBJECTIVE_TITLE_PREFIX + "Coins", 12);
		setScore(obj, coins + emptyLine(11), 11);
		setScore(obj, emptyLine(10), 10);
		setScore(obj, Lang.OBJECTIVE_TITLE_PREFIX + "Vittorie", 9);
		setScore(obj, wins + emptyLine(8), 8);
		setScore(obj, emptyLine(7), 7);
		setScore(obj, Lang.OBJECTIVE_TITLE_PREFIX + "Uccisioni", 6);
		setScore(obj, kills + emptyLine(5), 5);
		setScore(obj, emptyLine(4), 4);
		setScore(obj, Lang.OBJECTIVE_TITLE_PREFIX + "Morti", 3);
		setScore(obj, deaths + emptyLine(2), 2);
		setScore(obj, emptyLine(1), 1);
		WildConstants.Messages.displayIP(stats, obj, 0);

		getBase().setScoreboard(stats);
	}
	
	
	private static String emptyLine(int sideNumber) {
		if (sideNumber > 15 || sideNumber < 0) return "";
		return ChatColor.values()[sideNumber].toString();
	}
	
	private static OfflinePlayer setScore(Objective obj, String fakePlayer, int score) {
		OfflinePlayer fakeOfflinePlayer = new FakeOfflinePlayer(fakePlayer);
		obj.getScore(fakeOfflinePlayer).setScore(score);
		return fakeOfflinePlayer;
	}
	
	
	public void saveSync() {
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
			Debug.color("§dErrore I/O durante il salvataggio di " + getName());
		};
	}
	
	
	@SuppressWarnings("deprecation")
	public void saveAsync() {
		Bukkit.getScheduler().scheduleAsyncDelayedTask(QuakeCraft.plugin, new Runnable() { @Override

		public void run() {
			
			try {
				
				save();
				saveTries = 0;
				
			} catch (IOException e) {
				
				saveTries++;
				e.printStackTrace();
				Debug.color("§dErrore I/O durante il salvataggio di " + getName());
				
				if (saveTries > 5) return;
				
				Bukkit.getScheduler().scheduleAsyncDelayedTask(QuakeCraft.plugin, new Runnable() { @Override
				public void run() {
					
					//retry later...and continues on...
					saveAsync();
					
				}}, 10L);
			}
		}});
	}
	
}
