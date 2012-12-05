package com.spellofplay.dsp.controller;


import com.spellofplay.dsp.model.Line;
import com.spellofplay.dsp.model.ModelFacade;
import com.spellofplay.dsp.view.AndroidDraw;
import com.spellofplay.dsp.view.ITexture;
import com.spellofplay.dsp.view.MasterView;
import com.spellofplay.dsp.view.SimpleGui;
import com.spellofplay.common.view.Input;

public class MasterController {
	private ModelFacade m_model = new ModelFacade();
	private GameController m_game;
	
	private MasterView m_masterView;
	private Input m_input = null;
	private SimpleGui m_gui = new SimpleGui();
	
	private boolean m_showMenu = true;
	
	
	public MasterController(Input a_input, ITexture a_texture, ITexture a_player) {
		
		Line.test();
		
		m_masterView = new MasterView(a_texture, a_player);
		
		m_game = new GameController(m_masterView);
		m_input = a_input;
		
		m_showMenu = true;
		
		startNewGame();
		
	}

		public boolean onDraw(AndroidDraw drawable, float elapsedTimeSeconds) {
		int halfWidth = drawable.getWindowWidth()/2;
		int halfHeight = drawable.getWindowHeight()/2;
		if (m_showMenu) {
			if (m_gui.DoButtonCentered(halfWidth, halfHeight, "New Game", m_input, false)) {
				startNewGame();
				m_showMenu = false;
			}
			if (m_gui.DoButtonCentered(halfWidth, halfHeight + SimpleGui.BUTTON_HEIGHT, "Continue", m_input, false)) {
				m_showMenu = false;
			}
			if (m_gui.DoButtonCentered(halfWidth, halfHeight + SimpleGui.BUTTON_HEIGHT * 2, "Exit", m_input, false)) {
				return false;
			}
			
		} else {
		
			if (m_model.enemyHasWon()) {
				if (m_gui.DoButtonCentered(halfWidth, halfHeight, "Menu", m_input, false)) {
					m_showMenu = true;
				}
				
				m_masterView.drawGame(drawable, m_model, elapsedTimeSeconds);
				drawable.drawText("Game Over", 200, 10, drawable.m_guiText);
			} else if (m_model.playerHasWon()) {
				if (m_gui.DoButtonCentered(halfWidth, halfHeight, "restart", m_input, false)) {
					startNewGame();
				}
				
				m_masterView.drawGame(drawable, m_model, elapsedTimeSeconds);
				drawable.drawText("Game Won", 200, 10, drawable.m_guiText);
			} else {
				
				m_game.update(drawable, m_model, m_input, elapsedTimeSeconds);
				
			}
		}
		m_gui.DrawGui(drawable);
		
		m_input.IsMouseClicked();
		
		return true;
	}
	
	
	void startNewGame() {
		m_model.startNewGame(0);
		m_masterView.startNewGame(m_model);
	}

	public void ShowMenu() {
		m_showMenu = true;
	}

}
