package com.spellofplay.dsp.model;

public class Vector2 {
	public float m_x;
	public float m_y;
	
	public Vector2() {
		m_x = 0;
		m_y = 0;
	}
	
	
	public Vector2(float a_x, float a_y) {
		m_x = a_x;
		m_y = a_y;
	}


	public Vector2 sub(Vector2 other) {
		return new Vector2(m_x - other.m_x, m_y - other.m_y);
	}
	
	public Vector2 mul(float a) {
		return new Vector2(m_x * a, m_y  * a);
	}
	
	public void normalize() {
		float len = length();
		if (len > 0.0f) {
			m_x /= len;
			m_y /= len;
		}

	}

	public float length() {
		
		return (float) Math.sqrt(m_x * m_x + m_y * m_y);
	}
	
	@Override 
	public boolean equals(Object aThat) {
		
		Vector2 other = (Vector2) aThat;
		return other.m_x == m_x && other.m_y == m_y;
		
	}


	public Vector2 sub(float f, float g) {
		m_x -= f;
		m_y -= g;
		return this;
	}


	public float dot(Vector2 toP) {
		return m_x * toP.m_x + m_y + toP.m_y;
	}

}
