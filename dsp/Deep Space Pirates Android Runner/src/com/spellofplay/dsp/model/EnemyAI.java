package com.spellofplay.dsp.model;

import java.util.List;

public class EnemyAI {

	public void think(List<Enemy> enemies, List<Soldier> soldiers, IMoveAndVisibility a_moveAndVisibility, MultiCharacterListener a_clistener, MultiMovementListeners movementListeners) {
		
		
		int index = 0;
		for (Enemy e : enemies) {
			index++;
			
			if (e.getTimeUnits() > 0) {
				if (e.isSearching() ) {
					e.search();
					a_clistener.enemyAILog("is searching");
					break;
				} else if (e.isMoving()) {
					//if search or move failed remove timeunits...
					e.move(a_clistener, movementListeners, a_moveAndVisibility);
					a_clistener.enemyAILog("is moving");
					break;
				} else if (e.didSearchFail()) {
					e.m_timeUnits--;
					a_clistener.enemyAILog("failed search");
					break;
				} else {
					
					Soldier closest = getClosestSoldier(e.getPosition(), soldiers);
					
					if (RuleBook.couldFireIfHadTime(e, closest, a_moveAndVisibility) == true) {
					
						if (RuleBook.canFireAt(e, closest, a_moveAndVisibility) == true) {
							e.fireAt(closest, a_moveAndVisibility, a_clistener);
						} else {
							e.m_timeUnits--;
						}
						
					} else {
						
						
						e.setDestination(closest.getPosition(), a_moveAndVisibility, 1.0f, false);
						a_clistener.enemyAILog("set destination");
					}
					break; 
				}
			}
		}
		
	}

	private Soldier getClosestSoldier(ModelPosition a_position, List<Soldier> soldiers) {
		Soldier closest = null;
		
		float closestDistance = Float.MAX_VALUE;
		for (Soldier s : soldiers) {
			float distance  = s.getPosition().sub(a_position).length();
			if (distance < closestDistance) {
				closestDistance = distance;
				closest = s;
			}
		}
		
		return closest;
	}

}
