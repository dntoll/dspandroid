package com.spellofplay.dsp.controller;


import android.graphics.Color;

import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.IEventTarget;
import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.view.AndroidDraw;
import com.spellofplay.dsp.view.Input;
import com.spellofplay.dsp.view.InteractionView;
import com.spellofplay.dsp.view.LogView;
import com.spellofplay.dsp.view.MasterView;
import com.spellofplay.dsp.view.SimpleGui;

class GameController {
	
	private SimpleGui simpleGui = new SimpleGui();
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
		simpleGui.DrawGui(drawable);
		
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
		drawable.drawText("Enemy is moving", 200, 10, Color.WHITE);
	}

	

	private void doInteractWithSoldiers(AndroidDraw drawable, 
										IEventTarget eventTarget, 
										Input input, 
										float elapsedTime) {
		
		InteractionView actionView = masterView.getInteractionView();
		
		
		ICharacter selectedSoldier = actionView.getSelectedSoldier(model);
		
		int width = drawable.getWindowWidth();
		int height = drawable.getWindowHeight();
		if (selectedSoldier != null) {
			int y = height - SimpleGui.BUTTON_HEIGHT-16;
			if (simpleGui.DoButtonCentered(width - SimpleGui.BUTTON_WIDTH, y, "Watch", input)) {
				eventTarget.doWatch(selectedSoldier);
			}
			
			if (actionView.hasDestination(model)) {
				if (simpleGui.DoButtonCentered(width - SimpleGui.BUTTON_WIDTH*2, y, "Move", input)) {
					ModelPosition destination = actionView.getDestination(model);
					eventTarget.doMoveTo(selectedSoldier, destination);
					actionView.unselectPath();
				}
			}
			if (actionView.getFireTarget(model) != null) {
				if (simpleGui.DoButtonCentered(width - SimpleGui.BUTTON_WIDTH*3, y, "Fire", input)) {
					ICharacter fireTarget = actionView.getFireTarget(model);
					eventTarget.fireAt(selectedSoldier, fireTarget, multipleCharacterListenter);
				}
			}
			if (model.hasDoorCloseToIt(selectedSoldier)) {
				if (simpleGui.DoButtonCentered(width - SimpleGui.BUTTON_WIDTH*4, y, "Open", input)) {
					eventTarget.open(selectedSoldier);
					masterView.open();
				}
			}
		}
		
		actionView.setupInput(input, model, drawable.getWindowWidth(), drawable.getWindowHeight());
		
		
		
	}



	

	

	

	

}
