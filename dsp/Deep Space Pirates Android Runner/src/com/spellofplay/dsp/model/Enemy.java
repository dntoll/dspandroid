package com.spellofplay.dsp.model;

public class Enemy extends Character{

	public Enemy(ModelPosition startPosition) {
		super(startPosition, 3);

	}

	public boolean isDoingSomething() {
		
		return m_pathFinder != null;
	}

	public void attack(Soldier closest) {
		
		closest.m_hitpoints = 0;
	}

	public void removeTimeUnit() {
		m_timeUnits--;
		
	}

	
}
