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


	public void updatePlayers(ICharacterListener clistener) {
		m_game.updatePlayers(clistener);
	}
	
	public void updateEnemies(ICharacterListener clistener) {
		m_game.updateEnemies(clistener);
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
		boolean hasLineOfSight = canSee(selectedSoldier, fireTarget);
		return selectedSoldier.fireAt(fireTarget, hasLineOfSight);
	}


	public List<Soldier> canSee(Enemy e) {
		List<Soldier> aliveSoldiers = m_game.getAliveSoldiers();
		
		List<Soldier> soldiersThatCanSee = new ArrayList<Soldier>();
		
		for (Soldier s : aliveSoldiers) {
			if ( canSee(s, e) ) {
				soldiersThatCanSee.add(s);
				
			}
		}
		return soldiersThatCanSee;
	}
	
	public Enemy getClosestEnemyThatWeCanSee(Soldier selectedSoldier) {
		float minDist = Float.MAX_VALUE;
		Enemy target = null;
		List<Enemy> aliveEnemies = m_game.getAliveEnemies();
		for (Enemy e : aliveEnemies) {
			if ( canSee(selectedSoldier, e) ) {
				float distance = selectedSoldier.getPosition().sub(e.getPosition()).length();
				if (distance < minDist) {
					minDist = distance;
					target = e;
				}
				
			}
		}
		
		return target;
	}
	
	public boolean canSee(Soldier soldier, Enemy enemy) {
		return m_game.m_level.lineOfSight(soldier.getPosition().toCenterTileVector(), 
										    enemy.getPosition().toCenterTileVector() ) ;
	}


	public void doWait(Soldier selectedSoldier) {
		selectedSoldier.m_timeUnits = 0;
	}


	public IIsMovePossible getMovePossible() {
		return m_game;
	}


	


	



	
}
