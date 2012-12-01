package com.spellofplay.dsp.view;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import com.spellofplay.dsp.model.AStar;
import com.spellofplay.dsp.model.AStar.SearchResult;
import com.spellofplay.dsp.model.Enemy;
import com.spellofplay.dsp.model.ICharacterListener;
import com.spellofplay.dsp.model.ModelFacade;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.RuleBook;
import com.spellofplay.dsp.model.Soldier;
import com.spellofplay.dsp.model.Character;
import com.spellofplay.dsp.model.Vector2;
import com.spellofplay.common.view.Input;


public class GameView implements ICharacterListener {

	
	
	LevelDrawer m_level;// = new LevelDrawer();
	
	ShotAnimation m_shotAnimation = new ShotAnimation();
	boolean hasInitatedBuffer = false;
	Camera  m_camera = new Camera();
	Soldier m_selectedSoldier;
	Enemy   m_selectedEnemy;
	AStar m_selectedPath = null;
	
	Map<Enemy, ModelPosition> m_enemiesSeenThisRound = new HashMap<Enemy, ModelPosition>();
	Map<Character, VisualCharacter> m_characters = new HashMap<Character, VisualCharacter>();
	SimpleGui m_gui = new SimpleGui();
	
	ITexture m_texture;
	ITexture m_player;
	
	
	public GameView(ITexture a_texture, ITexture a_player) {
		m_level = new LevelDrawer(a_texture);
		m_texture = a_texture;
		m_player = a_player;
	}
	
	
	public void drawGame(AndroidDraw drawable, ModelFacade a_model) {
		redrawLevelBuffer(drawable, a_model);
		
		drawable.drawBackground(m_camera.m_displacement);

		Soldier selected = getSelectedSoldier(a_model);
		drawMovementAndVisibilityHelp(drawable, a_model, selected);
		drawMovementPath(drawable, a_model);
		drawSoldierSelection(drawable, a_model, selected);
		drawSoldiers(drawable, a_model, selected);
		drawEnemies(drawable, a_model);
		drawable.drawText(a_model.getGameTitle(), 10, 10, drawable.m_guiText);
		drawSightLines(drawable, a_model);
		m_shotAnimation.draw(drawable, m_camera);
		
		m_gui.DrawGui(drawable);
	}


	private void drawMovementAndVisibilityHelp(AndroidDraw drawable,
			ModelFacade a_model, Soldier selected) {
		m_level.drawPossibleMoveArea(a_model.getMovePossible(), drawable, m_camera, selected);
		m_level.drawNotVisible(a_model, drawable, m_camera);
	}


	private void drawSightLines(AndroidDraw drawable, ModelFacade a_model) {
		for (Enemy e : a_model.getAliveEnemies()) {
			List<Soldier> soldiersWhoSpotsEnemy = a_model.canSee(e);
			if (soldiersWhoSpotsEnemy.isEmpty() == false) {
				
				for(Soldier s : soldiersWhoSpotsEnemy) {
					
					if (a_model.canShoot(s, e)) {
						ViewPosition vsPos = m_characters.get(s).getVisualPosition(m_camera);
						ViewPosition vEpos = m_characters.get(e).getVisualPosition(m_camera);
						boolean hasCover = a_model.getMovePossible().targetHasCover(s, e);
						
						drawable.drawLine(vEpos, vsPos, hasCover ? Color.LTGRAY : Color.WHITE);
						
						if (getSelectedSoldier(a_model) == s) {
							
							Vector2 direction = vEpos.sub(vsPos).toVector2();
							direction.normalize();
							direction = direction.mul((float)m_camera.getScale() * 0.65f);
							
							Vector2 textAt = vEpos.toVector2().sub(direction);
							
							float chance = RuleBook.getToHitChance(s, e, hasCover);
							drawable.drawText( " " + (int)(100.0f * chance) + "%", (int)textAt.m_x, (int)textAt.m_y);
							
						}
					}
				}
			}
		}
	}


	private void drawEnemies(AndroidDraw drawable, ModelFacade a_model) {
		for (Enemy enemy : a_model.getAliveEnemies()) {
			
			boolean isSpotted = !a_model.canSee(enemy).isEmpty();
			
			//HAS THIS ENEMY HAS BEEN SEEN THIS ROUND?
			if (isSpotted == false) {
				if (m_enemiesSeenThisRound.containsKey(enemy) == false) {
					//not seen at all this round...
					continue;
				}
			} else {
				//add it to the list of enemies seen this round
				if (m_enemiesSeenThisRound.containsKey(enemy) == false) {
					m_enemiesSeenThisRound.put(enemy, enemy.getPosition());
				}
			}
			VisualCharacter vchar = m_characters.get(enemy);
			vchar.drawEnemy(drawable, m_camera, m_texture, isSpotted, m_enemiesSeenThisRound.get(enemy));
			
			
			
		}
	}


	private void drawSoldiers(AndroidDraw drawable, ModelFacade a_model,
			Soldier selected) {
		Enemy target = drawTargetSelection(drawable, a_model);
		for (Soldier soldier : a_model.getAliveSoldiers()) {
			VisualCharacter vchar = m_characters.get(soldier);
			vchar.drawSoldier(drawable, m_camera, m_player, selected == soldier ? target :  null);
		}
	}


	private Enemy drawTargetSelection(AndroidDraw drawable, ModelFacade a_model) {
		Enemy target = getFireTarget(a_model);
		if (target != null) {
			ViewPosition vEpos = m_camera.toViewPos(target.getPosition());
			drawable.drawCircle(vEpos, m_camera.getHalfScale(), Color.RED);
		}
		return target;
	}


	private void drawSoldierSelection(AndroidDraw drawable,
			ModelFacade a_model, Soldier selected) {
		if (selected != null && a_model.isSoldierTime() && selected.getTimeUnits() > 0) {
			ViewPosition vpos = m_characters.get(selected).getVisualPosition(m_camera);//.toViewPos(selected.getPosition());
			drawable.drawCircle(vpos, m_camera.getHalfScale(), Color.GREEN);
		}
	}


	private void drawMovementPath(AndroidDraw drawable, ModelFacade a_model) {
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


	private void redrawLevelBuffer(AndroidDraw drawable, ModelFacade a_model) {
		if (hasInitatedBuffer == false) 
		{
			m_level.draw(a_model.getLevel(), drawable, m_camera);
			hasInitatedBuffer = true;
		}
	}


	
	
	
	enum Action {
		None, Waiting, Moveing, Attacking
	}
	
	Action m_action = Action.None;
	
	
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
	
	public void setupInput(Input a_input, ModelFacade a_model, int a_width, int a_height) {
		
		m_action = Action.None;
		
		//On soldier?
		Soldier mouseOverSoldier = onSoldier(a_input, a_model);
		Enemy mouseOverEnemy = onEnemy(a_input, a_model);
		
		if (getSelectedSoldier(a_model) != null) {
			if (m_gui.DoButtonCentered(a_width - SimpleGui.BUTTON_WIDTH, a_height - SimpleGui.BUTTON_HEIGHT-16, "Wait", a_input, false)) {
				m_action = Action.Waiting;
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
				boolean moveIsPossible = length < soldier.getTimeUnits() * Math.sqrt(2.0);
				if (moveIsPossible) {
					m_selectedPath = new AStar(a_model.getMovePossible());
					m_selectedPath.InitSearch(soldier.getPosition(), clickOnLevelPosition, false, 0, false);
				}
			}
		}
	}


	private void selectSoldier(Soldier mouseOverSoldier) {
		m_selectedSoldier = mouseOverSoldier;
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
		
		
		
		if (m_selectedSoldier == null || m_selectedSoldier.getTimeUnits() == 0) {
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

	public void startNewRound() {
		m_enemiesSeenThisRound.clear();
	}
	
	public void startNewGame(ModelFacade a_model) {
		m_selectedEnemy = null;
		m_selectedSoldier = null;
		m_selectedPath = null;
		m_level.startUpdateVisibility();
		m_characters.clear();
		
		
		for (Soldier soldier : a_model.getAliveSoldiers()) {
			m_characters.put(soldier, new VisualCharacter(soldier));
		}
		
		for (Enemy enemy : a_model.getAliveEnemies()) {
			m_characters.put(enemy, new VisualCharacter(enemy));
		}
	}
	
	


	public void unselectPath() {
		m_selectedPath = null;
	}


	public boolean updateAnimations(ModelFacade a_model, float a_elapsedTime) {
		boolean doneAnimating = true;
		
		for (VisualCharacter vc : m_characters.values()) {
			if (vc.update(a_elapsedTime) == false) {
				doneAnimating = false;
			}
		}
		
		
		m_shotAnimation.update(a_elapsedTime);
		if (m_shotAnimation.isActive()) {
			doneAnimating = false;
		}
		
		
		return doneAnimating;
	}


	@Override
	public void moveTo(Character character) {
		m_level.startUpdateVisibility();
		
		
		m_characters.get(character).moveTo();
		
	}


	@Override
	public void fireAt(Character attacker, Character fireTarget, boolean didHit) {
		m_characters.get(attacker).attack();
		
		if (didHit) {
			m_characters.get(fireTarget).takeDamage();
			m_shotAnimation.addHit(m_characters.get(attacker).getInterpolatedModelPosition(), m_characters.get(fireTarget).getInterpolatedModelPosition());
		} else {
			m_shotAnimation.addMiss(m_characters.get(attacker).getInterpolatedModelPosition(), m_characters.get(fireTarget).getInterpolatedModelPosition());
		}
		
	}


	@Override
	public void cannotFireAt(Character character, Character fireTarget) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void enemyAILog(String string) {
		// TODO Auto-generated method stub
		
	}
}
