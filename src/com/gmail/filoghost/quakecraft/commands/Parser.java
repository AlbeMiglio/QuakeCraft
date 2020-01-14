package com.gmail.filoghost.quakecraft.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.filoghost.quakecraft.constants.Lang;

public class Parser {
	
	
	public static void isTrue(boolean b, String message) throws ArgumentException {
		if (!b) {
			throw new ArgumentException(message);
		}
	}
	
	
	public static void checkPerm(Player target, String perm) throws ArgumentException {
		if (!target.hasPermission(perm)) {
			throw new ArgumentException("Non hai il permesso.");
		}
	}
	
	
	public static Player getOnlinePlayer(String input) throws ArgumentException {
		Player target = Bukkit.getPlayerExact(input.toLowerCase());
		if (target == null) {
			throw new ArgumentException("Quel giocatore non è online!");
		} else {
			return target;
		}
	}
	
	
	public static int getPositiveIntPlusZero(String input) throws ArgumentException {
		try {
			int out = Integer.parseInt(input);
			if (out < 0) {
				throw new ArgumentException(input + " non è un numero positivo.");
			}
			return out;
		} catch (Exception ex) {
			throw new ArgumentException(input + " non è un numero intero valido.");
		}
	}

	public static int getPositiveInt(String input) throws ArgumentException {
		try {
			int out = Integer.parseInt(input);
			if (out <= 0) {
				throw new ArgumentException(input + " non è un numero positivo.");
			}
			return out;
		} catch (Exception ex) {
			throw new ArgumentException(input + " non è un numero intero positivo valido.");
		}
	}
	
	public static double getDouble(String input) throws ArgumentException {
		try {
			double out = Double.parseDouble(input);
			return out;
		} catch (Exception ex) {
			throw new ArgumentException(input + " non è un numero decimale valido.");
		}
	}
	
	public static void notNull(Object o, String message) throws ArgumentException {
		if (o == null) {
			throw new ArgumentException(message);
		}
	}
	
	public static void argumentLength(List<String> args, int minimum, String message) throws ArgumentException {
		if (args.size() < minimum) {
			throw new ArgumentException(message);
		}
	}
	
	public static void argumentLength(String[] args, int minimum, String message) throws ArgumentException {
		if (args.length < minimum) {
			throw new ArgumentException(message);
		}
	}
	
	public static void stringLength(String arg, int minimum, String message) throws ArgumentException {
		if (arg.length() < minimum) {
			throw new ArgumentException(message);
		}
	}
	
	public static void isWallSign(Block block, String message) throws ArgumentException {
		if (block.getType() != Material.WALL_SIGN) {
			throw new ArgumentException(message);
		}
	}
	
	public static void isPlayer(CommandSender sender) throws ArgumentException {
		if (!(sender instanceof Player)) {
			throw new ArgumentException(Lang.PLAYER_ONLY_COMMAND);
		}
	}
	
	public static void notAllZero(String message, int... args) throws ArgumentException {
		for (int i : args) {
			if (i != 0) return;
		}
		throw new ArgumentException(message);
	}
}
