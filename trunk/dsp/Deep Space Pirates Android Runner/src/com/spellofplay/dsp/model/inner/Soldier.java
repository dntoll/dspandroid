package com.spellofplay.dsp.model.inner;

import com.spellofplay.dsp.model.ModelPosition;

public class Soldier extends Character {

	private float m_fireSkill = 0.5f;
	private float m_dodgeSkill = 0.5f;
	
	
	Soldier(ModelPosition startPosition) {
		super(startPosition, 4);
	}

	
	public float getFireSkill() {
		return m_fireSkill;
	}

	public float getDodgeSkill() {
		return m_dodgeSkill;
	}
	
	@Override
	public float getRange() {
		return 12.0f;
	}


	public void addMaxTimeUnits() {
		m_experience--;
		maxTimeUnits++;
	}


	public void addShootSkill() {
		m_experience--;
		m_fireSkill += 0.1f;
	}


	public void addDodgeSkill() {
		m_experience--;
		m_dodgeSkill += 0.1f;
		
	}


	
}
