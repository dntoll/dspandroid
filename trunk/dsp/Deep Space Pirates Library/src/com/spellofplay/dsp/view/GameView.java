package com.spellofplay.dsp.view;


import java.util.List;

import android.graphics.PointF;
import android.graphics.Rect;

import com.spellofplay.dsp.model.Enemy;
import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Soldier;

public class GameView {

	
	
	LevelDrawer m_level;// = new LevelDrawer();
	boolean hasInitatedBuffer = false;
	Camera m_camera = new Camera();
	Soldier m_selectedSoldier;
	public static final int m_scale = 32;
	
	Rect enemy = new Rect(0, 128, 32, 128 + 32);
	Rect soldier = new Rect(32, 224, 64, 224 + 32);
	
	ITexture m_texture;
	public GameView(ITexture a_texture) {
		m_level = new LevelDrawer(a_texture);
		m_texture = a_texture;
		
		m_camera.m_scale = m_scale;
	}
	
	
	public void drawGame(IDraw drawable, IModel a_model) {
		if (hasInitatedBuffer == false) {
			m_level.draw(a_model.getLevel(), drawable);
			hasInitatedBuffer = true;
		}
		drawable.drawBackground();
		
		
		
		for (Soldier s : a_model.getAliveSoldiers()) {
			ViewPosition vpos = m_camera.toViewPos(s.getPosition());
			
			Rect dst = new Rect((int)vpos.m_x -m_scale/2, 
					(int)vpos.m_y -m_scale/2,
					(int)vpos.m_x + m_scale/2, 
					(int)vpos.m_y + m_scale/2);
			
			drawable.drawBitmap(m_texture, soldier, dst);
			
			drawable.drawText("" + s.getTimeUnits(), dst.left, dst.top);
		}
		
		for (Enemy s : a_model.getAliveEnemies()) {
			ViewPosition vpos = m_camera.toViewPos(s.getPosition());
			
			Rect dst = new Rect((int)vpos.m_x -m_scale/2, 
					(int)vpos.m_y -m_scale/2,
					(int)vpos.m_x + m_scale/2, 
					(int)vpos.m_y + m_scale/2);
			
			drawable.drawBitmap(m_texture, enemy, dst);
			
			drawable.drawText("" + s.getTimeUnits(), dst.left, dst.top);
		}
		
		drawable.drawText(a_model.getGameTitle(), 10, 10);
	}
	
	
	enum Action {
		None,
		SelectedSoldier,
		ClickedOnGround
	}
	
	Action m_action = Action.None;
	
	public void setupInput(IInput a_input, IModel a_model) {
		
		m_action = Action.None;
		
		//On soldier?
		Soldier mouseOverSoldier = onSoldier(a_input, a_model);
		
		if (mouseOverSoldier != null) {
			if (a_input.IsMouseClicked()) {
				m_selectedSoldier = mouseOverSoldier;
				m_action = Action.SelectedSoldier;
			}
		}
		
		if (m_action == Action.None && a_input.IsMouseClicked()) {
			 m_action = Action.ClickedOnGround;
		}
		
		
	}
	
	private Soldier onSoldier(IInput a_input, IModel a_model) {
		List<Soldier> soldiers = a_model.getAliveSoldiers();
		ViewPosition clickpos = a_input.getClickPosition();
		for (Soldier s : soldiers) {
			ModelPosition soldierModelPos = s.getPosition();
			
			ViewPosition viewPosition = m_camera.toViewPos(soldierModelPos);
			
			float soldierViewRadius = m_camera.toViewScale(s.getRadius());
			
			if (viewPosition.sub(clickpos).length() < soldierViewRadius) {
				return s;
			}
		}
		return null;
	}
	
	

	
	public Soldier getSelectedSoldier(IInput a_input, IModel a_model) {
		return m_selectedSoldier;
	}

	
	public ModelPosition getDestination(IInput a_input) {
		if (m_action == Action.ClickedOnGround) {
			
			ViewPosition mousePos = a_input.getClickPosition();
			
			return m_camera.toModelPos(mousePos);
		}
		return null;
	}

	

}