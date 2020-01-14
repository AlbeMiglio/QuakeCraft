package com.gmail.filoghost.quakecraft.commands;

import org.bukkit.ChatColor;

public class ArgumentException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message;
	
	public ArgumentException(String message) {
		this.message = ChatColor.RED + message;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
