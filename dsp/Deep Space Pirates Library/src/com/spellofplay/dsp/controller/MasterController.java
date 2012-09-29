package com.spellofplay.dsp.controller;


import com.spellofplay.dsp.model.ModelFacade;
import com.spellofplay.dsp.view.GameView;
import com.spellofplay.dsp.view.IDraw;
import com.spellofplay.dsp.view.ITexture;

import android.content.Context;
import android.graphics.Paint;

public class MasterController {
	private ModelFacade m_model = new ModelFacade();
	private GameView m_view;
	
	
	public MasterController(Context context, IInput m_input, ITexture a_texture) {
		// TODO Auto-generated constructor stub
		
		m_view = new GameView(a_texture);
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
