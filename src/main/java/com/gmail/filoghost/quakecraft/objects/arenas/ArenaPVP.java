package com.gmail.filoghost.quakecraft.objects.arenas;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.gmail.filoghost.quakecraft.enums.ArenaType;

public class ArenaPVP extends Arena {
	
	public ArenaPVP(FileConfiguration config, CommandSender sender) {
		super(config, sender);
		if (creationSuccessful) {
			super.minGamers = 2;
			super.maxGamers = 2;
			super.defaultSign();
		}
	}
	
	
	@Override
	public void giveRewards(Player winner, boolean syncro) {
		//don't do anything.
	}
	
	@Override
	public int getMaxKills() {
		return 10;
	}
	
	@Override
	public ArenaType getType() {
		return ArenaType.PVP;
	}
	
	@Override
	public int getCountdownSeconds() {
		return 11;
	}
	
	@Override
	public String getEndedEarlyMessage() {
		return "§cIl tuo avversario è uscito.";
	}
	
	@Override
	protected boolean canVipJoinFull() {
		return false;
	}
	
}
