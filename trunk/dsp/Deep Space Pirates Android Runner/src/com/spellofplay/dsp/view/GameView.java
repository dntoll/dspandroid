package com.spellofplay.dsp.view;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.graphics.Rect;

import com.spellofplay.dsp.model.AStar;
import com.spellofplay.dsp.model.AStar.SearchResult;
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
	Map<Character, VisualCharacter> m_characters = new HashMap<Character, VisualCharacter>();
	
	
	AStar m_selectedPath = null;// = new AStar();
	ModelPosition m_selectedDestination = null;
	
	SimpleGui m_gui = new SimpleGui();
	
	
	Rect enemy = new Rect(0, 128, 32, 128 + 32);

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
		
		//TargetDestionation
		if (getDestination() != null) {
			
			if (m_selectedPath != null) {
				SearchResult result = m_selectedPath.Update(100);
				boolean drawPath = false;
				switch (result) {
					case SearchSucceded : drawPath = true; break;
					case SearchFailedNoPath : drawPath = false; break;
				}
				if (drawPath) {
					
					for (ModelPosition loc : m_selectedPath.m_path)  {
						drawable.drawCircle(m_camera.toViewPos(loc), m_camera.getHalfScale()/2, Color.argb(128, 0, 0, 255));
					}
				}
				
			}
			
			drawable.drawCircle(m_camera.toViewPos(getDestination()), m_camera.getHalfScale(), Color.argb(128, 0, 0, 255));
		}
		
		Soldier selected = getSelectedSoldier(a_model);
		//DRAW SELECTION
		if (selected != null && a_model.isSoldierTime() && selected.getTimeUnits() > 0) {
			ViewPosition vpos = m_camera.toViewPos(selected.getPosition());
			drawable.drawCircle(vpos, m_camera.getHalfScale(), Color.GREEN);
		}
		//DRAW TARGET ENEMY
		Enemy target = getFireTarget(a_model);
		if (target != null) {
			ViewPosition vEpos = m_camera.toViewPos(target.getPosition());
			drawable.drawCircle(vEpos, m_camera.getHalfScale(), Color.RED);
		}
		
		//DRAW SOLDIERS
		for (Soldier soldier : a_model.getAliveSoldiers()) {
			VisualCharacter vchar = m_characters.get(soldier);
			vchar.drawSoldier(drawable, soldier, m_camera, m_player, selected == soldier ? target :  null);
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
				drawable.drawText("" + e.getTimeUnits(), dst.left, dst.top, drawable.m_guiText);
				drawable.drawText("" + e.getHitpoints(), dst.right, dst.top, drawable.m_guiText);
			}
		}
		
		drawable.drawText(a_model.getGameTitle(), 10, 10, drawable.m_guiText);
		
		
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
			if (m_gui.DoButtonCentered(a_width - SimpleGui.BUTTON_WIDTH, a_height - SimpleGui.BUTTON_HEIGHT-16, "Wait", a_input, false)) {
				m_action = Action.Waiting;
			}
			
			if (getDestination() != null) {
				if (m_gui.DoButtonCentered(a_width - SimpleGui.BUTTON_WIDTH*2, a_height - SimpleGui.BUTTON_HEIGHT-16, "Move", a_input, false)) {
					m_action = Action.Moveing;
				}
			}
			if (getFireTarget(a_model) != null) {
				if (m_gui.DoButtonCentered(a_width - SimpleGui.BUTTON_WIDTH*3, a_height - SimpleGui.BUTTON_HEIGHT-16, "Fire", a_input, false)) {
					m_action = Action.Attacking;
				}
			}
		}
		
		if (a_input.IsMouseClicked()) {
			if (mouseOverSoldier != null) {
				selectSoldier(mouseOverSoldier);
			} else if (mouseOverEnemy != null) {
				m_selectedEnemy = mouseOverEnemy;
			} else {
				
				ViewPosition clickpos = new ViewPosition(a_input.m_mousePosition.x, a_input.m_mousePosition.y);
				ModelPosition clickOnLevelPosition = m_camera.toModelPos(clickpos);
				
				selectDestination(a_model, clickOnLevelPosition);
				
				
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


	private void selectDestination(ModelFacade a_model, ModelPosition clickOnLevelPosition) {
		Soldier soldier = getSelectedSoldier(a_model);
		
		if (soldier != null) {
			if (a_model.getLevel().canMove(clickOnLevelPosition)) {
				
				float length = clickOnLevelPosition.sub( soldier.getPosition() ).length();
				
				if (length < soldier.getTimeUnits() * Math.sqrt(2.0)) {
				
					m_selectedDestination = clickOnLevelPosition;
					
					m_selectedPath = new AStar(a_model.getMovePossible());
					
					m_selectedPath.InitSearch(soldier.getPosition(), m_selectedDestination, false, 0, false);
				}
			}
		}
	}


	private void selectSoldier(Soldier mouseOverSoldier) {
		m_selectedSoldier = mouseOverSoldier;
		m_selectedDestination = null;
		m_selectedPath = null;
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
					selectSoldier(s);
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
		Soldier selected = getSelectedSoldier(a_model);
		
		if (selected == null) {
			return null;
		}
		
		if (selected.getTimeUnits() < selected.getFireCost()) {
			return null;
		}
		
			
		//Can we see the one we clicked on
		if (m_selectedEnemy != null && m_selectedEnemy.getHitpoints() > 0) {
			if (a_model.canSee(selected, m_selectedEnemy)) {
				return m_selectedEnemy;
			}
		}
		
		m_selectedEnemy = a_model.getClosestEnemyThatWeCanSee(selected);

		return m_selectedEnemy;
		
	}

	public void startNewRound() {
		m_enemiesSeenThisRound.clear();
	}
	
	public void startNewGame(ModelFacade a_model) {
		m_selectedEnemy = null;
		m_selectedSoldier = null;
		m_selectedDestination = null;
		m_selectedPath = null;
		m_characters.clear();
		
		for (Soldier soldier : a_model.getAliveSoldiers()) {
			m_characters.put(soldier, new VisualCharacter(soldier));
		}
		
		for (Enemy enemy : a_model.getAliveEnemies()) {
			m_characters.put(enemy, new VisualCharacter(enemy));
		}
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
