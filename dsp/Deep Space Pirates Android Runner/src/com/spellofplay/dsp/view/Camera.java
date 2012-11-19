package com.spellofplay.dsp.view;

import android.graphics.Point;

import com.spellofplay.dsp.model.Level;
import com.spellofplay.dsp.model.ModelPosition;

public class Camera {

	private static final int m_scale = 64;
	
	public ViewPosition m_displacement = new ViewPosition(0,0);

	public ViewPosition toViewPos(ModelPosition soldierModelPos) {
		
		
		
		return new ViewPosition(soldierModelPos.m_x * m_scale + m_displacement.m_x,
								soldierModelPos.m_y * m_scale + m_displacement.m_y);
	}
	
	public ViewPosition toViewPos(int x, int y) {
		return new ViewPosition(x * m_scale + m_displacement.m_x,
				y * m_scale + m_displacement.m_y);
	}
	
	public ViewPosition toViewPos(float x, float y) {
		return new ViewPosition(x * m_scale + m_displacement.m_x,
				y * m_scale + m_displacement.m_y);
	}

	public float toViewScale(float modelDistance) {
		// TODO Auto-generated method stub
		return modelDistance * m_scale;
	}

	public ModelPosition toModelPos(ViewPosition mousePos) {
		
		ViewPosition vp = mousePos.sub(m_displacement).sub(new ViewPosition(-m_scale/2,-m_scale/2));
		
		return new ModelPosition((int)(vp.m_x / m_scale), (int)(vp.m_y / m_scale));
	}

	public int getHalfScale() {
		return m_scale / 2;
	}

	public int getScale() {
		return m_scale;
	}

	
	boolean m_isScrolling = false;
    Point m_scrollStartPos = new Point();
	public void DoScroll(int screenWidth, int screenHeight, int a_dragX, int a_dragY) {
		if (m_isScrolling == false) {
			m_isScrolling = true;
			m_scrollStartPos.x = (int)m_displacement.m_x;
			m_scrollStartPos.y = (int)m_displacement.m_y;
		} else {
			m_displacement.m_x = m_scrollStartPos.x + a_dragX;
			m_displacement.m_y = m_scrollStartPos.y + a_dragY;
			
			int levelVisualWidth = m_scale * (Level.Width);
			//Om skärmens bredd är större än banan
			if (screenWidth >= levelVisualWidth)
				m_displacement.m_x = 0;
			else {
				//banan är bredare än skärmen
				int maxDisplacement = levelVisualWidth - screenWidth; 
				if ( m_displacement.m_x > 0)
					m_displacement.m_x = 0;
				if ( m_displacement.m_x < -maxDisplacement)
					m_displacement.m_x = -maxDisplacement;
			}
			
			int levelVisualHeight = m_scale * (Level.Height);
			//Om skärmens bredd är större än banan
			if (screenHeight >= levelVisualHeight)
				m_displacement.m_y = 0;
			else {
				//banan är bredare än skärmen
				int maxDisplacement = levelVisualHeight - screenHeight; 
				if ( m_displacement.m_y > 0)
					m_displacement.m_y = 0;
				if ( m_displacement.m_y < -maxDisplacement)
					m_displacement.m_y = -maxDisplacement;
			}
			
			
		}
		
	}
	
	public void StopScrolling() {
		m_isScrolling = false;
		
	}

	

	

}
