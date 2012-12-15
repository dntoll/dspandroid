package com.spellofplay.dsp.view;

import android.graphics.PointF;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class Input {
	public PointF m_mousePosition = new PointF();
	public PointF m_dragFrom = new PointF();
	
	public boolean m_isDragging = false;
	boolean m_dragClick = false;
	
	static int g_keys = 129;
	boolean m_keyDown[];
	boolean m_keyClicked[];
	
	public Input() {
		m_keyDown = new boolean[g_keys];
		m_keyClicked = new boolean[g_keys];
		
		for (int i = 0; i< g_keys; i++) {
			m_keyDown[i] = m_keyClicked[i] = false;
		}
	}
	
	
		
	public boolean IsMouseClicked() {
		if (m_dragClick) {
			m_dragClick = false;
			return true;
		}
		return false;
	}
	
	public boolean IsKeyClicked(int a_keyCode) {
		if (m_keyClicked[a_keyCode]) {
			m_keyClicked[a_keyCode] = false;
			return true;
		}
		return false;
	}

	public boolean onKeyUp(int a_keyCode, KeyEvent event) {
		m_keyClicked[a_keyCode] = true;
		m_keyDown[a_keyCode] = false;
		
		return IsKeyActive(a_keyCode);
	}

	public boolean onKeyPreIme(int a_keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return IsKeyActive(a_keyCode);
	}

	public boolean onKeyDown(int a_keyCode, KeyEvent event) {
		//m_keyClicked[a_keyCode] = false;
		m_keyDown[a_keyCode] = true;
		
		return IsKeyActive(a_keyCode);
	}

	private boolean IsKeyActive(int a_keyCode) {
		switch (a_keyCode) {
			case KeyEvent.KEYCODE_MENU :
			case KeyEvent.KEYCODE_BACK :
				
				return true;
		}
		
		return false;
	}


	public boolean onKeyShortcut(int a_keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return IsKeyActive(a_keyCode);
	}

	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		
		switch (action) { 
			case (MotionEvent.ACTION_DOWN) : // Touch screen pressed
				m_mousePosition.set(event.getX(), event.getY());
				m_dragFrom.set(event.getX(), event.getY());
				m_isDragging = true;
				return true;
			case (MotionEvent.ACTION_UP) : // Touch screen touch ended
				m_mousePosition.set(event.getX(), event.getY());
				m_isDragging = false;
				m_dragClick = true;
				return true;
			case (MotionEvent.ACTION_MOVE) : // Contact has moved across screen
				m_mousePosition.set(event.getX(), event.getY());
				
				return true;
			case (MotionEvent.ACTION_CANCEL) : // Touch event cancelled

				m_mousePosition.set(event.getX(), event.getY());
				return true;
			
	   }
		
	   return false;
		
	}


	public boolean IsDragging() {
		return m_isDragging;
	}
}
