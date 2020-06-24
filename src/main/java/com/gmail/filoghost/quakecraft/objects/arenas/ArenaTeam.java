package com.gmail.filoghost.quakecraft.objects.arenas;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import wild.api.WildCommons;
import wild.api.world.RayTrace;
import wild.api.world.SightInfo;

import com.gmail.filoghost.quakecraft.QuakeCraft;
import com.gmail.filoghost.quakecraft.constants.Items;
import com.gmail.filoghost.quakecraft.constants.Lang;
import com.gmail.filoghost.quakecraft.constants.Numbers;
import com.gmail.filoghost.quakecraft.constants.Permissions;
import com.gmail.filoghost.quakecraft.enums.ArenaType;
import com.gmail.filoghost.quakecraft.enums.GameState;
import com.gmail.filoghost.quakecraft.enums.TeamColor;
import com.gmail.filoghost.quakecraft.objects.KillStreak;
import com.gmail.filoghost.quakecraft.objects.TeamManager;
import com.gmail.filoghost.quakecraft.objects.player.QuakePlayer;
import com.gmail.filoghost.quakecraft.runnables.FireworksTask;
import com.gmail.filoghost.quakecraft.utils.Debug;
import com.gmail.filoghost.quakecraft.utils.ParticleUtils;
import com.gmail.filoghost.quakecraft.utils.Utils;


public class ArenaTeam extends Arena {
	
	public TeamManager teamManager;
	
	public ArenaTeam(FileConfiguration config, CommandSender sender) {
		super(config, sender);
		teamManager = new TeamManager(super.scoreboard);
	}

	
	@Override
	public void shotEvent(QuakePlayer shooter) {
		
		SightInfo target = RayTrace.getSightIncludePlayers(shooter.getBase(), gamers, Numbers.BOUNDING_BOX_GROWTH);
		
		double x = target.getLocation().getX();
		double y = target.getLocation().getY();
		double z = target.getLocation().getZ();
		
		
		ParticleUtils.trail(shooter.getTrail().getParticle(), shooter.getBase().getEyeLocation(), target.getLocation());
		
		List<Player> affectedPlayers = new ArrayList<Player>();
		
		//magari non l'ha preso in pieno
		if (target.getPlayer() != null && !target.getPlayer().isDead() && teamManager.areEnemy(target.getPlayer(), shooter.getBase())) {
			//pareggia il livello per le multikill
			y = target.getPlayer().getLocation().getY();
			
			if (target.getPlayer().hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
				shooter.getBase().sendMessage(Lang.ARENA_PLAYER_PROTECTED);
			} else {
				affectedPlayers.add(target.getPlayer());
			}
		}
		
		for (Player gamer : gamers) {
			if (gamer != shooter.getBase() && gamer != target.getPlayer() && teamManager.areEnemy(shooter.getBase(), gamer) && !gamer.isDead() && Utils.isInCylinder(gamer, x, y, z)) {
				
				if (gamer.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
					shooter.sendMessage(Lang.ARENA_PLAYER_PROTECTED);
				} else {
					affectedPlayers.add(gamer);
				}
				
			}
		}
		
		int amount = affectedPlayers.size();
		
		if (amount > 0) {
			
			goodHitSound(target.getLocation(), shooter);
			
			for (Player affected : affectedPlayers) {
				killEvent(shooter.getBase(), affected);
			}
			
			if (amount > 2) {
				if (amount == 2) {
					tellAll("§b" + shooter.getName() + " ha fatto una Doppia Uccisione!");
				} else {
					tellAll("§b" + shooter.getName() + " ha fatto una Uccisione Multipla (" + amount + ")!");
				}
			}
		
		} else {
			wrongHitSound(shooter.getBase().getLocation());
		}
	}
	
	@Override
	public void killEvent(Player killer, Player victim) {
		
		if (gamestate != GameState.GAME) {
			Debug.color("§dGiocatore ucciso dopo la partita: " + killer.getName() + " uccide " + victim.getName());
			return;
		}
		
		killer.sendMessage("§a» §8Hai ucciso §7" + victim.getName());
		victim.sendMessage("§c» §8Sei stato ucciso da §7" + killer.getName());
		
		KillStreak.addKill(killer);
		
		int killerConsecutiveKills = KillStreak.getConsecutiveKills(killer);
		if (killerConsecutiveKills == 5) {
			tellAll("§b" + killer.getName() + " sta facendo una strage §7(x5)!");
		}
		if (killerConsecutiveKills >= 10 && killerConsecutiveKills % 5 == 0) {
			tellAll("§b" + killer.getName() + " è inarrestabile §7(x" + killerConsecutiveKills + ")!");
		}
		
		if (KillStreak.getConsecutiveKills(victim) >= 5) {
			tellAll("§7" + killer.getName() + " ha fermato la furia di §b" + victim.getName() + "§7!");
		}
		KillStreak.resetConsecutiveKills(victim);
	
		victim.setHealth(0.0);
		Bukkit.getScheduler().runTaskLater(QuakeCraft.plugin, () -> {
			if (victim.isOnline()) {
				WildCommons.respawn(victim);
			}
		}, 20L);
		
		addDeath(victim);
		teamManager.addKill(killer);
		
		if (teamManager.getKills(teamManager.getTeamColor(killer)) >= getMaxKills()) {
			end(killer); //vince il team del killer
		}
	}
	
	@Override
	public void reset() {
		teamManager.resetAndCreate();
		super.reset();
	}
	
	@Override
	public void start() {
		teamManager.resetAndCreate();
		for (Player player : super.gamers) {
			teamManager.autoAssign(player);
		}
		super.start();
	}
	
	@Override
	public void end(Player winner) {
		
		gamestate = GameState.END;
		giveRewards(winner, false);
		
		for (Player gamer : gamers) {
			Utils.clearPlayer(gamer);
		}

		final BukkitScheduler scheduler = Bukkit.getScheduler();
		
		if (winner != null) {
			
			TeamColor winnerTeam = teamManager.getTeamColor(winner);
			String winnerTeamString;
			String color;
			if (winnerTeam == TeamColor.BLUE) {
				color = "§9";
				winnerTeamString = "blu";
			} else {
				color = "§c";
				winnerTeamString = "rossi";
			}
			
			soundAll(Sound.ENTITY_PLAYER_LEVELUP);
			Bukkit.broadcastMessage(Lang.QUAKE_PREFIX + "§f" + (winnerTeam == TeamColor.BLUE ? "I blu" : "I rossi") + " hanno vinto nell'arena " + name);
				
			for (Player player : gamers) {
				player.sendMessage(new String[] {
						"",
						Lang.GRAY_LINE_SEPARATOR,
						color + "§lVincitori:§f " + winnerTeamString,
						"",
						"§a§lUccisioni:§f " + getKills(player),
						"§a§lMorti:§f " + getDeaths(player),
						"§a§lMappa:§f " + this.name,
						Lang.GRAY_LINE_SEPARATOR,
						""
				});
			}
	
			final int fireworkTaskId = scheduler.scheduleSyncRepeatingTask(QuakeCraft.plugin, new FireworksTask(winner), 0L, 10L);
		
			scheduler.scheduleSyncDelayedTask(QuakeCraft.plugin, new Runnable(){ @Override
			public void run() {
				scheduler.cancelTask(fireworkTaskId);
			}}, 260L);
			
			scheduler.scheduleSyncDelayedTask(QuakeCraft.plugin, new Runnable(){ @Override
			public void run() {
				reset();
			}}, 300L);
		
		} else {
			reset();
		}
	}
	
	@Override
	public boolean isThereFewPlayers() {
		// Se una delle due squadre è vuota
		return teamManager.getBlueSize() == 0 || teamManager.getRedSize() == 0;
	}
	
	@Override
	public void giveRewards(Player winner, boolean syncro) {
		if (winner == null) {
			super.giveRewards(null, syncro);
			return;
			//nessun team aveva ancora vinto, ricompense normali
		}

		TeamColor winnerTeam = teamManager.getTeamColor(winner);
		
		for (Player player : gamers) {
			
			QuakePlayer quakePlayer = QuakePlayer.get(player);
			int kills = getKills(player);
			int coins = kills;
			
			if (teamManager.getTeamColor(player) == winnerTeam) {
				coins += Numbers.COINS_FOR_WIN;
				quakePlayer.addWin();
			}
			
			if (player.hasPermission(Permissions.BOOST_150)) {
				coins = (int) Math.floor(coins * 1.5);
			} else if (player.hasPermission(Permissions.BOOST_200)) {
				coins = coins * 2;
			}
			
			quakePlayer.addCoins(coins);
			quakePlayer.sendMessage("§3§o+" + coins + " Coins");
			quakePlayer.addKills(kills);
			quakePlayer.addDeaths(getDeaths(player));
			
			if (syncro)		quakePlayer.saveSync();
			else			quakePlayer.saveAsync();
		}
	}
	
	
	@Override
	public boolean removeGamer(Player player) {
		teamManager.remove(player);
		return super.removeGamer(player);
	}
	
	
	@Override
	public void createSidebar() {

	}
	
	@Override
	public void giveWeapon(Player gamer) {
		QuakePlayer.get(gamer).wearWeapon();
	}
	
	@Override
	public void giveArmor(Player gamer) {
		PlayerInventory inv = gamer.getInventory();
		if (teamManager.getTeamColor(gamer) == TeamColor.BLUE) {
			inv.setArmorContents(new ItemStack[]{Items.BLUE_BOOTS, Items.BLUE_LEGGINGS, Items.BLUE_BODY, Items.BLUE_HELMET});
			inv.setItem(8, Items.BLUE_WOOL);
		} else {
			inv.setArmorContents(new ItemStack[]{Items.RED_BOOTS, Items.RED_LEGGINGS, Items.RED_BODY, Items.RED_HELMET});
			inv.setItem(8, Items.RED_WOOL);
		}
	}
	
	@Override
	public ArenaType getType() {
		return ArenaType.TEAM;
	}
	
	@Override
	public int getMaxKills() {
		return 60;
	}

	@Override
	public int getKills(Player player) {
		return teamManager.getSingularKills(player);
	}
}
