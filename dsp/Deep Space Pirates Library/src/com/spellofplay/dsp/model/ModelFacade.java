package com.spellofplay.dsp.model;

import java.util.List;


public class ModelFacade implements IModel {
	
	Game m_game = new Game();
	
	
	/* (non-Javadoc)
	 * @see com.spellofplay.dsp.model.IModel#getGameTitle()
	 */
	@Override
	public String getGameTitle() {
		return "Deep Space Pirates ";
	}

	
	/* (non-Javadoc)
	 * @see com.spellofplay.dsp.model.IModel#getLevel()
	 */
	@Override
	public Level getLevel() {

		return m_game.m_level;
	}
	/* (non-Javadoc)
	 * @see com.spellofplay.dsp.model.IModel#hasUnfinishedActions()
	 */
	@Override
	public boolean hasUnfinishedActions() {
		// TODO Auto-generated method stub
		return false;
	}


	/* (non-Javadoc)
	 * @see com.spellofplay.dsp.model.IModel#isEnemyTime()
	 */
	@Override
	public boolean isEnemyTime() {
		// TODO Auto-generated method stub
		return false;
	}


	/* (non-Javadoc)
	 * @see com.spellofplay.dsp.model.IModel#updatePlayers()
	 */
	@Override
	public void updatePlayers() {
		// TODO Auto-generated method stub
		
	}
	
	/* (non-Javadoc)
	 * @see com.spellofplay.dsp.model.IModel#updateEnemies()
	 */
	@Override
	public void updateEnemies() {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see com.spellofplay.dsp.model.IModel#doMoveTo(com.spellofplay.dsp.model.Soldier, android.graphics.Point)
	 */
	@Override
	public void doMoveTo(Soldier selectedSoldier, ModelPosition destination) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public List<Soldier> getAliveSoldiers() {
		return m_game.getAliveSoldiers();
	}


	@Override
	public void startNewGame(int a_level) {
		m_game.startLevel(a_level);
		
	}


	
}
