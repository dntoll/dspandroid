package com.spellofplay.dsp.view;


import android.graphics.Color;

import com.spellofplay.dsp.model.CharacterIterable;
import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.ICharacterListener;
import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.Vector2;
import com.spellofplay.dsp.model.inner.RuleBook;


public class GameView implements ICharacterListener {
	private LevelDrawer m_level;
	private MovementMapView m_movement = new MovementMapView();
	private VisibilityView m_visibility;
	private CharacterDrawer m_characterDrawer;
	private boolean hasInitatedBuffer = false;
	private Camera  m_camera;
	private IModel model;
	
	public GameView(ITexture a_texture, ITexture a_player, Camera camera, IModel model) {
		m_level = new LevelDrawer(a_texture);
		setM_characterDrawer(new CharacterDrawer(a_texture, a_player));
		m_camera = camera;
		this.model = model;
		m_visibility = new VisibilityView();
	}
	
	public void drawMovementAndVisibilityHelp(AndroidDraw drawable, ICharacter selected) {
		
		m_movement.drawPossibleMoveArea(model.getMovePossible(), drawable, m_camera, selected);
		m_visibility.drawNotVisible(model, drawable, m_camera);
	}


	public void drawSightLines(AndroidDraw drawable, ICharacter selected) {
		for (ICharacter enemy : model.getAliveEnemies()) {
			CharacterIterable canShootEnemy = model.couldShootIfHadTime(enemy);
			
			for(ICharacter s : canShootEnemy) {

				ViewPosition vsPos = getCharacterDrawer().getVisualPosition(s, m_camera);
				ViewPosition vEpos = getCharacterDrawer().getVisualPosition(enemy, m_camera);
				boolean hasCover = model.getMovePossible().targetHasCover(s, enemy);
				
				drawable.drawLine(vEpos, vsPos, hasCover ? Color.LTGRAY : Color.WHITE);
				
				if (selected == s) {
					Vector2 direction = vEpos.sub(vsPos).toVector2();
					direction.normalize();
					direction = direction.mul((float)m_camera.getScale() * 0.65f);
					
					Vector2 textAt = vEpos.toVector2().sub(direction);
					
					float chance = RuleBook.getToHitChance(s, enemy, hasCover);
					drawable.drawText( " " + (int)(100.0f * chance) + "%", (int)textAt.m_x, (int)textAt.m_y);
				}
			}
		}
	}

	public void redrawLevelBuffer(AndroidDraw drawable, IModel model) {
		if (hasInitatedBuffer == false) {
			m_level.drawToBuffer(model, drawable, m_camera);
			hasInitatedBuffer = true;
		}
	}


	public void startNewRound() {
		getCharacterDrawer().startNewRound();
	}
	
	public void startNewGame(IModel model) {
		hasInitatedBuffer = false;
		m_visibility.clear();
		m_visibility.updateVisibility(model);
		getCharacterDrawer().startNewGame(model);
	}

	@Override
	public void moveTo(ICharacter character) {
		m_visibility.updateVisibility(model);
		getCharacterDrawer().moveTo(character);
		m_camera.focusOn(character.getPosition());
		m_movement.update();
	}

	@Override
	public void cannotFireAt(ICharacter character, ICharacter fireTarget) {
		
	}


	@Override
	public void enemyAILog(String string, ICharacter enemy) {
		
	}


	@Override
	public void fireAt(ICharacter attacker, ICharacter fireTarget, boolean didHit) {
		getCharacterDrawer().fireAt(attacker, fireTarget, didHit);
		m_movement.update();
	}


	public boolean updateAnimations(IModel model,
			float elapsedTimeSeconds) {
		return getCharacterDrawer().updateAnimations(model, elapsedTimeSeconds);
	}



	public void setM_characterDrawer(CharacterDrawer m_characterDrawer) {
		this.m_characterDrawer = m_characterDrawer;
	}





	public CharacterDrawer getCharacterDrawer() {
		return m_characterDrawer;
	}


	
}
