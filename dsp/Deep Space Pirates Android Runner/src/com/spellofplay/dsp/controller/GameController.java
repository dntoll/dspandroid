package com.spellofplay.dsp.controller;


import com.spellofplay.dsp.model.Enemy;
import com.spellofplay.dsp.model.ModelFacade;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.view.AndroidDraw;
import com.spellofplay.dsp.view.GameView;
import com.spellofplay.dsp.view.Input;

public class GameController {

	
	public void update(AndroidDraw drawable, ModelFacade a_model, GameView a_view, Input a_input, float elapsedTimeSeconds) {
		
		a_view.setupInput(a_input, a_model);
		
		
		
		
		if (a_model.enemyHasWon()) {
			//drawable.drawText("Game Over", 10, 100);
		} else if (a_model.playerHasWon()) {
			//drawable.drawText("Game Won", 10, 100);
		} else if (a_model.isEnemyTime()) {
			//drawable.drawText("Enemy moving", 10, 100);
			a_model.updateEnemies();
			
		} else if (a_model.isSoldierTime()) {
			doInteractWithSoldiers(a_model, a_view, a_input);
			
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

	private void doInteractWithSoldiers(ModelFacade a_model, GameView a_view,
			Input a_input) {
		com.spellofplay.dsp.model.Soldier selectedSoldier = a_view.getSelectedSoldier(a_input, a_model);
		
		if (selectedSoldier != null) {
			//Everything that can be done with selected target
			ModelPosition destination = a_view.getDestination(a_input);
			
			if (destination != null) {
				a_model.doMoveTo(selectedSoldier, destination);
			}
			
			Enemy fireTarget = a_view.getFireTarget(a_input);
			
			if (fireTarget != null) {
				if (a_model.fireAt(selectedSoldier, fireTarget) == false) {
					
				}
			}
		}
		
		
		a_model.updatePlayers();
	}

	

	

	

}
