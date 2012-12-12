package com.spellofplay.dsp.view;

import java.util.ArrayList;
import java.util.List;

import com.spellofplay.common.view.Input;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;

public class SimpleGui {
	public static final int BUTTON_HEIGHT = 32;
	public static final int BUTTON_WIDTH = 64;
	
	private Paint m_guiText = new Paint();
	private List<DrawCall> m_drawCalls = new ArrayList<DrawCall>();

	public boolean DoButtonCentered(int x, int y, String a_text, Input a_input) {
		
		int width = BUTTON_WIDTH;
		int height = BUTTON_HEIGHT;
		
		int left = x - width/2;
		int right = x + width/2;
		int top = y - height/2;
		int bottom = y + height/2;
		
		
		
		boolean mouseOver = false;
		if (a_input.m_mousePosition.x > left && 
			a_input.m_mousePosition.x < right &&
			a_input.m_mousePosition.y > top &&
			a_input.m_mousePosition.y < bottom) 
		{
			mouseOver = true;
		}
		
		m_drawCalls.add(new DrawButton(x, y, a_text, a_input.IsDragging(), height, left, right, top, bottom, mouseOver));
		
		
		if (mouseOver) {
			if (a_input.IsMouseClicked()) {
				return true;
			}
		}
		return false;
	}
	
	public void DrawGui(AndroidDraw a_draw) {
		for (DrawCall call : m_drawCalls) {
			call.Draw(a_draw);
		}
		
		m_drawCalls.clear();
	}
	
	private abstract class DrawCall {
		
		public abstract void Draw(AndroidDraw a_draw);
		
	}
	private class DrawButton extends DrawCall {
		private int x, y, left, top, right, bottom, height;
		private boolean mouseOver, isDragging;
		private String text;
		
		private DrawButton(int a_x, int a_y, String a_text,
				boolean a_isDragging, int a_height, int a_left,
				int a_right, int a_top, int a_bottom, boolean a_mouseOver) {
			x = a_x;
			y = a_y;
			text = a_text;
			isDragging = a_isDragging;
			height = a_height;
			left = a_left;
			right = a_right;
			top = a_top;
			bottom = a_bottom;
			mouseOver = a_mouseOver;
		}
		
		public void Draw(AndroidDraw a_draw) {
			m_guiText.setColor(Color.BLACK); 
			m_guiText.setStyle(Style.STROKE);
			
			a_draw.drawRect(left, top, right, bottom, m_guiText);
			m_guiText.setStyle(Style.FILL);
			if (mouseOver && isDragging) {
				m_guiText.setColor(Color.LTGRAY); 
			} else {
				m_guiText.setColor(Color.GRAY); 
			}
			a_draw.drawRect(left+1, top+1, right-1, bottom-1, m_guiText);
			
			m_guiText.setColor(Color.WHITE);
			
			m_guiText.setTextAlign(Align.CENTER);
			
			int textPosY = height / 2 - (int)m_guiText.getTextSize();
			
			a_draw.drawText(text, x, y + textPosY, Color.WHITE);
			m_guiText.setColor(Color.WHITE);
			m_guiText.setAlpha(255);
			
		}
	}
	
	
}
