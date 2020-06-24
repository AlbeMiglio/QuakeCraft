package com.gmail.filoghost.quakecraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.filoghost.quakecraft.QuakeCraft;
import com.gmail.filoghost.quakecraft.objects.player.QuakePlayer;

public class CoinsCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { try {
		
		Parser.isPlayer(sender);
		Player player = (Player) sender;
		
		if (args.length == 0) {
			QuakePlayer quakePlayer = QuakePlayer.get(player);
			player.sendMessage("");
			player.sendMessage("");
			player.sendMessage("§8§m============================");
			player.sendMessage("§e§lCoins: §f" + quakePlayer.getCoins());
			player.sendMessage("");
			player.sendMessage("§7§oAltri comandi: /coins help");
			player.sendMessage("§8§m============================");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("help")) {
			player.sendMessage("§6Comandi d'aiuto:");
			player.sendMessage("§e/coins §7- Controlla le tue statistiche");
			player.sendMessage("§e/coins send <giocatore> <numero> §7- Manda coins agli altri");
			return true;
		}
		
		
		if (args[0].equalsIgnoreCase("send")) {
			Parser.argumentLength(args, 3, "Utilizzo corretto: /coins send <giocatore> <numero>");
			int amount = Parser.getPositiveInt(args[2]);
			Player target = Parser.getOnlinePlayer(args[1]);
			Parser.isTrue(player != target, "Non puoi mandarti i Coins da solo.");
			QuakePlayer quakePlayer = QuakePlayer.get(player);
			Parser.isTrue(quakePlayer.getCoins() >= amount, "Non hai abbastanza Coins.");
			QuakePlayer quakeTarget = QuakePlayer.get(target);
			quakePlayer.removeCoins(amount);
			quakeTarget.addCoins(amount);
			
			if (!QuakeCraft.isPlaying(player)) {
				quakePlayer.displayCoins();
			}
			if (!QuakeCraft.isPlaying(target)) {
				quakeTarget.displayCoins();
			}
			player.sendMessage("§aHai mandato " + amount + " Coins a " + target.getName() + ".");
			target.sendMessage("§aHai ricevuto " + amount + " Coins da " + player.getName() + ".");
			quakePlayer.saveAsync();
			quakeTarget.saveAsync();
			return true;
		}
		
		
		player.sendMessage("§cComando non trovato. /coins help");
		return true;
		
	} catch (ArgumentException ex) {
		sender.sendMessage(ex.getMessage());
		return true;
	}}
}
