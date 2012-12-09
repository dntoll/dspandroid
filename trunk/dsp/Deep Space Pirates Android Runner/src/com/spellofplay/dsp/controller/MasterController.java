package com.spellofplay.dsp.controller;


import android.view.KeyEvent;

import com.spellofplay.dsp.model.ModelFacade;
import com.spellofplay.dsp.view.AndroidDraw;
import com.spellofplay.dsp.view.ITexture;
import com.spellofplay.dsp.view.MasterView;
import com.spellofplay.dsp.view.SimpleGui;
import com.spellofplay.common.view.Input;

public class MasterController {
	private ModelFacade model = new ModelFacade();
	private GameController game;
	private MasterView masterView;
	private Input input = null;
	private SimpleGui gui = new SimpleGui();
	private boolean showMenu = true;
	
	
	public MasterController(Input input, ITexture enemyAndTilesTexture, ITexture playerTexture) {
		this.masterView = new MasterView(enemyAndTilesTexture, playerTexture, model);
		this.game = new GameController(masterView, model);
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
			if (gui.DoButtonCentered(halfWidth, halfHeight, "restart", input)) {
				startNewGame();
			}
			drawGameWhenItsOver(drawable, elapsedTimeSeconds, "Game Won");
		} else {
			game.update(drawable, model, input, elapsedTimeSeconds);
		}
	}

	private void removeUnhandledClicks() {
		input.IsMouseClicked();
	}

	private void drawGameWhenItsOver(AndroidDraw drawable, float elapsedTimeSeconds, String message) {
		masterView.updateAnimations(model, elapsedTimeSeconds);
		masterView.drawGame(drawable, model, elapsedTimeSeconds);
		drawable.drawText(message, 200, 10, drawable.m_guiText);
	}
	
	private void startNewGame() {
		model.startNewGame(0);
		masterView.startNewGame(model);
	}

	public void ShowMenu() {
		showMenu = true;
	}

}
