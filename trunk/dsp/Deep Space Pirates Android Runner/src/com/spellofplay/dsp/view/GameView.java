package com.spellofplay.dsp.view;


import android.graphics.Color;
import com.spellofplay.dsp.model.Enemy;
import com.spellofplay.dsp.model.ICharacterListener;
import com.spellofplay.dsp.model.ModelFacade;
import com.spellofplay.dsp.model.RuleBook;
import com.spellofplay.dsp.model.Soldier;
import com.spellofplay.dsp.model.Character;
import com.spellofplay.dsp.model.CharacterCollection;
import com.spellofplay.dsp.model.Vector2;


public class GameView implements ICharacterListener {
	private LevelDrawer m_level;
	private MovementMapView m_movement = new MovementMapView();
	private VisibilityView m_visibility;
	private CharacterDrawer m_characterDrawer;
	private boolean hasInitatedBuffer = false;
	private Camera  m_camera;
	private ModelFacade model;
	
	public GameView(ITexture a_texture, ITexture a_player, Camera camera, ModelFacade model) {
		m_level = new LevelDrawer(a_texture);
		setM_characterDrawer(new CharacterDrawer(a_texture, a_player));
		m_camera = camera;
		this.model = model;
		m_visibility = new VisibilityView();
	}
	
	
	


	public void drawMovementAndVisibilityHelp(AndroidDraw drawable, Soldier selected) {
		
		m_movement.drawPossibleMoveArea(model.getMovePossible(), drawable, m_camera, selected);
		m_visibility.drawNotVisible(model, drawable, m_camera);
	}


	public void drawSightLines(AndroidDraw drawable, Soldier selected) {
		for (Enemy enemy : model.getAliveEnemies()) {
			CharacterCollection<Soldier> soldiersWhoSpotsEnemy = model.canSee(enemy);
			CharacterCollection<Soldier> canShootEnemy = soldiersWhoSpotsEnemy.couldShootIfHadTime(enemy, model.getMovePossible());
			
			for(Soldier s : canShootEnemy) {

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

	public void redrawLevelBuffer(AndroidDraw drawable, ModelFacade a_model) {
		if (hasInitatedBuffer == false) {
			m_level.drawToBuffer(a_model.getLevel(), drawable, m_camera);
			hasInitatedBuffer = true;
		}
	}


	public void startNewRound() {
		getCharacterDrawer().startNewRound();
	}
	
	public void startNewGame(ModelFacade model) {
		hasInitatedBuffer = false;
		m_visibility.clear();
		m_visibility.updateVisibility(model);
		getCharacterDrawer().startNewGame(model);
	}

	@Override
	public void moveTo(Character character) {
		m_visibility.updateVisibility(model);
		getCharacterDrawer().moveTo(character);
		m_camera.focusOn(character.getPosition());
		m_movement.update();
	}

	@Override
	public void cannotFireAt(Character character, Character fireTarget) {
		
	}


	@Override
	public void enemyAILog(String string, Enemy enemy) {
		
	}


	@Override
	public void fireAt(Character attacker, Character fireTarget, boolean didHit) {
		getCharacterDrawer().fireAt(attacker, fireTarget, didHit);
		m_movement.update();
	}


	public boolean updateAnimations(ModelFacade model,
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
