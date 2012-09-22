package com.spellofplay.dsp.controller;


import com.spellofplay.dsp.model.ModelFacade;
import com.spellofplay.dsp.view.GameView;
import com.spellofplay.dsp.view.IDraw;

import android.content.Context;
import android.graphics.Paint;

public class MasterController {
	private ModelFacade m_model = new ModelFacade();
	private GameView m_view = new GameView();
	
	
	public MasterController(Context context, IInput m_input) {
		// TODO Auto-generated constructor stub
	}

	public void update(float elapsedTimeSeconds) {
		m_model.update(elapsedTimeSeconds);
	}

	public Paint m_guiText = new Paint();
	
	public boolean onDraw(IDraw drawable) {
		
		m_view.drawGame(drawable, m_model);
		return true;
	}

	public void ShowMenu() {
		// TODO Auto-generated method stub
		
	}

}
