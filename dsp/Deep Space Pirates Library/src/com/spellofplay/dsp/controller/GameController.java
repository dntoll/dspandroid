package com.spellofplay.dsp.controller;

import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.view.IDraw;
import com.spellofplay.dsp.view.IGameView;
import com.spellofplay.dsp.view.IInput;

public class GameController {

	
	public void update(IModel a_model, IGameView a_view, IInput a_input, float elapsedTimeSeconds) {
		
		a_view.setupInput(a_input, a_model);
		
		if (a_model.isEnemyTime()) {
			a_model.updateEnemies();
		} else {
			com.spellofplay.dsp.model.Soldier selectedSoldier = a_view.getSelectedSoldier(a_input, a_model);
			
			if (selectedSoldier != null) {
				//Everything that can be done with selected target
				ModelPosition destination = a_view.getDestination(a_input);
				
				if (destination != null) {
					a_model.doMoveTo(selectedSoldier, destination);
				}
			}
			
			if (a_model.hasUnfinishedActions()) {
				a_model.updatePlayers();
			}
		}
		
		
		
		
	}

	public void drawGame(IDraw drawable, IModel m_model, IGameView m_view) {
		m_view.drawGame(drawable, m_model);
	}

	

	

}
