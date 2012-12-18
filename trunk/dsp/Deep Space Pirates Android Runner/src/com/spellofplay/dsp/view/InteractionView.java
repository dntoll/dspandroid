package com.spellofplay.dsp.view;

import android.graphics.Color;

import com.spellofplay.dsp.model.AStar;
import com.spellofplay.dsp.model.CharacterIterable;
import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.AStar.SearchResult;


public class InteractionView {
	
	
	private Camera camera;
	
	private ICharacter selectedSoldier;
	private ICharacter   selectedEnemy;
	private AStar selectedPath = null;
	private boolean throwGrenadeMode = false;
	private ModelPosition grenadeDestination;

	private IModel model;
	
	InteractionView(Camera camera, IModel model) {
		this.camera = camera;
		this.model = model;
	}

	private ICharacter onSoldier(Input a_input) {
		CharacterIterable soldiers = model.getAliveSoldiers();
		ViewPosition clickpos = new ViewPosition(a_input.m_mousePosition.x, a_input.m_mousePosition.y);
		return onCharacter(soldiers, clickpos);
	}
	
	private ICharacter onEnemy(Input a_input) {
		CharacterIterable enemies = model.getAliveEnemies();
		ViewPosition clickpos = new ViewPosition(a_input.m_mousePosition.x, a_input.m_mousePosition.y);
		return onCharacter(enemies, clickpos);
	}
	
	private ICharacter onCharacter(CharacterIterable characters, ViewPosition clickpos) {
		for (ICharacter s : characters) {
			ModelPosition modelPos = s.getPosition();
			
			ViewPosition viewPosition = camera.toViewPos(modelPos);
			
			float viewRadius = camera.toViewScale(s.getRadius());
			
			if (viewPosition.sub(clickpos).length() < viewRadius) {
				return s;
			}
		}
		return null;
	}
	
	public void setupInput(Input a_input, int a_width, int a_height) {
		if (a_input.IsMouseClicked()) {
			ICharacter mouseOverSoldier = onSoldier(a_input);
			ICharacter mouseOverEnemy = onEnemy(a_input);
			
			if (mouseOverSoldier != null) {
				selectSoldier(mouseOverSoldier, camera);
			} else if (mouseOverEnemy != null) {
				selectedEnemy = mouseOverEnemy;
			} else {
				ViewPosition clickpos = new ViewPosition(a_input.m_mousePosition.x, a_input.m_mousePosition.y);
				ModelPosition clickOnLevelPosition = camera.toModelPos(clickpos);
				
				if (throwGrenadeMode) {
					selectGrenadeDestination(clickOnLevelPosition);
				} else {
					selectDestination(clickOnLevelPosition);
				}
			}
		}
		
		if (a_input.IsDragging()) {
			camera.DoScroll( 	a_width, a_height, 
					(int)(a_input.m_mousePosition.x - a_input.m_dragFrom.x), 
					(int)(a_input.m_mousePosition.y - a_input.m_dragFrom.y));
		} else {
			camera.StopScrolling();
		}
		
	}

	private void selectGrenadeDestination(ModelPosition clickOnLevelPosition) {
		if (model.canSeeMapPosition(getSelectedSoldier(), clickOnLevelPosition)) {
			grenadeDestination = clickOnLevelPosition;
		}
	}
	
	private void selectDestination(ModelPosition clickOnLevelPosition) {
		ICharacter soldier = getSelectedSoldier();
		
		if (soldier != null) {
			if (model.canMove(clickOnLevelPosition)) {
				float length = clickOnLevelPosition.sub( soldier.getPosition() ).length();
				boolean moveIsPossible = length < soldier.getTimeUnits() * Math.sqrt(2.0);
				if (moveIsPossible) {
					selectedPath = new AStar(model.getMovePossible());
					selectedPath.InitSearch(soldier.getPosition(), clickOnLevelPosition, false, 0);
				}
			}
		}
	}

	public void unselectPath() {
		selectedPath = null;
	}
	
	public ICharacter getFireTarget() {
		ICharacter selected = getSelectedSoldier();
		
		if (selected == null) {
			return null;
		}
		
		if (selected.getTimeUnits() < selected.getFireCost()) {
			return null;
		}
		
			
		

		return selectedEnemy;
		
	}
	
	public ICharacter getSelectedSoldier() {
		return selectedSoldier;
	}

	void updateSelections(Camera camera) {
		if (selectedSoldier == null || selectedSoldier.getTimeUnits() == 0) {
			selectNextSoldier(camera);
		}
		
		
		if (selectedEnemy == null || 
			selectedEnemy.getHitPoints() <= 0 || 
			model.canShoot(selectedSoldier, selectedEnemy) == false) {
			selectedEnemy = model.getClosestEnemyThatWeCanShoot(getSelectedSoldier());	
		}
		
		
	}

	private void selectNextSoldier(Camera camera) {
		for (ICharacter soldier : model.getAliveSoldiers()) {
			if (soldier.getTimeUnits() > 0) {
				selectSoldier(soldier, camera);
				break;
			}
		}
	}
	
	public boolean hasDestination() {
		ICharacter selected = getSelectedSoldier();
		if (selected == null) {
			return false;
		}
		
		if (throwGrenadeMode)
			return false;
		
		if (selectedPath != null) {
			if (selectedPath.Update(100) == SearchResult.SearchSucceded)
			{
				if (selectedPath.path.size() <= selected.getTimeUnits() ) 
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public ModelPosition getDestination() {
		return selectedPath.path.get(selectedPath.path.size()-1);
	}
	
	
	
	private void selectSoldier(ICharacter mouseOverSoldier, Camera camera) {
		
		if (selectedSoldier != mouseOverSoldier) {
			selectedSoldier = mouseOverSoldier;
			camera.focusOn(mouseOverSoldier.getPosition());
		}
		selectedPath = null;
	}
	
	void drawMovementPath(AndroidDraw drawable) {
		if (hasDestination()) {

			//Vi har en vald path
			if (selectedPath != null) {
				if (selectedPath.Update(10) == SearchResult.SearchSucceded) {
					
					for (ModelPosition loc : selectedPath.path)  {
						drawable.drawCircle(camera.toViewPos(loc), camera.getHalfScale()/2, Color.argb(128, 0, 0, 255));
					}
				}
			}
			drawable.drawCircle(camera.toViewPos(getDestination()), camera.getHalfScale(), Color.argb(128, 0, 0, 255));
		}
		if (isInThrowGrenadeMode()) {
			if (hasGrenadeDestination()) {
				drawable.drawCircle(camera.toViewPos(getGrenadeDestination()), camera.getHalfScale(), Color.argb(128, 255, 0, 0));
			}
		}
	}

	void startNewGame() {
		selectedEnemy = null;
		selectedSoldier = null;
		selectedPath = null;
		
	}

	void startNewRound() {
		selectedSoldier = null;
	}

	public void setThrowGrenadeMode() {
		throwGrenadeMode = true;
		grenadeDestination = null;
	}

	public void normalMode() {
		grenadeDestination = null;
		throwGrenadeMode = false;
	}

	public boolean isInThrowGrenadeMode() {
		return throwGrenadeMode;
	}

	public boolean hasGrenadeDestination() {
		return grenadeDestination != null;
	}

	public ModelPosition getGrenadeDestination() {
		return grenadeDestination;
	}

	
}