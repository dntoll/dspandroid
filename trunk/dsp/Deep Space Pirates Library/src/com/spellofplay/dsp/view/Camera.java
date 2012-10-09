package com.spellofplay.dsp.view;

import com.spellofplay.dsp.model.ModelPosition;

public class Camera {

	public float m_scale = 1;
	public ViewPosition m_displacement = new ViewPosition(0,0);

	public ViewPosition toViewPos(ModelPosition soldierModelPos) {
		
		return new ViewPosition(soldierModelPos.m_x * m_scale + m_displacement.m_x,
								soldierModelPos.m_y * m_scale + m_displacement.m_y);
	}

	public float toViewScale(float modelDistance) {
		// TODO Auto-generated method stub
		return modelDistance * m_scale;
	}

	public ModelPosition toModelPos(ViewPosition mousePos) {
		
		ViewPosition vp = mousePos.sub(m_displacement);
		
		return new ModelPosition((int)(vp.m_x / m_scale), (int)(vp.m_y / m_scale));
	}

}
