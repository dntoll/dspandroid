package com.spellofplay.dsp.model;

public class Enemy extends Character{

	public Enemy(ModelPosition startPosition) {
		super(startPosition, 3);

	}

	public boolean isDoingSomething() {
		
		return m_pathFinder != null;
	}

	

	public void removeTimeUnit() {
		m_timeUnits--;
		
	}
	
	public float getFireSkill() {
		return 0.7f;
	}

	public float getDodgeSkill() {
		return 0.3f;
	}

	
}
