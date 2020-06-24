package com.gmail.filoghost.quakecraft.utils;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import wild.api.world.Particle;

import com.gmail.filoghost.quakecraft.QuakeCraft;
import com.gmail.filoghost.quakecraft.runnables.DetonateFireworkTask;

public class ParticleUtils {

	public static final Color[] colors = new Color[]{
		Color.fromRGB(255, 0, 0),
		Color.fromRGB(255, 128, 0),
		Color.fromRGB(255, 255, 0),
		Color.fromRGB(128, 255, 0),
		Color.fromRGB(0, 255, 0),
		Color.fromRGB(0, 255, 128),
		Color.fromRGB(0, 255, 255),
		Color.fromRGB(0, 128, 255),
		Color.fromRGB(0, 0, 255),
		Color.fromRGB(128, 0, 255),
		Color.fromRGB(255, 0, 255),
		Color.fromRGB(255, 0, 128),
	};
	
	
	private static double[] sinValues = new double[]{0.0, 0.5, 0.866, 1.0, 0.866, 0.5, 0.0, -0.5, -0.866, -1.0, -0.866, -0.5};
	private static double[] cosValues = new double[]{1.0, 0.866, 0.5, 0.0, -0.5, -0.866, -1.0, -0.866, -0.5, 0.0, 0.5, 0.866};
	private static int sinCosSteps = 12;
		
	public static void winningFirework(Location loc, int index) {
		Firework firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();
        
        Color currentColor = colors[index];
        
        int red = currentColor.getRed();
        int green = currentColor.getGreen();
        int blue = currentColor.getBlue();
        
        FireworkEffect effect = FireworkEffect.builder().withColor(Color.fromRGB(red, green, blue))
				.withColor(Color.fromRGB((red/4)*3, (green/4)*3, (blue/4)*3))
				.withColor(Color.fromRGB(red/2, green/2, blue/2))
				.with(Type.BURST)
				.withTrail()
				.build();
        
        meta.addEffect(effect);
        meta.setPower(0);
        firework.setFireworkMeta(meta);
	}
	
	public static void fireworkExplosion(Location loc, FireworkEffect[] effects) {
		World world = loc.getWorld();
		Firework firework = (Firework) world.spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.addEffects(effects);
        firework.setFireworkMeta(meta);
        Bukkit.getScheduler().scheduleSyncDelayedTask(QuakeCraft.plugin, new DetonateFireworkTask(firework), 2L);
	}
	
	public static void circle(Particle particle, Location start, double radius) {
		
		double x = start.getX();
		double y = start.getY();
		double z = start.getZ();
		
		World world = start.getWorld();
		
		for (int i = 0; i < sinCosSteps; i++) {
			singleParticle(new Location(world, x + sinValues[i] * radius, y, z + cosValues[i] * radius), particle);
		}
	}
	
	public static void trail(Particle particle, Location start, Location end) {
		
		double distance = start.distance(end);
		
		double x = start.getX();
		double y = start.getY();
		double z = start.getZ();
		
		double iterx = (end.getX()-x)/(distance*2);
		double itery = (end.getY()-y)/(distance*2);
		double iterz = (end.getZ()-z)/(distance*2);
		//modulo 1
		
		World world = start.getWorld();
		
		for (double i = 0; i < distance; i += 0.5) {
			x += iterx;
			y += itery;
			z += iterz;
			particle(new Location(world, x, y, z), particle);
		}
	}
	
	@Deprecated
	public static void trail(Particle particle, Location start, Location end, float speed) {
		
		double distance = start.distance(end);
		
		double x = start.getX();
		double y = start.getY();
		double z = start.getZ();
		
		double iterx = (end.getX()-x)/(distance*2);
		double itery = (end.getY()-y)/(distance*2);
		double iterz = (end.getZ()-z)/(distance*2);
		//modulo 1
		
		World world = start.getWorld();
		
		for (double i = 0; i < distance; i += 0.5) {
			x += iterx;
			y += itery;
			z += iterz;
			detailedParticle(new Location(world, x, y, z), particle, 0.3f, 0.3f, speed, 10);
		}
	}
	
	public static void smallTrail(Particle particle, Location start, Location end) {
		
		double distance = start.distance(end);
		
		double x = start.getX();
		double y = start.getY();
		double z = start.getZ();
		
		double iterx = (end.getX()-x)/(distance*2);
		double itery = (end.getY()-y)/(distance*2);
		double iterz = (end.getZ()-z)/(distance*2);
		//modulo 1
		
		World world = start.getWorld();
		
		for (double i = 0; i < distance; i += 0.5) {
			x += iterx;
			y += itery;
			z += iterz;
			smallParticle(new Location(world, x, y, z), particle);
		}
	}
	
	public static void detailedParticle(Location loc, Particle particle, float width, float height, float speed, int amount) {
		particle.display(loc, width, height, width, speed, amount);
	}
	
	public static void blood(Player target) {
		Location loc = target.getEyeLocation().subtract(0.0, 0.7, 0.0);
		detailedParticle(loc, Particle.RED_DUST, 0.4F, 0.8F, 0.0F, 80);
	}
	
	public static void bloodExplode(Player target) {
		Location loc = target.getEyeLocation().subtract(0.0, 0.7, 0.0);
		detailedParticle(loc, Particle.RED_DUST, 0.8F, 0.8F, 0.0F, 200);
	}
	
	public static void bigSmoke(Location loc) {
		detailedParticle(loc, Particle.SMOKE, 0.5F, 0.5F, 0.0F, 100);
	}
	
	public static void particle(Location loc, Particle particle) {
		detailedParticle(loc, particle, 0.1F, 0.1F, 0.0F, 4);
	}
	
	public static void singleParticle(Location loc, Particle particle) {
		detailedParticle(loc, particle, 0.0F, 0.0F, 0.0F, 1);
	}
	
	public static void bigFlames(Location loc) {
		detailedParticle(loc, Particle.FLAME, 0.4F, 0.4F, 0.0F, 10);
	}
	
	public static void regenEffect(Location loc) {
		detailedParticle(loc, Particle.SPELL, 0.3F, 1.0F, 0.0F, 20);
	}
	
	public static void smallParticle(Location loc, Particle particle) {
		detailedParticle(loc, particle, 0.1F, 0.1F, 0.0F, 2);
	}
}
