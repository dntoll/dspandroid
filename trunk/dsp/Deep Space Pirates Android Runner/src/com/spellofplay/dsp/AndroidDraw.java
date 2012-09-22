package com.spellofplay.dsp;

import android.graphics.Canvas;
import android.graphics.Paint;

public class AndroidDraw implements com.spellofplay.dsp.view.IDraw {
	Paint m_guiText = new Paint();
	Canvas m_canvas;
	
	public AndroidDraw(Canvas a_can) {
		m_canvas = a_can;
	}
	
	@Override
	public void drawText(String gameTitle, int i, int j) {
		
		
		m_canvas.drawText(gameTitle, i, j, m_guiText );
	}

	

}
