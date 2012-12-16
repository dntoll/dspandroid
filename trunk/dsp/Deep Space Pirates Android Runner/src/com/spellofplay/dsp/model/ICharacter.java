package com.spellofplay.dsp.model;



public interface ICharacter {
	ModelPosition getPosition();
	int getTimeUnits();
	int getHitPoints();
	float distance(ICharacter fireTarget);
	float getRange();
	boolean hasTimeToFire();
	
	boolean hasWatch();
	float getRadius();
	int getWatchTimeUnits();
	int getFireCost();
	boolean canSpendExperience();
	
	ISkillSet getSkills();
	Experience getExperience();

}
