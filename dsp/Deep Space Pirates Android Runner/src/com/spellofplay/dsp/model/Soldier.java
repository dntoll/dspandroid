package com.spellofplay.dsp.model;

public class Soldier extends Character {

	
	public Soldier(ModelPosition startPosition) {
		super(startPosition, 6);
	}

	
	public float getFireSkill() {
		return 0.5f;
	}

	public float getDodgeSkill() {
		return 0.5f;
	}
	
	@Override
	public float getRange() {
		return 10.0f;
	}


	
	

	

}
