package com.spellofplay.dsp.view;

import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.ICharacterListener;
import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.inner.IPersistance;

public class MasterView implements ICharacterListener {

	private GameView m_view;
	private InteractionView m_actionView;
	private VisibilityView visibility;
	private Camera  camera = new Camera();
	private IModel model;

	public MasterView(ITexture a_texture, ITexture a_player, IModel model) {
		m_view = new GameView(a_texture, a_player, camera, model);
		m_actionView = new InteractionView(camera, model);
		this.visibility = new VisibilityView();
		this.model = model;
	}

	public void startNewGame() {
		m_view.startNewGame(model);
		m_actionView.startNewGame();
		visibility.clear();
		visibility.recalculateVisibility(model);
	}
	
	public void drawGame(AndroidDraw drawable, float elapsedTimeSeconds) {
		
		
		camera.setScreenSize(drawable.getWindowWidth(), drawable.getWindowHeight());
		m_actionView.updateSelections(camera);
		ICharacter selected = m_actionView.getSelectedSoldier();
		ICharacter target = m_actionView.getFireTarget();
		
		camera.update(elapsedTimeSeconds);
		m_view.redrawLevelBuffer(drawable, model);
		drawable.drawBackground(camera.getDisplacement());
		m_view.drawDoors(drawable);
		m_view.getCharacterDrawer().drawCasualties(drawable, model, camera);
		if (m_actionView.isInThrowGrenadeMode()) {
			
		} else {
			m_view.drawMovementHelp(drawable, selected);
		}
		
		drawVisibility(drawable);
		
		m_actionView.drawMovementPath(drawable);
		
		
		
		m_view.getCharacterDrawer().draw(drawable, model, selected, camera, target);
		
		m_view.drawSightLines(drawable, selected);
		
		
		
	}
	
	private void drawVisibility(AndroidDraw drawable) {
		visibility.drawNotVisible(model, drawable, camera);
	}

	public void startNewRound() {
		m_view.startNewRound();
		m_actionView.startNewRound();
	}

	@Override
	public void moveTo(ICharacter character) {
		m_view.moveTo(character);
		visibility.recalculateVisibility(model);
		
		
	}

	@Override
	public void fireAt(ICharacter attacker, ICharacter fireTarget, boolean didHit) {
		m_view.fireAt(attacker, fireTarget, didHit);
		camera.focusOn(fireTarget.getPosition());
	}
	
	@Override
	public void takeDamage(ICharacter character) {
		m_view.takeDamage(character);
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

	public boolean updateAnimations(float elapsedTimeSeconds) {
		return m_view.updateAnimations(model, elapsedTimeSeconds);
	}

	public void open() {
		m_view.open();
		visibility.recalculateVisibility(model);
	}

	@Override
	public void enemySpotsNewSoldier() {
		m_view.enemySpotsNewSoldier();
	}

	@Override
	public void soldierSpotsNewEnemy() {
		m_view.soldierSpotsNewEnemy();
		
	}

	public void load(IPersistance persistence) throws Exception {
		visibility.Load(persistence);
		startNewGame();
	}

	public void save(IPersistance persistence) {
		visibility.Save(persistence);
	}
	
	

}
