package com.spellofplay.dsp.view;


import android.graphics.Color;

import com.spellofplay.dsp.model.CharacterIterable;
import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.ICharacterListener;
import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.Vector2;
import com.spellofplay.dsp.model.inner.RuleBook;


class GameView implements ICharacterListener {
	private LevelDrawer level;
	private MovementMapView movementView = new MovementMapView();
	private VisibilityView visibility;
	private CharacterDrawer m_characterDrawer;
	private boolean hasInitatedBuffer = false;
	private Camera  camera;
	private IModel model;
	
	GameView(ITexture a_texture, ITexture a_player, Camera camera, IModel model) {
		this.level = new LevelDrawer(a_texture, model);
		setM_characterDrawer(new CharacterDrawer(a_texture, a_player));
		this.camera = camera;
		this.model = model;
		this.visibility = new VisibilityView();
	}
	
	void drawMovementHelp(AndroidDraw drawable, ICharacter selected) {
		
		movementView.drawPossibleMoveArea(model.getMovePossible(), drawable, camera, selected);
	}
	void drawVisibility(AndroidDraw drawable) {
		visibility.drawNotVisible(model, drawable, camera);
	}
	
	public void drawDoors(AndroidDraw drawable) {
		level.drawDoors(drawable, camera);
	}


	void drawSightLines(AndroidDraw drawable, ICharacter selected) {
		for (ICharacter enemy : model.getAliveEnemies()) {
			CharacterIterable canShootEnemy = model.couldShootIfHadTime(enemy);
			
			for(ICharacter s : canShootEnemy) {

				ViewPosition vsPos = getCharacterDrawer().getVisualPosition(s, camera);
				ViewPosition vEpos = getCharacterDrawer().getVisualPosition(enemy, camera);
				boolean hasCover = model.getMovePossible().targetHasCover(s, enemy);
				
				drawable.drawLine(vEpos, vsPos, hasCover ? Color.LTGRAY : Color.WHITE);
				
				if (selected == s) {
					Vector2 direction = vEpos.sub(vsPos).toVector2();
					direction.normalize();
					direction = direction.mul((float)camera.getScale() * 0.65f);
					
					Vector2 textAt = vEpos.toVector2().sub(direction);
					
					float chance = RuleBook.getToHitChance(s, enemy, hasCover);
					drawable.drawText( " " + (int)(100.0f * chance) + "%", (int)textAt.x, (int)textAt.y);
				}
			}
		}
	}

	void redrawLevelBuffer(AndroidDraw drawable, IModel model) {
		if (hasInitatedBuffer == false) {
			level.drawToBuffer(drawable, camera);
			hasInitatedBuffer = true;
		}
	}


	void startNewRound() {
		getCharacterDrawer().startNewRound();
	}
	
	void startNewGame(IModel model) {
		hasInitatedBuffer = false;
		visibility.clear();
		visibility.recalculateVisibility(model);
		getCharacterDrawer().startNewGame(model);
	}

	@Override
	public void moveTo(ICharacter character) {
		visibility.recalculateVisibility(model);
		getCharacterDrawer().moveTo(character);
		camera.focusOn(character.getPosition());
		movementView.update();
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
		movementView.update();
	}


	boolean updateAnimations(IModel model, float elapsedTimeSeconds) {
		return getCharacterDrawer().updateAnimations(model, elapsedTimeSeconds);
	}



	public void setM_characterDrawer(CharacterDrawer m_characterDrawer) {
		this.m_characterDrawer = m_characterDrawer;
	}





	public CharacterDrawer getCharacterDrawer() {
		return m_characterDrawer;
	}

	public void open() {
		visibility.recalculateVisibility(model);
		movementView.update();
	}

	


	
}
