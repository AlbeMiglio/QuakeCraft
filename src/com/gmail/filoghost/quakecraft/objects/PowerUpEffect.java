package com.gmail.filoghost.quakecraft.objects;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.filoghost.quakecraft.QuakeCraft;
import com.gmail.filoghost.quakecraft.enums.ArenaType;
import com.gmail.filoghost.quakecraft.objects.arenas.Arena;
import com.gmail.filoghost.quakecraft.objects.arenas.ArenaTeam;
import com.gmail.filoghost.quakecraft.runnables.GiveArmorAndHatTask;
import com.gmail.filoghost.quakecraft.utils.PotionUtils;
import com.gmail.filoghost.quakecraft.utils.Utils;

public enum PowerUpEffect {
	
	SPEED ("Velocità", new ItemStack(Material.GOLD_BOOTS)) {
		
		public void apply(Player player, Arena arena) {
			player.sendMessage("§7§oHai ottenuto Velocità IV.");
			player.removePotionEffect(PotionEffectType.SLOW);
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30 * 20, 3), true);
			
			final String playerName = player.getName();
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(QuakeCraft.plugin, new Runnable() {
				public void run() {
					Player player = Bukkit.getPlayerExact(playerName);
					if (player != null && !player.isDead() && QuakeCraft.isPlaying(player)) {
						if (!player.hasPotionEffect(PotionEffectType.SLOW)) {
							player.removePotionEffect(PotionEffectType.SPEED);
							PotionUtils.giveSpeed(player, 1);
						}
					}
				}
			}, 30 * 20 - 10);
		}
	},
	
	
	JUMP ("Salto potenziato", new ItemStack(Material.FEATHER)) {

		@Override
		public void apply(Player player, Arena arena) {
			player.sendMessage("§7§oHai ottenuto Salto potenziato II.");
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 45 * 20, 1), true);
		}
		
	},

	CONFUSION_TO_OPPONENTS("Confusione", new ItemStack(Material.POTION, 1, (short) 16388)) {
		
		public void apply(Player player, Arena arena) {
			player.sendMessage("§7§oHai confuso i tuoi avversari.");
			if (arena.getType() == ArenaType.TEAM) {
				for (Player gamer : ((ArenaTeam) arena).teamManager.getEnemies(player)) {
					gamer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 20, 0), true);
				}
			} else {
				for (Player gamer : arena.gamers) {
					if (gamer != player) {
						gamer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 20, 0), true);
					}
				}
			}
		}
	},
	
	
	BLINDNESS_TO_OPPONENTS("Cecità", new ItemStack(Material.EYE_OF_ENDER)) {
		
		public void apply(Player player, Arena arena) {
			player.sendMessage("§7§oHai accecato i tuoi avversari.");
			
			if (arena.getType() == ArenaType.TEAM) {
				for (Player gamer : ((ArenaTeam) arena).teamManager.getEnemies(player)) {
					gamer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10 * 20, 0), true);
				}
			} else {
				for (Player gamer : arena.gamers) {
					if (gamer != player) {
						gamer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10 * 20, 0), true);
					}
				}
			}
		}
	},
	
	
	SLOWNESS_TO_OPPONENTS("Lentezza", new ItemStack(Material.ANVIL)) {
		
		public void apply(Player player, Arena arena) {
			player.sendMessage("§7§oHai rallentato i tuoi avversari.");
			if (arena.getType() == ArenaType.TEAM) {
				for (Player gamer : ((ArenaTeam) arena).teamManager.getEnemies(player)) {
					gamer.removePotionEffect(PotionEffectType.SPEED);
					gamer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0), false);
				}
			} else {
				for (Player gamer : arena.gamers) {
					if (gamer != player) {
						gamer.removePotionEffect(PotionEffectType.SPEED);
						gamer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0), false);
					}
				}
			}
		}
	},
	
	INVISIBILITY("Invisibilità", new ItemStack(Material.THIN_GLASS)) {
		
		public void apply(Player player, Arena arena) {
			if (Utils.hasPieceOfArmor(player)) {
				player.sendMessage("§7§oSei invisibile. E' stata nascosta l'armatura.");
			} else {
				player.sendMessage("§7§oSei invisibile.");
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10 * 20, 0), true);
			player.getInventory().setArmorContents(null);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(QuakeCraft.plugin, new GiveArmorAndHatTask(player), 10 * 20);
		}
	},
	
	HASTE("Fuoco rapido", new ItemStack(Material.DIAMOND_HOE)) {
		
		public void apply(Player player, Arena arena) {
			player.sendMessage("§7§oLa tua arma spara due volte più veloce per 10 secondi.");
			player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 10 * 20, 0), true);
		}
	};
	
	private String name;
	private ItemStack icon;
	
	private static Random random = new Random();

	PowerUpEffect(String name, ItemStack icon) {
		this.name = name;
		this.icon = icon;
	}
	
	public String getName() {
		return name;
	}
	
	public ItemStack getIcon() {
		return icon;
	}
	
	public abstract void apply(Player player, Arena arena);
	
	public static PowerUpEffect randomEffect() {
		return PowerUpEffect.values()[random.nextInt(PowerUpEffect.values().length)];
	}
	
}
