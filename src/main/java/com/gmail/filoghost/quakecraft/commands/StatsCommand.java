package com.gmail.filoghost.quakecraft.commands;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.filoghost.quakecraft.objects.player.QuakePlayer;

public class StatsCommand implements CommandExecutor {

	private static final DecimalFormat format = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH));
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { try {
		
		Parser.isPlayer(sender);
		Player player = (Player) sender;
		
		QuakePlayer quakePlayer = QuakePlayer.get(player);
		player.sendMessage("§6§m-----§r §6§lLe tue statistiche §6§m-----");
		player.sendMessage("§e§lCoins:§f " + quakePlayer.getCoins());
		player.sendMessage("§e§lVittorie:§f " + quakePlayer.getWins());
		player.sendMessage("§e§lUccisioni:§f " + quakePlayer.getKills());
		player.sendMessage("§e§lMorti:§f " + quakePlayer.getDeaths());
		player.sendMessage("§e§lRapporto Uccisioni/Morti:§f " + calculateKDR(quakePlayer.getKills(), quakePlayer.getDeaths()));
		player.sendMessage("");
		player.sendMessage("§7Classifica globale: §f/classifica");
		player.sendMessage("");
		return true;

	} catch (ArgumentException ex) {
		sender.sendMessage(ex.getMessage());
		return true;
	}}
	
	public static String calculateKDR(int kills, int deaths) {
		if (kills <= 0) {
			return "0";
		}
		
		if (deaths <= 0) {
			return "\u221E";
		}
		
		return format.format((double) kills / (double) deaths);
	}
	
}
