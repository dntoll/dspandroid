package com.spellofplay.dsp.model;

public interface ICharacter {
	abstract public ModelPosition getPosition();
	abstract public int getTimeUnits();
	abstract public int getHitPoints();
	abstract public float distance(ICharacter fireTarget);
	abstract public float getRange();
	abstract public boolean hasTimeToFire();
	abstract public float getFireSkill();
	abstract public float getDodgeSkill();
	abstract public boolean hasWatch();
	abstract public float getRadius();
	abstract public int getWatchTimeUnits();
	abstract public int getFireCost();
}
