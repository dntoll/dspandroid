package com.spellofplay.dsp.controller;


import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.ModelFacade;
import com.spellofplay.dsp.view.GameView;
import com.spellofplay.dsp.view.IDraw;
import com.spellofplay.dsp.view.IInput;
import com.spellofplay.dsp.view.ITexture;

import android.content.Context;
import android.graphics.Paint;

public class MasterController {
	private IModel m_model = new ModelFacade();
	private GameController m_game = new GameController();
	private GameView m_view;
	private IInput m_input = null;
	
	
	public MasterController(Context context, IInput a_input, ITexture a_texture) {
		
		m_view = new GameView(a_texture);
		m_input = a_input;
		
		
		m_model.startNewGame(0);
	}

	

	public Paint m_guiText = new Paint();
	
	public boolean onDraw(IDraw drawable, float elapsedTimeSeconds) {
		
		m_game.update(drawable, m_model, m_view, m_input, elapsedTimeSeconds);
		
		return true;
	}

	public void ShowMenu() {
		// TODO Auto-generated method stub
		
	}

}
