package com.spellofplay.dsp.view;

public interface IDraw {

	Object mainFont = null;

	void drawText(String gameTitle, int i, int j);

	
	void drawMeshToBackground(com.spellofplay.common.view.Mesh a_backgroundMeshBlocked, 
			 int a_width, 
			 int a_height, 
			 int a_scale, com.spellofplay.dsp.view.ITexture a_textureMap);
	
	
	public void drawBackground();
}
