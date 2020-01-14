package com.gmail.filoghost.quakecraft.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import com.gmail.filoghost.quakecraft.QuakeCraft;
import com.gmail.filoghost.quakecraft.constants.Permissions;
import com.gmail.filoghost.quakecraft.utils.Debug;

public class RestrictionsListener implements Listener {

	/*
	 *   #################################
	 *   #            PLAYERS            #
	 *   #################################
	 */
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void playerDeath(PlayerDeathEvent event) {
		event.setDroppedExp(0);
		event.setKeepLevel(false);
		event.getDrops().clear();
		event.setDeathMessage(null);
	}
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void playerDamage(EntityDamageEvent event) {
		if (event.getEntityType() == EntityType.PLAYER) {
			DamageCause cause = event.getCause();
		
			if (cause == DamageCause.VOID) {
				event.setDamage(1000.0);
			} else if (cause == DamageCause.CUSTOM) {
				// Ok, per i plugin
			} else {
				event.setCancelled(true);
			}
		} else if (event.getEntityType() == EntityType.ITEM_FRAME) {
			boolean cancel = true;
			
			if (event instanceof EntityDamageByEntityEvent) {
				Entity breaker = ((EntityDamageByEntityEvent) event).getDamager();
				
				if (breaker instanceof Player && ((Player) breaker).hasPermission(Permissions.ADMIN)) {
					cancel = false;
				}
			}
			
			if (cancel) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void teleport(PlayerTeleportEvent event) {
		if (event.getFrom().getWorld() == event.getTo().getWorld()) return;
		
		//it's another world
		if (!event.getPlayer().hasPermission(Permissions.ADMIN)) {
			Debug.ln("A player tried to access another world: " + event.getTo().getWorld().getName());
			event.setCancelled(true);
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void sneak(PlayerToggleSneakEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void playerInteract(PlayerInteractEvent event) {
		if (!event.getPlayer().hasPermission(Permissions.ADMIN)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void food(FoodLevelChangeEvent event) {
		HumanEntity human = event.getEntity();
		if (human instanceof Player) {
			event.setCancelled(true);
			((Player) human).setSaturation(20F);
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void changeExp(PlayerExpChangeEvent event) {
		event.setAmount(0);
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void changeLevel(PlayerLevelChangeEvent event) {
		event.getPlayer().setLevel(0);
	}
	
	/*
	 *   #################################
	 *   #             ITEMS             #
	 *   #################################
	 */
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void itemDrop(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}
	
	/*
	 *   #################################
	 *   #             BLOCKS            #
	 *   #################################
	 */
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockPhysics(BlockPhysicsEvent event) {
		
		Material type = event.getBlock().getType();
		
		if (type == Material.PORTAL || type == Material.WALL_SIGN || type == Material.IRON_DOOR_BLOCK || type == Material.SNOW) {
			event.setCancelled(true);
		}

	}
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void blockBreak(BlockBreakEvent event) {
		if (event.getPlayer().hasPermission(Permissions.ADMIN)) {
			Block block = event.getBlock();
			if (block.getType() == Material.WALL_SIGN) {
				Sign sign = (Sign) block.getState();
				String firstLine = sign.getLine(0);
				if (firstLine != null && firstLine.length() > 0) {
					if (QuakeCraft.getArenaByName(ChatColor.stripColor(firstLine)) != null) {
						event.setCancelled(true);
					}
				}
			}
		} else {
			event.setCancelled(true);
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void blockPlace(BlockPlaceEvent event) {
		if (!event.getPlayer().hasPermission(Permissions.ADMIN))
			event.setCancelled(true);
	}
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void blockBurn(BlockBurnEvent event) { event.setCancelled(true); }
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void blockDamage(BlockDamageEvent event) { event.setCancelled(true); }
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void blockDispense(BlockDispenseEvent event) { event.setCancelled(true); }
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void blockFade(BlockFadeEvent event) { event.setCancelled(true); }
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void blockForm(BlockFormEvent event) { event.setCancelled(true); }
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void blockGrow(BlockGrowEvent event) { event.setCancelled(true); }
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void blockIgnite(BlockIgniteEvent event) { event.setCancelled(true); }
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void blockPistonExtend(BlockPistonExtendEvent event) { event.setCancelled(true); }
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void blockPistonRetract(BlockPistonRetractEvent event) { event.setCancelled(true); }
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void blockSpread(BlockSpreadEvent event) { event.setCancelled(true); }
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void blockFormByEntity(EntityBlockFormEvent event) { event.setCancelled(true); }
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void blockDecay(LeavesDecayEvent event) { event.setCancelled(true); }
	/*
	 *   #################################
	 *   #              MISC             #
	 *   #################################
	 */
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void mobSpawn(CreatureSpawnEvent event) {
		if (event.getSpawnReason() != SpawnReason.CUSTOM) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void weather(WeatherChangeEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void explode(EntityExplodeEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void chunkLoad(ChunkLoadEvent event) {
		Entity[] entities = event.getChunk().getEntities();
		for (Entity entity : entities) {
			if (entity instanceof LivingEntity && entity.getType() != EntityType.PLAYER) {
				entity.remove();
			}

		}
	}
}
