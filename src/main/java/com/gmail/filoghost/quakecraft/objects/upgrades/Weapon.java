package com.gmail.filoghost.quakecraft.objects.upgrades;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.filoghost.quakecraft.enums.UpgradeType;

public final class Weapon extends Upgrade {

	private ItemStack weapon;
	
	public Weapon(Integer ID, String name, String description, Material material, int price) {
		this(ID, name, description, new ItemStack(material), price);
	}
	
	public Weapon(Integer ID, String name, String description, ItemStack item, int price) {
		super(ID, name, description, item, price);
		weapon = new ItemStack(item);
		ItemMeta itemMeta = weapon.getItemMeta();
		itemMeta.setDisplayName("§a" + name);
		//itemMeta.setLore(super.getDescription());
		weapon.setItemMeta(itemMeta);
	}
	
	public ItemStack getWeapon() {
		return weapon;
	}
	
	@Override
	public UpgradeType getType() {
		return UpgradeType.WEAPON;
	}

}
