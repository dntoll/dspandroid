package com.spellofplay.dsp.model.inner;

import java.util.Random;

import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.IMoveAndVisibility;

public class RuleBook {

	private static Random m_die = new Random();
	
	static boolean DetermineFireSuccess(ICharacter character, ICharacter fireTarget, boolean targetHasCover) {

		float toHitChance = getToHitChance(character, fireTarget, targetHasCover);
		
		if (m_die.nextDouble() < toHitChance)
			return true;
		
		return false;
		
		
		
	}
	
	static boolean couldFireIfHadTime(ICharacter character, ICharacter fireTarget, IMoveAndVisibility a_moveAndVisibility) {
		if (a_moveAndVisibility.hasClearSight(character, fireTarget) == false)
			return false;
		
		float distance = character.distance(fireTarget);
		if (distance > character.getRange())
			return false;
		
		return true;
	}
	
	public static boolean canFireAt(ICharacter character, ICharacter fireTarget, IMoveAndVisibility a_moveAndVisibility) {
		
		if (fireTarget.getHitPoints() <= 0) {
			return false;
		}
		
		if (couldFireIfHadTime(character, fireTarget, a_moveAndVisibility) == false)
			return false;
		
		
		if (character.hasTimeToFire() == false) {
			return false;
		}
		return true;
	}
	
	
	public static float getToHitChance(ICharacter character, ICharacter fireTarget, boolean targetHasCover) {
		 float toHitChance = 0.5f + character.getFireSkill() - fireTarget.getDodgeSkill();
		 
		 float length = character.distance(fireTarget);
		 
		 //TODO magic numbers into constants
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
