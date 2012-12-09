package com.spellofplay.dsp.model.inner;

import java.util.ArrayList;
import java.util.List;

import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.ICharacterListener;
import com.spellofplay.dsp.model.IMoveAndVisibility;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.MultiMovementListeners;
import com.spellofplay.dsp.model.Preferences;
import com.spellofplay.dsp.model.TileType;
import com.spellofplay.dsp.model.inner.levelgenerator.LevelGenerator;

public class Game implements IMoveAndVisibility {

	private Soldier[] m_soldiers = new Soldier[Preferences.MAX_SOLDIERS];
	private Enemy[] m_enemies = new Enemy[Preferences.MAX_ENEMIES];
	private Level m_level = new Level();
	
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

	private <T extends ICharacter> List<T> getCharacters(T[] list, boolean isAlive) {
		List<T> ret = new ArrayList<T>();
		for (T s : list) {
			if (s != null) {
				if (isAlive) {
					if (s.getHitPoints() > 0) {
						ret.add(s);	
					}
				} else {
					if (s.getHitPoints() <= 0) {
						ret.add(s);	
					}
				}
			}
		}
		return ret;
	}

	public void startLevel(int a_level) {
		for (int i = 0; i < Preferences.MAX_SOLDIERS; i++) {
			m_soldiers[i] = new Soldier(new ModelPosition(5,5));
		}
		
		LevelGenerator gen = new LevelGenerator(a_level);
		
		gen.generate(m_level);

		try {
			for (int i = 0; i < Preferences.MAX_SOLDIERS; i++) {
				if (m_soldiers[i] != null) {
					m_soldiers[i].reset(m_level.getStartLocation(i));
				} else {
					m_soldiers[i] = null;
				}
			}
		} catch (LevelHasToFewSoldierPositions e) {
			
		}
		
		try {
			for (int i = 0; i < Preferences.MAX_ENEMIES; i++) {
				m_enemies[i] = new Enemy(m_level.getEnemyStartLocation(i));
			}
		} catch (LevelHasToFewEnemiesException e) {
			
		} 
		
	}

	
	public void doMoveTo(ICharacter selectedSoldier, ModelPosition destination) {
		
		Character soldier = (Character)selectedSoldier;
		
		soldier.setDestination(destination, this, 0.0f);
		
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
	
	private SoldierMemory m_soldierMemory = new SoldierMemory();
	
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
	public boolean isMovePossible(ModelPosition pos) {
		if (m_level.canMove(pos) == false)
			return false;
		
		if (isOccupied(pos)) {
			return false;
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
	public boolean lineOfSight(ICharacter cha1, ICharacter cha2) {
		if (m_level.lineOfSight(cha1.getPosition(), cha2.getPosition()) == false) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean hasClearSight(ICharacter cha1, ICharacter cha2) {
		
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
	public boolean targetHasCover(ICharacter attacker, ICharacter target) {
		
		return m_level.hasCoverFrom(target.getPosition(), attacker.getPosition().sub(target.getPosition()));
	}

	public TileType getTile(int x, int y) {
		return m_level.GetTile(x, y);
	}

	public boolean canMove(ModelPosition clickOnLevelPosition) {
		return m_level.canMove(clickOnLevelPosition);
	}

	public boolean hasDoorCloseToIt(ModelPosition position) {
		return m_level.hasDoorCloseToIt(position);
	}

	public void open(ModelPosition position) {
		m_level.open(position);
		
	}

	

	

	

		
}
