package com.spellofplay.dsp.model;

public class Enemy {

	int m_timeUnits = 0;
	public int getTimeUnits() {
		// TODO Auto-generated method stub
		return m_timeUnits;
	}

	public void startNewRound() {
		m_timeUnits = 3;
		
	}

	public boolean update() {
		m_timeUnits = 0;
		return true;
	}

}
