package com.spellofplay.dsp.model;

import java.util.ArrayList;
import java.util.List;

import com.spellofplay.NotImplementedException;
import com.spellofplay.dsp.model.AStar.SearchResult;

public class Game {

	public static final int MAX_SOLDIERS = 4;
	public static final int MAX_ENEMIES = 4;
	Soldier[] m_soldiers = new Soldier[MAX_SOLDIERS];
	Enemy[] m_enemies = new Enemy[MAX_ENEMIES];
	Level m_level = new Level();
	
	public Game() {
		m_soldiers[0] = new Soldier(new ModelPosition(5,5));
		m_soldiers[1] = new Soldier(new ModelPosition(10,5));
	}
	
	public List<Soldier> getAliveSoldiers() {
		
		List<Soldier> ret = new ArrayList<Soldier>();
		for (Soldier s : m_soldiers) {
			if (s != null) {
				ret.add(s);
			}
		}
		
		return ret;
	}
	
	public List<Enemy>  getAliveEnemies() {
		List<Enemy> ret = new ArrayList<Enemy>();
		for (Enemy s : m_enemies) {
			if (s != null) {
				ret.add(s);
			}
		}
		
		return ret;
	}

	public void startLevel(int a_level) {
		
		m_level.loadLevel(a_level);
		for (int i = 0; i < MAX_SOLDIERS; i++) {
			if (m_soldiers[i] != null) {
				m_soldiers[i].reset(m_level.getStartLocation(i));
			}
		}
		
		for (int i = 0; i < MAX_ENEMIES; i++) {
			m_enemies[i] = m_level.getEnemy(i);
		}
		
	}

	
	public void doMoveTo(Soldier selectedSoldier, ModelPosition destination) {
		selectedSoldier.setDestination(destination, m_level);
		
	}

	public void updatePlayers() {
		List<Soldier> soldiers = getAliveSoldiers();
		for (Soldier s : soldiers) {
			s.update();
		}

	}
	
	public void updateEnemies() {
		List<Enemy> enemies = getAliveEnemies();
		for (Enemy s : enemies) {
			s.update();
		}
		
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

	

}
