package com.gmail.filoghost.quakecraft.objects.player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.gmail.filoghost.quakecraft.constants.ConfigNodes;
import com.gmail.filoghost.quakecraft.constants.Permissions;
import com.gmail.filoghost.quakecraft.enums.UpgradeType;
import com.gmail.filoghost.quakecraft.objects.upgrades.Armor;
import com.gmail.filoghost.quakecraft.objects.upgrades.Explosion;
import com.gmail.filoghost.quakecraft.objects.upgrades.Hat;
import com.gmail.filoghost.quakecraft.objects.upgrades.Trail;
import com.gmail.filoghost.quakecraft.objects.upgrades.Upgrade;
import com.gmail.filoghost.quakecraft.objects.upgrades.Weapon;
import com.gmail.filoghost.quakecraft.utils.Validator;

public abstract class Equippable extends Configurable {
	
	private Hat hat;
	private Armor armor;
	private Weapon weapon;

	private Trail trail;
	private Explosion explosion;
	
	private List<Upgrade> upgrades;
	
	public Equippable(Player base, File configFile) {
		super(base, configFile);
		weapon = Upgrade.DEFAULT_WEAPON;
		trail = Upgrade.DEFAULT_TRAIL;
		explosion = Upgrade.DEFAULT_EXPLOSION;
		upgrades = new ArrayList<Upgrade>();
	}
	
	public void unequip(UpgradeType type) {
		
		Validator.notNull(type);
		
		switch(type) {
			case ARMOR:
				setArmor(null);
				break;
			case EXPLOSION:
				setExplosion(null);
				break;
			case HAT:
				setHat(null);
				break;
			case TRAIL:
				setTrail(null);
				break;
			case WEAPON:
				setWeapon(null);
				break;
		}
	}
	
	public void equip(Upgrade upgrade) {
		
		Validator.notNull(upgrade);
		
		switch(upgrade.getType()) {
			case ARMOR:
				setArmor(upgrade);
				break;
			case EXPLOSION:
				setExplosion(upgrade);
				break;
			case HAT:
				setHat(upgrade);
				break;
			case TRAIL:
				setTrail(upgrade);
				break;
			case WEAPON:
				setWeapon(upgrade);
				break;
		}
	}
	
	public boolean isEquipped(Upgrade upgrade) {
		
		Validator.notNull(upgrade);
		
		Upgrade sameType = getEquipByType(upgrade.getType());
		
		if (sameType == null) {
			return false;
		}
		
		return upgrade.equals(sameType);
	}
	
	public Upgrade getEquipByType(UpgradeType type) {
		
		Validator.notNull(type);
		
		switch (type) {
			case ARMOR:
				return armor;
			case EXPLOSION:
				return explosion;
			case HAT:
				return hat;
			case TRAIL:
				return trail;
			case WEAPON:
				return weapon;
		}
		
		return null;
	}
	
	public boolean canUseVIPUpgrade(Upgrade upgrade) {
		if (upgrade.isVipOnly() && !getBase().hasPermission(Permissions.VIP_ITEMS)) {
			return false;
		}

		return true;
	}
	
	public boolean ownsUpgrade(Upgrade upgrade) {
		if (!canUseVIPUpgrade(upgrade)) {
			return false;
		}
		
		if (upgrade.getPrice() <= 0) {
			return true;
		}
		
		return upgrades.contains(upgrade);
	}
	
	public void setArmor(Upgrade armor) {
		if (armor != null) {
			this.armor = (Armor) armor;
			getConfig().set(ConfigNodes.ARMOR, armor.getID());
		} else {
			this.armor = null;
			getConfig().set(ConfigNodes.ARMOR, null);
		}
	}
	
	public void setHat(Upgrade hat) {
		if (hat != null) {
			this.hat = (Hat) hat;
			getConfig().set(ConfigNodes.HAT, hat.getID());
		} else {
			this.hat = null;
			getConfig().set(ConfigNodes.HAT, null);
		}
	}
	
	public void setWeapon(Upgrade weapon) {
		if (weapon != null) {
			this.weapon = (Weapon) weapon;
			getConfig().set(ConfigNodes.WEAPON, weapon.getID());
		} else {
			this.weapon = Upgrade.DEFAULT_WEAPON;
			getConfig().set(ConfigNodes.WEAPON, null);
		}
	}
	
	public void setTrail(Upgrade trail) {
		if (trail != null) {
			this.trail = (Trail) trail;
			getConfig().set(ConfigNodes.TRAIL, trail.getID());
		} else {
			this.trail = Upgrade.DEFAULT_TRAIL;
			getConfig().set(ConfigNodes.TRAIL, null);
		}
	}
	
	public void setExplosion(Upgrade explosion) {
		if (explosion != null) {
			this.explosion = (Explosion) explosion;
			getConfig().set(ConfigNodes.EXPLOSION, explosion.getID());
		} else {
			this.explosion = Upgrade.DEFAULT_EXPLOSION;
			getConfig().set(ConfigNodes.EXPLOSION, null);
		}
	}
	
	
	
	public void addUpgrade(Upgrade upgrade) {
		
		Validator.isTrue(!upgrades.contains(upgrade), "upgrade already owned");
		
		upgrades.add(upgrade);
	}
	
	public void buyUpgrade(Upgrade upgrade) {
		addUpgrade(upgrade);
		List<Integer> currentUpgrades = getConfig().getIntegerList(ConfigNodes.UPGRADES);
		currentUpgrades.add(upgrade.getID());
		getConfig().set(ConfigNodes.UPGRADES, currentUpgrades);
	}
	
	public Hat getHat() {
		return hat;
	}

	public Armor getArmor() {
		return armor;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public Trail getTrail() {
		return trail;
	}

	public Explosion getExplosion() {
		return explosion;
	}
	
}
