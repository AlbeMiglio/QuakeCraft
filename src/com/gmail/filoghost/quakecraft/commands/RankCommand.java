package com.gmail.filoghost.quakecraft.commands;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.filoghost.quakecraft.Configuration;
import com.gmail.filoghost.quakecraft.QuakeCraft;
import com.gmail.filoghost.quakecraft.constants.ConfigNodes;

public class RankCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { try {
		
		Parser.isPlayer(sender);
		Player player = (Player) sender;
		
		if (args.length < 1) {
			player.sendMessage("§6========== Comandi /rank ==========");
			player.sendMessage("§e/rank killer-head <1 | 2 | 3>§7 - Imposta la testa dei classificati.");
			player.sendMessage("§e/rank killer-sign <1 | 2 | 3>§7 - Imposta il cartello dei classificati.");
			player.sendMessage("§e/rank winner-head <1 | 2 | 3>§7 - Imposta la testa dei classificati.");
			player.sendMessage("§e/rank winner-sign <1 | 2 | 3>§7 - Imposta il cartello dei classificati.");
			return true;
		}
		

		if (args[0].equalsIgnoreCase("killer-head")) {
			Parser.argumentLength(args, 2, "Utilizzo corretto: /" + label + " killer-head <1 | 2 | 3>");
			Block block = player.getTargetBlock((Set<Material>) null, 64);
			Parser.isTrue(block.getType() == Material.SKULL, "Non stai guardando una testa.");
			
			if (args[1].equalsIgnoreCase("1")) {
				Configuration.saveBlock(QuakeCraft.plugin.getConfig(), ConfigNodes.FIRST_KILLER_HEAD, block);
				player.sendMessage("§aImpostata la prima testa.");
			} else if (args[1].equalsIgnoreCase("2")) {
				Configuration.saveBlock(QuakeCraft.plugin.getConfig(), ConfigNodes.SECOND_KILLER_HEAD, block);
				player.sendMessage("§aImpostata la seconda testa.");
			} else if (args[1].equalsIgnoreCase("3")) {
				Configuration.saveBlock(QuakeCraft.plugin.getConfig(), ConfigNodes.THIRD_KILLER_HEAD, block);
				player.sendMessage("§aImpostata la terza testa.");
			} else {
				player.sendMessage("§cUtilizzo corretto: /" + label + " killer-head <1 | 2 | 3>");
			}
			return true;
		}
		
		if (args[0].equalsIgnoreCase("killer-sign")) {
			Parser.argumentLength(args, 2, "Utilizzo corretto: /" + label + " killer-sign <1 | 2 | 3>");
			Block block = player.getTargetBlock((Set<Material>) null, 64);
			Parser.isTrue(block.getType() == Material.WALL_SIGN, "Non stai guardando un cartello a muro.");
			
			if (args[1].equalsIgnoreCase("1")) {
				Configuration.saveBlock(QuakeCraft.plugin.getConfig(), ConfigNodes.FIRST_KILLER_SIGN, block);
				player.sendMessage("§aImpostato il primo cartello.");
			} else if (args[1].equalsIgnoreCase("2")) {
				Configuration.saveBlock(QuakeCraft.plugin.getConfig(), ConfigNodes.SECOND_KILLER_SIGN, block);
				player.sendMessage("§aImpostato il secondo cartello.");
			} else if (args[1].equalsIgnoreCase("3")) {
				Configuration.saveBlock(QuakeCraft.plugin.getConfig(), ConfigNodes.THIRD_KILLER_SIGN, block);
				player.sendMessage("§aImpostato il terzo cartello.");
			} else {
				player.sendMessage("§cUtilizzo corretto: /" + label + " killer-sign <1 | 2 | 3>");
			}
			return true;
		}
		
		
		if (args[0].equalsIgnoreCase("winner-head")) {
			Parser.argumentLength(args, 2, "Utilizzo corretto: /" + label + " winner-head <1 | 2 | 3>");
			Block block = player.getTargetBlock((Set<Material>) null, 64);
			Parser.isTrue(block.getType() == Material.SKULL, "Non stai guardando una testa.");
			
			if (args[1].equalsIgnoreCase("1")) {
				Configuration.saveBlock(QuakeCraft.plugin.getConfig(), ConfigNodes.FIRST_WINNER_HEAD, block);
				player.sendMessage("§aImpostata la prima testa.");
			} else if (args[1].equalsIgnoreCase("2")) {
				Configuration.saveBlock(QuakeCraft.plugin.getConfig(), ConfigNodes.SECOND_WINNER_HEAD, block);
				player.sendMessage("§aImpostata la seconda testa.");
			} else if (args[1].equalsIgnoreCase("3")) {
				Configuration.saveBlock(QuakeCraft.plugin.getConfig(), ConfigNodes.THIRD_WINNER_HEAD, block);
				player.sendMessage("§aImpostata la terza testa.");
			} else {
				player.sendMessage("§cUtilizzo corretto: /" + label + " winner-head <1 | 2 | 3>");
			}
			return true;
		}
		
		if (args[0].equalsIgnoreCase("winner-sign")) {
			Parser.argumentLength(args, 2, "Utilizzo corretto: /" + label + " winner-sign <1 | 2 | 3>");
			Block block = player.getTargetBlock((Set<Material>) null, 64);
			Parser.isTrue(block.getType() == Material.WALL_SIGN, "Non stai guardando un cartello a muro.");
			
			if (args[1].equalsIgnoreCase("1")) {
				Configuration.saveBlock(QuakeCraft.plugin.getConfig(), ConfigNodes.FIRST_WINNER_SIGN, block);
				player.sendMessage("§aImpostato il primo cartello.");
			} else if (args[1].equalsIgnoreCase("2")) {
				Configuration.saveBlock(QuakeCraft.plugin.getConfig(), ConfigNodes.SECOND_WINNER_SIGN, block);
				player.sendMessage("§aImpostato il secondo cartello.");
			} else if (args[1].equalsIgnoreCase("3")) {
				Configuration.saveBlock(QuakeCraft.plugin.getConfig(), ConfigNodes.THIRD_WINNER_SIGN, block);
				player.sendMessage("§aImpostato il terzo cartello.");
			} else {
				player.sendMessage("§cUtilizzo corretto: /" + label + " winner-sign <1 | 2 | 3>");
			}
			return true;
		}
		

		player.sendMessage("§cComando non trovato. /rank per la lista dei comandi.");
		return true;
		
	} catch (ArgumentException ex) {
		sender.sendMessage(ex.getMessage());
		return true;
	}}
}
