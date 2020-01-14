package com.gmail.filoghost.quakecraft.objects.upgrades;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.filoghost.quakecraft.enums.UpgradeType;

public class Hat extends Upgrade {

	ItemStack hat;	
	
	public Hat(Integer ID, String name, String description, Material material, int price) {
		this(ID, name, description, new ItemStack(material), price);
	}
	
	public Hat(Integer ID, String name, String description, ItemStack stack, int price) {
		super(ID, name, description, stack, price);
		hat = new ItemStack(stack);
		ItemMeta hatMeta = hat.getItemMeta();
		hatMeta.setDisplayName("§f" + name);
		hat.setItemMeta(hatMeta);
	}

	public ItemStack getHat() {
		return hat;
	}
	
	@Override
	public UpgradeType getType() {
		return UpgradeType.HAT;
	}
}
