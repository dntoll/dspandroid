package com.spellofplay.dsp.view;

import android.graphics.Color;

import com.spellofplay.common.view.Input;
import com.spellofplay.dsp.model.AStar;
import com.spellofplay.dsp.model.CharacterIterable;
import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.AStar.SearchResult;


public class InteractionView {
	
	
	private Camera m_camera;
	
	private ICharacter m_selectedSoldier;
	private ICharacter   m_selectedEnemy;
	private AStar m_selectedPath = null;
	
	InteractionView(Camera camera) {
		m_camera = camera;
	}

	private ICharacter onSoldier(Input a_input, IModel a_model) {
		CharacterIterable soldiers = a_model.getAliveSoldiers();
		ViewPosition clickpos = new ViewPosition(a_input.m_mousePosition.x, a_input.m_mousePosition.y);
		return onCharacter(soldiers, clickpos);
	}
	
	private ICharacter onEnemy(Input a_input, IModel a_model) {
		CharacterIterable enemies = a_model.getAliveEnemies();
		ViewPosition clickpos = new ViewPosition(a_input.m_mousePosition.x, a_input.m_mousePosition.y);
		return onCharacter(enemies, clickpos);
	}
	
	private ICharacter onCharacter(CharacterIterable characters, ViewPosition clickpos) {
		for (ICharacter s : characters) {
			ModelPosition modelPos = s.getPosition();
			
			ViewPosition viewPosition = m_camera.toViewPos(modelPos);
			
			float viewRadius = m_camera.toViewScale(s.getRadius());
			
			if (viewPosition.sub(clickpos).length() < viewRadius) {
				return s;
			}
		}
		return null;
	}
	
	public void setupInput(Input a_input, IModel a_model, int a_width, int a_height) {
		if (a_input.IsMouseClicked()) {
			ICharacter mouseOverSoldier = onSoldier(a_input, a_model);
			ICharacter mouseOverEnemy = onEnemy(a_input, a_model);
			
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
					a_height, 
					(int)(a_input.m_mousePosition.x - a_input.m_dragFrom.x), 
					(int)(a_input.m_mousePosition.y - a_input.m_dragFrom.y));
		} else {
			m_camera.StopScrolling();
		}
		
	}

	public void unselectPath() {
		m_selectedPath = null;
	}
	
	public ICharacter getFireTarget(IModel a_model) {
		ICharacter selected = getSelectedSoldier(a_model);
		
		if (selected == null) {
			return null;
		}
		
		if (selected.getTimeUnits() < selected.getFireCost()) {
			return null;
		}
		
			
		

		return m_selectedEnemy;
		
	}
	
	public ICharacter getSelectedSoldier(IModel a_model) {
		return m_selectedSoldier;
	}

	void updateSelections(IModel a_model, Camera camera) {
		if (m_selectedSoldier == null || m_selectedSoldier.getTimeUnits() == 0) {
			selectNextSoldier(a_model, camera);
		}
		
		
		if (m_selectedEnemy == null || 
			m_selectedEnemy.getHitPoints() <= 0 || 
			a_model.canShoot(m_selectedSoldier, m_selectedEnemy) == false) {
			m_selectedEnemy = a_model.getClosestEnemyThatWeCanShoot(getSelectedSoldier(a_model));	
		}
		
		
	}

	private void selectNextSoldier(IModel a_model, Camera camera) {
		for (ICharacter soldier : a_model.getAliveSoldiers()) {
			if (soldier.getTimeUnits() > 0) {
				selectSoldier(soldier, camera);
				break;
			}
		}
	}
	
	public boolean hasDestination(IModel a_model) {
		ICharacter selected = getSelectedSoldier(a_model);
		if (selected == null) {
			return false;
		}
		
		if (m_selectedPath != null) {
			if (m_selectedPath.Update(100) == SearchResult.SearchSucceded)
			{
				if (m_selectedPath.path.size() <= selected.getTimeUnits() ) 
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public ModelPosition getDestination(IModel a_model) {
		return m_selectedPath.path.get(m_selectedPath.path.size()-1);
	}
	
	private void selectDestination(IModel a_model, ModelPosition clickOnLevelPosition) {
		ICharacter soldier = getSelectedSoldier(a_model);
		
		if (soldier != null) {
			if (a_model.canMove(clickOnLevelPosition)) {
				float length = clickOnLevelPosition.sub( soldier.getPosition() ).length();
				boolean moveIsPossible = length < soldier.getTimeUnits() * Math.sqrt(2.0);
				if (moveIsPossible) {
					m_selectedPath = new AStar(a_model.getMovePossible());
					m_selectedPath.InitSearch(soldier.getPosition(), clickOnLevelPosition, false, 0);
				}
			}
		}
	}
	
	private void selectSoldier(ICharacter mouseOverSoldier, Camera camera) {
		
		if (m_selectedSoldier != mouseOverSoldier) {
			m_selectedSoldier = mouseOverSoldier;
			camera.focusOn(mouseOverSoldier.getPosition());
		}
		m_selectedPath = null;
	}
	
	void drawMovementPath(AndroidDraw drawable, IModel a_model) {
		if (hasDestination(a_model)) {

			//Vi har en vald path
			if (m_selectedPath != null) {
				if (m_selectedPath.Update(10) == SearchResult.SearchSucceded) {
					
					for (ModelPosition loc : m_selectedPath.path)  {
						drawable.drawCircle(m_camera.toViewPos(loc), m_camera.getHalfScale()/2, Color.argb(128, 0, 0, 255));
					}
				}
			}
			drawable.drawCircle(m_camera.toViewPos(getDestination(a_model)), m_camera.getHalfScale(), Color.argb(128, 0, 0, 255));
		}
	}

	void startNewGame() {
		m_selectedEnemy = null;
		m_selectedSoldier = null;
		m_selectedPath = null;
		
	}

	void startNewRound() {
		m_selectedSoldier = null;
	}

	
}