package com.spellofplay.dsp.model;

import java.util.Random;

public class RuleBook {

	private static Random m_die = new Random();
	
	public static boolean DetermineFireSuccess(Character character, Character fireTarget) {

		float toHitChance = getToHitChance(character, fireTarget);
		
		if (m_die.nextDouble() < toHitChance)
			return true;
		
		return false;
		
		
		
	}
	
	public static float getToHitChance(Character character, Character fireTarget) {
		 float toHitChance = 0.5f + character.getFireSkill() - fireTarget.getDodgeSkill();
		 
		 if (toHitChance < 0.1f)
			 toHitChance = 0.1f;
		 if (toHitChance > 0.9f)
			 toHitChance = 0.9f;
		 
		 return toHitChance;
	}

}
