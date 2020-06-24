package com.gmail.filoghost.quakecraft.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.gmail.filoghost.quakecraft.QuakeCraft;
import com.gmail.filoghost.quakecraft.constants.ConfigNodes;
import com.gmail.filoghost.quakecraft.constants.Lang;
import com.gmail.filoghost.quakecraft.enums.Gui;
import com.gmail.filoghost.quakecraft.objects.gui.ArenasIconMenu;

public class ArenaCommand implements CommandExecutor {
	
	private static FileConfiguration creatingArena;
	private static File creatingArenaFile;

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { try {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cComando per soli giocatori.");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (args.length < 1) {
			player.sendMessage("§6========== Comandi /arena ==========");
			player.sendMessage("§e/arena new <nome>§7 - Seleziona l'arena da modificare");
			player.sendMessage("§e/arena settype <nome>§7 - Imposta il nome dell'arena");
			player.sendMessage("§e/arena setplayers <min> <max>§7 - Imposta il numero di giocatori");
			player.sendMessage("§e/arena setsign <nome>§7 - Imposta il cartello dell'arena");
			player.sendMessage("§e/arena addspawn <raggio>§7 - Aggiungi uno spawnpoint");
			player.sendMessage("§e/arena removespawn <numero>§7 - Rimuovi uno spawnpoint");
			player.sendMessage("§e/arena info§7 - Status dell'arena selezionata");
			player.sendMessage("§e/arena save§7 - Salva su disco");
			player.sendMessage("§e/arena load <file>§7 - Carica l'arena da disco");
			player.sendMessage("§e/arena edit <file>§7 - Modifica l'arena salvata");
			return true;
		}
		
		
		
		
		
		if (args[0].equalsIgnoreCase("new")) {
			Parser.argumentLength(args, 2, "Utilizzo corretto: /" + label + " new <nome>");
			Parser.stringLength(args[1], 3, "Il nome deve essere almeno di 3 caratteri.");
			Parser.isTrue(QuakeCraft.getArenaByName(args[1]) == null, "Arena già esistente.");
			
			try {
				creatingArenaFile = new File(QuakeCraft.plugin.getDataFolder(), Lang.ARENAS_FOLDER + File.separator + args[1].toLowerCase() + ".yml");
				creatingArena = makeDefault();
				creatingArena.set(ConfigNodes.NAME, WordUtils.capitalize(args[1]));
				player.sendMessage("§aInizio fase di creazione per l'arena \"" + WordUtils.capitalize(args[1]) + "\".");
				player.sendMessage("§eProcedi con /arena setplayers <min> <max>.");
			} catch (Exception ex) {
				ex.printStackTrace();
				player.sendMessage("§cErrore nel creare l'arena. Guarda la console.");
			}
			
			return true;
		}
		
		

		if (args[0].equalsIgnoreCase("settype")) {
			Parser.notNull(creatingArena, "Prima devi creare un arena con /arena new <nome>");
			Parser.argumentLength(args, 2, "Utilizzo corretto: /" + label + " settype <nome>");
			Parser.isTrue((args[1].equalsIgnoreCase("normal") ||
							args[1].equalsIgnoreCase("pvp") ||
							args[1].equalsIgnoreCase("fast") ||
							args[1].equalsIgnoreCase("team")), "I tipi validi sono: §enormal§c, §epvp§c, §efast, team");
			
			creatingArena.set(ConfigNodes.ARENA_TYPE, (args[1].toLowerCase()));
			player.sendMessage("§aImpostato il tipo di arena: " + args[1] + ".");
			return true;
		}
		
		
		if (args[0].equalsIgnoreCase("setplayers")) {
			Parser.notNull(creatingArena, "Prima devi creare un arena con /arena new <nome>");
			Parser.argumentLength(args, 3, "Utilizzo corretto: /" + label + " setplayers <min> <max>");
			int min = Parser.getPositiveInt(args[1]);
			int max = Parser.getPositiveInt(args[2]);
			Parser.isTrue(min >= 2 && min <= 20, "Il minimo deve essere tra 2 e 20");
			Parser.isTrue(max >= 2 && max <= 20, "Il massimo deve essere tra 2 e 20");
			Parser.isTrue(min <= max, "Il minimo dei giocatori è maggiore del massimo!");
			creatingArena.set(ConfigNodes.PLAYERS_MIN, min);
			creatingArena.set(ConfigNodes.PLAYERS_MAX, max);
			player.sendMessage("§aImpostato il numero di giocatori: da " + min + " a " + max + ".");
			player.sendMessage("§eProcedi con /arena setsign.");
			return true;
		}


		
		
		if (args[0].equalsIgnoreCase("setsign")) {
			Parser.notNull(creatingArena, "Prima devi creare un arena con /arena new <nome>");
			Parser.notNull(creatingArena.getString(ConfigNodes.NAME), "Devi inserire un nome valido");
			
			Block block = player.getTargetBlock((Set<Material>) null, 64);
			Parser.isWallSign(block, "Non stai guardando un cartello! Stai guardando " + block.getType().toString());
			
			creatingArena.set(ConfigNodes.SIGN_X, block.getX());
			creatingArena.set(ConfigNodes.SIGN_Y, block.getY());
			creatingArena.set(ConfigNodes.SIGN_Z, block.getZ());
			
			player.sendMessage("§aAggiunto il cartello.");
			player.sendMessage("§eProcedi con /arena addspawn.");
			return true;
		}
		
		
		
		
		
		if (args[0].equalsIgnoreCase("addspawn")) {
			Parser.notNull(creatingArena, "Prima devi creare un arena con /arena new <nome>");
			Parser.argumentLength(args, 2, "Utilizzo corretto: /" + label + " addspawn <raggio>");
			int radius = Parser.getPositiveIntPlusZero(args[1]);
			
			List<String> spawnpoints = creatingArena.getStringList(ConfigNodes.SPAWNPOINTS);
			Location playerLocation = player.getLocation();
			
			int x = playerLocation.getBlockX();
			int y = playerLocation.getBlockY();
			int z = playerLocation.getBlockZ();
			
			spawnpoints.add(x + "," + y + "," + z + "," + radius);
			creatingArena.set(ConfigNodes.SPAWNPOINTS, spawnpoints);
			player.sendMessage("§aAggiunto spawnpoint n." + spawnpoints.size() + " (" + x + ", " + y + ", " + z + ", raggio: " + radius + ").");
			return true;
		}
		
		
		
		if (args[0].equalsIgnoreCase("removespawn")) {
			Parser.notNull(creatingArena, "Prima devi creare un arena con /arena new <nome>");
			Parser.argumentLength(args, 2, "Utilizzo corretto: /" + label + " removespawn <numero>");
			
			int index = Parser.getPositiveInt(args[1]) - 1;
			if (index < 0) index = 0;
			
			List<String> spawnpoints = creatingArena.getStringList(ConfigNodes.SPAWNPOINTS);
			
			if (spawnpoints == null || spawnpoints.size() == 0) {
				player.sendMessage("§cDevi prima settare almeno uno spawnpoint.");
				return true;
			}
			
			if (index >= spawnpoints.size()) {
				player.sendMessage("§cNon esiste quello spawnpoint (lista: /arena info).");
				return true;
			}
			
			spawnpoints.remove(index);
			creatingArena.set(ConfigNodes.SPAWNPOINTS, spawnpoints);
			player.sendMessage("§aRimosso spawnpoint n." + (index + 1) + ".");
			return true;
		}
		
		
		
		if (args[0].equalsIgnoreCase("info")) {
			Parser.notNull(creatingArena, "Prima devi creare un arena con /arena new <nome>");
			
			String name = creatingArena.getString(ConfigNodes.NAME);
			String type = creatingArena.getString(ConfigNodes.ARENA_TYPE);
			int min = creatingArena.getInt(ConfigNodes.PLAYERS_MIN);
			int max = creatingArena.getInt(ConfigNodes.PLAYERS_MAX);
			int signX = creatingArena.getInt(ConfigNodes.SIGN_X);
			int signY = creatingArena.getInt(ConfigNodes.SIGN_Y);
			int signZ = creatingArena.getInt(ConfigNodes.SIGN_Z);
			
			List<String> spawnpoints = creatingArena.getStringList(ConfigNodes.SPAWNPOINTS);
			
			player.sendMessage("§8==========[ §dInformazioni Arena §8]==========");
			
			if (name != null && name.length() > 0) {
				player.sendMessage("§aNome: §7" + name);
			} else {
				player.sendMessage("§aNome: §cnon impostato");
			}
			
			if (type != null && type.length() > 0) {
				player.sendMessage("§aTipo: §7" + type);
			} else {
				player.sendMessage("§aTipo: §7normale");
			}
			
			if (min != 0 && max != 0) {
				player.sendMessage("§aGiocatori: §7da " + min + " a " + max);
			} else {
				player.sendMessage("§aGiocatori: §cnon impostato");
			}
			
			
			if (!(signX == 0 && signY == 0 && signZ == 0)) {
				player.sendMessage("§aCoordinate cartello: §7" + signX + ", " + signY + ", " + signZ);
			} else {
				player.sendMessage("§aCoordinate cartello: §cnon impostato");
			}
			
			if (spawnpoints != null && spawnpoints.size() > 0) {
				player.sendMessage("§aSpawnpoints (x, y, z, raggio): ");
				int listSize = spawnpoints.size();
				for (int i = 0; i < listSize; i++) {
					int index = i + 1;
					player.sendMessage("§f" + index + ")§7   " + spawnpoints.get(i).replace(",", "§f,§7 "));
				}
			} else {
				player.sendMessage("§aSpawnpoints: §cnon impostato");
			}
			
			return true;
		}
		
		
		
		
		
		
		if (args[0].equalsIgnoreCase("save")) {
			Parser.notNull(creatingArena, "Prima devi creare un arena con /arena new <nome>");
			Parser.notNull(creatingArena.getString(ConfigNodes.NAME), "Non hai dato un nome all'arena!");
			Parser.notAllZero("Non hai impostato il numero di giocatori!", creatingArena.getInt(ConfigNodes.PLAYERS_MIN), creatingArena.getInt(ConfigNodes.PLAYERS_MAX));
			Parser.notAllZero("Non hai impostato il cartello!", creatingArena.getInt(ConfigNodes.SIGN_X),
																creatingArena.getInt(ConfigNodes.SIGN_Y),
																creatingArena.getInt(ConfigNodes.SIGN_Z));
			Parser.notNull(creatingArena.getStringList(ConfigNodes.SPAWNPOINTS), "Non hai settato gli spawnpoints!");
			Parser.argumentLength(creatingArena.getStringList(ConfigNodes.SPAWNPOINTS), 2, "Servono almeno 2 spawnpoints.");
			
			try {
				creatingArena.save(creatingArenaFile);
				player.sendMessage("§aArena salvata con successo!");
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				player.sendMessage("§cArena non salvata. Guarda la console.");
				return true;
			}
		}
		
		
		
		
		
		
		if (args[0].equalsIgnoreCase("edit")) {
			Parser.argumentLength(args, 2, "Utilizzo corretto: /" + label + " edit <file>");
			File f = new File(QuakeCraft.plugin.getDataFolder(), Lang.ARENAS_FOLDER + File.separator + args[1].toLowerCase() + ".yml");
			Parser.isTrue(f.exists(), "Il file non è stato trovato o caricato correttamente.");
			creatingArena = YamlConfiguration.loadConfiguration(f);
			creatingArenaFile = f;
			player.sendMessage("§aCaricato il file \"" + args[1].toLowerCase() + ".yml\".");
			return true;
		}
		
		
		
		
		
		if (args[0].equalsIgnoreCase("load")) {
			Parser.argumentLength(args, 2, "Utilizzo corretto: /" + label + " load <file>");
			
			File file = new File(QuakeCraft.plugin.getDataFolder(), Lang.ARENAS_FOLDER + File.separator + args[1].replace("/", File.separator).replace("\\", File.separator));
			
			Parser.isTrue(file.exists(), "Il file specificato non esiste.");
			Parser.isTrue(file.isFile(), "Il file è una cartella.");
			Parser.isTrue(file.getName().endsWith(".yml"), "Il file non ha estensione \".yml\".");
			
			try {
				QuakeCraft.loadArena(file, sender);
				((ArenasIconMenu) Gui.ARENAS.getMenu()).refreshOrderedArenas();
			} catch (Exception e) {
				e.printStackTrace();
				sender.sendMessage("§cImpossibile caricare l'arena. Guarda la console.");
			}
			return true;
		}
		
		

		player.sendMessage("§cComando non trovato. /arena per la lista dei comandi.");
		return true;
		
	} catch (ArgumentException ex) {
		sender.sendMessage(ex.getMessage());
		return true;
	}}
	
	private static FileConfiguration makeDefault() {
		YamlConfiguration config = new YamlConfiguration();
		config.set(ConfigNodes.NAME, "");
		config.set(ConfigNodes.SIGN_X, 0);
		config.set(ConfigNodes.SIGN_Y, 0);
		config.set(ConfigNodes.SIGN_Z, 0);
		config.set(ConfigNodes.SPAWNPOINTS, new ArrayList<String>());
		return config;
	}
}
