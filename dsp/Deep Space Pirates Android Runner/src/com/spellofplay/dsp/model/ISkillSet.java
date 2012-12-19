package com.spellofplay.dsp.model;

public interface ISkillSet {
	enum SkillType {
		MAX_HITPOINTS,
		MAX_TIMEUNITS,
		FIRESKILL,
		DODGESKILL;
		
		public static int[] toArray(int maxHP, int maxTimeUnits, int fireSkill, int dodgeSkill) {
			int[] values = new int[SkillType.values().length];
			
			values[MAX_HITPOINTS.ordinal()] = maxHP;
			values[MAX_TIMEUNITS.ordinal()] = maxTimeUnits;
			values[FIRESKILL.ordinal()] = fireSkill;
			values[DODGESKILL.ordinal()] = dodgeSkill;
			
			
			return values;
			
		}
	}
	abstract ISkill getSkill(SkillType a_type);
	abstract int getMaxHitPoints();
	
	
}
