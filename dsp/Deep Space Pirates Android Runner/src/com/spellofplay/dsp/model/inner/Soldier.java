package com.spellofplay.dsp.model.inner;

import com.spellofplay.dsp.model.ISkillSet.SkillType;
import com.spellofplay.dsp.model.ModelPosition;

class Soldier extends Character {

	private static int[] values = {9,9,9,4};
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
	
}
