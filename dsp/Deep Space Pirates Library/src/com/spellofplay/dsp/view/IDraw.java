package com.spellofplay.dsp.view;

import android.graphics.Color;
import android.graphics.Rect;

public interface IDraw {

	Object mainFont = null;

	void drawText(String gameTitle, int i, int j);

	
	void drawMeshToBackground(com.spellofplay.common.view.Mesh a_backgroundMeshBlocked, 
			 int a_width, 
			 int a_height, 
			 int a_scale, com.spellofplay.dsp.view.ITexture a_textureMap);
	
	
	void drawBackground();


	void drawBitmap(com.spellofplay.dsp.view.ITexture a_textureMap, Rect src, Rect dst);
	
	public void drawCircle(ViewPosition center, int radius, int color);


	void drawLine(ViewPosition vEpos, ViewPosition vsPos, int white);
}
