package com.gmail.filoghost.quakecraft.objects.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import wild.api.WildCommons;
import wild.api.item.ItemBuilder;

import com.gmail.filoghost.quakecraft.enums.Gui;
import com.gmail.filoghost.quakecraft.objects.player.QuakePlayer;
import com.gmail.filoghost.quakecraft.utils.Utils;


public class BaseShopMenu implements AbstractMenu {

	private static final String HAT_MENU_ICON_NAME = "§a§lSeleziona un elmo";
	private static final String WEAPON_MENU_ICON_NAME = "§a§lSeleziona un'arma";
	private static final String ARMOR_MENU_ICON_NAME = "§a§lSeleziona un'armatura";
	private static final String TRAIL_MENU_ICON_NAME = "§a§lSeleziona una scia";
	private static final String EXPLOSION_MENU_ICON_NAME = "§a§lSeleziona un'esplosione";
	
    public void open(Player player, String menuName) {
    	
    	QuakePlayer quakePlayer = QuakePlayer.get(player);
    	
    	Inventory inventory = Bukkit.createInventory(null, 45, menuName);
    	
    	List<String> hatLore = Utils.createList("Scegli un elmo.", "Gli elmi non proteggono in partita,", "sono decorazioni estetiche.");
    	if (quakePlayer.getHat() != null && !quakePlayer.getHat().isDefault()) {
    		hatLore.add("");
    		hatLore.add("§e❒ " + quakePlayer.getHat().getName());
    	}
    	
    	List<String> weaponLore = Utils.createList("Scegli un'arma.", "Più le armi sono potenti, minore è il", "tempo che impiegano per ricaricare.");
    	if (quakePlayer.getWeapon() != null && !quakePlayer.getWeapon().isDefault()) {
    		weaponLore.add("");
    		weaponLore.add("§e❒ " + quakePlayer.getWeapon().getName());
    	}
    	
    	List<String> armorLore = Utils.createList("Scegli un'armatura.", "Le armature non proteggono in partita,", "sono decorazioni estetiche.");
    	if (quakePlayer.getArmor() != null && !quakePlayer.getArmor().isDefault()) {
    		armorLore.add("");
    		armorLore.add("§e❒ " + quakePlayer.getArmor().getName());
    	}
    	
    	List<String> trailLore = Utils.createList("Cambia il colore della scia che", "i tuoi proiettili creano quando spari.");
    	if (quakePlayer.getTrail() != null && !quakePlayer.getTrail().isDefault()) {
    		trailLore.add("");
    		trailLore.add("§e❒ " + quakePlayer.getTrail().getName());
    	}
    	
    	List<String> explosionLore = Utils.createList("Cambia il colore dell'esplosione", "quando uccidi un nemico.");
    	if (quakePlayer.getExplosion() != null && !quakePlayer.getExplosion().isDefault()) {
    		explosionLore.add("");
    		explosionLore.add("§e❒ " + quakePlayer.getExplosion().getName());
    	}
    	
    	
    	inventory.setItem(12, WildCommons.removeAttributes(ItemBuilder.of(Material.LEATHER_HELMET).name(HAT_MENU_ICON_NAME).lore(hatLore).build()));
    	inventory.setItem(20, WildCommons.removeAttributes(ItemBuilder.of(Material.WOOD_HOE).name(WEAPON_MENU_ICON_NAME).lore(weaponLore).build()));
    	inventory.setItem(21, WildCommons.removeAttributes(ItemBuilder.of(Material.LEATHER_CHESTPLATE).name(ARMOR_MENU_ICON_NAME).lore(armorLore).build()));
    	inventory.setItem(23, ItemBuilder.of(Material.INK_SACK).durability((short) 10).name(TRAIL_MENU_ICON_NAME).lore(trailLore).build());
    	inventory.setItem(24, ItemBuilder.of(Material.FIREWORK).name(EXPLOSION_MENU_ICON_NAME).lore(explosionLore).build());
    	
    	 inventory.setItem(36, ItemBuilder.of(Material.SKULL_ITEM).durability(3).name("§a§lIl tuo profilo").lore(
    			 "§fArma: §7" + (quakePlayer.getWeapon() != null ? quakePlayer.getWeapon().getName() : "-"),
    			 "§fElmo: §7" + (quakePlayer.getHat() != null ? quakePlayer.getHat().getName() : "-"),
    			 "§fArmatura: §7" + (quakePlayer.getArmor() != null ? quakePlayer.getArmor().getName() : "-"),
    			 "§fScia: §7" + (quakePlayer.getTrail() != null ? quakePlayer.getTrail().getName() : "-"),
    			 "§fEsplosione: §7" + (quakePlayer.getExplosion() != null ? quakePlayer.getExplosion().getName() : "-"),
    			 "",
    			 "§fCoins: §7" + quakePlayer.getCoins(),
    			 "§fVittorie: §7" + quakePlayer.getWins(),
    			 "§fUccisioni: §7" + quakePlayer.getKills()
    	 ).build());
    	 
        inventory.setItem(43, ItemBuilder.of(Material.EMERALD).name("§a§l" + quakePlayer.getCoins() + " Coins").lore("Uccisione = 1 Coin", "Vittoria = 10 Coins").build());
        inventory.setItem(44, ItemBuilder.of(Material.BOOK_AND_QUILL).name("§a§lInformazioni").lore("Passa con il mouse su un oggetto per leggere",
        																					  "la descrizione. Clicca sui vari oggetti per",
        																					  "aprire dei menù secondari. In questi menù",
        																					  "secondari puoi acquistare o equipaggiare",
        																					  "cliccando sugli oggetti.").build());
        
        player.openInventory(inventory);
        
    }
    
    public void onClick(Player player, ItemStack clickedItem) {
    	switch (clickedItem.getItemMeta().getDisplayName()) {
    		case WEAPON_MENU_ICON_NAME:
    			Gui.SHOP_WEAPON.open(player);
    			break;
    		case ARMOR_MENU_ICON_NAME:
    			Gui.SHOP_ARMOR.open(player);
    			break;
    		case TRAIL_MENU_ICON_NAME:
    			Gui.SHOP_TRAIL.open(player);
    			break;
    		case HAT_MENU_ICON_NAME:
    			Gui.SHOP_HAT.open(player);
    			break;
    		case EXPLOSION_MENU_ICON_NAME:
    			Gui.SHOP_EXPLOSION.open(player);
    			break;
    	}
    }
}
