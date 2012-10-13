package com.spellofplay.dsp.controller;

import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.view.IDraw;
import com.spellofplay.dsp.view.GameView;
import com.spellofplay.dsp.view.IInput;

public class GameController {

	
	public void update(IDraw drawable, IModel a_model, GameView a_view, IInput a_input, float elapsedTimeSeconds) {
		
		a_view.setupInput(a_input, a_model);
		
		
		
		
		if (a_model.enemyHasWon()) {
			//drawable.drawText("Game Over", 10, 100);
		} else if (a_model.playerHasWon()) {
			//drawable.drawText("Game Won", 10, 100);
		} else if (a_model.isEnemyTime()) {
			//drawable.drawText("Enemy moving", 10, 100);
			a_model.updateEnemies();
			
		} else if (a_model.isSoldierTime()) {
			com.spellofplay.dsp.model.Soldier selectedSoldier = a_view.getSelectedSoldier(a_input, a_model);
			
			if (selectedSoldier != null) {
				//Everything that can be done with selected target
				ModelPosition destination = a_view.getDestination(a_input);
				
				if (destination != null) {
					a_model.doMoveTo(selectedSoldier, destination);
				}
			}
			
			
			a_model.updatePlayers();
			
		} else  {
			a_model.startNewRound();
		}
		
		
		a_view.drawGame(drawable, a_model);
		
		if (a_model.enemyHasWon()) {
			drawable.drawText("Game Over", 10, 100);
		} else if (a_model.playerHasWon()) {
			drawable.drawText("Game Won", 10, 100);
		} else if (a_model.isEnemyTime()) {
			drawable.drawText("Enemy moving", 10, 100);
		}
		
	}

	

	

	

}
