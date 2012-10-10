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
		for (Soldier s : m_game.getAliveSoldiers()) {
			if (s.getTimeUnits() > 0) {
				return true;
			}
		}
		return false;
	}


	/* (non-Javadoc)
	 * @see com.spellofplay.dsp.model.IModel#isEnemyTime()
	 */
	@Override
	public boolean isEnemyTime() {
		if (hasUnfinishedActions())
			return false;
		
		for (Enemy e : m_game.getAliveEnemies()) {
			if (e.getTimeUnits() > 0) {
				return true;
			}
		}
		return false;
	}


	/* (non-Javadoc)
	 * @see com.spellofplay.dsp.model.IModel#updatePlayers()
	 */
	@Override
	public void updatePlayers() {
		m_game.updatePlayers();
	}
	
	/* (non-Javadoc)
	 * @see com.spellofplay.dsp.model.IModel#updateEnemies()
	 */
	@Override
	public void updateEnemies() {
		m_game.updateEnemies();
	}


	/* (non-Javadoc)
	 * @see com.spellofplay.dsp.model.IModel#doMoveTo(com.spellofplay.dsp.model.Soldier, android.graphics.Point)
	 */
	@Override
	public void doMoveTo(Soldier selectedSoldier, ModelPosition destination) {
		m_game.doMoveTo(selectedSoldier, destination);
	}


	@Override
	public List<Soldier> getAliveSoldiers() {
		return m_game.getAliveSoldiers();
	}


	@Override
	public void startNewGame(int a_level) {
		m_game.startLevel(a_level);
		
	}


	@Override
	public void startNewRound() {
		m_game.startNewRound();
	}


	@Override
	public boolean enemyHasWon() {
		// TODO Auto-generated method stub
		return m_game.getAliveSoldiers().size() == 0;
	}


	@Override
	public boolean playerHasWon() {
		// TODO Auto-generated method stub
		return m_game.getAliveEnemies().size() == 0 && m_game.getAliveSoldiers().size() > 0;
	}


	
}
