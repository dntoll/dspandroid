package com.spellofplay.dsp.controller;


import com.spellofplay.dsp.model.Enemy;
import com.spellofplay.dsp.model.ModelFacade;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.view.AndroidDraw;
import com.spellofplay.dsp.view.GameView;
import com.spellofplay.dsp.view.SimpleGui;
import com.spellofplay.common.view.Input;

public class GameController {
	SimpleGui m_gui = new SimpleGui();
	
	public void update(AndroidDraw drawable, ModelFacade a_model, GameView a_view, Input a_input, float elapsedTimeSeconds) {
		
		
		if (a_model.enemyHasWon()) {
			if (m_gui.DoButtonCentered(drawable.getWindowWidth()/2, drawable.getWindowHeight()/2, "restart", a_input, false)) {
				a_model.startNewGame(0);
			}
			
			a_view.drawGame(drawable, a_model);
			m_gui.DrawGui(drawable);
			drawable.drawText("Game Over", 200, 10);
		} else if (a_model.playerHasWon()) {
			if (m_gui.DoButtonCentered(drawable.getWindowWidth()/2, drawable.getWindowHeight()/2, "restart", a_input, false)) {
				a_model.startNewGame(0);
			}
			
			a_view.drawGame(drawable, a_model);
			m_gui.DrawGui(drawable);
			
			drawable.drawText("Game Won", 200, 10);
		} else if (a_model.isEnemyTime()) {
			a_model.updateEnemies();
			a_view.drawGame(drawable, a_model);
			drawable.drawText("Enemy is moving", 200, 10);
			
		} else if (a_model.isSoldierTime()) {
		
			
			
			a_view.setupInput(a_input, a_model, drawable.getWindowWidth(), drawable.getWindowHeight());
			doInteractWithSoldiers(a_model, a_view, a_input);
			a_view.drawGame(drawable, a_model);	
			
		} else  {
			
			a_model.startNewRound();
			a_view.startNewRound();
			
			a_view.drawGame(drawable, a_model);
		}
		
		
		
	}

	private void doInteractWithSoldiers(ModelFacade a_model, GameView a_view,
			Input a_input) {
		com.spellofplay.dsp.model.Soldier selectedSoldier = a_view.getSelectedSoldier(a_model);
		
		if (selectedSoldier != null) {
			
			
			
			
			if (a_view.userWantsToMove()) {
				//Everything that can be done with selected target
				ModelPosition destination = a_view.getDestination();
				
				if (destination != null) {
					
					a_model.doMoveTo(selectedSoldier, destination);
				}
			} 
			
			if (a_view.userWantsToWait()){
				a_model.doWait(selectedSoldier);
			}
			
			
			if (a_view.userWantsToFire()){ 
				Enemy fireTarget = a_view.getFireTarget(a_model);
				
				if (fireTarget != null) {
					if (a_model.fireAt(selectedSoldier, fireTarget) == false) {
						
					}
				}
			}
		}
		
		
		a_model.updatePlayers();
	}

	

	

	

}
