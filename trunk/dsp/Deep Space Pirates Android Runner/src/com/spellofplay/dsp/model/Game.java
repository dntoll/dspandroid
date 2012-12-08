package com.spellofplay.dsp.model;

import java.util.ArrayList;
import java.util.List;

import com.spellofplay.dsp.model.levelgenerator.LevelGenerator;

public class Game implements IMoveAndVisibility {

	public static final int MAX_SOLDIERS = 3;
	public static final int MAX_ENEMIES = 10;
	Soldier[] m_soldiers = new Soldier[MAX_SOLDIERS];
	Enemy[] m_enemies = new Enemy[MAX_ENEMIES];
	Level m_level = new Level();
	
	public Game() {
		startLevel(0);
	}
	
	public CharacterCollection<Soldier> getAliveSoldiers() {
		return new CharacterCollection<Soldier>(getCharacters(m_soldiers, true));
	}
	
	public CharacterCollection<Enemy>  getAliveEnemies() {
		return new CharacterCollection<Enemy>(getCharacters(m_enemies, true));
	}
	
	public CharacterCollection<Enemy> getDeadEnemies() {
		return new CharacterCollection<Enemy>(getCharacters(m_enemies, false));
	}

	private <T extends Character> List<T> getCharacters(T[] list, boolean isAlive) {
		List<T> ret = new ArrayList<T>();
		for (T s : list) {
			if (s != null) {
				if (isAlive) {
					if (s.m_hitpoints > 0) {
						ret.add(s);	
					}
				} else {
					if (s.m_hitpoints <= 0) {
						ret.add(s);	
					}
				}
			
				
			}
		}
		
		return ret;
	}

	public void startLevel(int a_level) {
		for (int i = 0; i < MAX_SOLDIERS; i++) {
			m_soldiers[i] = new Soldier(new ModelPosition(5,5));
		}
		
		LevelGenerator gen = new LevelGenerator(a_level);
		
		gen.generate(m_level);

		try {
			for (int i = 0; i < MAX_SOLDIERS; i++) {
				if (m_soldiers[i] != null) {
					m_soldiers[i].reset(m_level.getStartLocation(i));
				} else {
					m_soldiers[i] = null;
				}
			}
		} catch (LevelHasToFewSoldierPositions e) {
			
		}
		
		try {
			for (int i = 0; i < MAX_ENEMIES; i++) {
				m_enemies[i] = new Enemy(m_level.getEnemyStartLocation(i));
			}
		} catch (LevelHasToFewEnemiesException e) {
			
		} 
		
	}

	
	public void doMoveTo(Soldier selectedSoldier, ModelPosition destination) {
		selectedSoldier.setDestination(destination, this, 0.0f, false);
		
	}

	public void movePlayers(ICharacterListener clistener) {
		
		
		MultiMovementListeners multiListener= getSoldierListeners();
		
		CharacterCollection<Soldier> soldiers = getAliveSoldiers();
		
		
		for (Soldier s : soldiers) {
			s.getPathFinder().search();

			
			s.move(clistener, multiListener, this);
			
		}

		updateEnemySights();
		updateSoldierSights();
	}
	
	SoldierMemory m_soldierMemory = new SoldierMemory();
	
	private void updateSoldierSights() {
		CharacterCollection<Enemy> enemies = getAliveEnemies();
		CharacterCollection<Soldier> soldiers = getAliveSoldiers();
		if (m_soldierMemory.seeNewEnemies(soldiers, enemies, this) )
		{
			soldiers.stopAllMovement();
			m_soldierMemory.updateSights(soldiers, enemies, this);
		}
		
	}

	private void updateEnemySights() {
		CharacterCollection<Enemy> enemies = getAliveEnemies();
		CharacterCollection<Soldier> soldiers = getAliveSoldiers();
		for (Enemy enemy : enemies) {
			enemy.updateSights(soldiers, this);
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

	
	public void updateEnemies(ICharacterListener clistener) {
		CharacterCollection<Enemy> enemies = getAliveEnemies();
		CharacterCollection<Soldier> soldiers = getAliveSoldiers();
		MultiMovementListeners multiListener= getEnemyListeners();
		EnemyAI ai = new EnemyAI();
		ai.think(enemies, soldiers, this, clistener, multiListener);
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
