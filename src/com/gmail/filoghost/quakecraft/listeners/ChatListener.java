package com.gmail.filoghost.quakecraft.listeners;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.gmail.filoghost.quakecraft.QuakeCraft;
import com.gmail.filoghost.quakecraft.objects.arenas.Arena;

public class ChatListener implements Listener {
	
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void playerChat(AsyncPlayerChatEvent event) {

		String message = event.getMessage();
		
		if (message.startsWith("!")) {
			event.setMessage(message.substring(1));
			event.setFormat("§7[Chat §eGlobale§7]§r " + event.getFormat());
			return;
		}
		
		Arena arena = QuakeCraft.getArenaByPlayer(event.getPlayer());
		
		if (arena != null) {
			//playing
			Set<Player> recipients = event.getRecipients();
			recipients.clear();
			
			for (Player gamer : arena.getGamers()) {
				recipients.add(gamer);
			}
			
			event.setFormat("§7[Chat §bArena§7]§r " + event.getFormat());
			
		} else {
			//lobby
			event.setFormat("§7[Chat §aLobby§7]§r " + event.getFormat());
			Set<Player> recipients = event.getRecipients();
			recipients.removeAll(QuakeCraft.playerMap.keySet());
		}
	}

	
}
