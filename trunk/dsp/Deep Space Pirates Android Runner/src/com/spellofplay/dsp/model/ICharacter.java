package com.spellofplay.dsp.model;

public abstract class ICharacter {

	//read only
	abstract public ModelPosition getPosition();
	abstract public int getTimeUnits();
	abstract public int getHitPoints();
	abstract public void doWatch();
	abstract public float distance(ICharacter fireTarget);
	abstract public float getRange();
	abstract public boolean hasTimeToFire();
	abstract public float getFireSkill();
	abstract public float getDodgeSkill();
	abstract public boolean hasWatch();
	abstract public float getRadius();
	abstract public int getWatchTimeUnits();
	abstract public int getFireCost();
	
	//write
	abstract public void startNewRound();
	abstract public void watchMovement(ICharacter mover, IMoveAndVisibility moveAndVisibility, ICharacterListener a_listener);
	abstract public void stopAllMovement();
	abstract public boolean fireAt(ICharacter fireTarget, IMoveAndVisibility m_game,ICharacterListener listener);
	
}
