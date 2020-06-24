package com.gmail.filoghost.quakecraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.filoghost.quakecraft.utils.Utils;

public class KillallCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Utils.removeNonPlayers();
		sender.sendMessage("§aRimossi tutti i mob.");
		return true;
	}
}
