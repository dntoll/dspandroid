package com.spellofplay.dsp.model.inner;

import com.spellofplay.dsp.model.ModelPosition;

public class Soldier extends Character {

	
	
	public Soldier(ModelPosition startPosition) {
		super(startPosition, 7);
	}

	
	public float getFireSkill() {
		return 0.75f;
	}

	public float getDodgeSkill() {
		return 0.6f;
	}
	
	@Override
	public float getRange() {
		return 12.0f;
	}


	
}
