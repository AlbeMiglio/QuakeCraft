package com.gmail.filoghost.quakecraft.utils;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.filoghost.quakecraft.constants.Numbers;

public class PotionUtils {

	public static void giveSpeed(Player player, int amplifier) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, amplifier, true), true);
	}
	
	public static void giveRespawnResistance(Player player) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Numbers.RESPAWN_PROTECTION_TICKS, 0, false), true);
	}
	
}
