package com.spellofplay.dsp.controller;


import com.spellofplay.dsp.model.Enemy;
import com.spellofplay.dsp.model.ModelFacade;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Soldier;
import com.spellofplay.dsp.view.AndroidDraw;
import com.spellofplay.dsp.view.InteractionView;
import com.spellofplay.dsp.view.LogView;
import com.spellofplay.dsp.view.MasterView;
import com.spellofplay.common.view.Input;

public class GameController {
	
	
	MasterView m_masterView;
	private LogView m_log = new LogView();
	
	public GameController(MasterView m_masterView) {
		this.m_masterView = m_masterView;
	}



	public void update(AndroidDraw drawable, ModelFacade a_model, Input a_input, float elapsedTimeSeconds) {
		
		MultiCharacterListener mcl = new MultiCharacterListener();
		mcl.addListener(m_masterView);
		mcl.addListener(m_log);
		
		if (a_model.isSoldierTime()) {
			updateSoldiers(drawable, a_model, a_input, elapsedTimeSeconds, mcl);
			if (a_model.isSoldierTime() == false) {
				a_model.startNewEnemyRound();
			}
		} else if (a_model.isEnemyTime()) {
			updateEnemies(drawable, a_model, elapsedTimeSeconds, mcl);
		} else  {
			startNewSoldierRound(drawable, a_model);
		}
		
		m_log.draw(drawable);
		
	}
	

	public void startNewSoldierRound(AndroidDraw drawable, ModelFacade a_model) {
		a_model.startNewSoldierRound();
		m_masterView.startNewRound();
		
		m_masterView.drawGame(drawable, a_model);
		m_log.doLog("start new round");
	}



	public void updateSoldiers(AndroidDraw drawable, ModelFacade a_model, Input a_input, float elapsedTimeSeconds, MultiCharacterListener mcl) {
		
		doInteractWithSoldiers(drawable, a_model, a_input, elapsedTimeSeconds, mcl);
		
		if (m_masterView.updateAnimations(a_model, elapsedTimeSeconds)) {
			a_model.updatePlayers(mcl);
		}
		m_masterView.drawGame(drawable, a_model);
	}



	public void updateEnemies(AndroidDraw drawable, ModelFacade a_model, float elapsedTimeSeconds, MultiCharacterListener mcl) {
		if (m_masterView.updateAnimations(a_model, elapsedTimeSeconds)) {
			a_model.updateEnemies(mcl);
		}
		m_masterView.drawGame(drawable, a_model);
		
		drawable.drawText("Enemy is moving", 200, 10, drawable.m_guiText);
	}

	

	private void doInteractWithSoldiers(AndroidDraw drawable, 
										ModelFacade a_model, 
										Input a_input, 
										float a_elapsedTime, 
										MultiCharacterListener a_mcl) {
		
		InteractionView actionView = m_masterView.getInteractionView();
		
		actionView.setupInput(a_input, a_model, drawable.getWindowWidth(), drawable.getWindowHeight());
		
		Soldier selectedSoldier = actionView.getSelectedSoldier(a_model);
		
		if (selectedSoldier != null) {
			if (actionView.userWantsToMove()) {
				//Everything that can be done with selected target
				ModelPosition destination = actionView.getDestination(a_model);
				
				if (destination != null) {
					
					a_model.doMoveTo(selectedSoldier, destination);
					actionView.unselectPath();
				}
			} 
			
			if (actionView.userWantsToWatch()){
				a_model.doWatch(selectedSoldier);
			}
			
			
			if (actionView.userWantsToFire()){ 
				Enemy fireTarget = actionView.getFireTarget(a_model);
				
				if (fireTarget != null) {
					if (a_model.fireAt(selectedSoldier, fireTarget, a_mcl) == false) {
						
					}
				}
			}
		}
	}



	

	

	

	

}
