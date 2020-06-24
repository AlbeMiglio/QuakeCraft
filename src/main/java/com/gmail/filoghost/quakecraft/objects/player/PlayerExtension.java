package com.gmail.filoghost.quakecraft.objects.player;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import com.gmail.filoghost.quakecraft.utils.Validator;

public class PlayerExtension {

	private Player base;
	
	public PlayerExtension(Player base) {
		Validator.notNull(base);
		this.base = base;
	}
	
	public void sendMessage(String message) {
		base.sendMessage(message);
	}
	
	public void playSound(Sound sound) {
		base.playSound(base.getLocation(), sound, 1, 1);
	}
	
	public void playSound(Sound sound, float pitch) {
		base.playSound(base.getLocation(), sound, 1, pitch);
	}
	
	public Player getBase() {
		return base;
	}
	
	public String getName() {
		return base.getName();
	}
	
	public PlayerInventory getInventory() {
		return base.getInventory();
	}
	
}
