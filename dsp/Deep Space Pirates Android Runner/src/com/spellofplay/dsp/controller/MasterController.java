package com.spellofplay.dsp.controller;


import com.spellofplay.dsp.model.Line;
import com.spellofplay.dsp.model.ModelFacade;
import com.spellofplay.dsp.view.AndroidDraw;
import com.spellofplay.dsp.view.GameView;
import com.spellofplay.dsp.view.ITexture;
import com.spellofplay.dsp.view.SimpleGui;
import com.spellofplay.common.view.Input;
import android.content.Context;

public class MasterController {
	private ModelFacade m_model = new ModelFacade();
	private GameController m_game = new GameController();
	private GameView m_view;
	
	private Input m_input = null;
	private SimpleGui m_gui = new SimpleGui();
	
	private boolean m_showMenu = true;
	
	
	public MasterController(Context context, Input a_input, ITexture a_texture, ITexture a_player) {
		
		Line.test();
		
		m_view = new GameView(a_texture, a_player);
		m_input = a_input;
		
		m_showMenu = true;
		
		startNewGame();
		
	}

	

	
	
	public boolean onDraw(AndroidDraw drawable, float elapsedTimeSeconds) {
		if (m_showMenu) {
			if (m_gui.DoButtonCentered(drawable.getWindowWidth()/2, drawable.getWindowHeight()/2, "New Game", m_input, false)) {
				startNewGame();
				m_showMenu = false;
			}
			if (m_gui.DoButtonCentered(drawable.getWindowWidth()/2, drawable.getWindowHeight()/2 + 32, "Continue", m_input, false)) {
				m_showMenu = false;
			}
			if (m_gui.DoButtonCentered(drawable.getWindowWidth()/2, drawable.getWindowHeight()/2 + 64, "Exit", m_input, false)) {
				return false;
			}
			
		} else {
		
			if (m_model.enemyHasWon()) {
				if (m_gui.DoButtonCentered(drawable.getWindowWidth()/2, drawable.getWindowHeight()/2, "Menu", m_input, false)) {
					m_showMenu = true;
				}
				
				m_view.drawGame(drawable, m_model);
				drawable.drawText("Game Over", 200, 10, drawable.m_guiText);
			} else if (m_model.playerHasWon()) {
				if (m_gui.DoButtonCentered(drawable.getWindowWidth()/2, drawable.getWindowHeight()/2, "restart", m_input, false)) {
					startNewGame();
				}
				
				m_view.drawGame(drawable, m_model);
				drawable.drawText("Game Won", 200, 10, drawable.m_guiText);
			} else {
				m_game.update(drawable, m_model, m_view, m_input, elapsedTimeSeconds);
				
			}
		}
		m_gui.DrawGui(drawable);
		
		m_input.IsMouseClicked();
		
		return true;
	}
	
	void startNewGame() {
		m_model.startNewGame(0);
		m_view.startNewGame(m_model);
	}

	public void ShowMenu() {
		m_showMenu = true;
	}

}
