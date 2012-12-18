package com.spellofplay.dsp.model.inner;

import com.spellofplay.dsp.model.ISkillSet.SkillType;
import com.spellofplay.dsp.model.ModelPosition;

class Soldier extends Character {

	private static int[] values = {9,9,9,4};
	public int grenades = 1;
	
	Soldier(ModelPosition startPosition) {
		super(startPosition, new SkillSet(values));
	}
	
	@Override
	public float getRange() {
		return 12.0f;
	}

	void spendExperience(SkillType skillType) {
		skills.spendExperience(skillType, experience);
	}

	@Override
	public int getFireCost() {
		return 3;
	}

	@Override
	protected int getDamage() {
		return 1;
	}

	@Override
	public boolean canThrowGrenade() {
		return grenades > 0;
	}

	public void throwGrenade() {
		grenades--;
	}

	

	
	
}
