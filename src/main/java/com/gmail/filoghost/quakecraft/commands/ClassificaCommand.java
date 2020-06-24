package com.gmail.filoghost.quakecraft.commands;

import java.util.List;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.filoghost.quakecraft.utils.Ranking;

public class ClassificaCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 0) {
			sender.sendMessage("§6§m-----§r §6§lComandi classifica §6§m-----");
			sender.sendMessage("§e/classifica uccisioni");
			sender.sendMessage("§e/classifica vittorie");
			sender.sendMessage("§e/stats §7- Le tue statistiche");
			sender.sendMessage("");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("uccisioni")) {
			sender.sendMessage("§6§m-----§r §6§lClassifica uccisioni §6§m-----");
			List<Entry<String, Integer>> list = Ranking.getTopKills(15);
			int size = list.size() - 1;
			for (int i = 0; i < size; i++) {
				Entry<String, Integer> entry = list.get(i);
				sender.sendMessage("§8" + (i+1) + ". §7" + entry.getValue() + " §8- §f" + entry.getKey());
			}
			sender.sendMessage("");
		}
		
		if (args[0].equalsIgnoreCase("vittorie")) {
			sender.sendMessage("§6§m-----§r §6§lClassifica vittorie §6§m-----");
			List<Entry<String, Integer>> list = Ranking.getTopWins(15);
			int size = list.size() - 1;
			for (int i = 0; i < size; i++) {
				Entry<String, Integer> entry = list.get(i);
				sender.sendMessage("§8" + (i+1) + ". §7" + entry.getValue() + " §8- §f" + entry.getKey());
			}
			sender.sendMessage("");
		}
		
		return true;
		
	}
	
}
