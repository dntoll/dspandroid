package com.spellofplay.dsp.model;

public enum Weapon {
	GUN, GRENADE;
	
	int magazineSize = 5;
	int bulletsInMagazine = 5;;
	int ammunition = 100;
	boolean canBeReloaded = false;
	boolean unlimitedAmmo = true;

	public int getFireCost() {
		return 3;
	}
	
	public float getRange() {
		return 3;
	}

	public int getDamage() {
		return 1;
	}
	

	public boolean hasAmmo() {
		return bulletsInMagazine > 0;
	}

	public boolean doBlastDamage() {
		return getBlastRadius() > 0;
	}

	public float getBlastRadius() {
		return 0;
	}
	public int getBlastDamage() {
		return 0;
	}

	
	
	

}
