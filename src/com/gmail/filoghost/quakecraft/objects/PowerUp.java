package com.gmail.filoghost.quakecraft.objects;

import java.util.Set;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.quakecraft.QuakeCraft;
import com.gmail.filoghost.quakecraft.constants.Lang;
import com.gmail.filoghost.quakecraft.enums.GameState;
import com.gmail.filoghost.quakecraft.objects.arenas.Arena;
import com.gmail.filoghost.quakecraft.utils.ParticleUtils;
import com.google.common.collect.Sets;

public class PowerUp {

	private PowerUpEffect effect;
	private Arena arena;
	public Hologram hologram;
	
	public static Set<PowerUp> active = Sets.newHashSet();
	
	public PowerUp(PowerUpEffect effectVar, Location where, Arena arenaVar) {
		this.effect = effectVar;
		this.arena = arenaVar;
		where.add(0, 0.7, 0);
		hologram = HologramsAPI.createHologram(QuakeCraft.plugin, where);
		hologram.appendTextLine("§e§l" + effect.getName().replace("à", "a'").replace("è", "e'").toUpperCase());
		ItemLine itemLine = hologram.appendItemLine(effect.getIcon());
		
		where.getWorld().playEffect(where, Effect.MOBSPAWNER_FLAMES, 100);
		
		itemLine.setPickupHandler(new PickupHandler() {
			public void onPickup(Player player) {
				if (arena.getState() == GameState.GAME && arena.getGamers().contains(player)) {
					arena.soundAll(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
					effect.apply(player, arena);
					ParticleUtils.bigSmoke(hologram.getLocation().add(0.0, 0.0, 0.0));
					arena.tellAll(Lang.QUAKE_PREFIX + "§a" + player.getName() + " ha raccolto " + effect.getName());
				}
				
				destroy();
			}
		});
		
		active.add(this);
	}
	
	
	
	
	public void destroy() {
		hologram.delete();

		active.remove(this);
	}
	
	public boolean exists() {
		return !hologram.isDeleted();
	}
	
	public static void removeAll() {
		for (PowerUp powerUp : active) {
			powerUp.hologram.delete();
		}
		
		active.clear();
	}
}
