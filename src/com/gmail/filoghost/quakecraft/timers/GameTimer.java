package com.gmail.filoghost.quakecraft.timers;

import org.bukkit.Sound;

import com.gmail.filoghost.quakecraft.Configuration;
import com.gmail.filoghost.quakecraft.constants.Lang;
import com.gmail.filoghost.quakecraft.objects.arenas.Arena;
import com.gmail.filoghost.quakecraft.utils.Debug;

public class GameTimer extends TimerMaster {
	
	private Integer countDown; //for null checks
	private String preGameMotd;
	private Arena arena;

	
	
	public GameTimer(Arena arena) {
		this.arena = arena;
		preGameMotd = Configuration.preGameMotd;
	}

	
	@Override
	public void startNewTask() {
		Debug.ln("E' incominciato il countdown nell'arena " + arena.getName());
		countDown = arena.getCountdownSeconds();
		super.startNewTask();
	}
	
	@Override
	public void stopTask() {
		Debug.ln("E' finito countdown nell'arena " + arena.getName());
		super.stopTask();
	}

	public long getDelayBeforeFirstRun() {
		return 0;
	}

	

	public long getDelayBetweenEachRun() {
		return 20;
	}
	
	
	
	public void resetTimer(int countDownInSeconds) {
		countDown = countDownInSeconds;
		stopTask();
		startNewTask();
	}
	
	
	public int getRemainingSeconds() {
		return countDown;
	}
	
	
	public void setRemainingSeconds(int seconds) {
		if (super.isStarted()) {
			countDown = seconds;
		}
	}
	
	
	public String getFormattedTime() {
		if (countDown >= 60) {
			
			if (countDown % 15 == 0) {
				
			int minutes = countDown / 60;
			String minutesLang = "minuti";
			if (minutes == 1) minutesLang = "minuto";
				
			return "§a[" + minutes + " " + minutesLang + "]";
			
			}
		
		//meno di 1 minuto
		} else {
			
			//più di 10 secondi
			if (countDown > 10) {
				if (countDown % 10 == 0) {
					int seconds = countDown;
					String secondsLang = "secondi";
					if (seconds == 1) secondsLang = "secondo";
					return "§a[" + seconds + " " + secondsLang + "]";
				}
				
			//meno di 10 secondi
			} else {
				int seconds = countDown;
				String secondsLang = "secondi";
				if (seconds == 1) secondsLang = "secondo";
				return "§a[" + seconds + " " + secondsLang + "]";
			}
		}
		
		return arena.getState().getName();
	}
	
	public String getFormattedTimeForMenu() {
		if (countDown >= 60) {
				
			int minutes = countDown / 60;
			String minutesLang = "minuti";
			if (minutes == 1) minutesLang = "minuto";
			return minutes + " " + minutesLang;

		//meno di 1 minuto
		} else {
			
			//più di 10 secondi
			if (countDown > 10) {
				int seconds = (countDown / 10) * 10 + 10;
				return seconds + " secondi";

			//meno di 10 secondi
			} else {
				int seconds = countDown;
				String secondsLang = "secondi";
				if (seconds == 1) secondsLang = "secondo";
				return seconds + " " + secondsLang;
			}
		}
	}
	
	public void run() {
		//countdown finito
		if (countDown < 1) {
			this.stopTask();
			arena.start();
			return;
		}
		
		//più di 1 minuto
		if (countDown >= 60) {
			
			if (countDown % 15 == 0) {
				
			int minutes = countDown / 60;
			String minutesLang = "minuti";
			if (minutes == 1) minutesLang = "minuto";
				
			arena.defaultSign("§a[" + minutes + " " + minutesLang + "]");
			String motd = preGameMotd.replace("%t", minutes + " " + minutesLang);
			arena.tellAll(Lang.QUAKE_PREFIX + motd);
			
			}
		
		//meno di 1 minuto
		} else {
			
			//più di 10 secondi
			if (countDown > 10) {
				if (countDown % 10 == 0) {
					int seconds = countDown;
					String secondsLang = "secondi";
					if (seconds == 1) secondsLang = "secondo";
					arena.defaultSign("§a[" + seconds + " " + secondsLang + "]");
					String motd = preGameMotd.replace("%t", seconds + " " + secondsLang);
					arena.tellAll(Lang.QUAKE_PREFIX + motd);
				}
				
			//meno di 10 secondi
			} else {
				int seconds = countDown;
				String secondsLang = "secondi";
				if (seconds == 1) secondsLang = "secondo";
				arena.defaultSign("§a[" + seconds + " " + secondsLang + "]");
				String motd = preGameMotd.replace("%t", seconds + " " + secondsLang);
				arena.tellAll(Lang.QUAKE_PREFIX + motd);
				arena.soundAll(Sound.UI_BUTTON_CLICK);
			}
		}
	
		countDown--;
	}
	
	
	/*
	public void run() {
		//countdown finito
		if (countDown < 1) {
			this.stopTask();
			arena.start();
			return;
		}
		
		//più di 1 minuto
		if (countDown >= 60) {
			
			if (countDown % 15 == 0) {
				
			int minutes = countDown / 60;
			String minutesLang = "minuti";
			if (minutes == 1) minutesLang = "minuto";
				
			arena.setSignTimer("§a[" + minutes + " " + minutesLang + "]");
			String motd = preGameMotd.replace("%t", minutes + " " + minutesLang);
			arena.tellAll(Lang.QUAKE_PREFIX + motd);
			
			}
		
		//meno di 1 minuto
		} else {
			
			//più di 10 secondi
			if (countDown > 10) {
				if (countDown % 10 == 0) {
					int seconds = countDown;
					String secondsLang = "secondi";
					if (seconds == 1) secondsLang = "secondo";
					arena.setSignTimer("§a[" + seconds + " " + secondsLang + "]");
					String motd = preGameMotd.replace("%t", seconds + " " + secondsLang);
					arena.tellAll(Lang.QUAKE_PREFIX + motd);
				}
				
			//meno di 10 secondi
			} else {
				int seconds = countDown;
				String secondsLang = "secondi";
				if (seconds == 1) secondsLang = "secondo";
				arena.setSignTimer("§a[" + seconds + " " + secondsLang + "]");
				String motd = preGameMotd.replace("%t", seconds + " " + secondsLang);
				arena.tellAll(Lang.QUAKE_PREFIX + motd);
				arena.soundAll(Sound.CLICK);
			}
		}
	
		countDown--;
	}
	*/
}
