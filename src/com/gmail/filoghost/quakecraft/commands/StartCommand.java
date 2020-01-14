package com.gmail.filoghost.quakecraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.filoghost.quakecraft.QuakeCraft;
import com.gmail.filoghost.quakecraft.constants.Permissions;
import com.gmail.filoghost.quakecraft.enums.GameState;
import com.gmail.filoghost.quakecraft.objects.arenas.Arena;

public class StartCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { try {
		
		Parser.isPlayer(sender);
		Player player = (Player) sender;
		
		Parser.checkPerm(player, Permissions.START);
		Arena arena = QuakeCraft.getArenaByPlayer(player);
		Parser.notNull(arena, "Non sei in una arena.");
		
		if (arena.getState() != GameState.PREGAME) {
			player.sendMessage("§cL'arena è già iniziata.");
			return true;
		}
		
		if (!arena.gameTimer.isStarted()) {
			player.sendMessage("§cNon è ancora partito il countdown.");
			return true;
		}

		arena.gameTimer.stopTask();
		arena.start();
		return true;
		
	} catch (ArgumentException ex) {
		sender.sendMessage(ex.getMessage());
		return true;
	}}
}
