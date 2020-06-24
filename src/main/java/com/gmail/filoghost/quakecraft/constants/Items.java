package com.gmail.filoghost.quakecraft.constants;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class Items {

	public static ItemStack RED_HELMET = getColoredArmor(Material.LEATHER_HELMET, Color.RED);
	public static ItemStack RED_BODY = getColoredArmor(Material.LEATHER_CHESTPLATE, Color.RED);
	public static ItemStack RED_LEGGINGS = getColoredArmor(Material.LEATHER_LEGGINGS, Color.RED);
	public static ItemStack RED_BOOTS = getColoredArmor(Material.LEATHER_BOOTS, Color.RED);
	
	public static ItemStack BLUE_HELMET = getColoredArmor(Material.LEATHER_HELMET, Color.BLUE);
	public static ItemStack BLUE_BODY = getColoredArmor(Material.LEATHER_CHESTPLATE, Color.BLUE);
	public static ItemStack BLUE_LEGGINGS = getColoredArmor(Material.LEATHER_LEGGINGS, Color.BLUE);
	public static ItemStack BLUE_BOOTS = getColoredArmor(Material.LEATHER_BOOTS, Color.BLUE);
	
	public static ItemStack RED_WOOL = new ItemStack(Material.WOOL, 1, (short) 14);
	public static ItemStack BLUE_WOOL = new ItemStack(Material.WOOL, 1, (short) 11);
	
	
	private static ItemStack getColoredArmor(Material mat, Color color) {
		ItemStack item = new ItemStack(mat);
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(color);
		item.setItemMeta(meta);
		return item;
	}
	
}
