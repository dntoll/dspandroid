package com.spellofplay.dsp.controller;


import com.spellofplay.dsp.model.Enemy;
import com.spellofplay.dsp.model.ModelFacade;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.MultiCharacterListener;
import com.spellofplay.dsp.view.AndroidDraw;
import com.spellofplay.dsp.view.GameView;
import com.spellofplay.dsp.view.LogView;
import com.spellofplay.common.view.Input;

public class GameController {
	
	private LogView m_log = new LogView();
	
	public void update(AndroidDraw drawable, ModelFacade a_model, GameView a_view, Input a_input, float elapsedTimeSeconds) {
		
		MultiCharacterListener mcl = new MultiCharacterListener();
		mcl.addListener(a_view);
		mcl.addListener(m_log);
		
		if (a_model.isEnemyTime()) {
			
			if (a_view.updateAnimations(a_model, elapsedTimeSeconds)) {
				a_model.updateEnemies(mcl);
			}
			a_view.drawGame(drawable, a_model);
			
			drawable.drawText("Enemy is moving", 200, 10, drawable.m_guiText);
			
		} else if (a_model.isSoldierTime()) {
		
			a_view.setupInput(a_input, a_model, drawable.getWindowWidth(), drawable.getWindowHeight());
			doInteractWithSoldiers(a_model, a_view, a_input, elapsedTimeSeconds, mcl);
			
			if (a_view.updateAnimations(a_model, elapsedTimeSeconds)) {
				a_model.updatePlayers(mcl);
			}
			a_view.drawGame(drawable, a_model);	
			
		} else  {
			
			a_model.startNewRound();
			a_view.startNewRound();
			
			a_view.drawGame(drawable, a_model);
			m_log.doLog("start new round");
		}
		
		m_log.draw(drawable);
		
	}

	

	private void doInteractWithSoldiers(ModelFacade a_model, GameView a_view, Input a_input, float a_elapsedTime, MultiCharacterListener a_mcl) {
		com.spellofplay.dsp.model.Soldier selectedSoldier = a_view.getSelectedSoldier(a_model);
		
		if (selectedSoldier != null) {
			if (a_view.userWantsToMove()) {
				//Everything that can be done with selected target
				ModelPosition destination = a_view.getDestination(a_model);
				
				if (destination != null) {
					
					a_model.doMoveTo(selectedSoldier, destination);
					a_view.unselectPath();
				}
			} 
			
			if (a_view.userWantsToWait()){
				a_model.doWait(selectedSoldier);
			}
			
			
			if (a_view.userWantsToFire()){ 
				Enemy fireTarget = a_view.getFireTarget(a_model);
				
				if (fireTarget != null) {
					if (a_model.fireAt(selectedSoldier, fireTarget, a_mcl) == false) {
						
					}
				}
			}
		}
		
		
	}

	

	

	

}
