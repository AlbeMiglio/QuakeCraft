package com.gmail.filoghost.quakecraft.objects.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface AbstractMenu {

	public void open(Player player, String menuName);
	
	public void onClick(Player clicker, ItemStack clickedItem);
	
}
