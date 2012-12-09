package com.spellofplay.dsp.controller;


import com.spellofplay.common.view.Input;
import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.IEventTarget;
import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.view.AndroidDraw;
import com.spellofplay.dsp.view.InteractionView;
import com.spellofplay.dsp.view.LogView;
import com.spellofplay.dsp.view.MasterView;

class GameController {
	
	
	private MasterView masterView;
	private LogView logger = new LogView();
	private IModel model;
	private MultiCharacterListener multipleCharacterListenter;
	
	
	GameController(MasterView m_masterView, IModel model) {
		this.masterView = m_masterView;
		this.model = model;
		multipleCharacterListenter = new MultiCharacterListener();
		multipleCharacterListenter.addListener(m_masterView);
		multipleCharacterListenter.addListener(logger);
	}



	public void update(AndroidDraw drawable, IEventTarget eventTarget, Input input, float elapsedTimeSeconds) {
		
		if (model.isSoldierTime()) {
			updateSoldiers(drawable, eventTarget, input, elapsedTimeSeconds);
			if (model.isSoldierTime() == false) {
				eventTarget.startNewEnemyRound();
			}
			
		} else if (model.isEnemyTime()) {
			updateEnemies(drawable, eventTarget, elapsedTimeSeconds);
		} else {
			startNewSoldierRound(drawable, eventTarget, elapsedTimeSeconds);
		}
		masterView.drawGame(drawable, model, elapsedTimeSeconds);
		
		logger.draw(drawable);
		
	}
	

	private void startNewSoldierRound(AndroidDraw drawable, IEventTarget eventTarget, float elapsedTimeSeconds) {
		eventTarget.startNewSoldierRound();
		masterView.startNewRound();
		
		
		logger.doLog("start new round");
	}



	private void updateSoldiers(AndroidDraw drawable, IEventTarget eventTarget, Input input, float elapsedTimeSeconds) {
		
		doInteractWithSoldiers(drawable, eventTarget, input, elapsedTimeSeconds);
		
		if (masterView.updateAnimations(model, elapsedTimeSeconds)) {
			eventTarget.updatePlayers(multipleCharacterListenter);
		}
	}



	private void updateEnemies(AndroidDraw drawable, IEventTarget eventTarget, float elapsedTimeSeconds) {
		if (masterView.updateAnimations(model, elapsedTimeSeconds)) {
			eventTarget.updateEnemies(multipleCharacterListenter);
		}
		drawable.drawText("Enemy is moving", 200, 10, drawable.m_guiText);
	}

	

	private void doInteractWithSoldiers(AndroidDraw drawable, 
										IEventTarget eventTarget, 
										Input input, 
										float elapsedTime) {
		
		InteractionView actionView = masterView.getInteractionView();
		actionView.setupInput(input, model, drawable.getWindowWidth(), drawable.getWindowHeight());
		
		ICharacter selectedSoldier = actionView.getSelectedSoldier(model);
		
		if (selectedSoldier != null) {
			if (actionView.userWantsToMove()) {
				ModelPosition destination = actionView.getDestination(model);
				eventTarget.doMoveTo(selectedSoldier, destination);
				actionView.unselectPath();
			} else if (actionView.userWantsToWatch()){
				eventTarget.doWatch(selectedSoldier);
			} else if (actionView.userWantsToFire()){ 
				ICharacter fireTarget = actionView.getFireTarget(model);
				eventTarget.fireAt(selectedSoldier, fireTarget, multipleCharacterListenter);
			}  else if (actionView.userWantsToOpenDoor()){ 
				eventTarget.open(selectedSoldier);
				masterView.open();
			}
		}
	}



	

	

	

	

}
