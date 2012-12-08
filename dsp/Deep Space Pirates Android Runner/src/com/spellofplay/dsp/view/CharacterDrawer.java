package com.spellofplay.dsp.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.graphics.Color;
import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.Vector2;

class CharacterDrawer {
	private Map<ICharacter, VisualCharacter> m_characters = new HashMap<ICharacter, VisualCharacter>();
	private Map<ICharacter, Vector2> m_enemiesSeenThisRound = new HashMap<ICharacter, Vector2>();
	private ShotAnimation m_shotAnimation = new ShotAnimation();
	
	private ITexture m_player;
	private ITexture m_texture;
	
	public CharacterDrawer(ITexture a_texture, ITexture a_player) {
		m_player = a_player;
		m_texture = a_texture;
	}

	ViewPosition getVisualPosition(ICharacter character, Camera camera) {
		return m_characters.get(character).getVisualPosition(camera);
	}
	
	void startNewGame(IModel a_model) {
		m_characters.clear();
		m_shotAnimation.removeAnimations();

		for (ICharacter soldier : a_model.getAliveSoldiers()) {
			m_characters.put(soldier, new VisualCharacter(soldier));
		}
		
		for (ICharacter enemy : a_model.getAliveEnemies()) {
			m_characters.put(enemy, new VisualCharacter(enemy));
		}
		
	}
	
	boolean updateAnimations(IModel a_model, float a_elapsedTime) {
		boolean doneAnimating = true;
		
		doneAnimating = animateCharacterMovement(a_elapsedTime, doneAnimating);
		
		//only animate shots if movement is done
		if (doneAnimating) {
			doneAnimating = animateShots(a_elapsedTime, doneAnimating);
		}
		
		
		return doneAnimating;
	}

	private boolean animateShots(float a_elapsedTime, boolean doneAnimating) {
		m_shotAnimation.update(a_elapsedTime);
		if (m_shotAnimation.isActive()) {
			doneAnimating = false;
		}
		return doneAnimating;
	}

	private boolean animateCharacterMovement(float a_elapsedTime,
			boolean doneAnimating) {
		for (VisualCharacter vc : m_characters.values()) {
			if (vc.update(a_elapsedTime) == false) {
				doneAnimating = false;
			}
		}
		return doneAnimating;
	}
	
	void fireAt(ICharacter attacker, ICharacter fireTarget, boolean didHit) {
		m_characters.get(attacker).attack();
		
		Random rand = new Random();
		
		Vector2 attackerPos = m_characters.get(attacker).getInterpolatedModelPosition();
		VisualCharacter targetPos = m_characters.get(fireTarget);
		
		if (didHit)
			m_characters.get(fireTarget).takeDamage();
		

		for (int i = 0; i< 3;i++) {
			float delay = 0.2f * (float)i+ 0.01f;
			if (didHit) {
				m_shotAnimation.addHit(attackerPos, targetPos, rand, delay);
			} else {
				m_shotAnimation.addMiss(attackerPos, targetPos, rand, delay);
			}
		}
		
	}
	
	void drawCasualties(AndroidDraw drawable, IModel a_model, Camera camera) {
		for (ICharacter enemy : a_model.getDeadEnemies()) {
			VisualCharacter vchar = m_characters.get(enemy);
			
			vchar.drawDeadEnemy(drawable, camera, m_texture);
		}
	}
	
	private void drawEnemies(AndroidDraw drawable, IModel a_model, Camera camera) {
		
		for (ICharacter enemy : a_model.getAliveEnemies()) {
			boolean isSpotted = a_model.canSee(enemy).isEmpty() == false;
			VisualCharacter vchar = m_characters.get(enemy);
			
			if (isSpotted == false) {
				if (m_enemiesSeenThisRound.containsKey(enemy) == false) {
					continue;
				}
				vchar.drawEnemyNotSpotted(drawable, camera, m_texture, m_enemiesSeenThisRound.get(enemy));
			} else {
				if (m_enemiesSeenThisRound.containsKey(enemy) == false) {
					m_enemiesSeenThisRound.put(enemy, vchar.getInterpolatedModelPosition());
				}
				vchar.drawEnemySpotted(drawable, camera, m_texture);
			}
		}
		
		
	}
	
	private void drawSoldiers(AndroidDraw drawable, IModel a_model,
			ICharacter selected, Camera camera, ICharacter enemyTarget) {
		ICharacter target = drawTargetSelection(drawable, a_model, camera, enemyTarget);
		for (ICharacter soldier : a_model.getAliveSoldiers()) {
			VisualCharacter vchar = m_characters.get(soldier);
			vchar.drawSoldier(drawable, camera, m_player, selected == soldier ? target :  null);
		}
	}
	
	private ICharacter drawTargetSelection(AndroidDraw drawable, IModel a_model, Camera camera, ICharacter target) {
		
		if (target != null) {
			ViewPosition vEpos = camera.toViewPos(target.getPosition());
			drawable.drawCircle(vEpos, camera.getHalfScale(), Color.RED);
		}
		return target;
	}


	private void drawSoldierSelection(AndroidDraw drawable,
			IModel a_model, ICharacter selected, Camera camera) {
		if (selected != null && a_model.isSoldierTime() && selected.getTimeUnits() > 0) {
			ViewPosition vpos = m_characters.get(selected).getVisualPosition(camera);//.toViewPos(selected.getPosition());
			drawable.drawCircle(vpos, camera.getHalfScale(), Color.GREEN);
		}
	}

	void moveTo(ICharacter character) {
		m_characters.get(character).moveTo();
		
	}

	void startNewRound() {
		m_enemiesSeenThisRound.clear();
		
	}

	void draw(AndroidDraw drawable, IModel model, ICharacter selected, Camera camera, ICharacter target) {
		drawSoldierSelection(drawable, model, selected, camera);
		drawSoldiers(drawable, model, selected, camera, target);
		drawEnemies(drawable, model, camera);
		m_shotAnimation.draw(drawable, camera);
	}
}
