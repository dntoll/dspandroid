package com.spellofplay.dsp.model;

import java.util.Random;

public class RuleBook {

	private static Random m_die = new Random();
	
	public static boolean DetermineFireSuccess(Character character, Character fireTarget, boolean targetHasCover) {

		float toHitChance = getToHitChance(character, fireTarget, targetHasCover);
		
		if (m_die.nextDouble() < toHitChance)
			return true;
		
		return false;
		
		
		
	}
	
	public static boolean canFireAt(Character character, Character fireTarget, IMoveAndVisibility level) {
		if (level.lineOfSight(character, fireTarget) == false)
			return false;
		
		
		
		if (character.getTimeUnits() < character.getFireCost()) {
			return false;
		}
		return true;
	}
	
	
	public static float getToHitChance(Character character, Character fireTarget, boolean targetHasCover) {
		 float toHitChance = 0.5f + character.getFireSkill() - fireTarget.getDodgeSkill();
		 
		 float length = character.getPosition().sub(fireTarget.getPosition()).length();
		 
		 toHitChance -= length / 20.0f;
		 
		 if (targetHasCover)
			 toHitChance -= 0.3f;
		 
		 if (toHitChance < 0.1f)
			 toHitChance = 0.1f;
		 if (toHitChance > 0.9f)
			 toHitChance = 0.9f;
		 
		 return toHitChance;
	}

}