package com.spellofplay.dsp.model;

import java.util.ArrayList;
import java.util.List;


public class ModelFacade {
	
	Game m_game = new Game();
	
	
	
	public String getGameTitle() {
		return "Deep Space Pirates ";
	}

	
	public Level getLevel() {

		return m_game.m_level;
	}
	public boolean isSoldierTime() {
		for (Soldier s : m_game.getAliveSoldiers()) {
			if (s.getTimeUnits() > 0) {
				return true;
			}
		}
		return false;
	}


	public boolean isEnemyTime() {
		if (isSoldierTime())
			return false;
		
		for (Enemy e : m_game.getAliveEnemies()) {
			if (e.getTimeUnits() > 0) {
				return true;
			}
		}
		return false;
	}


	public void updatePlayers() {
		m_game.updatePlayers();
	}
	
	public void updateEnemies() {
		m_game.updateEnemies();
	}


	public void doMoveTo(Soldier selectedSoldier, ModelPosition destination) {
		m_game.doMoveTo(selectedSoldier, destination);
	}


	public List<Soldier> getAliveSoldiers() {
		return m_game.getAliveSoldiers();
	}
	
	public List<Enemy> getAliveEnemies() {
		return m_game.getAliveEnemies();
	}


	public void startNewGame(int a_level) {
		m_game.startLevel(a_level);
		
	}

	public void startNewRound() {
		m_game.startNewRound();
	}

	public boolean enemyHasWon() {
		// TODO Auto-generated method stub
		return m_game.getAliveSoldiers().size() == 0;
	}

	public boolean playerHasWon() {
		// TODO Auto-generated method stub
		return m_game.getAliveEnemies().size() == 0 && m_game.getAliveSoldiers().size() > 0;
	}


	public boolean fireAt(Soldier selectedSoldier, Enemy fireTarget) {
		boolean hasLineOfSight = m_game.m_level.lineOfSight(selectedSoldier.m_position.toCenterTileVector(), fireTarget.m_position.toCenterTileVector());
		return selectedSoldier.fireAt(fireTarget, hasLineOfSight);
	}


	public List<Soldier> canSee(Enemy e) {
		List<Soldier> aliveSoldiers = m_game.getAliveSoldiers();
		
		List<Soldier> soldiersThatCanSee = new ArrayList<Soldier>();
		
		for (Soldier s : aliveSoldiers) {
			if ( m_game.m_level.lineOfSight(s.getPosition().toCenterTileVector(), e.getPosition().toCenterTileVector())) {
				soldiersThatCanSee.add(s);
				
			}
		}
		return soldiersThatCanSee;
	}


	public void doWait(Soldier selectedSoldier) {
		selectedSoldier.m_timeUnits = 0;
	}



	
}
