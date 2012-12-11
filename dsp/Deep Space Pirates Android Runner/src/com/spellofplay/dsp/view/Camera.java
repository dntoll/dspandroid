package com.spellofplay.dsp.view;

import android.graphics.Point;
import android.graphics.Rect;

import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Preferences;
import com.spellofplay.dsp.model.Vector2;

class Camera {

	private int m_screenWidth;
	private int m_screenHeight;
	ViewPosition m_displacement = new ViewPosition(0,0);
	
	private static final int m_scale = 32;
	private boolean m_isScrolling = false;
	private Point m_scrollStartPos = new Point();
	
	private ViewPosition m_targetDisplacement = new ViewPosition(0,0);
	
	
	public Camera() {
	}
	
	void setScreenSize(int windowWidth, int windowHeight) {
		m_screenWidth = windowWidth;
		m_screenHeight = windowHeight;
	}

	
	ViewPosition toViewPos(Vector2 modelPos) {
		return new ViewPosition(modelPos.x * m_scale + m_displacement.m_x,
				modelPos.y * m_scale + m_displacement.m_y);
	}
	
	ViewPosition toViewPos(ModelPosition modelPos) {
		return new ViewPosition(modelPos.x * m_scale + m_displacement.m_x,
								modelPos.y * m_scale + m_displacement.m_y);
	}
	
	ViewPosition toViewPos(int x, int y) {
		return new ViewPosition(x * m_scale + m_displacement.m_x,
				y * m_scale + m_displacement.m_y);
	}
	
	Rect toViewRect(int x, int y) {
		ViewPosition vp = toViewPos(x, y);
		return toRect(vp);
	}
	
	Rect toRect(ViewPosition vp) {
		return new Rect((int)vp.m_x - getHalfScale(), 
				(int)vp.m_y - getHalfScale(),
				(int)vp.m_x + getHalfScale(), 
				(int)vp.m_y + getHalfScale());
	}
	
	float toViewScale(float modelDistance) {
		return modelDistance * m_scale;
	}

	ModelPosition toModelPos(ViewPosition mousePos) {
		
		ViewPosition vp = mousePos.sub(m_displacement).sub(new ViewPosition(-m_scale/2,-m_scale/2));
		
		return new ModelPosition((int)(vp.m_x / m_scale), (int)(vp.m_y / m_scale));
	}

	public int getHalfScale() {
		return m_scale / 2;
	}

	public int getScale() {
		return m_scale;
	}

	
	
	void DoScroll(int screenWidth, int screenHeight, int a_dragX, int a_dragY) {
		if (m_isScrolling == false) {
			m_isScrolling = true;
			m_scrollStartPos.x = (int)m_displacement.m_x;
			m_scrollStartPos.y = (int)m_displacement.m_y;
			m_targetDisplacement.m_x = m_displacement.m_x;
			m_targetDisplacement.m_y = m_displacement.m_y;
		} else {
			m_displacement.m_x = m_scrollStartPos.x + a_dragX;
			m_displacement.m_y = m_scrollStartPos.y + a_dragY;
			m_targetDisplacement.m_x = m_displacement.m_x;
			m_targetDisplacement.m_y = m_displacement.m_y;
			
			displacementWithinLevel();
			
			
		}
		
	}

	private void displacementWithinLevel() {
		int levelVisualWidth = m_scale * (Preferences.WIDTH);
		if (m_screenWidth >= levelVisualWidth)
			m_displacement.m_x = 0;
		else {
			int maxDisplacement = levelVisualWidth - m_screenWidth; 
			if ( m_displacement.m_x > 0)
				m_displacement.m_x = 0;
			if ( m_displacement.m_x < -maxDisplacement)
				m_displacement.m_x = -maxDisplacement;
		}
		
		int levelVisualHeight = m_scale * (Preferences.HEIGHT);
		if (m_screenHeight >= levelVisualHeight)
			m_displacement.m_y = 0;
		else {
			int maxDisplacement = levelVisualHeight - m_screenHeight; 
			if ( m_displacement.m_y > 0)
				m_displacement.m_y = 0;
			if ( m_displacement.m_y < -maxDisplacement)
				m_displacement.m_y = -maxDisplacement;
		}
	}
	
	void StopScrolling() {
		m_isScrolling = false;
		
	}

	void focusOn(ModelPosition focusOn) {
		
		float vfx = focusOn.x * m_scale;
		float vfy = focusOn.y * m_scale;
		
		m_targetDisplacement.m_x = -vfx + m_screenWidth/2;
		m_targetDisplacement.m_y = -vfy + m_screenHeight/2;
		
		
		displacementWithinLevel();
	}

	void update(float elapsedTimeSeconds) {
		Vector2 direction = m_targetDisplacement.sub(m_displacement).toVector2();
		
		float distance = direction.length();
		float speed = distance;
		float movementLength = speed * elapsedTimeSeconds;
		
		if (distance > movementLength) {
			direction.normalize();
			m_displacement.m_x += direction.x * movementLength;
			m_displacement.m_y += direction.y * movementLength;
		} else {
			m_displacement.m_x = m_targetDisplacement.m_x;
			m_displacement.m_y = m_targetDisplacement.m_y;
		}
		
		displacementWithinLevel();
	}


	
	

}
