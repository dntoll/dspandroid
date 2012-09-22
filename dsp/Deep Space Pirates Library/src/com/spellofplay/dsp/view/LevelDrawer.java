package com.spellofplay.dsp.view;

import com.spellofplay.dsp.model.Level;

public class LevelDrawer {

	public void draw(Level level, IDraw drawable) {
		
		for (int x = 0; x < level.Width; x++) {
			for (int y = 0; y < level.Height; y++) {
				drawable.drawText("x", x*10, y*10);
			}	
		}
		
		
	}

}
