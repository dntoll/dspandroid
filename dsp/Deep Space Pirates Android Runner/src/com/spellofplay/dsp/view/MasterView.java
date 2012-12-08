package com.spellofplay.dsp.view;

import com.spellofplay.dsp.model.Character;
import com.spellofplay.dsp.model.Enemy;
import com.spellofplay.dsp.model.ICharacterListener;
import com.spellofplay.dsp.model.ModelFacade;
import com.spellofplay.dsp.model.Soldier;

public class MasterView implements ICharacterListener {

	private GameView m_view;
	private InteractionView m_actionView;
	private Camera  m_camera = new Camera();

	public MasterView(ITexture a_texture, ITexture a_player, ModelFacade model) {
		m_view = new GameView(a_texture, a_player, m_camera, model);
		m_actionView = new InteractionView(m_camera);
	}

	public void startNewGame(ModelFacade model) {
		m_view.startNewGame(model);
		m_actionView.startNewGame();
	}
	
	public void drawGame(AndroidDraw drawable, ModelFacade model, float elapsedTimeSeconds) {
		
		m_camera.setScreenSize(drawable.getWindowWidth(), drawable.getWindowHeight());
		
		m_actionView.updateSoldierSelection(model, m_camera, drawable.getWindowWidth(), drawable.getWindowHeight());
		m_camera.update(elapsedTimeSeconds);
		
		m_view.redrawLevelBuffer(drawable, model);
		drawable.drawBackground(m_camera.m_displacement);

		Soldier selected = m_actionView.getSelectedSoldier(model);
		Enemy target = m_actionView.getFireTarget(model);
		
		m_view.getCharacterDrawer().drawCasualties(drawable, model, m_camera);
		
		m_view.drawMovementAndVisibilityHelp(drawable, selected);
		m_actionView.drawMovementPath(drawable, model);

		m_view.getCharacterDrawer().draw(drawable, model, selected, m_camera, target);
		m_view.drawSightLines(drawable, selected);
		
		m_actionView.Draw(drawable);
		
	}

	public void startNewRound() {
		m_view.startNewRound();
		m_actionView.startNewRound();
	}

	@Override
	public void moveTo(Character character) {
		m_view.moveTo(character);
		
		
	}

	@Override
	public void fireAt(Character attacker, Character fireTarget, boolean didHit) {
		m_view.fireAt(attacker, fireTarget, didHit);
		m_camera.focusOn(fireTarget.getPosition());
	}

	@Override
	public void cannotFireAt(Character character, Character fireTarget) {
		m_view.cannotFireAt(character, fireTarget);
		
	}

	@Override
	public void enemyAILog(String string, Enemy enemy) {
		m_view.enemyAILog(string, enemy);
	}

	public InteractionView getInteractionView() {
		return m_actionView;
	}

	public boolean updateAnimations(ModelFacade model,
			float elapsedTimeSeconds) {
		return m_view.updateAnimations(model, elapsedTimeSeconds);
	}
	
	

}
