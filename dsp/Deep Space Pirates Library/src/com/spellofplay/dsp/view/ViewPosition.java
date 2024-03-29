package com.spellofplay.dsp.view;

public class ViewPosition {

	public float m_x;
	public float m_y;

	public ViewPosition(float x, float y) {
		m_x = x;
		m_y = y;
	}

	public ViewPosition sub(ViewPosition other) {
		return new ViewPosition(m_x - other.m_x, m_y - other.m_y);
	}

	public float length() {
		
		return (float) Math.sqrt(m_x * m_x + m_y * m_y);
	}

}
