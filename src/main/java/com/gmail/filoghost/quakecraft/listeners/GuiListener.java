package com.gmail.filoghost.quakecraft.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;

import com.gmail.filoghost.quakecraft.QuakeCraft;
import com.gmail.filoghost.quakecraft.constants.Permissions;
import com.gmail.filoghost.quakecraft.enums.Gui;

public class GuiListener implements Listener {
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void inventory(InventoryClickEvent event) {
		
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
			return;
		}
		
		Player clicker = (Player) event.getWhoClicked();
		
		if (QuakeCraft.isPlaying(clicker) && !clicker.hasPermission(Permissions.ADMIN)) {
			// Non può muovere niente durante la partita se non è admin
			event.setCancelled(true);
			return;
		}
		
		if (event.getSlotType() == SlotType.ARMOR) {
			event.setCancelled(true);
			return;
		}
		
		Inventory inv = event.getInventory();
	
		for (Gui gui : Gui.values()) {
			
			if (inv.getTitle().equals(gui.getName())) {
				event.setCancelled(true);
				
				if (event.getRawSlot() < inv.getSize()) {
					gui.onClick(clicker, event.getCurrentItem());
				}
			}
		}
	}

}
