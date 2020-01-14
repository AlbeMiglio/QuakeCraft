package com.gmail.filoghost.quakecraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.filoghost.quakecraft.QuakeCraft;
import com.gmail.filoghost.quakecraft.constants.Lang;
import com.gmail.filoghost.quakecraft.enums.ArenaType;
import com.gmail.filoghost.quakecraft.enums.GameState;
import com.gmail.filoghost.quakecraft.objects.arenas.Arena;
import com.gmail.filoghost.quakecraft.objects.player.QuakePlayer;
import com.gmail.filoghost.quakecraft.utils.Utils;

public class SpawnCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(Lang.PLAYER_ONLY_COMMAND);
			return true;
		}
		Player player = (Player) sender;
		Arena arena = QuakeCraft.getArenaByPlayer(player);
		
		if (arena != null)
			arena.removeGamer(player);
		
		if (arena != null && arena.getState() == GameState.GAME) {
			Utils.toTheLobby(QuakePlayer.get(player), false, false);
			
			if (arena.getType() != ArenaType.PVP)
				player.sendMessage("§c§oHai interrotto la partita, non hai ricevuto Coins o uccisioni.");
			else
				player.sendMessage("§c§oHai interrotto la partita.");
			
			
		} else {
			Utils.toTheLobby(QuakePlayer.get(player), true, false);
		}
		
		return true;
	}
}
