package com.spellofplay.dsp.controller;


import android.graphics.Color;
import android.view.KeyEvent;

import com.spellofplay.dsp.model.IEventTarget;
import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.view.AndroidDraw;
import com.spellofplay.dsp.view.ITexture;
import com.spellofplay.dsp.view.MasterView;
import com.spellofplay.dsp.view.SimpleGui;
import com.spellofplay.common.view.Input;

public class MasterController {
	
	private IEventTarget eventTarget;
	private IModel model;
	private GameController game;
	private LevelUpController levelup;
	private MasterView masterView;
	private Input input = null;
	private SimpleGui gui = new SimpleGui();
	private boolean showMenu = true;
	
	
	public MasterController(Input input, ITexture enemyAndTilesTexture, ITexture playerTexture, IEventTarget target, IModel model) {
		
		
		this.eventTarget = target;
		this.model = model;
		
		this.masterView = new MasterView(enemyAndTilesTexture, playerTexture, model);
		this.game = new GameController(masterView, model);
		this.levelup = new LevelUpController(model, eventTarget, input, gui);
		this.input = input;
		this.showMenu = true;

		startNewGame();
	}

	public boolean doMenuOrGame(AndroidDraw drawable, float elapsedTimeSeconds) {
		showMenuOnBackOrMenuCommand();
		if (showMenu) {
			if (doMenu(drawable) == false) {
				return false;
			}
		} else {
			doGameView(drawable, elapsedTimeSeconds);
		}
		
		gui.DrawGui(drawable);
		
		removeUnhandledClicks();
		
		return true;
	}
	
	private void showMenuOnBackOrMenuCommand() {
		if (input.IsKeyClicked(KeyEvent.KEYCODE_MENU)  || 
        	input.IsKeyClicked(KeyEvent.KEYCODE_BACK)) {
			showMenu = true;
		}
	}
	
	private boolean doMenu(AndroidDraw drawable) {
		int halfWidth = drawable.getWindowWidth()/2;
		int halfHeight = drawable.getWindowHeight()/2;
		
		if (gui.DoButtonCentered(halfWidth, halfHeight, "New Game", input)) {
			startNewGame();
			showMenu = false;
		}
		if (gui.DoButtonCentered(halfWidth, halfHeight + SimpleGui.BUTTON_HEIGHT, "Continue", input)) {
			showMenu = false;
		}
		if (gui.DoButtonCentered(halfWidth, halfHeight + SimpleGui.BUTTON_HEIGHT * 2, "Exit", input)) {
			return false;
		}
		
		return true;
	}
	
	private void doGameView(AndroidDraw drawable, float elapsedTimeSeconds) {
		int halfWidth = drawable.getWindowWidth()/2;
		int halfHeight = drawable.getWindowHeight()/2;
		
		if (model.enemyHasWon()) {
			
			if (gui.DoButtonCentered(halfWidth, halfHeight, "Menu", input)) {
				showMenu = true;
			}
			drawGameWhenItsOver(drawable, elapsedTimeSeconds, "Game Over");
		} else if (model.playerHasWon()) {
			if (levelup.doLevelUp(drawable) == true) {
				newLevel();
			}
			
		} else {
			game.update(drawable, eventTarget, input, elapsedTimeSeconds);
		}
	}

	private void removeUnhandledClicks() {
		input.IsMouseClicked();
	}

	private void drawGameWhenItsOver(AndroidDraw drawable, float elapsedTimeSeconds, String message) {
		masterView.updateAnimations(model, elapsedTimeSeconds);
		masterView.drawGame(drawable, model, elapsedTimeSeconds);
		drawable.drawText(message, 200, 10, Color.WHITE);
	}
	
	private void newLevel() {
		eventTarget.newLevel();
		masterView.startNewGame(model);
	}
	
	private void startNewGame() {
		eventTarget.startNewGame();
		masterView.startNewGame(model);
	}

	public void ShowMenu() {
		showMenu = true;
	}

}
