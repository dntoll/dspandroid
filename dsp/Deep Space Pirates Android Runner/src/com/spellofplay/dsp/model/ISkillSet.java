package com.spellofplay.dsp.model;

public interface ISkillSet {
	enum SkillType {
		MAX_HITPOINTS,
		MAX_TIMEUNITS,
		FIRESKILL,
		DODGESKILL
	}
	abstract ISkill getSkill(SkillType a_type);
	abstract int getMaxHitPoints();
}
