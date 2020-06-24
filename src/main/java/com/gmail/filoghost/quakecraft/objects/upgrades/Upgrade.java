package com.gmail.filoghost.quakecraft.objects.upgrades;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import wild.api.WildCommons;
import wild.api.item.ItemBuilder;
import wild.api.world.Particle;

import com.gmail.filoghost.quakecraft.enums.UpgradeType;
import com.gmail.filoghost.quakecraft.utils.Utils;

public abstract class Upgrade {
	
	private int ID;
	private String name;
	private ArrayList<String> description;
	private ItemStack icon;
	private int price;
	
	@Getter
	private boolean vipOnly;
	
	public static List<Upgrade> list = new ArrayList<Upgrade>();
	
	public static Weapon DEFAULT_WEAPON = new Weapon(-1, "Rivoltella", "L'arma di base./Ricarica: 1.5 secondi", Material.WOOD_HOE, 0);
	public static Trail DEFAULT_TRAIL = new Trail(-1, "Scia di base", Particle.INSTANT_SPELL, "", new ItemStack(Material.INK_SACK, 1, (byte) 15), 0);
	public static Explosion DEFAULT_EXPLOSION = new Explosion(-1, "Esplosione di base", "", new ItemStack(Material.WOOL, 1, (byte) 15), 0, FireworkEffect.builder().withColor(Color.RED, Color.WHITE).with(Type.BALL).build());
	
	
	public Upgrade(int ID, String name, String description, Material material, int price) {
		this(ID, name, description, new ItemStack(material, 1), price);
	}
	
	public Upgrade(int ID, String name, String description, ItemStack icon, int price) {
		
		this.ID = ID;
		this.name = name;
		
		this.description = new ArrayList<String>();
		for (String part : description.split("/")) {
			if (!part.startsWith("§")) {
				part = "§7" + part;
			}
			part = Utils.parseSymbols(part);
			this.description.add(part);
		}
		
		this.icon = WildCommons.removeAttributes(icon);
		this.price = price;
	}
	
	public abstract UpgradeType getType();
	
	public int getID() {
		return ID;
	}
	
	public ItemStack getIcon() {
		return icon.clone();
	}
	
	public String getName() {
		return name;
	}
	
	public List<String> getDescription() {
		return new ArrayList<String>(description);
	}
	
	public int getPrice() {
		return price;
	}
	
	public boolean isDefault() {
		return this.ID == -1;
	}
	
	public static Upgrade getByName(String input) {
		for (Upgrade upgrade : list) {
			if (upgrade.getName().equalsIgnoreCase(input))
				return upgrade;
		}
		return null;
	}
	
	public static Upgrade getByID(int ID) {
		for (Upgrade upgrade : list) {
			if (upgrade.getID() == ID)
				return upgrade;
		}
		return null;
	}
	
	@Override
	public boolean equals(Object o) {
		return ((Upgrade) o).getID() == this.ID;
		
	}
		
	public static void setupItems(){
		list = new ArrayList<Upgrade>();
		
		/**
		 * ELMI
		 */
		list.add(new Hat(100, "Elmo di Vetro", "Un elmo semplice./Decorazione estetica.", Material.GLASS, 100));
		list.add(new Hat(101, "Elmo di Cactus", "Farà molto male!/Decorazione estetica.", Material.CACTUS, 150));
		list.add(new Hat(102, "Elmo di Melone", "Un cappello esotico./Decorazione estetica.", Material.MELON_BLOCK, 150));
		list.add(new Hat(103, "Elmo di Zucca", "Per spaventare le persone./Decorazione estetica.", Material.JACK_O_LANTERN, 150));
		
		list.add(new Hat(109, "Elmo di Mattoni", "Molto pesante./Decorazione estetica.", ItemBuilder.of(Material.BRICK).durability(3).build(), 150));
		list.add(new Hat(110, "Elmo di Legno", "Quando si dice \"Testa di legno\"./Decorazione estetica.", Material.LOG, 150));
		list.add(new Hat(111, "Elmo da Lavoro", "Può tornare utile./Decorazione estetica.", Material.WORKBENCH, 150));
		list.add(new Hat(112, "Elmo di Redstone", "Accendi la mente./Decorazione estetica.", Material.REDSTONE_BLOCK, 150));
		list.add(new Hat(113, "Elmo Sonoro", "Non fa rumore./Decorazione estetica.", Material.NOTE_BLOCK, 150));
		list.add(new Hat(114, "Cappello di Paglia", "Purtroppo non diventi di gomma./Decorazione estetica.", Material.HAY_BLOCK, 200));
		list.add(new Hat(115, "Elmo Slender", "Nascondi la tua faccia./Decorazione estetica.", Material.QUARTZ_BLOCK, 200));
		list.add(new Hat(116, "Elmo Mimetico", "Perfettamente camuffato./Decorazione estetica.", Material.LEAVES, 200));
		list.add(new Hat(118, "Elmo Libreria", "Diventerai molto saggio./Decorazione estetica.", Material.BOOKSHELF, 200));
		list.add(new Hat(117, "Elmo di Ghiaccio", "Per rinfrescarsi./Decorazione estetica.", Material.ICE, 250));
		
		list.add(new Hat(104, "Elmo Infernale", "Una testa di scherbero./Decorazione estetica.", ItemBuilder.of(Material.SKULL_ITEM).durability(1).build(), 300));
		
		list.add(new Hat(119, "Teschio", "Una testa di scheletro./Decorazione estetica.", ItemBuilder.of(Material.SKULL_ITEM).durability(0).build(), 300));
		list.add(new Hat(120, "Testa di Zombie", "Molto credibile.../Decorazione estetica.", ItemBuilder.of(Material.SKULL_ITEM).durability(2).build(), 300));
		list.add(new Hat(121, "Testa di Creeper", "Ssshhh... Boom!/Decorazione estetica.", ItemBuilder.of(Material.SKULL_ITEM).durability(4).build(), 300));
		list.add(new Hat(122, "Elmo Ender", "Metti il cervello al sicuro./Decorazione estetica.", Material.ENDER_CHEST, 300));
		list.add(new Hat(123, "Fascia di Ferro", "All'ultima moda./Decorazione estetica.", Material.IRON_PLATE, 400));
		list.add(new Hat(124, "Elmo di Ossidiana", "Molto resistente./Decorazione estetica.", Material.OBSIDIAN, 400));
		list.add(new Hat(125, "Elmo di Luce", "Illuminante./Decorazione estetica.", Material.GLOWSTONE, 400));
		
		list.add(new Hat(105, "Elmo di Bedrock", "Un elmo indistruttibile./Decorazione estetica.", Material.BEDROCK, 500));
		list.add(new Hat(106, "Elmo di TNT", "Tutti i griefer ne vogliono uno./Decorazione estetica.", Material.TNT, 500));
		
		list.add(new Hat(126, "Elmo Spawner", "Per sentirsi al sicuro./Decorazione estetica.", Material.MOB_SPAWNER, 500));
		
		list.add(new Hat(107, "Elmo d'Oro", "Solo per i ricconi./Decorazione estetica.", Material.GOLD_HELMET, 1500));
		list.add(new Hat(108, "Elmo di Diamante", "Per chi ha soldi da sprecare./Decorazione estetica.", Material.DIAMOND_HELMET, 3000));
		
		
		/**
		 * ARMATURE
		 */
		list.add(new Armor(200, "Armatura Standard", "Un'armatura non colorata./Decorazione estetica.", Material.LEATHER_CHESTPLATE, 300));
		
		list.add(new Armor(208, "Armatura Bianca", "Decorazione estetica.", Color.WHITE, 600));
		list.add(new Armor(209, "Armatura Grigia", "Decorazione estetica.", Color.GRAY, 600));
		
		list.add(new Armor(201, "Armatura Rossa", "Decorazione estetica.", Color.RED, 600));
		list.add(new Armor(202, "Armatura Verde", "Decorazione estetica.", Color.GREEN, 600));
		list.add(new Armor(203, "Armatura Blu", "Decorazione estetica.", Color.BLUE, 600));
		list.add(new Armor(204, "Armatura Gialla", "Decorazione estetica.", Color.YELLOW, 600));
		
		list.add(new Armor(210, "Armatura Viola", "Decorazione estetica.", Color.fromRGB(89, 0, 212), 600));
		list.add(new Armor(211, "Armatura Arancione", "Decorazione estetica.", Color.fromRGB(255, 136, 0), 600));
		list.add(new Armor(212, "Armatura Azzurra", "Decorazione estetica.", Color.fromRGB(0, 196, 255), 600));
		list.add(new Armor(213, "Armatura Rosa", "Decorazione estetica.", Color.fromRGB(255, 128, 215), 600));
		list.add(new Armor(214, "Armatura Verde Acqua", "Decorazione estetica.", Color.fromRGB(0, 255, 191), 600));
		
		list.add(new Armor(205, "Armatura Nera", "Decorazione estetica.", Color.BLACK, 800));
		list.add(new Armor(206, "Armatura d'Oro", "Solo per i ricconi./Decorazione estetica.", Material.GOLD_CHESTPLATE, 2000));
		list.add(new Armor(207, "Armatura di Diamante", "Per chi ha soldi da sprecare./Decorazione estetica.", Material.DIAMOND_CHESTPLATE, 4000));
		
		
		/**
		 * ARMI
		 */
		list.add(new Weapon(300, "Fucile a pompa", "Ricarica: 1.4 secondi", Material.STONE_HOE, 1500));
		list.add(new Weapon(301, "Fucile d'assalto", "Ricarica: 1.3 secondi/Richiede l'arma precedente.", Material.IRON_HOE, 4000));
		list.add(new Weapon(302, "Lanciagranate", "Ricarica: 1.2 secondi/Richiede l'arma precedente.", Material.GOLD_HOE, 8500));
		list.add(new Weapon(303, "Mitragliatrice", "Ricarica: 1.1 secondi/Richiede l'arma precedente.", Material.DIAMOND_HOE, 20000));
		list.add(new Weapon(304, "Macchina Mortale", "Ricarica: 1.0 secondi/Richiede l'arma precedente.", ItemBuilder.of(Material.DIAMOND_HOE).enchant(Enchantment.DAMAGE_ALL).build(), 80000));
		
		/**
		 * SCIE
		 */
		list.add(new Trail(400, "Scia rossa", Particle.RED_DUST, "Cambia il colore della scia/per la tua arma./Decorazione estetica.", new ItemStack(Material.REDSTONE), 500));
		list.add(new Trail(401, "Scia viola", Particle.WITCH_MAGIC, "Cambia il colore della scia/per la tua arma./Decorazione estetica.", new ItemStack(Material.INK_SACK, 1, (short) 5), 500));
		
		list.add(new Trail(405, "Scia incantesimi", Particle.ENCHANTMENT_TABLE, "Cambia il colore della scia/per la tua arma./Decorazione estetica.", new ItemStack(Material.ENCHANTMENT_TABLE), 500));
		
		list.add(new Trail(402, "Scia di cuoricini", Particle.HEART, "Cambia il colore della scia/per la tua arma./Decorazione estetica.", new ItemStack(Material.INK_SACK, 1, (short) 9), 1000));
		
		list.add(new Trail(406, "Scia d'acqua", Particle.DRIP_WATER, "Cambia il colore della scia/per la tua arma./Decorazione estetica.", new ItemStack(Material.WATER_BUCKET), 1000));
		list.add(new Trail(407, "Scia villico arrabbiato", Particle.ANGRY_VILLAGER, "Cambia il colore della scia/per la tua arma./Decorazione estetica.", new ItemStack(Material.INK_SACK, 1, (short) 11), 1000));
		list.add(new Trail(408, "Scia nera", Particle.MOB_SPELL, "Cambia il colore della scia/per la tua arma./Decorazione estetica.", new ItemStack(Material.COAL), 1500));
		list.add(new Trail(409, "Scia musicale", Particle.NOTE, "Cambia il colore della scia/per la tua arma./Decorazione estetica.", new ItemStack(Material.NOTE_BLOCK), 1500));
		
		list.add(new Trail(403, "Scia verde", Particle.HAPPY_VILLAGER, "Cambia il colore della scia/per la tua arma./Decorazione estetica.", new ItemStack(Material.INK_SACK, 1, (short) 10), 2000));
		list.add(new Trail(404, "Scia infuocata", Particle.FLAME, "Cambia il colore della scia/per la tua arma./Decorazione estetica.", new ItemStack(Material.BLAZE_POWDER), 2500));
		
		
		/**
		 * ESPLOSIONI
		 */
		list.add(new Explosion(500, "Esplosione rosso scuro", "Piccola esplosione sferica./Decorazione estetica.", ItemBuilder.of(Material.STAINED_GLASS).durability(14).build(), 300, FireworkEffect.builder().withColor(Color.RED, Color.fromRGB(127, 0, 0)).with(Type.BALL).build()));
		list.add(new Explosion(501, "Esplosione viola", "Piccola esplosione sferica./Decorazione estetica.", ItemBuilder.of(Material.STAINED_GLASS).durability(10).build(), 500, FireworkEffect.builder().withColor(Color.fromRGB(127, 0, 255), Color.fromRGB(63, 0, 127)).with(Type.BALL).build()));
		
		list.add(new Explosion(506, "Esplosione verde", "Piccola esplosione sferica./Decorazione estetica.", ItemBuilder.of(Material.STAINED_GLASS).durability(5).build(), 500, FireworkEffect.builder().withColor(Color.fromRGB(0, 163, 19), Color.fromRGB(31, 255, 57)).with(Type.BALL).build()));
		list.add(new Explosion(507, "Esplosione bianco e nero", "Esplosione con varie tonalità/di grigio./Decorazione estetica.", ItemBuilder.of(Material.STAINED_GLASS).durability(8).build(), 500, FireworkEffect.builder().withColor(Color.GRAY, Color.BLACK, Color.WHITE).with(Type.BALL).build()));
		list.add(new Explosion(508, "Esplosione arancione", "Esplosione a forma di stella./Decorazione estetica.", ItemBuilder.of(Material.STAINED_GLASS).durability(1).build(), 800, FireworkEffect.builder().withColor(Color.ORANGE, Color.fromRGB(196, 137, 0)).with(Type.STAR).build()));
		
		list.add(new Explosion(502, "Esplosione gialla", "Esplosione verso l'alto./Decorazione estetica.", ItemBuilder.of(Material.STAINED_GLASS).durability(4).build(), 800, FireworkEffect.builder().withColor(Color.YELLOW, Color.fromRGB(255, 205, 0)).with(Type.BURST).withTrail().build()));
		list.add(new Explosion(503, "Esplosione nera", "Grande esplosione sferica nera./Decorazione estetica", ItemBuilder.of(Material.STAINED_GLASS).durability(15).build(), 1000, FireworkEffect.builder().with(Type.BALL_LARGE).withColor(Color.BLACK).build()));
		list.add(new Explosion(504, "Esplosione creeper verde", "Esplosione verde a forma di creeper./Decorazione estetica.", ItemBuilder.of(Material.STAINED_GLASS).durability(13).build(), 1500, FireworkEffect.builder().withColor(Color.GREEN, Color.LIME).with(Type.CREEPER).build()));
		
		list.add(new Explosion(509, "Esplosione arcobaleno", "Esplosione verso l'alto multicolore./Decorazione estetica.", ItemBuilder.of(Material.GLASS).build(), 2000, FireworkEffect.builder().withColor(Color.RED, Color.YELLOW, Color.FUCHSIA, Color.GREEN, Color.BLUE, Color.LIME, Color.ORANGE, Color.AQUA, Color.PURPLE, Color.TEAL).with(Type.BURST).build()));
		
		list.add(new Explosion(505, "Esplosione finale", "Grande esplosione sferica/azzurra con molti effetti./Decorazione estetica.", ItemBuilder.of(Material.STAINED_GLASS).durability(11).enchant(Enchantment.DURABILITY).build(), 3000,
				FireworkEffect.builder().with(Type.BALL).withColor(Color.AQUA, Color.fromRGB(0, 204, 204)).withFade(Color.fromRGB(0, 0, 150)).withFlicker().build(),
				FireworkEffect.builder().with(Type.BALL_LARGE).withColor(Color.fromRGB(102, 255, 255), Color.fromRGB(51, 51, 255)).withFade(Color.fromRGB(0, 0, 150)).build(),
				FireworkEffect.builder().with(Type.STAR).withColor(Color.AQUA, Color.BLUE).withTrail().withFade(Color.fromRGB(0, 0, 150)).withFlicker().build()));
	}
}
