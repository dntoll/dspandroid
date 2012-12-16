package com.spellofplay.dsp.model;

import java.util.Random;

import com.spellofplay.dsp.model.ISkillSet.SkillType;


public class RuleBook {

	private static Random m_die = new Random();
	
	public static boolean DetermineFireSuccess(ICharacter character, ICharacter fireTarget, boolean targetHasCover) {

		float toHitChance = getToHitChance(character, fireTarget, targetHasCover);
		
		if (m_die.nextDouble() < toHitChance) {
			return true;
		}
		
		return false;
		
		
		
	}
	
	public static boolean couldFireIfHadTime(ICharacter character, ICharacter fireTarget, IMoveAndVisibility a_moveAndVisibility) {
		if (a_moveAndVisibility.hasClearSight(character, fireTarget) == false) {
			return false;
		}
		
		float distance = character.distance(fireTarget);
		if (distance > character.getRange()) {
			return false;
		}
		
		return true;
	}
	
	public static boolean canFireAt(ICharacter character, ICharacter fireTarget, IMoveAndVisibility a_moveAndVisibility) {
		
		if (fireTarget.getHitPoints() <= 0) {
			return false;
		}
		
		if (couldFireIfHadTime(character, fireTarget, a_moveAndVisibility) == false) {
			return false;
		}
		
		
		if (character.hasTimeToFire() == false) {
			return false;
		}
		return true;
	}
	
	//TODO: Tighten interface to ISKill * 2, distance and cover
	public static float getToHitChance(ICharacter character, ICharacter fireTarget, boolean targetHasCover) {
		
		 final ISkill fireSkill = character.getSkills().getSkill(SkillType.FIRESKILL);
		 final ISkill dodgeSkill = character.getSkills().getSkill(SkillType.DODGESKILL);
		
		 float toHitChance = 0.5f + (float)(fireSkill.getValue() - dodgeSkill.getValue()) * 0.1f;
		 
		 final float length = character.distance(fireTarget);
		 
		 //TODO magic numbers into constants
		 toHitChance -= length / 20.0f;
		 
		 if (targetHasCover) {
			 toHitChance -= 0.3f;
		 }
		 
		 if (toHitChance < 0.1f) {
			 toHitChance = 0.1f;
		 }
		 if (toHitChance > 0.9f) {
			 toHitChance = 0.9f;
		 }
		 
		 return toHitChance;
	}

	

}
