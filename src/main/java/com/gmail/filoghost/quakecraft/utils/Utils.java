package com.gmail.filoghost.quakecraft.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Scoreboard;

import wild.api.WildCommons;
import wild.api.bridges.CosmeticsBridge;
import wild.api.bridges.CosmeticsBridge.Status;
import wild.api.item.ItemBuilder;

import com.gmail.filoghost.quakecraft.Configuration;
import com.gmail.filoghost.quakecraft.QuakeCraft;
import com.gmail.filoghost.quakecraft.constants.Lang;
import com.gmail.filoghost.quakecraft.constants.Numbers;
import com.gmail.filoghost.quakecraft.objects.player.Configurable;
import com.gmail.filoghost.quakecraft.objects.player.QuakePlayer;
import com.gmail.filoghost.quakecraft.runnables.DisplayCoinsTask;

public class Utils {
	
	public static Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
	public static Random random = new Random();
	public static ItemStack book;
	
	public static final ItemStack
	
			shop = ItemBuilder.of(Material.EMERALD)
					.name("§aShop §7(Click destro)")
					.lore("§7Per aprire fai click con mouse destro", "§7mentre tieni l'oggetto in mano.")
					.build(),
					
			blazeRod = ItemBuilder.of(Material.WOOD_PICKAXE)
					.name("§aMitragliatrice")
					.build(),
	
			arenaNavigator = ItemBuilder.of(Material.PAPER)
						.name("§aArene §7(Click destro)")
						.lore("§7Per aprire fai click con mouse destro", "§7mentre tieni l'oggetto in mano.")
						.build(),
						
			goBack = ItemBuilder.of(Material.ARROW)
						.name("§fTorna al menù precedente.").build();
	
	public static FileConfiguration loadFileOnly(String path) {
		
		if (!path.endsWith(".yml")) path += ".yml";
		
		File file = new File(QuakeCraft.plugin.getDataFolder(), path);
		
		if (!file.exists()) {
			Debug.ln("The file does not exist: " + path);
			return null;
		}
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		return config;
		
	}
	
	public static List<String> createList(String... lines) {
		List<String> list = new ArrayList<>();
		for (String line : lines) {
			list.add(line);
		}
		return list;
	}
	
	public static FileConfiguration loadArenaFile(String arenaName) {
		return loadFileOnly(Lang.ARENAS_FOLDER + File.separator + arenaName.toLowerCase());
	}
	
	public static FileConfiguration loadFile(String path) {
		
		if (!path.endsWith(".yml")) path += ".yml";
		
		File file = new File(QuakeCraft.plugin.getDataFolder(), path);
		
		if (!file.exists()) {
			try	{
				QuakeCraft.plugin.saveResource(path, false);
			}
			catch (Exception e) {
				e.printStackTrace();
		    }
		}
			 
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		return config;
	}
	
	public static String getMethodName() {
		return Thread.currentThread().getStackTrace()[3].getMethodName();
	}
	
	public static FileConfiguration createDefaultFileAndLoad(File f) {
		
		FileConfiguration config = null;
			
		if (!f.exists()) {
			try {
				f.createNewFile();
				Configurable.DEFAULT_CONFIGURATION.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			config = YamlConfiguration.loadConfiguration(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return config;
	}
	
	public static void removeNonPlayers() {
		for (World world : Bukkit.getWorlds()) {
			for (Entity entity : world.getEntities()) {
				if (!entity.getType().equals(EntityType.PLAYER)) entity.remove();
			}
		}
	}
	
	public static void clearPlayer(Player player) {
		if (player.getGameMode() != GameMode.SURVIVAL) {
			player.setGameMode(GameMode.SURVIVAL);
		}
		WildCommons.clearInventoryFully(player);
		player.setHealth(20.0);
		player.setFoodLevel(20);
		player.setSaturation(20F);
		player.setLevel(0);
		player.setExp(0);
		for (PotionEffect effect : player.getActivePotionEffects()) {
			player.removePotionEffect(effect.getType());
		}
	}
	
	public static void setNight(Player player) {
		player.setPlayerTime(14000, false);
	}
	
	
	public static void toTheLobby(QuakePlayer player, boolean message, boolean scheduledScoreboard) {
		
		clearPlayer(player.getBase());
		giveLobbyStuff(player);
		
		player.getBase().resetPlayerTime();
		player.getBase().teleport(Configuration.lobby);
		PotionUtils.giveSpeed(player.getBase(), 0);
		if (scheduledScoreboard) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(QuakeCraft.plugin, new DisplayCoinsTask(player), 1L);
		} else {
			player.displayCoins();
		}
		
		if (message) {
			player.sendMessage(Lang.TELEPORTED_TO_SPAWN);
		}
	}
	
	public static void giveLobbyStuff(QuakePlayer player) {
		Inventory inv = player.getInventory();
		
		inv.setItem(0, book);
		inv.setItem(1, shop);
		inv.setItem(2, arenaNavigator);
		CosmeticsBridge.giveCosmeticsItems(inv);
		
		player.wearHat();
		player.wearArmor();
	}

	public static void setValue(Object instance, String fieldName, Object value) {
		try {
			Field field = instance.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(instance, value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }
	
	public static boolean isInCylinder(Player player, double x, double y, double z) {
		
		Location loc = player.getLocation();
		double py = loc.getY();
		
		if (py > y - Numbers.EXPLOSION_HEIGHT_DOWN && py < y + Numbers.EXPLOSION_HEIGHT) {
			
			double px = loc.getX();
			double pz = loc.getZ();
			
			double distanceSquared = (px - x)*(px - x) + (pz - z)*(pz - z);
		
			if (distanceSquared < Numbers.EXPLOSION_RADIUS)
				return true;
			else
				return false;

		} else {
			return false;
		}
	}
	
	public static void setHeadName(Block block, String name) {
		if (block  == null || name == null) return;
		BlockState state = block.getState();
		if (state instanceof Skull) {
			((Skull) state).setOwner(name);
			state.update();
		}
	}
	
	public static void setSign(Block block, String first, String second, String third, String fourth) {
		if (block  == null) return;
		BlockState state = block.getState();
		if (state instanceof Sign) {
			Sign sign = (Sign) state;
			sign.setLine(0, first);
			sign.setLine(1, second);
			sign.setLine(2, third);
			sign.setLine(3, fourth);
			state.update();
		}
	}

	
	public static String parseSymbols(String input) {
		
		return input.replace("&", "§")
		.replace("<3", "❤")
		.replace("[*]", "★")
		.replace("[**]", "✹")
		.replace("[p]", "●")
		.replace("[v]", "✔")
		.replace("[+]", "♦")
		.replace("[++]", "✦");

	}
	
	
	static {
		book = new ItemStack(Material.WRITTEN_BOOK);
		List<String> content = (loadFile("book.yml")).getStringList("content");
		BookMeta bm = (BookMeta) book.getItemMeta();
		bm.setTitle("§aTutorial");
		bm.setAuthor("QuakeCraft");
		
		List<String> pages = new ArrayList<String>();
		pages.add("");
		
		for(String line : content)  {
			if (line.equals("<newpage>")) {
				pages.add("");
			} else {
				line = line.replace("&", "§").replace("->", "➡");
				int index = pages.size()-1;
				pages.set(index, pages.get(index) + line + "\n");
			}
		}
		
		bm.setPages(pages);
		book.setItemMeta(bm);
	}

	public static boolean hasPieceOfArmor(Player player) {
		PlayerInventory inv = player.getInventory();
		return inv.getHelmet() != null || inv.getChestplate() != null || inv.getLeggings() != null || inv.getBoots() != null;
	}
}
