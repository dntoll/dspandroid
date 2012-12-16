package com.spellofplay.dsp.model.inner;

import com.spellofplay.dsp.model.ISkillSet.SkillType;
import com.spellofplay.dsp.model.Experience;
import com.spellofplay.dsp.model.ModelPosition;

import junit.framework.TestCase;

public class CharacterTests extends TestCase {
	Soldier one = new Soldier(new ModelPosition(0,0));
	Soldier two = new Soldier(new ModelPosition(0,0));
	Soldier notEqual;
	
	public void testEqual() {
		boolean actual = one.equals(two);
		assertTrue(actual);
	}
	
	public void setUp() {
		notEqual = new Soldier(new ModelPosition(0,0));
	}
	
	
	public void testEqualModelPosition() {
		notEqual = new Soldier(new ModelPosition(3,0));
		theseShouldNotBeSame();
	}

	private void theseShouldNotBeSame() {
		boolean actual = one.equals(notEqual);
		assertFalse(actual);
	}
	
	
	public void testEqualTimeUnits() {
		notEqual.timeUnits = 5;
		theseShouldNotBeSame();
	}
	
	public void testEqualHitpoints() {
		notEqual.hitPoints = 5;
		theseShouldNotBeSame();
	}
	
	public void testEqualExperience() {
		notEqual.experience.experience = 5;
		theseShouldNotBeSame();
	}
	
	public void testEqualSkills() {
		Experience xp = new Experience();
		xp.experience = 100;
		notEqual.getSkills().spendExperience(SkillType.FIRESKILL, xp);
		
		theseShouldNotBeSame();
	}
	
	public void testResetResetsExperience()  {
		notEqual.experience.experience = 5;
		notEqual.reset(new ModelPosition());
		
		assertEquals(0, notEqual.experience.experience);
	}
	
	public void testResetResetsHitPoints()  {
		notEqual.hitPoints = 1;
		notEqual.reset(new ModelPosition());
		
		assertEquals(notEqual.getSkills().getMaxHitPoints(), notEqual.hitPoints);
	}
}
