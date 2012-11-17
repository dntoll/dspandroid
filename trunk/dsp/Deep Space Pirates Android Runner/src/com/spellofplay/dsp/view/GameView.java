package com.spellofplay.dsp.view;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.graphics.Rect;

import com.spellofplay.dsp.model.Enemy;
import com.spellofplay.dsp.model.ModelFacade;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Soldier;
import com.spellofplay.dsp.model.Character;
import com.spellofplay.common.view.Input;
//import com.spellofplay.common.view.ViewPosition;

public class GameView {

	
	
	LevelDrawer m_level;// = new LevelDrawer();
	boolean hasInitatedBuffer = false;
	Camera  m_camera = new Camera();
	Soldier m_selectedSoldier;
	Enemy   m_selectedEnemy;
	Map<Enemy, ModelPosition> m_enemiesSeenThisRound = new HashMap<Enemy, ModelPosition>();
	ModelPosition m_selectedDestination = null;
	
	SimpleGui m_gui = new SimpleGui();
	
	
	Rect enemy = new Rect(0, 128, 32, 128 + 32);
	Rect soldier = new Rect(0, 0, 255, 255);
	private final int TRANSPARENT = Color.argb(128, 255, 255, 255);
	
	ITexture m_texture;
	ITexture m_player;
	public GameView(ITexture a_texture, ITexture a_player) {
		m_level = new LevelDrawer(a_texture);
		m_texture = a_texture;
		m_player = a_player;
	}
	
	
	public void drawGame(AndroidDraw drawable, ModelFacade a_model) {
		if (hasInitatedBuffer == false) 
		{
			m_level.draw(a_model.getLevel(), drawable, m_camera);
			hasInitatedBuffer = true;
		}
		drawable.drawBackground(m_camera.m_displacement);
		
		if (getDestination() != null) {
			drawable.drawCircle(m_camera.toViewPos(getDestination()), m_camera.getHalfScale(), Color.BLUE);
		}
		
		//DRAW SOLDIERS
		for (Soldier s : a_model.getAliveSoldiers()) {
			ViewPosition vpos = m_camera.toViewPos(s.getPosition());
			
			Rect dst = new Rect((int)vpos.m_x -m_camera.getHalfScale(), 
					(int)vpos.m_y -m_camera.getHalfScale(),
					(int)vpos.m_x + m_camera.getHalfScale(), 
					(int)vpos.m_y + m_camera.getHalfScale());
			
			//DRAW SELECTION
			if (s == getSelectedSoldier(a_model) && a_model.isSoldierTime() && s.getTimeUnits() > 0) {
				drawable.drawCircle(vpos, m_camera.getHalfScale(), Color.GREEN);
			}
			drawable.drawBitmap(m_player, soldier, dst, Color.WHITE);
			
			//DRAW SOLDIER INFO
			drawable.drawText("" + s.getTimeUnits(), dst.left, dst.top);
			drawable.drawText("" + s.getHitpoints(), dst.right, dst.top);
		}
		
		for (Enemy e : a_model.getAliveEnemies()) {
			
			List<Soldier> soldiersWhoSpotsEnemy = a_model.canSee(e);
			
			//HAS THIS ENEMY HAS BEEN SEEN THIS ROUND
			if (soldiersWhoSpotsEnemy.isEmpty() ) {
				if (m_enemiesSeenThisRound.containsKey(e) == false) {
					continue;
				}
			} else {
				if (m_enemiesSeenThisRound.containsKey(e) == false) {
					m_enemiesSeenThisRound.put(e, e.getPosition());
				}
			}
			
			//Plats och färg för synliga fiender
			ViewPosition vEpos = m_camera.toViewPos(e.getPosition());
			int color = Color.WHITE;
			
			//NOBODY CAN SEE THE ENEMY RIGHT `KNOW
			if (soldiersWhoSpotsEnemy.isEmpty() ) {
				//TRANSPARENT
				color =  TRANSPARENT;
				//LAST KNOWN POSITION
				vEpos = m_camera.toViewPos(m_enemiesSeenThisRound.get(e)); 
			} 
			
			//DRAW SELECTION
			if (soldiersWhoSpotsEnemy.isEmpty() == false) {
				if (e == getFireTarget(a_model)) {
					drawable.drawCircle(vEpos, m_camera.getHalfScale(), Color.RED);
				}
				for(Soldier s : soldiersWhoSpotsEnemy) {
					ViewPosition vsPos = m_camera.toViewPos(s.getPosition());
					
					drawable.drawLine(vEpos, vsPos, TRANSPARENT);
					
				}
			}
			
			
			Rect dst = new Rect((int)vEpos.m_x -m_camera.getHalfScale(), 
					(int)vEpos.m_y -m_camera.getHalfScale(),
					(int)vEpos.m_x + m_camera.getHalfScale(), 
					(int)vEpos.m_y + m_camera.getHalfScale());
			drawable.drawBitmap(m_texture, enemy, dst, color);
			
			//DRAW ENEMY INFO
			if (soldiersWhoSpotsEnemy.isEmpty() == false) {
				drawable.drawText("" + e.getTimeUnits(), dst.left, dst.top);
				drawable.drawText("" + e.getHitpoints(), dst.right, dst.top);
			}
		}
		
		drawable.drawText(a_model.getGameTitle(), 10, 10);
		
		
		m_gui.DrawGui(drawable);
	}
	
	
	enum Action {
		None, Waiting, Moveing, Attacking
	}
	
	Action m_action = Action.None;
	
	
	public void setupInput(Input a_input, ModelFacade a_model, int a_width, int a_height) {
		
		m_action = Action.None;
		
		//On soldier?
		Soldier mouseOverSoldier = onSoldier(a_input, a_model);
		Enemy mouseOverEnemy = onEnemy(a_input, a_model);
		
		if (getSelectedSoldier(a_model) != null) {
			if (m_gui.DoButtonCentered(a_width - SimpleGui.BUTTON_WIDTH, a_height - SimpleGui.BUTTON_HEIGHT-32, "Wait", a_input, false)) {
				m_action = Action.Waiting;
			}
			
			if (getDestination() != null) {
				if (m_gui.DoButtonCentered(a_width - SimpleGui.BUTTON_WIDTH*2, a_height - SimpleGui.BUTTON_HEIGHT-32, "Move", a_input, false)) {
					m_action = Action.Moveing;
				}
			}
			if (getFireTarget(a_model) != null) {
				if (m_gui.DoButtonCentered(a_width - SimpleGui.BUTTON_WIDTH*3, a_height - SimpleGui.BUTTON_HEIGHT-32, "Fire", a_input, false)) {
					m_action = Action.Attacking;
				}
			}
		}
		
		if (a_input.IsMouseClicked()) {
			if (mouseOverSoldier != null) {
				m_selectedSoldier = mouseOverSoldier;
			} else if (mouseOverEnemy != null) {
				m_selectedEnemy = mouseOverEnemy;
			} else {
				
				ViewPosition clickpos = new ViewPosition(a_input.m_mousePosition.x, a_input.m_mousePosition.y);
				m_selectedDestination = m_camera.toModelPos(clickpos);
			}
		}
		
		if (a_input.IsDragging()) {
			m_camera.DoScroll( 	a_width, 
					a_height - SimpleGui.BUTTON_HEIGHT-32, 
					(int)(a_input.m_mousePosition.x - a_input.m_dragFrom.x), 
					(int)(a_input.m_mousePosition.y - a_input.m_dragFrom.y));
		} else {
			m_camera.StopScrolling();
		}
		
	}
	
	
	
	private Soldier onSoldier(Input a_input, ModelFacade a_model) {
		List<Soldier> soldiers = a_model.getAliveSoldiers();
		ViewPosition clickpos = new ViewPosition(a_input.m_mousePosition.x, a_input.m_mousePosition.y);
		return onCharacter(soldiers, clickpos);
	}
	
	private Enemy onEnemy(Input a_input, ModelFacade a_model) {
		List<Enemy> enemies = a_model.getAliveEnemies();
		ViewPosition clickpos = new ViewPosition(a_input.m_mousePosition.x, a_input.m_mousePosition.y);
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
	
	
	

	
	public Soldier getSelectedSoldier(ModelFacade a_model) {
		if (a_model.isSoldierTime() == false)
			return null;
		
		if (m_selectedSoldier == null) {
			return null;
		}
		
		if (m_selectedSoldier.getTimeUnits() == 0) {
			//SELECT THE NEXT SOLDIER
			for (Soldier s : a_model.getAliveSoldiers()) {
				if (s.getTimeUnits() > 0) {
					m_selectedSoldier = s;
					break;
				}
			}
		}
		
		
		
		return m_selectedSoldier;
	}

	
	public ModelPosition getDestination() {
		
		if (m_selectedSoldier == null) {
			return null;
		}
		
		return m_selectedDestination;
	}


	public Enemy getFireTarget(ModelFacade a_model) {
		if (getSelectedSoldier(a_model) == null) {
			return null;
		}
		
			
		//Can we see the one we clicked on
		if (m_selectedEnemy != null && m_selectedEnemy.getHitpoints() > 0) {
			if (a_model.canSee(getSelectedSoldier(a_model), m_selectedEnemy)) {
				return m_selectedEnemy;
			}
		}
		
		m_selectedEnemy = a_model.getClosestEnemyThatWeCanSee(getSelectedSoldier(a_model));

		return m_selectedEnemy;
		
	}

	public void startNewRound() {
		m_enemiesSeenThisRound.clear();
	}
	
	public boolean userWantsToWait() {
		if (m_action == Action.Waiting)
			return true;
		return false;
	}


	public boolean userWantsToMove() {
		if (m_action == Action.Moveing)
			return true;
		return false;
	}


	public boolean userWantsToFire() {
		
		if (m_action == Action.Attacking)
			return true;
		return false;
	}
}
