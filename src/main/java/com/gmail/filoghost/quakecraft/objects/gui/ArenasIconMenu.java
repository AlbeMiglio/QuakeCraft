package com.gmail.filoghost.quakecraft.objects.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import wild.api.item.ItemBuilder;

import com.gmail.filoghost.quakecraft.QuakeCraft;
import com.gmail.filoghost.quakecraft.enums.ArenaType;
import com.gmail.filoghost.quakecraft.enums.StainedClayColor;
import com.gmail.filoghost.quakecraft.objects.arenas.Arena;

public class ArenasIconMenu implements AbstractMenu {
    
	Inventory inventory;
	ItemStack infoItem;
	List<Arena> orderedArenas;
    
	private String menuName;
	
    public ArenasIconMenu(String menuName) {
    	this.menuName = menuName;
    	infoItem = ItemBuilder.of(Material.BOOK_AND_QUILL).name("§d§lArene disponibili")
    													.lore("§7Passa sopra i blocchi per visualizzare il nome",
    														  "§7delle arene e i dettagli. Le arene evidenziate",
    														  "§7sono quelle che devono ancora iniziare, e",
    														  "§7all'interno delle quali c'è almeno un giocatore.").build();
    	orderedArenas = getOrderedArenas();
    	inventory = Bukkit.createInventory(null, 54, menuName);
    	update();
    }
    
    private static ItemStack getItemStack(Arena arena) {
    	ItemStack item = new ItemStack(Material.STAINED_CLAY);
    	
    	StainedClayColor color = StainedClayColor.GREEN; //default
    	
    	int playersInside = arena.gamers.size();
    	String status = "-";
    	switch (arena.getState()) {
			case END:
				color = StainedClayColor.RED;
				status = "§cPartita in corso.";
				break;
			case GAME:
				color = StainedClayColor.RED;
				status = "§cPartita in corso.";
				break;
			case PREGAME:
				
				if (arena.gameTimer.isStarted()) {
					status = "§aInizia fra " + arena.gameTimer.getFormattedTimeForMenu() + ".";
				} else {
					status = "§f" + arena.minGamers + " giocatori per iniziare.";
				}
				
				if (playersInside > 0) {
					if (playersInside < arena.maxGamers) {
						color = StainedClayColor.YELLOW;
					} else {
						color = StainedClayColor.ORANGE;
					}
				} else {
					color = StainedClayColor.GREEN;
				}

				break;
    	}
    	
    	
    	
    	if (playersInside > 1) {
    		//si può vedere solo quando è più di 1
    		item.setAmount(playersInside);
    	}
    	
    	item.setDurability(color.getData());
    	
    	ItemMeta meta = item.getItemMeta();
    	meta.setDisplayName("§6§l" + arena.getName());
    	meta.setLore(Arrays.asList("§7" + playersInside + "/" + arena.maxGamers, "§7Tipo: " + arena.getType().getName(), "", status));
    	item.setItemMeta(meta);
    	return item;
    	
    }
    
    public void open(Player player, String menuName) {
    	player.openInventory(inventory);
    }
    
    private List<Arena> getOrderedArenas() {
    	Collection<Arena> arenas = QuakeCraft.arenaMap.values();
    	List<Arena> orderedArenas = new ArrayList<Arena>();
    	for (ArenaType type : ArenaType.values()) {
    		for (Arena a : arenas) {
    			if (a.getType() == type) {
    				orderedArenas.add(a);
    			}
    		}
    	}
    	return orderedArenas;
    }
    
    public void refreshOrderedArenas() {
    	orderedArenas = getOrderedArenas();
    	inventory = Bukkit.createInventory(null, 54, menuName);
    	update();
    }
    
    public void update() {
    	inventory.clear();
    	
    	int index = 0;
    	ArenaType type = null;
    	for (Arena arena : orderedArenas) {
    		if (type == null) {
    			type = arena.getType();
    		}
    		
    		if (arena.getType() != type) {
    			// diverso dal precedente
    			index = round9Up(index);
    		}
    		
    		type = arena.getType();
    		inventory.setItem(index, getItemStack(arena));
    		index++;
    	}
    	
    	inventory.setItem(53, infoItem);
    }

	public void onClick(Player clicker, ItemStack currentItem) {

		String arenaName = ChatColor.stripColor(currentItem.getItemMeta().getDisplayName());
		Arena arena = QuakeCraft.getArenaByName(arenaName);
		if (arena != null) {
			arena.addGamer(clicker);
		}
	}
	
	private int round9Up(int i) {
		if (i % 9 == 0) {
			return i;
		}
		
		return (i + (9 - i%9));
	}
    
}
