package com.spellofplay.dsp.view;

import android.graphics.Rect;

import com.spellofplay.dsp.model.Soldier;

public interface IDraw {

	Object mainFont = null;

	void drawText(String gameTitle, int i, int j);

	
	void drawMeshToBackground(com.spellofplay.common.view.Mesh a_backgroundMeshBlocked, 
			 int a_width, 
			 int a_height, 
			 int a_scale, com.spellofplay.dsp.view.ITexture a_textureMap);
	
	
	void drawBackground();


	void drawBitmap(com.spellofplay.dsp.view.ITexture a_textureMap, Rect src, Rect dst);
}
