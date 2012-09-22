package com.spellofplay.dsp.view;

import com.spellofplay.dsp.model.ModelFacade;

public class GameView {

	LevelDrawer level = new LevelDrawer();
	
	public void drawGame(IDraw drawable, ModelFacade a_model) {
		drawable.drawText(a_model.getGameTitle(), 10, 10);
		
		 
		level.draw(a_model.getLevel(), drawable);
	}

}
