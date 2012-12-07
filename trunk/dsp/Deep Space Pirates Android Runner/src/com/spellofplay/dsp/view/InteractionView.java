package com.spellofplay.dsp.view;

import android.graphics.Color;

import com.spellofplay.common.view.Input;
import com.spellofplay.dsp.model.AStar;
import com.spellofplay.dsp.model.Character;
import com.spellofplay.dsp.model.Enemy;
import com.spellofplay.dsp.model.ModelFacade;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Soldier;
import com.spellofplay.dsp.model.AStar.SearchResult;
import com.spellofplay.dsp.model.CharacterCollection;


public class InteractionView {
	
	SimpleGui m_gui = new SimpleGui();
	Camera m_camera;
	
	Soldier m_selectedSoldier;
	Enemy   m_selectedEnemy;
	AStar m_selectedPath = null;
	
	enum Action {
		None, Watch, Moveing, Attacking
	}
	
	public Action m_action;

	public InteractionView() {
		this.m_action = Action.None;
	}
	
	public InteractionView(Camera camera) {
		m_camera = camera;
	}

	public boolean userWantsToWatch() {
		if (m_action == Action.Watch)
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
	
	private Soldier onSoldier(Input a_input, ModelFacade a_model) {
		CharacterCollection<Soldier> soldiers = a_model.getAliveSoldiers();
		ViewPosition clickpos = new ViewPosition(a_input.m_mousePosition.x, a_input.m_mousePosition.y);
		return onCharacter(soldiers, clickpos);
	}
	
	private Enemy onEnemy(Input a_input, ModelFacade a_model) {
		CharacterCollection<Enemy> enemies = a_model.getAliveEnemies();
		ViewPosition clickpos = new ViewPosition(a_input.m_mousePosition.x, a_input.m_mousePosition.y);
		return onCharacter(enemies, clickpos);
	}
	
	private <T extends Character> T onCharacter(CharacterCollection<T> soldiers, ViewPosition clickpos) {
		for (T s : soldiers) {
			ModelPosition modelPos = s.getPosition();
			
			ViewPosition viewPosition = m_camera.toViewPos(modelPos);
			
			float viewRadius = m_camera.toViewScale(s.getRadius());
			
			if (viewPosition.sub(clickpos).length() < viewRadius) {
				return s;
			}
		}
		return null;
	}
	
	public void setupInput(Input a_input, ModelFacade a_model, int a_width, int a_height) {
		
		m_action = Action.None;
		
		//On soldier?
		Soldier mouseOverSoldier = onSoldier(a_input, a_model);
		Enemy mouseOverEnemy = onEnemy(a_input, a_model);
		
		if (getSelectedSoldier(a_model) != null) {
			if (m_gui.DoButtonCentered(a_width - SimpleGui.BUTTON_WIDTH, a_height - SimpleGui.BUTTON_HEIGHT-16, "Watch", a_input, false)) {
				m_action = Action.Watch;
			}
			
			if (getDestination(a_model) != null) {
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
				selectSoldier(mouseOverSoldier, m_camera);
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
	
	public void unselectPath() {
		m_selectedPath = null;
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

			if (a_model.canShoot(selected, m_selectedEnemy)) {
				return m_selectedEnemy;
			}
		}
		
		m_selectedEnemy = a_model.getClosestEnemyThatWeCanShoot(selected);

		return m_selectedEnemy;
		
	}
	
	public Soldier getSelectedSoldier(ModelFacade a_model) {
		if (a_model.isSoldierTime() == false)
			return null;
		
		return m_selectedSoldier;
	}

	void updateSoldierSelection(ModelFacade a_model, Camera camera, int width, int height) {
		if (m_selectedSoldier == null || m_selectedSoldier.getTimeUnits() == 0) {
			//SELECT THE NEXT SOLDIER
			for (Soldier soldier : a_model.getAliveSoldiers()) {
				if (soldier.getTimeUnits() > 0) {
					selectSoldier(soldier, camera);
					break;
				}
			}
		}
	}
	
	public ModelPosition getDestination(ModelFacade a_model) {
		
		Soldier selected = getSelectedSoldier(a_model);
		if (selected == null) {
			return null;
		}
		
		if (m_selectedPath != null) {
			if (m_selectedPath.Update(100) == SearchResult.SearchSucceded)
			{
				if (m_selectedPath.m_path.size() <= selected.getTimeUnits() ) 
				{
					return m_selectedPath.m_path.get(m_selectedPath.m_path.size()-1);
				}
			}
		}
		
		return null;
	}
	
	private void selectDestination(ModelFacade a_model, ModelPosition clickOnLevelPosition) {
		Soldier soldier = getSelectedSoldier(a_model);
		
		if (soldier != null) {
			if (a_model.getLevel().canMove(clickOnLevelPosition)) {
				float length = clickOnLevelPosition.sub( soldier.getPosition() ).length();
				boolean moveIsPossible = length < soldier.getTimeUnits() * Math.sqrt(2.0);
				if (moveIsPossible) {
					m_selectedPath = new AStar(a_model.getMovePossible());
					m_selectedPath.InitSearch(soldier.getPosition(), clickOnLevelPosition, false, 0, false);
				}
			}
		}
	}
	
	private void selectSoldier(Soldier mouseOverSoldier, Camera camera) {
		
		if (m_selectedSoldier != mouseOverSoldier) {
			m_selectedSoldier = mouseOverSoldier;
			camera.focusOn(mouseOverSoldier.getPosition());
		}
		m_selectedPath = null;
	}
	
	
	
	

	public void Draw(AndroidDraw drawable) {
		m_gui.DrawGui(drawable);
		
	}
	
	public void drawMovementPath(AndroidDraw drawable, ModelFacade a_model) {
		if (getDestination(a_model) != null) {

			//Vi har en vald path
			if (m_selectedPath != null) {
				if (m_selectedPath.Update(10) == SearchResult.SearchSucceded) {
					
					for (ModelPosition loc : m_selectedPath.m_path)  {
						drawable.drawCircle(m_camera.toViewPos(loc), m_camera.getHalfScale()/2, Color.argb(128, 0, 0, 255));
					}
				}
			}
			drawable.drawCircle(m_camera.toViewPos(getDestination(a_model)), m_camera.getHalfScale(), Color.argb(128, 0, 0, 255));
		}
	}

	public void startNewGame() {
		m_selectedEnemy = null;
		m_selectedSoldier = null;
		m_selectedPath = null;
		
	}

	public void startNewRound() {
		m_selectedSoldier = null;
	}
}