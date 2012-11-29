package com.spellofplay.dsp.model;

public class ModelPosition {
	public int m_x;
	public int m_y;
	
	public ModelPosition() {
		m_x = 0;
		m_y = 0;
	}
	
	
	public ModelPosition(int a_x, int a_y) {
		m_x = a_x;
		m_y = a_y;
	}


	public Vector2 sub(ModelPosition other) {
		return new Vector2(m_x - other.m_x, m_y - other.m_y);
	}


	public float length() {
		
		return android.util.FloatMath.sqrt(m_x * m_x + m_y * m_y);
	}
	
	@Override 
	public boolean equals(Object aThat) {
		
		ModelPosition other = (ModelPosition) aThat;
		return other.m_x == m_x && other.m_y == m_y;
		
	}


	public Vector2 toCenterTileVector() {
		return new Vector2(m_x + 0.5f, m_y + 0.5f);
	}


	

}
