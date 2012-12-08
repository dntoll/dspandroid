package com.spellofplay.dsp.view;

import android.graphics.Point;

import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Vector2;
import com.spellofplay.dsp.model.inner.Level;

public class Camera {

	public int m_screenWidth;
	public int m_screenHeight;
	public ViewPosition m_displacement = new ViewPosition(0,0);
	
	private static final int m_scale = 32;
	private boolean m_isScrolling = false;
	private Point m_scrollStartPos = new Point();
	
	public ViewPosition m_targetDisplacement = new ViewPosition(0,0);
	
	
	public Camera() {
	}
	
	public void setScreenSize(int windowWidth, int windowHeight) {
		m_screenWidth = windowWidth;
		m_screenHeight = windowHeight;
	}

	
	public ViewPosition toViewPos(Vector2 modelPos) {
		return new ViewPosition(modelPos.m_x * m_scale + m_displacement.m_x,
				modelPos.m_y * m_scale + m_displacement.m_y);
	}
	
	public ViewPosition toViewPos(ModelPosition modelPos) {
		return new ViewPosition(modelPos.m_x * m_scale + m_displacement.m_x,
								modelPos.m_y * m_scale + m_displacement.m_y);
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

	
	
	public void DoScroll(int screenWidth, int screenHeight, int a_dragX, int a_dragY) {
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
		int levelVisualWidth = m_scale * (Level.Width);
		if (m_screenWidth >= levelVisualWidth)
			m_displacement.m_x = 0;
		else {
			int maxDisplacement = levelVisualWidth - m_screenWidth; 
			if ( m_displacement.m_x > 0)
				m_displacement.m_x = 0;
			if ( m_displacement.m_x < -maxDisplacement)
				m_displacement.m_x = -maxDisplacement;
		}
		
		int levelVisualHeight = m_scale * (Level.Height);
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
	
	public void StopScrolling() {
		m_isScrolling = false;
		
	}

	public void focusOn(ModelPosition focusOn) {
		
		float vfx = focusOn.m_x * m_scale;
		float vfy = focusOn.m_y * m_scale;
		
		m_targetDisplacement.m_x = -vfx + m_screenWidth/2;
		m_targetDisplacement.m_y = -vfy + m_screenHeight/2;
		
		
		displacementWithinLevel();
	}

	public void update(float elapsedTimeSeconds) {
		Vector2 direction = m_targetDisplacement.sub(m_displacement).toVector2();
		
		float distance = direction.length();
		float speed = distance;
		float movementLength = speed * elapsedTimeSeconds;
		
		if (distance > movementLength) {
			direction.normalize();
			m_displacement.m_x += direction.m_x * movementLength;
			m_displacement.m_y += direction.m_y * movementLength;
		} else {
			m_displacement.m_x = m_targetDisplacement.m_x;
			m_displacement.m_y = m_targetDisplacement.m_y;
		}
		
		displacementWithinLevel();
	}


	
	

}
