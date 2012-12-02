package com.spellofplay.dsp.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.graphics.Color;

import com.spellofplay.dsp.model.Character;
import com.spellofplay.dsp.model.Enemy;
import com.spellofplay.dsp.model.ModelFacade;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Soldier;
import com.spellofplay.dsp.model.Vector2;

public class CharacterDrawer {
	Map<Character, VisualCharacter> m_characters = new HashMap<Character, VisualCharacter>();
	Map<Enemy, ModelPosition> m_enemiesSeenThisRound = new HashMap<Enemy, ModelPosition>();
	ShotAnimation m_shotAnimation = new ShotAnimation();
	
	ITexture m_player;
	ITexture m_texture;
	
	public CharacterDrawer(ITexture a_texture, ITexture a_player) {
		m_player = a_player;
		m_texture = a_texture;
	}

	ViewPosition getVisualPosition(Character character, Camera camera) {
		return m_characters.get(character).getVisualPosition(camera);
	}
	
	public void startNewGame(ModelFacade a_model) {
		m_characters.clear();
		
		
		for (Soldier soldier : a_model.getAliveSoldiers()) {
			m_characters.put(soldier, new VisualCharacter(soldier));
		}
		
		for (Enemy enemy : a_model.getAliveEnemies()) {
			m_characters.put(enemy, new VisualCharacter(enemy));
		}
		m_shotAnimation.removeAnimations();
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
	
	public void fireAt(Character attacker, Character fireTarget, boolean didHit) {
		m_characters.get(attacker).attack();
		
		Random rand = new Random();
		
		Vector2 attackerPos = m_characters.get(attacker).getInterpolatedModelPosition();
		Vector2 targetPos = m_characters.get(fireTarget).getInterpolatedModelPosition();
		
		if (didHit)
			m_characters.get(fireTarget).takeDamage();
		

		for (int i = 0; i< 3;i++) {
			float delay = 0.2f * (float)i;
			if (didHit) {
				m_shotAnimation.addHit(attackerPos, targetPos, rand, delay);
			} else {
				m_shotAnimation.addMiss(attackerPos, targetPos, rand, delay);
			}
		}
		
	}
	
	void drawEnemies(AndroidDraw drawable, ModelFacade a_model, Camera camera) {
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
			vchar.drawEnemy(drawable, camera, m_texture, isSpotted, m_enemiesSeenThisRound.get(enemy));
			
			
			
		}
	}
	
	void drawSoldiers(AndroidDraw drawable, ModelFacade a_model,
			Soldier selected, Camera camera, Enemy enemyTarget) {
		Enemy target = drawTargetSelection(drawable, a_model, camera, enemyTarget);
		for (Soldier soldier : a_model.getAliveSoldiers()) {
			VisualCharacter vchar = m_characters.get(soldier);
			vchar.drawSoldier(drawable, camera, m_player, selected == soldier ? target :  null);
		}
	}
	
	private Enemy drawTargetSelection(AndroidDraw drawable, ModelFacade a_model, Camera camera, Enemy target) {
		
		if (target != null) {
			ViewPosition vEpos = camera.toViewPos(target.getPosition());
			drawable.drawCircle(vEpos, camera.getHalfScale(), Color.RED);
		}
		return target;
	}


	void drawSoldierSelection(AndroidDraw drawable,
			ModelFacade a_model, Soldier selected, Camera camera) {
		if (selected != null && a_model.isSoldierTime() && selected.getTimeUnits() > 0) {
			ViewPosition vpos = m_characters.get(selected).getVisualPosition(camera);//.toViewPos(selected.getPosition());
			drawable.drawCircle(vpos, camera.getHalfScale(), Color.GREEN);
		}
	}

	public void moveTo(Character character) {
		m_characters.get(character).moveTo();
		
	}

	public void startNewRound() {
		m_enemiesSeenThisRound.clear();
		
	}

	public void draw(AndroidDraw drawable, ModelFacade model, Soldier selected, Camera camera, Enemy target) {
		drawSoldierSelection(drawable, model, selected, camera);
		drawSoldiers(drawable, model, selected, camera, target);
		drawEnemies(drawable, model, camera);
		m_shotAnimation.draw(drawable, camera);
	}
}
