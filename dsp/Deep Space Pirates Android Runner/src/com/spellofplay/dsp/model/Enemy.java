package com.spellofplay.dsp.model;

import java.util.HashMap;
import java.util.Map;

public class Enemy extends Character{

	class EnemyMemory {
		private Map<Soldier, ModelPosition> m_soldiersLastPositions = new HashMap<Soldier, ModelPosition>(); 
		
		public void updateSights(CharacterCollection<Soldier> soldiers, IMoveAndVisibility a_moveAndVisibility) { 
			
			CharacterCollection<Soldier> soldiersThatEnemyCanSee = soldiers.thatCanSee(a_moveAndVisibility, Enemy.this);
			
			for (Soldier soldier : soldiersThatEnemyCanSee) {
				m_soldiersLastPositions.put(soldier, soldier.getPosition() );
			}
		}

		//TODO yet another distance calculationon a soldier array
		public Soldier getClosestSoldierSpotted() {
			float distance = Float.MAX_VALUE;
			Soldier closest = null;
			
			for (Soldier soldier : m_soldiersLastPositions.keySet()) {
				if (soldier.getHitpoints() > 0) {
					float dist = soldier.distance(Enemy.this);
					if (dist < distance) {
						distance = dist;
						closest = soldier;
					}
				}
			}
			return closest;
		}
	}
	
	EnemyMemory m_memory = new EnemyMemory();
	
	public Enemy(ModelPosition startPosition) {
		super(startPosition, 5);

	}
	
	public void updateSights(CharacterCollection<Soldier> soldiers, IMoveAndVisibility moveAndVisibility) { 
		m_memory.updateSights(soldiers, moveAndVisibility);
	}
	
	public Soldier getClosestSoldierSpotted() {
		return m_memory.getClosestSoldierSpotted();
	}

	

	

	public void removeTimeUnit() {
		m_timeUnits--;
		
	}
	
	public float getFireSkill() {
		return 0.7f;
	}

	public float getDodgeSkill() {
		return 0.3f;
	}
	
	@Override
	public float getRange() {
		return 6.0f;
	}
	
	
	public void stopMoving() {
		m_pathFinder = null;
	}
	
	public boolean isDoingSomething() {
		return m_pathFinder != null;
	}

	public boolean isSearching() {
		return m_pathFinder != null && m_pathFinder.isSearching();
		
	}

	public boolean isMoving() {
		return m_pathFinder != null && m_pathFinder.m_path.size() > 0;
	}

	public boolean didSearchFail() {
		return m_pathFinder != null && m_pathFinder.didSearchFail();
	}

}
