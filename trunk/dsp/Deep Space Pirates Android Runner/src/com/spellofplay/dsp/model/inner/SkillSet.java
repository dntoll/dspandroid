package com.spellofplay.dsp.model.inner;

import com.spellofplay.dsp.model.Experience;
import com.spellofplay.dsp.model.ISkill;
import com.spellofplay.dsp.model.ISkillSet;

class SkillSet implements ISkillSet{
	private Skill[] skills = new Skill[SkillType.values().length];
	
	

	
	SkillSet(int[] startValues) {
		for (int i = 0; i< SkillType.values().length; i++) {
			skills[i] = new Skill(startValues[i]);
		}
	}
	
	@Override
	public ISkill getSkill(SkillType a_type) {
		return skills[a_type.ordinal()];
	}

	public int getHitPoints() {
		return getSkill(SkillType.MAX_HITPOINTS).getValue();
	}

	public int getTimeUnits() {
		return getSkill(SkillType.MAX_TIMEUNITS).getValue();
	}

	void spendExperience(SkillType skillType, Experience stash) {
		Skill skill = (Skill)getSkill(skillType);
		skill.improve(stash);
	}

	@Override
	public int getMaxHitPoints() {
		return getSkill(SkillType.MAX_HITPOINTS).getValue();
	}

	public boolean canSpendExperience(Experience experience) {
		for (int i = 0; i< SkillType.values().length; i++) {
			if (skills[i].canImprove(experience)) {
				return true;
			}
		}
		
		return false;
	}
	
}
