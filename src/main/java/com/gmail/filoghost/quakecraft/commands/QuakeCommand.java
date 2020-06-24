package com.gmail.filoghost.quakecraft.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.gmail.filoghost.quakecraft.Configuration;
import com.gmail.filoghost.quakecraft.QuakeCraft;
import com.gmail.filoghost.quakecraft.constants.ConfigNodes;
import com.gmail.filoghost.quakecraft.constants.Lang;
import com.gmail.filoghost.quakecraft.objects.PowerUp;
import com.gmail.filoghost.quakecraft.objects.arenas.Arena;
import com.gmail.filoghost.quakecraft.objects.player.QuakePlayer;
import com.gmail.filoghost.quakecraft.utils.Debug;
import com.gmail.filoghost.quakecraft.utils.Purge;
import com.gmail.filoghost.quakecraft.utils.Ranking;
import com.gmail.filoghost.quakecraft.utils.Utils;

public class QuakeCommand implements CommandExecutor {

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { try {
		
		
		if (args.length == 0) {
			sender.sendMessage("§6========== Comandi /quake ==========");
			sender.sendMessage("§e/quake info");
			sender.sendMessage("§e/quake stats <player>");
			sender.sendMessage("§e/quake setspawn");
			sender.sendMessage("§e/quake tp <arena>");
			sender.sendMessage("§e/quake join <arena>");
			sender.sendMessage("§e/quake topcoins");
			sender.sendMessage("§e/quake topwins");
			sender.sendMessage("§e/quake purge");
			sender.sendMessage("§e/quake debug");
			if (sender instanceof ConsoleCommandSender) {
				sender.sendMessage("§c(Hidden) §e/quake reset");
				sender.sendMessage("§c(Hidden) §e/quake addcoins <player> <amount>");
				sender.sendMessage("§c(Hidden) §e/quake setcoins <player> <amount>");
				sender.sendMessage("§c(Hidden) §e/quake setkills <player> <amount>");
			}
			return true;
		}

		if (args[0].equalsIgnoreCase("info")) {
			sender.sendMessage("§8==========[ §dInformazioni Generali §8]==========");
			sender.sendMessage("§aGiocatori online: §f" + Bukkit.getOnlinePlayers().size());
			sender.sendMessage("§aGiocatori caricati: §f" + QuakePlayer.playersMap.size());
			sender.sendMessage("§aGiocatori nelle arene: §f" + QuakeCraft.playerMap.size());
			sender.sendMessage("§aArena caricate: §f" + QuakeCraft.arenaMap.size());
			sender.sendMessage("§aPowerUps attivi: §f" + PowerUp.active.size());
			return true;
		}
		
		
		if (args[0].equalsIgnoreCase("tp")) {
			Parser.isPlayer(sender);
			Parser.argumentLength(args, 2, "Utilizzo corretto: /quake tp <arena>");
			Arena arena = QuakeCraft.getArenaByName(args[1]);
			Parser.notNull(arena, "L'arena non esiste. Hai scritto maiuscole e minuscole correttamente?");
			((Player) sender).teleport(arena.getRandomSpawn());
			sender.sendMessage("§aSei stato teletrasportato nell'arena.");
			return true;
		}
		
		
		if (args[0].equalsIgnoreCase("join")) {
			Parser.isPlayer(sender);
			Parser.argumentLength(args, 2, "Utilizzo corretto: /quake join <arena>");
			Arena arena = QuakeCraft.getArenaByName(WordUtils.capitalizeFully(args[1]));
			Parser.notNull(arena, "L'arena non esiste.");
			((Player) sender).setScoreboard(arena.scoreboard);
			sender.sendMessage("§aSei entrato nell'arena come admin.");
			return true;
		}
		
		
		if (args[0].equalsIgnoreCase("stats")) {
			Parser.argumentLength(args, 2, "Utilizzo corretto: /quake stats <player>");
			Player target = Parser.getOnlinePlayer(args[1]);
			QuakePlayer quakeTarget = QuakePlayer.get(target);
			sender.sendMessage("§6Statistiche di " + target.getName() + ":");
			sender.sendMessage("§eCoins: §f" + quakeTarget.getCoins());
			sender.sendMessage("§eUccisioni: §f" + quakeTarget.getKills());
			sender.sendMessage("§eVittorie: §f" + quakeTarget.getWins());
			return true;
		}
		
		
		if (args[0].equalsIgnoreCase("addcoins") && (sender instanceof ConsoleCommandSender)) {
			Parser.argumentLength(args, 3, "Utilizzo corretto: /quake addcoins <player> <amount>");
			int amount = Parser.getPositiveInt(args[2]);
			Player target = Bukkit.getPlayerExact(args[1]);
			if (target != null && target.isOnline()) {
				QuakePlayer quakeTarget = QuakePlayer.get(target);
				quakeTarget.addCoins(amount);
				quakeTarget.displayCoins();
				quakeTarget.saveSync();
				sender.sendMessage("§aIl giocatore " + quakeTarget.getName() + " ha ora " + quakeTarget.getCoins() + " coins.");
			} else {
				File offlineFile = new File(QuakeCraft.plugin.getDataFolder(), Lang.PLAYERS_FOLDER + File.separator + args[1].toLowerCase() + ".yml");
				FileConfiguration config = Utils.createDefaultFileAndLoad(offlineFile);
				config.set(ConfigNodes.COINS, config.getInt(ConfigNodes.COINS) + amount);
				try {
					config.save(offlineFile);
				} catch (IOException e) {
					e.printStackTrace();
				};
				sender.sendMessage("§aIl giocatore " + args[1] + " ha ora " + config.getInt(ConfigNodes.COINS) + " coins. §4(Era offline)");
			}
			return true;
		}
		
		
		if (args[0].equalsIgnoreCase("setcoins") && (sender instanceof ConsoleCommandSender)) {
			Parser.argumentLength(args, 3, "Utilizzo corretto: /quake setcoins <player> <amount>");
			int amount = Parser.getPositiveIntPlusZero(args[2]);
			Player target = Parser.getOnlinePlayer(args[1]);
			QuakePlayer quakeTarget = QuakePlayer.get(target);
			quakeTarget.setCoins(amount);
			quakeTarget.displayCoins();
			quakeTarget.saveSync();
			sender.sendMessage("§aIl giocatore " + quakeTarget.getName() + " ha ora " + amount + " coins.");
			return true;
		}
		
		
		if (args[0].equalsIgnoreCase("setkills") && (sender instanceof ConsoleCommandSender)) {
			Parser.argumentLength(args, 3, "Utilizzo corretto: /quake setkills <player> <amount>");
			int amount = Parser.getPositiveIntPlusZero(args[2]);
			Player target = Parser.getOnlinePlayer(args[1]);
			QuakePlayer quakeTarget = QuakePlayer.get(target);
			quakeTarget.setKills(amount);
			quakeTarget.displayCoins();
			quakeTarget.saveSync();
			sender.sendMessage("§aIl giocatore " + quakeTarget.getName() + " ha ora " + amount + " uccisioni.");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("reset") && (sender instanceof ConsoleCommandSender)) {
			Parser.isTrue(Bukkit.getOnlinePlayers().size() == 0, "Non ci devono essere giocatori online!");
			Purge.cleanKillsDeathsWins();
			return true;
		}
		
		
		if (args[0].equalsIgnoreCase("setspawn")) {
			Parser.isPlayer(sender);
			Location newLobby = ((Player) sender).getLocation();
			Configuration.setLobby(newLobby);
			sender.sendMessage("§aSettato lo spawn.");
			return true;
		}
		
		
		if (args[0].equalsIgnoreCase("topcoins")) {
			sender.sendMessage("§a§l» Classifica Coins (primi 25)");
			List<Entry<String, Integer>> list = Ranking.getTopCoins(25);
			int size = list.size() - 1;
			for (int i = 0; i < size; i++) {
				Entry<String, Integer> entry = list.get(i);
				sender.sendMessage("§8" + (i+1) + ". §f" + entry.getKey() + "  §7- " + entry.getValue());
			}
			return true;
		}
		
		if (args[0].equalsIgnoreCase("topwins")) {
			sender.sendMessage("§a§l» Classifica Vittorie (primi 25)");
			List<Entry<String, Integer>> list = Ranking.getTopWins(25);
			int size = list.size() - 1;
			for (int i = 0; i < size; i++) {
				Entry<String, Integer> entry = list.get(i);
				sender.sendMessage("§8" + (i+1) + ". §f" + entry.getKey() + "  §7- " + entry.getValue());
			}
			return true;
		}
		
		if (args[0].equalsIgnoreCase("purge")) {
			if (Bukkit.getOnlinePlayers().size() > 0) {
				sender.sendMessage("§cNon puoi farlo mentre ci sono giocatori online.");
				return true;
			}
			sender.sendMessage("§eSto pulendo gli account vuoti...");
			Purge.purgeEmptyAccounts();
			sender.sendMessage("§aFinito!");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("debug")) {
			if (Debug.enable) {
				Debug.enable = false;
				sender.sendMessage("§eDebug disabilitato.");
			} else {
				Debug.enable = true;
				sender.sendMessage("§eDebug abilitato.");
			}
			return true;
		}
		
		sender.sendMessage("§cComando non trovato. /quake per la lista dei comandi");
		return true;
		
	} catch (ArgumentException ex) {
		sender.sendMessage(ex.getMessage());
		return true;
	}}

}
