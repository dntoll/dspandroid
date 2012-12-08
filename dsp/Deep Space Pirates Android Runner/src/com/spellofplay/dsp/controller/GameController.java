package com.spellofplay.dsp.controller;


import com.spellofplay.common.view.Input;
import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.IEventTarget;
import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.ModelFacade;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.view.AndroidDraw;
import com.spellofplay.dsp.view.InteractionView;
import com.spellofplay.dsp.view.LogView;
import com.spellofplay.dsp.view.MasterView;

public class GameController {
	
	
	private MasterView m_masterView;
	private LogView m_log = new LogView();
	private IModel model;
	
	
	public GameController(MasterView m_masterView, IModel model) {
		this.m_masterView = m_masterView;
		this.model = model;
	}



	public void update(AndroidDraw drawable, IEventTarget eventTarget, Input input, float elapsedTimeSeconds) {
		
		MultiCharacterListener mcl = new MultiCharacterListener();
		mcl.addListener(m_masterView);
		mcl.addListener(m_log);
		
		if (model.isSoldierTime()) {
			updateSoldiers(drawable, eventTarget, input, elapsedTimeSeconds, mcl);
			if (model.isSoldierTime() == false) {
				eventTarget.startNewEnemyRound();
			}
		} else if (model.isEnemyTime()) {
			updateEnemies(drawable, eventTarget, elapsedTimeSeconds, mcl);
		} else  {
			startNewSoldierRound(drawable, eventTarget, elapsedTimeSeconds);
		}
		
		m_log.draw(drawable);
		
	}
	

	private void startNewSoldierRound(AndroidDraw drawable, IEventTarget eventTarget, float elapsedTimeSeconds) {
		eventTarget.startNewSoldierRound();
		m_masterView.startNewRound();
		
		m_masterView.drawGame(drawable, model, elapsedTimeSeconds);
		m_log.doLog("start new round");
	}



	private void updateSoldiers(AndroidDraw drawable, IEventTarget eventTarget, Input input, float elapsedTimeSeconds, MultiCharacterListener listener) {
		
		doInteractWithSoldiers(drawable, eventTarget, input, elapsedTimeSeconds, listener);
		
		if (m_masterView.updateAnimations(model, elapsedTimeSeconds)) {
			eventTarget.updatePlayers(listener);
		}
		m_masterView.drawGame(drawable, model, elapsedTimeSeconds);
	}



	private void updateEnemies(AndroidDraw drawable, IEventTarget eventTarget, float elapsedTimeSeconds, MultiCharacterListener listener) {
		if (m_masterView.updateAnimations(model, elapsedTimeSeconds)) {
			eventTarget.updateEnemies(listener);
		}
		m_masterView.drawGame(drawable, model, elapsedTimeSeconds);
		
		drawable.drawText("Enemy is moving", 200, 10, drawable.m_guiText);
	}

	

	private void doInteractWithSoldiers(AndroidDraw drawable, 
										IEventTarget eventTarget, 
										Input input, 
										float elapsedTime, 
										MultiCharacterListener mcl) {
		
		InteractionView actionView = m_masterView.getInteractionView();
		actionView.setupInput(input, model, drawable.getWindowWidth(), drawable.getWindowHeight());
		
		ICharacter selectedSoldier = actionView.getSelectedSoldier(model);
		
		if (selectedSoldier != null) {
			if (actionView.userWantsToMove()) {
				ModelPosition destination = actionView.getDestination(model);
				if (destination != null) {
					eventTarget.doMoveTo(selectedSoldier, destination);
					actionView.unselectPath();
				}
			} else if (actionView.userWantsToWatch()){
				eventTarget.doWatch(selectedSoldier);
			} else if (actionView.userWantsToFire()){ 
				ICharacter fireTarget = actionView.getFireTarget(model);
				
				if (fireTarget != null) {
					if (eventTarget.fireAt(selectedSoldier, fireTarget, mcl) == false) {
						
					}
				}
			}
		}
	}



	

	

	

	

}
