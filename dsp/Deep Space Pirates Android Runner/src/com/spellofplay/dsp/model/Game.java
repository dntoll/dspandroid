package com.spellofplay.dsp.model;

import java.util.ArrayList;
import java.util.List;

public class Game implements IIsMovePossible {

	public static final int MAX_SOLDIERS = 4;
	public static final int MAX_ENEMIES = 5;
	Soldier[] m_soldiers = new Soldier[MAX_SOLDIERS];
	Enemy[] m_enemies = new Enemy[MAX_ENEMIES];
	Level m_level = new Level();
	
	public Game() {
		startLevel(0);
	}
	
	public List<Soldier> getAliveSoldiers() {
		return getCharacters(m_soldiers);
	}
	
	public List<Enemy>  getAliveEnemies() {
		return getCharacters(m_enemies);
	}

	private <T extends Character> List<T> getCharacters(T[] a_list) {
		List<T> ret = new ArrayList<T>();
		for (T s : a_list) {
			if (s != null  && s.m_hitpoints > 0) {
				ret.add(s);
			}
		}
		
		return ret;
	}

	public void startLevel(int a_level) {
		for (int i = 0; i < MAX_SOLDIERS; i++) {
			m_soldiers[i] = new Soldier(new ModelPosition(5,5));
		}
		
		
		m_level.loadLevel(a_level);
		for (int i = 0; i < MAX_SOLDIERS; i++) {
			if (m_soldiers[i] != null && m_level.getStartLocation(i) != null) {
				m_soldiers[i].reset(m_level.getStartLocation(i));
			} else {
				m_soldiers[i] = null;
			}
		}
		
		for (int i = 0; i < MAX_ENEMIES; i++) {
			m_enemies[i] = m_level.getEnemy(i);
		}
		
	}

	
	public void doMoveTo(Soldier selectedSoldier, ModelPosition destination) {
		selectedSoldier.setDestination(destination, this, 0.0f, false);
		
	}

	public void updatePlayers() {
		List<Soldier> soldiers = getAliveSoldiers();
		for (Soldier s : soldiers) {
			s.update(this);
		}

	}
	
	EnemyAI m_ai = new EnemyAI();
	public void updateEnemies() {
		List<Enemy> enemies = getAliveEnemies();
		List<Soldier> soldiers = getAliveSoldiers();
		m_ai.think(enemies, soldiers, this);
	}

	public void startNewRound() {
		List<Soldier> soldiers = getAliveSoldiers();
		for (Soldier s : soldiers) {
			s.startNewRound();
		}
		
		List<Enemy> enemies = getAliveEnemies();
		for (Enemy s : enemies) {
			s.startNewRound();
		}
	}

	@Override
	public boolean isMovePossible(ModelPosition pos, boolean a_canMoveThroughObstacles) {
		if (m_level.isClear(pos) == false)
			return false;
		
		if (a_canMoveThroughObstacles == false) {
			if (isOccupied(pos)) {
				return false;
			}
		}
		
		return true;
	}

	private boolean isOccupied(ModelPosition pos) {
		List<Soldier> soldiers = getAliveSoldiers();
		for (Soldier s : soldiers) {
			if (s.getPosition().equals(pos))
				return true;
		}
		
		List<Enemy> enemies = getAliveEnemies();
		for (Enemy s : enemies) {
			if (s.getPosition().equals(pos))
				return true;
		}
		
		return false;
	}

	

	

	
}
