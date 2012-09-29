package com.spellofplay.dsp.view;

import com.spellofplay.dsp.model.ModelFacade;

public class GameView {

	
	
	LevelDrawer m_level;// = new LevelDrawer();
	boolean hasInitatedBuffer = false;
	public GameView(ITexture a_texture) {
		m_level = new LevelDrawer(a_texture);
	}
	
	public void drawGame(IDraw drawable, ModelFacade a_model) {
		
		
		
		if (hasInitatedBuffer == false) {
			m_level.draw(a_model.getLevel(), drawable);
			hasInitatedBuffer = true;
		}
		
		drawable.drawBackground();
		
		drawable.drawText(a_model.getGameTitle(), 10, 10);
	}

}
