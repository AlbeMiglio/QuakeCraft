package com.gmail.filoghost.quakecraft.objects.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.filoghost.quakecraft.constants.Lang;
import com.gmail.filoghost.quakecraft.enums.Gui;
import com.gmail.filoghost.quakecraft.enums.UpgradeType;
import com.gmail.filoghost.quakecraft.objects.player.QuakePlayer;
import com.gmail.filoghost.quakecraft.objects.upgrades.Upgrade;
import com.gmail.filoghost.quakecraft.utils.Utils;

public class ExplosionShopMenu implements AbstractMenu {

    public void open(Player player, String menuName) {
    	
    	QuakePlayer quakePlayer = QuakePlayer.get(player);
    	
    	Inventory inventory;
    	boolean newInv = false;
    	if (player.getOpenInventory().getTitle().equals(menuName)) {
    		inventory = player.getOpenInventory().getTopInventory();
    		inventory.clear();
    	} else {
    		newInv = true;
    		inventory = Bukkit.createInventory(player, 27, menuName);
    	}
        
        int index = 0;
        
        for (Upgrade upgrade : Upgrade.list) {
        	if (upgrade.getType() == UpgradeType.EXPLOSION) {
        		
        		ItemStack singleIcon = upgrade.getIcon();
            	ItemMeta iconMeta = singleIcon.getItemMeta();
            	
            	String displayName = upgrade.getName();
            	List<String> lore = upgrade.getDescription();
            	
            	if (!quakePlayer.ownsUpgrade(upgrade)) {
            		lore.add("");
            		lore.add("§7Prezzo: §6" + (upgrade.getPrice() > 0 ? Integer.toString(upgrade.getPrice()) : "Gratis"));
            		displayName = "§c§l" + displayName;
            	} else {
            		displayName = "§a§l" + displayName;
            		
            		if (quakePlayer.isEquipped(upgrade)) {
					lore.add("");
					lore.add(Lang.EQUIPPED_CHECKMARK);
				}
            	}
            	
            	if (upgrade.isVipOnly()) {
            		displayName = "§6§l[VIP] " + displayName;
            	}
        		
        		iconMeta.setDisplayName(displayName);
            	iconMeta.setLore(lore);
            	
            	singleIcon.setItemMeta(iconMeta);
            	inventory.setItem(index, singleIcon);
            	
            	index++;
        	}
        }
        
        inventory.setItem(inventory.getSize() - 1, Utils.goBack);
        if (newInv) {
        	player.openInventory(inventory);
        }
    }

	@Override
	public void onClick(Player clicker, ItemStack clickedItem) {
		if (clickedItem.isSimilar(Utils.goBack)) {
			Gui.SHOP_MAIN.open(clicker);
		} else {
			
			String strippedName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).replace("[VIP]", "").trim();

			for (Upgrade upgrade : Upgrade.list) {
				if (upgrade.getType() == UpgradeType.EXPLOSION) {
					if (upgrade.getName().equals(strippedName)) {
						QuakePlayer.get(clicker).clickedOnUpgrade(upgrade);
						return;
					}
				}
			}
		}
	}
}
