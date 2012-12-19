package com.spellofplay.dsp.model;

import com.spellofplay.dsp.model.inner.CharacterType;



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
	boolean canThrowGrenade();
	CharacterType getCharacterType();
	

}
