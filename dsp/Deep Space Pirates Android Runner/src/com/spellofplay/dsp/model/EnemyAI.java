package com.spellofplay.dsp.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnemyAI {
	
	class EnemyMemory {
		Map<Soldier, ModelPosition> m_soldiersLastPositions = new HashMap<Soldier, ModelPosition>(); 
	}
	
	Map<Enemy, EnemyMemory> m_memories = new HashMap<Enemy, EnemyMemory>();
	

	public void think(List<Enemy> enemies, List<Soldier> soldiers, IMoveAndVisibility a_moveAndVisibility, MultiCharacterListener a_clistener, MultiMovementListeners movementListeners) {
		
		
		for (Enemy enemy : enemies) {
			EnemyMemory enemyMemory = m_memories.get(enemy);
			
			if (enemyMemory == null) {
				enemyMemory = new EnemyMemory();
				m_memories.put(enemy, enemyMemory);
			}
			
			for (Soldier soldier : soldiers) {
				if (a_moveAndVisibility.lineOfSight(enemy, soldier)) {
					enemyMemory.m_soldiersLastPositions.put(soldier, soldier.getPosition() );
				}
			}
			
			if (enemy.getTimeUnits() > 0) {
				if (enemy.isSearching() ) {
					enemy.search();
					a_clistener.enemyAILog("is searching");
					break;
				} else if (enemy.isMoving()) {
					enemy.move(a_clistener, movementListeners, a_moveAndVisibility);
					a_clistener.enemyAILog("is moving");
					break;
				} else if (enemy.didSearchFail()) {
					enemy.doWatch();
					a_clistener.enemyAILog("failed search do Watch");
					break;
				} else {
					
					Soldier closest = getClosestSoldier(enemy, soldiers, a_moveAndVisibility);
					
					if (closest != null) {
						if (RuleBook.couldFireIfHadTime(enemy, closest, a_moveAndVisibility) == true) {
						
							if (RuleBook.canFireAt(enemy, closest, a_moveAndVisibility) == true) {
								enemy.fireAt(closest, a_moveAndVisibility, a_clistener);
							} else {
								enemy.m_timeUnits--;
							}
							
						} else {
							
							
							enemy.setDestination(closest.getPosition(), a_moveAndVisibility, 1.0f, false);
							a_clistener.enemyAILog("set destination");
						}
					} else {
						enemy.doWatch();
						a_clistener.enemyAILog("no visible soldiers, watch");
					}
					break; 
				}
			}
		}
		
	}

	private Soldier getClosestSoldier(Enemy enemy, List<Soldier> soldiers, IMoveAndVisibility a_moveAndVisibility) {
		Soldier closest = null;
		
		float closestDistance = Float.MAX_VALUE;
		for (Soldier s : soldiers) {
			if (a_moveAndVisibility.lineOfSight(enemy, s)) {
				float distance  = s.getPosition().sub(enemy.getPosition()).length();
				if (distance < closestDistance) {
					closestDistance = distance;
					closest = s;
				}
			}
		}
		
		EnemyMemory enemyMemory = m_memories.get(enemy);
		if (enemyMemory != null) {
			for (Soldier s : soldiers) {
				ModelPosition lastSeenPosition = enemyMemory.m_soldiersLastPositions.get(s);
				
				if (lastSeenPosition != null) {
					float distance  = lastSeenPosition.sub(enemy.getPosition()).length();
					if (distance < closestDistance) {
						closestDistance = distance;
						closest = s;
					}
				}
			}
		}
		
		return closest;
	}

}
