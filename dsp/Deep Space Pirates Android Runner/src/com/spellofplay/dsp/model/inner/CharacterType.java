package com.spellofplay.dsp.model.inner;

import com.spellofplay.dsp.model.ISkillSet;

public enum CharacterType {
	HAXOR(1,6,3,3),
	TANK(12,6,3,6),
	SNIPER(3, 6, 10, 3),
	ENEMY(2, 6, 4, 4);
	
	
	int maxHP;
	int maxTimeUnits;
	int dodgeSkill;
	int fireSkill;
	private CharacterType(int maxHP, int maxTimeUnits, int fireSkill, int dodgeSkill) {
	        this.maxHP = maxHP;
	        this.maxTimeUnits = maxTimeUnits;
	        this.fireSkill = fireSkill;
	        this.dodgeSkill = dodgeSkill;
	}
	public int[] getSkillArray() {
		 return ISkillSet.SkillType.toArray(maxHP, maxTimeUnits, fireSkill, dodgeSkill);
	}
}