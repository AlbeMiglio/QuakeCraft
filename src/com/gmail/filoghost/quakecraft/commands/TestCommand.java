package com.gmail.filoghost.quakecraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.filoghost.quakecraft.QuakeCraft;
import com.gmail.filoghost.quakecraft.enums.Gui;
import com.gmail.filoghost.quakecraft.objects.gui.BaseShopMenu;
import com.gmail.filoghost.quakecraft.objects.upgrades.Explosion;
import com.gmail.filoghost.quakecraft.objects.upgrades.Upgrade;
import com.gmail.filoghost.quakecraft.utils.ParticleUtils;
import com.gmail.filoghost.quakecraft.utils.Purge;
import com.gmail.filoghost.quakecraft.utils.Utils;


@SuppressWarnings("unused")
public class TestCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return true;
	}
}
