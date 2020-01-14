package com.gmail.filoghost.quakecraft.objects.upgrades;

import org.bukkit.FireworkEffect;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.quakecraft.enums.UpgradeType;

public class Explosion extends Upgrade {

	private FireworkEffect[] effects;
	
	public Explosion(Integer ID, String name, String description, ItemStack icon, int price, FireworkEffect... effects) {
		super(ID, name, description, icon, price);
		this.effects = effects;
	}
	
	public FireworkEffect[] getEffects() {
		return effects;
	}

	@Override
	public UpgradeType getType() {
		return UpgradeType.EXPLOSION;
	}

}
