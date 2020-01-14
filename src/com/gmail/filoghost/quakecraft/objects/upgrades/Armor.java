package com.gmail.filoghost.quakecraft.objects.upgrades;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.gmail.filoghost.quakecraft.enums.UpgradeType;
import com.gmail.filoghost.quakecraft.utils.Debug;

public class Armor extends Upgrade {

	private ItemStack body;
	private ItemStack leggings;
	private ItemStack boots;
	
	public Armor(Integer ID, String name, String description, Material material, int price) {
		super(ID, name, description, material, price);
		
		switch (material) {
		case LEATHER_CHESTPLATE:
			body = new ItemStack(Material.LEATHER_CHESTPLATE);
			leggings = new ItemStack(Material.LEATHER_LEGGINGS);
			boots = new ItemStack(Material.LEATHER_BOOTS);
			break;
		case IRON_CHESTPLATE:
			body = new ItemStack(Material.IRON_CHESTPLATE);
			leggings = new ItemStack(Material.IRON_LEGGINGS);
			boots = new ItemStack(Material.IRON_BOOTS);
			break;
		case CHAINMAIL_CHESTPLATE:
			body = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
			leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
			boots = new ItemStack(Material.CHAINMAIL_BOOTS);
			break;
		case GOLD_CHESTPLATE:
			body = new ItemStack(Material.GOLD_CHESTPLATE);
			leggings = new ItemStack(Material.GOLD_LEGGINGS);
			boots = new ItemStack(Material.GOLD_BOOTS);
			break;
		case DIAMOND_CHESTPLATE:
			body = new ItemStack(Material.DIAMOND_CHESTPLATE);
			leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
			boots = new ItemStack(Material.DIAMOND_BOOTS);
			break;
		default:
			Debug.color("§cCannot find such armor: " + material.toString());
			body = new ItemStack(Material.BEDROCK);
			leggings = new ItemStack(Material.BEDROCK);
			boots = new ItemStack(Material.BEDROCK);
			break;
		}
		
		ItemMeta bodyMeta = body.getItemMeta();
		bodyMeta.setDisplayName("§f" + name);
		body.setItemMeta(bodyMeta);
		
		ItemMeta leggingsMeta = leggings.getItemMeta();
		leggingsMeta.setDisplayName("§f" + name);
		leggings.setItemMeta(leggingsMeta);
		
		ItemMeta bootsMeta = boots.getItemMeta();
		bootsMeta.setDisplayName("§f" + name);
		boots.setItemMeta(bootsMeta);
		
	}
	
	public Armor(Integer ID, String name, String description, Color color, int price) {
		super(ID, name, description, getColoredArmor(color), price);
		
		body = new ItemStack(Material.LEATHER_CHESTPLATE);
		leggings = new ItemStack(Material.LEATHER_LEGGINGS);
		boots = new ItemStack(Material.LEATHER_BOOTS);
		
		LeatherArmorMeta bodyMeta = (LeatherArmorMeta) body.getItemMeta();
		bodyMeta.setDisplayName("§f" + name);
		//bodyMeta.setLore(super.getDescription());
		bodyMeta.setColor(color);
		body.setItemMeta(bodyMeta);
		
		LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta();
		leggingsMeta.setDisplayName("§f" + name);
		//leggingsMeta.setLore(super.getDescription());
		leggingsMeta.setColor(color);
		leggings.setItemMeta(leggingsMeta);
		
		LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
		bootsMeta.setDisplayName("§f" + name);
		//bootsMeta.setLore(super.getDescription());
		bootsMeta.setColor(color);
		boots.setItemMeta(bootsMeta);
	}
	
	public ItemStack getBody() {
		return body;
	}

	public ItemStack getLeggings() {
		return leggings;
	}

	public ItemStack getBoots() {
		return boots;
	}
	
	public static ItemStack getColoredArmor(Color color) {
		ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(color);
		item.setItemMeta(meta);
		return item;
	}

	@Override
	public UpgradeType getType() {
		return UpgradeType.ARMOR;
	}

}
