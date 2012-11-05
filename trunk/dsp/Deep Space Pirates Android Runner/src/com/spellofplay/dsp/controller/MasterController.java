package com.spellofplay.dsp.controller;


import com.spellofplay.dsp.model.ModelFacade;
import com.spellofplay.dsp.view.AndroidDraw;
import com.spellofplay.dsp.view.GameView;
import com.spellofplay.dsp.view.ITexture;
import com.spellofplay.dsp.view.Input;

import android.content.Context;
import android.graphics.Paint;

public class MasterController {
	private ModelFacade m_model = new ModelFacade();
	private GameController m_game = new GameController();
	private GameView m_view;
	private Input m_input = null;
	
	
	public MasterController(Context context, Input a_input, ITexture a_texture, ITexture a_player) {
		
		m_view = new GameView(a_texture, a_player);
		m_input = a_input;
		
		
		m_model.startNewGame(0);
	}

	

	public Paint m_guiText = new Paint();
	
	public boolean onDraw(AndroidDraw drawable, float elapsedTimeSeconds) {
		
		m_game.update(drawable, m_model, m_view, m_input, elapsedTimeSeconds);
		
		return true;
	}

	public void ShowMenu() {
		// TODO Auto-generated method stub
		
	}

}
