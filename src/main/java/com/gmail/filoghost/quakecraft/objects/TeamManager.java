package com.gmail.filoghost.quakecraft.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import wild.api.util.FakeOfflinePlayer;

import com.gmail.filoghost.quakecraft.constants.Lang;
import com.gmail.filoghost.quakecraft.enums.TeamColor;
import com.gmail.filoghost.quakecraft.utils.Debug;

public class TeamManager {
	
	private Scoreboard scoreboard;
	
	private List<Player> blueList;
	private List<Player> redList;
	
	private OfflinePlayer fakeRedPlayer;
	private OfflinePlayer fakeBluePlayer;
	
	private Objective obj;
	
	private Team scoreboardBlueTeam;
	private Team scoreboardRedTeam;
	
	private Objective standardKills;
	
	public TeamManager(Scoreboard attachedScoreboard) {
		this.scoreboard = attachedScoreboard;
		blueList = new ArrayList<Player>();
		redList = new ArrayList<Player>();
		fakeRedPlayer = new FakeOfflinePlayer("§c█ §f§lRossi");
		fakeBluePlayer = new FakeOfflinePlayer("§9█ §f§lBlu");
	}
	
	public int getSingularKills(Player player) {
		return standardKills.getScore(player).getScore();
	}

	public int getKills(TeamColor tc) {
		if (tc == TeamColor.BLUE) {
			return obj.getScore(fakeBluePlayer).getScore();
		} else {
			return obj.getScore(fakeRedPlayer).getScore();
		}
	}
	
	public void addToRed(Player player) {
		if (blueList.contains(player)) {
			blueList.remove(player);
		}
		redList.add(player);
		scoreboardRedTeam.addPlayer(player);
		player.sendMessage(Lang.QUAKE_PREFIX + "§fSei stato aggiunto alla squadra rossa!");
	}
	
	public void addToBlue(Player player) {
		if (redList.contains(player)) {
			redList.remove(player);
		}
		blueList.add(player);
		scoreboardBlueTeam.addPlayer(player);
		player.sendMessage(Lang.QUAKE_PREFIX + "§fSei stato aggiunto alla squadra blu!");
	}
	
	public int getRedSize() {
		return redList.size();
	}
	
	public int getBlueSize() {
		return blueList.size();
	}
	
	public void autoAssign(Player player) {
		if (blueList.size() > redList.size()) {
			addToRed(player);
		} else {
			addToBlue(player);
		}
	}
	
	public TeamColor getTeamColor(Player player) {
		if (blueList.contains(player)) {
			return TeamColor.BLUE;
		} else if (redList.contains(player)) {
			return TeamColor.RED;
		} else {
			try {
				throw new Exception();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			Debug.color("§d" + player.getName() + " non era in nessun team!");
			Debug.color("§dBlu: " + blueList.toString());
			Debug.color("§dRossi: " + redList.toString());
			return TeamColor.BLUE;
		}
	}
	
	public List<Player> getEnemies(Player player) {
		if (getTeamColor(player) == TeamColor.BLUE) {
			return new ArrayList<Player>(redList);
		} else {
			return new ArrayList<Player>(blueList);
		}
	}
	
	public void addKill(Player player) {
		
		standardKills.getScore(player).setScore(standardKills.getScore(player).getScore() + 1);
		
		Score score = (getTeamColor(player) == TeamColor.BLUE) ? obj.getScore(fakeBluePlayer) : obj.getScore(fakeRedPlayer);
		score.setScore(score.getScore() + 1);
	}
	
	public boolean areEnemy(Player one, Player two) {
		
		if (one == null || two == null) {
			return false;
		}
		
		if (blueList.contains(one) && blueList.contains(two)) {
			return false;
		} else if (redList.contains(one) && redList.contains(two)) {
			return false;
		} else {
			return true;
		}
	}
	
	public void remove(Player player) {
		blueList.remove(player);
		redList.remove(player);
		Team team = scoreboard.getPlayerTeam(player);
		if (team != null) {
			team.removePlayer(player);
		}
	}
	
	public void resetAndCreate() {
		blueList = new ArrayList<Player>();
		redList = new ArrayList<Player>();
		
		if (scoreboard.getObjective("classifica") != null) {
			scoreboard.getObjective("classifica").unregister();
		}
		
		obj = scoreboard.registerNewObjective("classifica", "dummy");
		obj.setDisplayName(Lang.OBJECTIVE_CLASSIFICA_NAME);
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		obj.getScore(fakeBluePlayer).setScore(1);
		obj.getScore(fakeBluePlayer).setScore(0);
		obj.getScore(fakeRedPlayer).setScore(1);
		obj.getScore(fakeRedPlayer).setScore(0);
		
		if (scoreboard.getObjective("uccisioni") != null) {
			scoreboard.getObjective("uccisioni").unregister();
		}
		standardKills = scoreboard.registerNewObjective("uccisioni", "dummy");
		
		if (scoreboard.getTeam("blue") != null) {
			scoreboard.getTeam("blue").unregister();
		}
		
		if (scoreboard.getTeam("red") != null) {
			scoreboard.getTeam("red").unregister();
		}
		
		scoreboardBlueTeam = scoreboard.registerNewTeam("blue");
		scoreboardBlueTeam.setPrefix("§9");
		scoreboardRedTeam = scoreboard.registerNewTeam("red");
		scoreboardRedTeam.setPrefix("§c");
	}
}
