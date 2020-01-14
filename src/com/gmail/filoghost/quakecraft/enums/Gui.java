package com.gmail.filoghost.quakecraft.enums;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.quakecraft.constants.Lang;
import com.gmail.filoghost.quakecraft.objects.gui.AbstractMenu;
import com.gmail.filoghost.quakecraft.objects.gui.ArenasIconMenu;
import com.gmail.filoghost.quakecraft.objects.gui.ArmorShopMenu;
import com.gmail.filoghost.quakecraft.objects.gui.BaseShopMenu;
import com.gmail.filoghost.quakecraft.objects.gui.ExplosionShopMenu;
import com.gmail.filoghost.quakecraft.objects.gui.HatShopMenu;
import com.gmail.filoghost.quakecraft.objects.gui.TrailShopMenu;
import com.gmail.filoghost.quakecraft.objects.gui.WeaponShopMenu;

public enum Gui {

	SHOP_MAIN (Lang.GUI_BASE_SHOP, new BaseShopMenu()),
	SHOP_WEAPON (Lang.GUI_WEAPON_SHOP, new WeaponShopMenu()),
	SHOP_ARMOR (Lang.GUI_ARMOR_SHOP, new ArmorShopMenu()),
	SHOP_TRAIL (Lang.GUI_TRAIL_SHOP, new TrailShopMenu()),
	SHOP_HAT (Lang.GUI_HAT_SHOP, new HatShopMenu()),
	SHOP_EXPLOSION (Lang.GUI_EXPLOSION_SHOP, new ExplosionShopMenu()),
	ARENAS (Lang.GUI_ARENAS, new ArenasIconMenu(Lang.GUI_ARENAS));
	
	private AbstractMenu menu;
	private String uniqueName;
	
	private Gui(String uniqueName, AbstractMenu menu) {
		this.uniqueName = uniqueName;
		this.menu = menu;
	}
	
	public AbstractMenu getMenu() {
		return menu;
	}
	
	public void open(Player player) {
		menu.open(player, uniqueName);
	}
	
	public void onClick(Player clicked, ItemStack clickedItem) {
		menu.onClick(clicked, clickedItem);
	}

	public String getName() {
		return uniqueName;
	}
}
