package com.gmail.filoghost.quakecraft.objects.arenas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import wild.api.WildConstants;
import wild.api.world.RayTrace;
import wild.api.world.SightInfo;

import com.gmail.filoghost.quakecraft.Configuration;
import com.gmail.filoghost.quakecraft.QuakeCraft;
import com.gmail.filoghost.quakecraft.constants.ConfigNodes;
import com.gmail.filoghost.quakecraft.constants.Lang;
import com.gmail.filoghost.quakecraft.constants.Numbers;
import com.gmail.filoghost.quakecraft.constants.Permissions;
import com.gmail.filoghost.quakecraft.enums.ArenaType;
import com.gmail.filoghost.quakecraft.enums.GameState;
import com.gmail.filoghost.quakecraft.objects.CyclicIterator;
import com.gmail.filoghost.quakecraft.objects.KillStreak;
import com.gmail.filoghost.quakecraft.objects.PowerUp;
import com.gmail.filoghost.quakecraft.objects.PowerUpEffect;
import com.gmail.filoghost.quakecraft.objects.SpawnPoint;
import com.gmail.filoghost.quakecraft.objects.player.QuakePlayer;
import com.gmail.filoghost.quakecraft.runnables.FireworksTask;
import com.gmail.filoghost.quakecraft.runnables.SpeedAndResistancePotionTask;
import com.gmail.filoghost.quakecraft.timers.Cooldown;
import com.gmail.filoghost.quakecraft.timers.GameTimer;
import com.gmail.filoghost.quakecraft.timers.PowerUpTimer;
import com.gmail.filoghost.quakecraft.timers.RespawnTimer;
import com.gmail.filoghost.quakecraft.utils.Debug;
import com.gmail.filoghost.quakecraft.utils.LogFile;
import com.gmail.filoghost.quakecraft.utils.ParticleUtils;
import com.gmail.filoghost.quakecraft.utils.PotionUtils;
import com.gmail.filoghost.quakecraft.utils.Utils;
import com.google.common.collect.Lists;

public class Arena {
	
	protected String name;
	protected List<SpawnPoint> spawnpoints = new ArrayList<SpawnPoint>();
	protected GameState gamestate = GameState.PREGAME;
	public int maxGamers;
	public int minGamers;
	public PowerUp currentPowerUp;
	protected Block signBlock;
	public Scoreboard scoreboard;
	public Objective sidebar;
	protected boolean isNight;
	
	protected long timeStarted;
	protected int playersAtStart;
	
	public List<Player> gamers = new ArrayList<Player>();
	protected Map<Player, Integer> deaths = new HashMap<Player, Integer>();
	
	public boolean creationSuccessful = false;
	
	public GameTimer gameTimer;
	protected PowerUpTimer powerUpTimer;
	
	public Arena(FileConfiguration config, CommandSender sender) {
		
		if (config == null) {
			sender.sendMessage("§cFile non valido!");
			return;
		}
		
		/*
		 * Loading the name
		 */
		name = config.getString(ConfigNodes.NAME);
		if (name == null || name.length() == 0) {
			sender.sendMessage("§cIl nome non può essere vuoto!");
			return;
		}
		
		if (QuakeCraft.arenaMap.get(name) != null) {
			sender.sendMessage("§c" + name + ": è già caricata!");
			return;
		}
		
		/*
		 * Load the sign
		 */
		Location signLoc = new Location(QuakeCraft.mainWorld,
										config.getInt(ConfigNodes.SIGN_X),
										config.getInt(ConfigNodes.SIGN_Y),
										config.getInt(ConfigNodes.SIGN_Z));
		
		Block block = signLoc.getBlock();
		if (block.getType() == Material.WALL_SIGN) {
			signBlock = block;
		} else {
			sender.sendMessage("§c" + name + ": il blocco non è un cartello da muro!");
			return;
		}
		
		/*
		 * Loading the scoreboard
		 */
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		
		/*
		 * Loading the spawnpoints
		 */
		List<String> points = config.getStringList(ConfigNodes.SPAWNPOINTS);
		for (String line : points) {
			String[] coordinates = line.split(",");
			if (coordinates.length < 4) {
				sender.sendMessage("§c" + name + ": impossibile caricare un spawnpoint (poche coordinate)!");
			} else {
				spawnpoints.add(new SpawnPoint(
						Integer.parseInt(coordinates[0]),
						Integer.parseInt(coordinates[1]),
						Integer.parseInt(coordinates[2]),
						Integer.parseInt(coordinates[3])));
			}
		}
		if (points.size() <= 1) {
			sender.sendMessage("§c" + name + ": gli spawn devono essere 2 o più!");
			return;
		}
		
		
		int min = config.getInt(ConfigNodes.PLAYERS_MIN);
		int max = config.getInt(ConfigNodes.PLAYERS_MAX);
		
		if (min == 0) sender.sendMessage("§e" + config + ": minimo player non impostato.");
		if (max == 0) sender.sendMessage("§e" + config + ": massimo dei player non impostato.");
		
		if (min < 2) min = 2;
		if (min > 20) min = 20;
		
		if (max > 20) max = 20;
		if (max < 2) max = 2;
		
		if (max < min) {
			sender.sendMessage("§c" + config + ": il minimo dei giocatore è maggiore del massimo!");
			return;
		}
		
		/*
		 * Caricamento notte - giorno
		 */
		isNight = config.getBoolean("night", false);

		
		this.minGamers = min;
		this.maxGamers = max;
		
		gameTimer = new GameTimer(this);
		powerUpTimer = new PowerUpTimer(this);
		
		defaultSign();
		creationSuccessful = true;
		sender.sendMessage("§aCaricata con successo \"" + name + "\".");
	}
	
	public Location getRandomSpawn() {
		Random random = new Random();
		for (Player gamer : gamers) {
			//TODO evitare player vicini?
		}
		return spawnpoints.get(random.nextInt(spawnpoints.size())).getRandomLocation();
	}
	
	public String getName() {
		return name;
	}
	
	public GameState getState() {
		return gamestate;
	}
	
	public boolean removeGamer(Player player) {
		if (gamers.remove(player)) {
			QuakeCraft.playerMap.remove(player);
			tellAll(Lang.QUAKE_PREFIX + "§7" + player.getName() + " è uscito " + "(§f" + gamers.size() + "§7/§f" + maxGamers + "§7)");
			
			if (gamestate == GameState.PREGAME && gameTimer.isStarted()) { //countdown already started
				if (gamers.size() < minGamers) {
					gameTimer.stopTask();
					defaultSign();
					tellAll(Lang.QUAKE_PREFIX + "§cConto alla rovescia interrotto: pochi giocatori.");
				}
			}
			
			if (gamestate == GameState.GAME && isThereFewPlayers()) {
				//interruped game, no players
				tellAll(Lang.QUAKE_PREFIX + getEndedEarlyMessage());
				giveRewards(null, false);
				if (getType() != ArenaType.PVP) {
					player.sendMessage(Lang.QUAKE_PREFIX + "§cNon hai ricevuto ricompense, perché la partita non era terminata.");
				}
				reset();
			}
			
			scoreboard.resetScores(player);
			defaultSign();
			deaths.remove(player);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isThereFewPlayers() {
		// Uno da solo o zero
		return gamers.size() <= 1;
	}
	
	protected boolean canVipJoinFull() {
		return true;
	}
	
	/**
	 * @return true if the player successfully joins the arena.
	 */
	public boolean addGamer(Player player) {
		if (gamestate != GameState.PREGAME) {
			player.sendMessage(Lang.ARENA_ALREADY_STARTED);
			return false;
		}
		
		if (gamers.size() >= maxGamers) {
			
			if (canVipJoinFull() && player.hasPermission(Permissions.JOIN_FULL)) {
				// Allowed
			} else {
				player.sendMessage(Lang.ARENA_FULL);
				return false;
			}
		}
		
		if (QuakeCraft.isPlaying(player)) {
			player.sendMessage("§cSei già in un'arena.");
			player.sendMessage("§cUsa il comando /spawn per uscire (non ricevi punti).");
			return false;
		}
		
		QuakeCraft.playerMap.put(player, this.name);
		gamers.add(player);
		Utils.clearPlayer(player);
		player.updateInventory();
		
		if (isNight) {
			Utils.setNight(player);
		}
		player.teleport(getRandomSpawn());
		player.setScoreboard(scoreboard); //empty at the moment
		KillStreak.resetConsecutiveKills(player);
		defaultSign();
		
		tellAll(Lang.QUAKE_PREFIX + "§7" + player.getName() + " è entrato " + "(§f" + gamers.size() + "§7/§f" + maxGamers + "§7)");
		
		if (gamestate == GameState.PREGAME) {
			if (gamers.size() >= minGamers && !gameTimer.isStarted()) {
				//start the timer if not already started
				gameTimer.startNewTask();
			}
		}
		
		if (gamers.size() == maxGamers && gameTimer.isStarted() && this.getType() != ArenaType.PVP) {
			if (gameTimer.getRemainingSeconds() > 15) {
				gameTimer.setRemainingSeconds(15);
				tellAll(Lang.QUAKE_PREFIX + "§eL'arena è piena, conto alla rovescia ridotto!");
			}
		}
		return true;
	}
	
	public List<Player> getGamers() {
		return gamers;
	}
	
	
	public void dropRandomPowerUp() {
		
		if (currentPowerUp != null && currentPowerUp.exists()) {
			currentPowerUp.destroy();
		}

		currentPowerUp = new PowerUp(PowerUpEffect.randomEffect(), getRandomSpawn(), this);
	}
	
	
	public void handleRespawn(PlayerRespawnEvent event) {
		if (gamestate == GameState.GAME) {
			event.setRespawnLocation(getRandomSpawn());
			final Player player = event.getPlayer();
			giveArmor(player);
			giveWeapon(player);
			Bukkit.getScheduler().scheduleSyncDelayedTask(QuakeCraft.plugin, new SpeedAndResistancePotionTask(player, 1));
			new RespawnTimer(player).startNewTask(); //attivo finché ha resistenza
		} else {
			event.setRespawnLocation(getRandomSpawn());
		}
	}
	
	public void killEvent(Player killer, Player victim) {
		
		if (gamestate != GameState.GAME) {
			Debug.color("§dGiocatore ucciso dopo la partita: " + killer.getName() + " uccide " + victim.getName());
			return;
		}
		
		killer.sendMessage("§a» §8Hai ucciso §7" + victim.getName());
		victim.sendMessage("§c» §8Sei stato ucciso da §7" + killer.getName());
		
		KillStreak.addKill(killer);
		
		int killerConsecutiveKills = KillStreak.getConsecutiveKills(killer);
		if (killerConsecutiveKills == 5) {
			tellAll("§b" + killer.getName() + " sta facendo una strage §7(x5)!");
		}
		if (killerConsecutiveKills >= 10 && killerConsecutiveKills % 5 == 0) {
			tellAll("§b" + killer.getName() + " è inarrestabile §7(x" + killerConsecutiveKills + ")!");
		}
		
		if (KillStreak.getConsecutiveKills(victim) >= 5) {
			tellAll("§7" + killer.getName() + " ha fermato la furia di §b" + victim.getName() + "§7!");
		}
		KillStreak.resetConsecutiveKills(victim);
		victim.setHealth(0.0);
		
		addDeath(victim);
		Score score = sidebar.getScore(killer);
		score.setScore(score.getScore() + 1);
		if (score.getScore() >= getMaxKills()) {
			end(killer);
		}
	}
	
	
	public int getCooldown(Player player, ItemStack item) {
		
		int cooldown;
		
		switch (item.getType()) {
			case WOOD_HOE:
				cooldown = 30;
				break;
			case STONE_HOE:
				cooldown = 28;
				break;
			case IRON_HOE:
				cooldown = 26;
				break;
			case GOLD_HOE:
				cooldown = 24;
				break;
			case DIAMOND_HOE:
				if (item.containsEnchantment(Enchantment.DAMAGE_ALL)) {
					cooldown = 20;
				} else {
					cooldown = 22;
				}
				break;
			default:
				return 0;
		}
		
		if (player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
			cooldown = cooldown / 2;
		}
		
		return cooldown;
	}
	
	public void rightClickEvent(Player player, ItemStack item) {
		
		if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) return;
		
		if (!Cooldown.canShot(player)) return;
		
		int cooldown = getCooldown(player, item);
		if (cooldown == 0) return;
		
		Cooldown.addPlayer(player, cooldown);
		shotEvent(QuakePlayer.get(player));
	}
	
	
	public void shotEvent(QuakePlayer shooter) {
		
		SightInfo target = RayTrace.getSightIncludePlayers(shooter.getBase(), gamers, Numbers.BOUNDING_BOX_GROWTH);
		
		double x = target.getLocation().getX();
		double y = target.getLocation().getY();
		double z = target.getLocation().getZ();
		
		
		ParticleUtils.trail(shooter.getTrail().getParticle(), shooter.getBase().getEyeLocation(), target.getLocation());
		
		List<Player> affectedPlayers = new ArrayList<Player>();
		
		//magari non l'ha preso in pieno
		if (target.getPlayer() != null && !target.getPlayer().isDead()) {
			//pareggia il livello per le multikill
			y = target.getPlayer().getLocation().getY();
			
			if (target.getPlayer().hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
				shooter.sendMessage(Lang.ARENA_PLAYER_PROTECTED);
			} else {
				affectedPlayers.add(target.getPlayer());
			}
		}
		
		for (Player gamer : gamers) {
			if (gamer != shooter.getBase() && gamer != target.getPlayer() && !gamer.isDead() && Utils.isInCylinder(gamer, x, y, z)) {
				
				if (gamer.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
					shooter.sendMessage(Lang.ARENA_PLAYER_PROTECTED);
				} else {
					affectedPlayers.add(gamer);
				}
				
			}
		}
		
		int amount = affectedPlayers.size();
		
		if (amount > 0) {
			
			goodHitSound(target.getLocation(), shooter);
			
			for (Player affected : affectedPlayers) {
				killEvent(shooter.getBase(), affected);
			}
				
			if (amount > 2) {
				if (amount == 2) {
					tellAll("§b" + shooter.getName() + " ha fatto una Doppia Uccisione!");
				} else {
					tellAll("§b" + shooter.getName() + " ha fatto una Uccisione Multipla (" + amount + ")!");
				}
			}
		
		} else {
			wrongHitSound(shooter.getBase().getLocation());
		}
	}
	
	public void giveArmor(Player gamer) {
		QuakePlayer qp = QuakePlayer.get(gamer);
		qp.wearHat();
		qp.wearArmor();
	}
	
	public void giveWeapon(Player gamer) {
		QuakePlayer.get(gamer).wearWeapon();
	}
	
	public void setSignLines(String one, String two, String three, String four) {
		BlockState state = signBlock.getState();
		if (state instanceof Sign) {
			Sign sign = (Sign) state;
			sign.setLine(0, one);
			sign.setLine(1, two);
			sign.setLine(2, three);
			sign.setLine(3, four);
			sign.update(true, false); // forse mettere force non è necessario
		}
	}
	
	public void defaultSign() {
		setSignLines(
						"§l" + name,
						"----------------",
						gameTimer.isStarted() ? gameTimer.getFormattedTime() : gamestate.getName(),
						"§8[" + gamers.size() + "/" + maxGamers + "]"
					);
	}
	
	public void defaultSign(String formattedTime) {
		setSignLines(
						"§l" + name,
						"----------------",
						formattedTime,
						"§8[" + gamers.size() + "/" + maxGamers + "]"
					);
	}
	
	/*
	public void updateSignGamers() {
		setSignLines(null, null, null, "§8[" + gamers.size() + "/" + maxGamers + "]");
	}
	
	public void setSignTimer(String time) {
		setSignLines(null, null, time, null);
	}
	*/
	
	
	public void removeSidebar() {
		Objective obj = scoreboard.getObjective(DisplaySlot.SIDEBAR);
		
		if (obj != null) {
			obj.unregister();
		}
	}
	
	public void start() {
		
		timeStarted = System.currentTimeMillis();
		playersAtStart = gamers.size();
		
		gamestate = GameState.GAME;
		defaultSign();
		
		List<SpawnPoint> randomizedSpawnpoints = Lists.newArrayList(spawnpoints);
		Collections.shuffle(randomizedSpawnpoints);
		CyclicIterator iter = new CyclicIterator(randomizedSpawnpoints);
		createSidebar();
		
		for (Player player : gamers) {
			if (player.isOnline()) {

				Utils.clearPlayer(player);
				PotionUtils.giveSpeed(player, 1);
				PotionUtils.giveRespawnResistance(player);
				giveArmor(player);
				giveWeapon(player);
				
				player.teleport(randomizedSpawnpoints.get(iter.next()).getRandomLocation());
				player.sendMessage(Lang.QUAKE_PREFIX + "§cLa partita è iniziata!");
				player.sendMessage(Lang.QUAKE_PREFIX + "§cClicca con mouse destro per sparare!");
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1F, 1F);
			}
		}
		
		for (Player player : gamers) {
			player.setScoreboard(scoreboard);
		}
		
		powerUpTimer.startNewTask();
	}
	
	public void createSidebar() {
		removeSidebar();
		sidebar = scoreboard.registerNewObjective("classifica", "dummy");
		sidebar.setDisplayName(Lang.OBJECTIVE_CLASSIFICA_NAME);
		sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		for (Player player : gamers) {
			sidebar.getScore(player).setScore(1);
			sidebar.getScore(player).setScore(0);
		}
		
		sidebar.getScore("§r§r§r").setScore(-1);
		WildConstants.Messages.displayIP(scoreboard, sidebar, -2);
	}
	
	public void end(Player winner) {
		
		gamestate = GameState.END;
		giveRewards(winner, false);
		
		for (Player gamer : gamers) {
			Utils.clearPlayer(gamer);
		}

		final BukkitScheduler scheduler = Bukkit.getScheduler();
		
		if (winner != null) {
			
			int playersNow = gamers.size();
			long secondsElapsed = (System.currentTimeMillis() - timeStarted) / 1000;
			
			LogFile.writeLine(name + " ended in " + secondsElapsed + "s with " + playersNow + " players (originally " + playersAtStart + ")");
			
			soundAll(Sound.ENTITY_PLAYER_LEVELUP);
			Bukkit.broadcastMessage(Lang.QUAKE_PREFIX + "§f" + winner.getName() + " ha vinto nell'arena " + name);
			
			for (Player player : gamers) {
				player.sendMessage(new String[] {
						"",
						Lang.GRAY_LINE_SEPARATOR,
						"§6§lVincitore:§f " + winner.getName(),
						"",
						"§a§lUccisioni:§f " + getKills(player),
						"§a§lMorti:§f " + getDeaths(player),
						"§a§lMappa:§f " + name,
						Lang.GRAY_LINE_SEPARATOR,
						""
				});
			}
			
	
			final int fireworkTaskId = scheduler.scheduleSyncRepeatingTask(QuakeCraft.plugin, new FireworksTask(winner), 0L, 10L);
		
			scheduler.scheduleSyncDelayedTask(QuakeCraft.plugin, () -> {
				scheduler.cancelTask(fireworkTaskId);
			}, 260L);
			
			scheduler.scheduleSyncDelayedTask(QuakeCraft.plugin, () -> {
				reset();
			}, 300L);
		
		} else {
			reset();
		}
	}
	
	public void reset() {
		for (Player gamer : gamers) {
			QuakeCraft.playerMap.remove(gamer);
			if (!gamer.isDead()) Utils.toTheLobby(QuakePlayer.get(gamer), true, false);
		}
		gamers = new ArrayList<Player>();
		deaths = new HashMap<Player, Integer>();
		gamestate = GameState.PREGAME;
		removeSidebar();
		defaultSign();
		powerUpTimer.stopTask();
		if (currentPowerUp != null && currentPowerUp.exists()) {
			currentPowerUp.destroy();
		}
	}
	
	public void giveRewards(Player winner, boolean syncro) {
		
		for (Player player : gamers) {
				
			QuakePlayer quakePlayer = QuakePlayer.get(player);
			int kills = getKills(player);
			int coins = kills;
			
			if (player.equals(winner)) {
				coins += Numbers.COINS_FOR_WIN;
				quakePlayer.addWin();
			}
			
			if (player.hasPermission(Permissions.BOOST_150)) {
				coins = (int) Math.floor(coins * 1.5);
			} else if (player.hasPermission(Permissions.BOOST_200)) {
				coins = coins * 2;
			}
			
			quakePlayer.addCoins(coins);
			quakePlayer.addKills(kills);
			quakePlayer.addDeaths(getDeaths(player));
			
			if (syncro)		quakePlayer.saveSync();
			else			quakePlayer.saveAsync();
		}
	}
	
	
	public int getKills(Player player) {
		return sidebar.getScore(player).getScore();
	}
	
	public void addDeath(Player player) {
		Integer old = deaths.get(player);
		if (old == null) {
			deaths.put(player, Integer.valueOf(1));
		} else {
			deaths.put(player, Integer.valueOf(old + 1));
		}
	}
	
	public int getDeaths(Player player) {
		Integer amount = deaths.get(player);
		if (amount == null) return 0;
		else return amount.intValue();
	}

	public int getMaxKills() {
		return 25;
	}
	
	public ArenaType getType() {
		return ArenaType.NORMAL;
	}
	
	public int getCountdownSeconds() {
		return Configuration.countDown;
	}
	
	public String getEndedEarlyMessage() {
		return "§cSono usciti troppi giocatori, la partita finisce senza vincitore. Riceverai la ricompensa.";
	}
	
	
	public void tellAll(String broadcast) {
		for (Player player : gamers) {
			player.sendMessage(broadcast);
		}
	}
	
	public void tellAll(String[] broadcast) {
		for (Player player : gamers) {
			player.sendMessage(broadcast);
		}
	}
	
	public void soundAll(Sound sound) {
		for (Player player : gamers) {
			player.playSound(player.getLocation(), sound, 1F, 1F);
		}
	}
	
	protected void wrongHitSound(Location center) {
		World world = center.getWorld();
		world.playSound(center, Sound.BLOCK_LAVA_POP, 1.3F, 1.4F);
		world.playSound(center, Sound.ENTITY_BLAZE_HURT, 1.3F, 2.0F);
	}
	
	protected void goodHitSound(Location center, QuakePlayer shooter) {
		World world = center.getWorld();
		world.playSound(center, Sound.ENTITY_BLAZE_DEATH, 2.0F, 2.0F);
		world.playSound(center, Sound.ENTITY_GENERIC_EXPLODE, 2.0F, 1.4F);
		
		ParticleUtils.fireworkExplosion(center, shooter.getExplosionEffects());
	}
}
