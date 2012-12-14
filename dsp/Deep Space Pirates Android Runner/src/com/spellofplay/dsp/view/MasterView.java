package com.spellofplay.dsp.view;

import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.ICharacterListener;
import com.spellofplay.dsp.model.IModel;

public class MasterView implements ICharacterListener {

	private GameView m_view;
	private InteractionView m_actionView;
	private Camera  m_camera = new Camera();

	public MasterView(ITexture a_texture, ITexture a_player, IModel model) {
		m_view = new GameView(a_texture, a_player, m_camera, model);
		m_actionView = new InteractionView(m_camera);
	}

	public void startNewGame(IModel model) {
		m_view.startNewGame(model);
		m_actionView.startNewGame();
	}
	
	public void drawGame(AndroidDraw drawable, IModel model, float elapsedTimeSeconds) {
		
		
		m_camera.setScreenSize(drawable.getWindowWidth(), drawable.getWindowHeight());
		m_actionView.updateSelections(model, m_camera);
		ICharacter selected = m_actionView.getSelectedSoldier(model);
		ICharacter target = m_actionView.getFireTarget(model);
		
		m_camera.update(elapsedTimeSeconds);
		m_view.redrawLevelBuffer(drawable, model);
		drawable.drawBackground(m_camera.getDisplacement());
		m_view.drawDoors(drawable);
		m_view.getCharacterDrawer().drawCasualties(drawable, model, m_camera);
		m_view.drawMovementHelp(drawable, selected);
		m_view.drawVisibility(drawable);
		m_actionView.drawMovementPath(drawable, model);
		
		
		
		m_view.getCharacterDrawer().draw(drawable, model, selected, m_camera, target);
		
		m_view.drawSightLines(drawable, selected);
		
		
		
	}

	public void startNewRound() {
		m_view.startNewRound();
		m_actionView.startNewRound();
	}

	@Override
	public void moveTo(ICharacter character) {
		m_view.moveTo(character);
		
		
	}

	@Override
	public void fireAt(ICharacter attacker, ICharacter fireTarget, boolean didHit) {
		m_view.fireAt(attacker, fireTarget, didHit);
		m_camera.focusOn(fireTarget.getPosition());
	}

	@Override
	public void cannotFireAt(ICharacter character, ICharacter fireTarget) {
		m_view.cannotFireAt(character, fireTarget);
		
	}

	@Override
	public void enemyAILog(String string, ICharacter enemy) {
		m_view.enemyAILog(string, enemy);
	}

	public InteractionView getInteractionView() {
		return m_actionView;
	}

	public boolean updateAnimations(IModel model,
			float elapsedTimeSeconds) {
		return m_view.updateAnimations(model, elapsedTimeSeconds);
	}

	public void open() {
		m_view.open();
	}

	@Override
	public void enemySpotsNewSoldier() {
		m_view.enemySpotsNewSoldier();
	}

	@Override
	public void soldierSpotsNewEnemy() {
		m_view.soldierSpotsNewEnemy();
		
	}
	
	

}
