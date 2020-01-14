package com.gmail.filoghost.quakecraft.utils;

import org.bukkit.Bukkit;

public class Debug {

	public static boolean enable = true;
	
	public static void ln(String s) {
		if (enable)
			System.out.println(s);
	}
	
	public static void color(String s) {
		if (enable)
			Bukkit.getConsoleSender().sendMessage(s);
	}
}
