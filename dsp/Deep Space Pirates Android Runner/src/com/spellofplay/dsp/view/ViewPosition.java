package com.spellofplay.dsp.view;

import com.spellofplay.dsp.model.Vector2;

class ViewPosition {

	public float m_x;
	float m_y;

	ViewPosition(float x, float y) {
		m_x = x;
		m_y = y;
	}

	public ViewPosition sub(ViewPosition other) {
		return new ViewPosition(m_x - other.m_x, m_y - other.m_y);
	}

	public float length() {
		
		return android.util.FloatMath.sqrt(m_x * m_x + m_y * m_y);
	}
	
	Vector2 toVector2()  {
		return new Vector2(m_x, m_y);
	}

}
