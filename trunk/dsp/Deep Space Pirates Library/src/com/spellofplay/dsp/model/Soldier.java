package com.spellofplay.dsp.model;

public class Soldier {

	ModelPosition m_position;

	public Soldier(ModelPosition startPosition) {
		m_position = startPosition;
	}

	public ModelPosition getPosition() {
		return m_position;
	}

	public float getRadius() {
		return 0.5f;
	}

	public void reset(ModelPosition startLocation) {
		m_position = startLocation;
		
	}

}
