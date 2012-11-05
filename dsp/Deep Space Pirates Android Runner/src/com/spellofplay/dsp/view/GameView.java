package com.spellofplay.dsp.view;


import java.util.List;

import android.graphics.Color;
import android.graphics.Rect;

import com.spellofplay.dsp.model.Enemy;
import com.spellofplay.dsp.model.ModelFacade;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Soldier;
import com.spellofplay.dsp.model.Character;

public class GameView {

	
	
	LevelDrawer m_level;// = new LevelDrawer();
	boolean hasInitatedBuffer = false;
	Camera  m_camera = new Camera();
	Soldier m_selectedSoldier;
	Enemy   m_selectedEnemy;
	public static final int m_scale = 32;
	
	Rect enemy = new Rect(0, 128, 32, 128 + 32);
	Rect soldier = new Rect(0, 0, 255, 255);
	
	ITexture m_texture;
	ITexture m_player;
	public GameView(ITexture a_texture, ITexture a_player) {
		m_level = new LevelDrawer(a_texture);
		m_texture = a_texture;
		m_player = a_player;
		
		m_camera.m_scale = m_scale;
	}
	
	
	public void drawGame(AndroidDraw drawable, ModelFacade a_model) {
		if (hasInitatedBuffer == false) {
			m_level.draw(a_model.getLevel(), drawable, m_scale);
			hasInitatedBuffer = true;
		}
		drawable.drawBackground();
		
		
		
		for (Soldier s : a_model.getAliveSoldiers()) {
			ViewPosition vpos = m_camera.toViewPos(s.getPosition());
			
			Rect dst = new Rect((int)vpos.m_x -m_scale/2, 
					(int)vpos.m_y -m_scale/2,
					(int)vpos.m_x + m_scale/2, 
					(int)vpos.m_y + m_scale/2);
			
			if (s == m_selectedSoldier && a_model.isSoldierTime() && s.getTimeUnits() > 0) {
				drawable.drawCircle(vpos, m_scale/2, Color.GREEN);
			}
			drawable.drawBitmap(m_player, soldier, dst);
			
			
			drawable.drawText("" + s.getTimeUnits(), dst.left, dst.top);
			drawable.drawText("" + s.getHitpoints(), dst.right, dst.top);
		}
		
		for (Enemy e : a_model.getAliveEnemies()) {
			ViewPosition vEpos = m_camera.toViewPos(e.getPosition());
			List<Soldier> soldiersWhoSpotsEnemy = a_model.canSee(e);
			
			if (soldiersWhoSpotsEnemy.isEmpty() ) {
				continue;
			} else {
				
				for(Soldier s : soldiersWhoSpotsEnemy) {
					ViewPosition vsPos = m_camera.toViewPos(s.getPosition());
					
					drawable.drawLine(vEpos, vsPos, Color.WHITE);
				}
			}
			
			
			Rect dst = new Rect((int)vEpos.m_x -m_scale/2, 
					(int)vEpos.m_y -m_scale/2,
					(int)vEpos.m_x + m_scale/2, 
					(int)vEpos.m_y + m_scale/2);
			
			drawable.drawBitmap(m_texture, enemy, dst);
			
			drawable.drawText("" + e.getTimeUnits(), dst.left, dst.top);
			drawable.drawText("" + e.getHitpoints(), dst.right, dst.top);
		}
		
		drawable.drawText(a_model.getGameTitle(), 10, 10);
	}
	
	
	enum Action {
		None,
		SelectedSoldier,
		ClickedOnGround, ClickedOnEnemy
	}
	
	Action m_action = Action.None;
	
	
	public void setupInput(Input a_input, ModelFacade a_model) {
		
		m_action = Action.None;
		
		//On soldier?
		Soldier mouseOverSoldier = onSoldier(a_input, a_model);
		Enemy mouseOverEnemy = onEnemy(a_input, a_model);
		
		if (a_input.IsMouseClicked()) {
			if (mouseOverSoldier != null) {
				m_selectedSoldier = mouseOverSoldier;
				m_action = Action.SelectedSoldier;
			} else if (mouseOverEnemy != null) {
				m_selectedEnemy = mouseOverEnemy;
				m_action = Action.ClickedOnEnemy;
			} else {
			   m_action = Action.ClickedOnGround;
			}
	}
		
		
	}
	
	
	
	private Soldier onSoldier(Input a_input, ModelFacade a_model) {
		List<Soldier> soldiers = a_model.getAliveSoldiers();
		ViewPosition clickpos = a_input.getClickPosition();
		return onCharacter(soldiers, clickpos);
	}
	
	private Enemy onEnemy(Input a_input, ModelFacade a_model) {
		List<Enemy> enemies = a_model.getAliveEnemies();
		ViewPosition clickpos = a_input.getClickPosition();
		return onCharacter(enemies, clickpos);
	}


	private <T extends Character> T onCharacter(List<T> characters, ViewPosition clickpos) {
		for (T s : characters) {
			ModelPosition modelPos = s.getPosition();
			
			ViewPosition viewPosition = m_camera.toViewPos(modelPos);
			
			float viewRadius = m_camera.toViewScale(s.getRadius());
			
			if (viewPosition.sub(clickpos).length() < viewRadius) {
				return s;
			}
		}
		return null;
	}
	
	
	

	
	public Soldier getSelectedSoldier(Input a_input, ModelFacade a_model) {
		return m_selectedSoldier;
	}

	
	public ModelPosition getDestination(Input a_input) {
		if (m_action == Action.ClickedOnGround) {
			ViewPosition mousePos = a_input.getClickPosition();
			return m_camera.toModelPos(mousePos);
		}
		return null;
	}


	public Enemy getFireTarget(Input a_input) {
		if (m_action == Action.ClickedOnEnemy) {
			return m_selectedEnemy;
		}
		return null;
	}

	

}
