package com.spellofplay.dsp.model;

import java.util.ArrayList;
import java.util.List;

public class Game implements IMoveAndVisibility {

	public static final int MAX_SOLDIERS = 4;
	public static final int MAX_ENEMIES = 5;
	Soldier[] m_soldiers = new Soldier[MAX_SOLDIERS];
	Enemy[] m_enemies = new Enemy[MAX_ENEMIES];
	Level m_level = new Level();
	
	public Game() {
		startLevel(0);
	}
	
	public CharacterCollection<Soldier> getAliveSoldiers() {
		return new CharacterCollection<Soldier>(getCharacters(m_soldiers));
	}
	
	public CharacterCollection<Enemy>  getAliveEnemies() {
		return new CharacterCollection<Enemy>(getCharacters(m_enemies));
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

	public void movePlayers(ICharacterListener clistener) {
		
		
		MultiMovementListeners multiListener= getSoldierListeners();
		
		CharacterCollection<Soldier> soldiers = getAliveSoldiers();
		for (Soldier s : soldiers) {
			s.search();
			s.move(clistener, multiListener, this);
		}

		updateEnemySights(this);
	}
	
	
	private void updateEnemySights(IMoveAndVisibility moveAndVisibility) {
		CharacterCollection<Enemy> enemies = getAliveEnemies();
		CharacterCollection<Soldier> soldiers = getAliveSoldiers();
		for (Enemy enemy : enemies) {
			enemy.updateSights(soldiers, moveAndVisibility);
		}
	}

	private MultiMovementListeners getSoldierListeners() {
		CharacterCollection<Enemy> enemies = getAliveEnemies();
		return enemies.getMovementListeners();
	}
	
	private MultiMovementListeners getEnemyListeners() {
		
		CharacterCollection<Soldier> soldiers = getAliveSoldiers();
		return soldiers.getMovementListeners();
		
		
	}

	EnemyAI m_ai = new EnemyAI();
	public void updateEnemies(ICharacterListener clistener) {
		CharacterCollection<Enemy> enemies = getAliveEnemies();
		CharacterCollection<Soldier> soldiers = getAliveSoldiers();
		MultiMovementListeners multiListener= getEnemyListeners();
		m_ai.think(enemies, soldiers, this, clistener, multiListener);
	}

	public void startNewSoldierRound() {
		CharacterCollection<Soldier> soldiers = getAliveSoldiers();
		soldiers.startNewRound();
		
	}
	public void startNewEnemyRound() {	
		CharacterCollection<Enemy> enemies = getAliveEnemies();
		enemies.startNewRound();
	}

	@Override
	public boolean isMovePossible(ModelPosition pos, boolean a_canMoveThroughObstacles) {
		if (m_level.canMove(pos) == false)
			return false;
		
		if (a_canMoveThroughObstacles == false) {
			if (isOccupied(pos)) {
				return false;
			}
		}
		
		return true;
	}

	private boolean isOccupied(ModelPosition pos) {
		CharacterCollection<Soldier> soldiers = getAliveSoldiers();
		if (soldiers.occupies(pos)) {
			return true;
		}
		CharacterCollection<Enemy> enemies = getAliveEnemies();
		if (enemies.occupies(pos)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean lineOfSight(ModelPosition pos1, ModelPosition pos2) {
		if (m_level.lineOfSight(pos1, pos2) == false) {
			return false;
		}
		return true;
	}

	@Override
	public boolean lineOfSight(Character cha1, Character cha2) {
		if (m_level.lineOfSight(cha1.getPosition(), cha2.getPosition()) == false) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean hasClearSight(Character cha1, Character cha2) {
		
		return hasClearSight(cha1.getPosition(), cha2.getPosition());
		/*if (lineOfSight(cha1, cha2) == false)
			return false;
		
		Line line = new Line(cha1.getPosition().toCenterTileVector(), cha2.getPosition().toCenterTileVector());
		
		CharacterCollection<Soldier> soldiers = getAliveSoldiers();
		soldiers.remove(cha1);
		soldiers.remove(cha2);
		
		if (soldiers.blocks(line))
			return false;
		
		CharacterCollection<Enemy> enemies = getAliveEnemies();
		enemies.remove(cha1);
		enemies.remove(cha2);
		
		if (enemies.blocks(line))
			return false;
		
		return true;*/
	}
	
	@Override
	public boolean hasClearSight(ModelPosition from, ModelPosition to) {
		if (lineOfSight(from, to) == false)
			return false;
		
		Line line = new Line(from.toCenterTileVector(), to.toCenterTileVector());
		
		CharacterCollection<Soldier> soldiers = getAliveSoldiers();
		soldiers.remove(from);
		soldiers.remove(to);
		
		if (soldiers.blocks(line))
			return false;
		
		CharacterCollection<Enemy> enemies = getAliveEnemies();
		enemies.remove(from);
		enemies.remove(to);
		
		if (enemies.blocks(line))
			return false;
		
		return true;
	}

	@Override
	public boolean targetHasCover(Character attacker, Character target) {
		
		return m_level.hasCoverFrom(target.getPosition(), attacker.getPosition().sub(target.getPosition()));
	}

	

	

		
}
