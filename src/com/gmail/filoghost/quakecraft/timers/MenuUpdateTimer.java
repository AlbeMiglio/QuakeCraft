package com.gmail.filoghost.quakecraft.timers;

import com.gmail.filoghost.quakecraft.enums.Gui;
import com.gmail.filoghost.quakecraft.objects.gui.ArenasIconMenu;

public class MenuUpdateTimer extends TimerMaster {

	
	
	public MenuUpdateTimer() {}
	
	
	public long getDelayBeforeFirstRun() {
		return 5;
	}

	

	public long getDelayBetweenEachRun() {
		return 15; //meno di 1 secondo così sono aggiornate in tempo reale...ci mette meno di 1 millis ad aprirsi
	}
	
	
	
	public void run() {
		((ArenasIconMenu) Gui.ARENAS.getMenu()).update();
	}
}
