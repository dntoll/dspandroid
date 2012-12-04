package com.spellofplay.dsp.model;

import java.util.List;

public class EnemyAI {

	

	public void think(List<Enemy> enemies, List<Soldier> soldiers, IMoveAndVisibility a_moveAndVisibility, ICharacterListener a_clistener, MultiMovementListeners movementListeners) {
		
		
		for (Enemy enemy : enemies) {
			
			
			
			
			
			if (enemy.getTimeUnits() > 0) {
				if (enemy.isSearching() ) {
					enemy.search();
					a_clistener.enemyAILog("is searching");
					break;
				} else if (enemy.isMoving()) {
					enemy.move(a_clistener, movementListeners, a_moveAndVisibility);
					a_clistener.enemyAILog("is moving");
					
					stopIfSoldierInSight(soldiers, a_moveAndVisibility, enemy);
					break;
				} else if (enemy.didSearchFail()) {
					enemy.doWatch();
					a_clistener.enemyAILog("failed search do Watch");
					break;
				} else {
					
					decideWhatToDo(soldiers, a_moveAndVisibility, a_clistener, enemy);
					break; 
				}
			}
		}
		
	}

	public void stopIfSoldierInSight(List<Soldier> soldiers,
			IMoveAndVisibility a_moveAndVisibility, Enemy enemy) {
		Soldier closestThatWeCanSee = getClosestSoldierThatWeCanSee(enemy, soldiers, a_moveAndVisibility);
		if (closestThatWeCanSee != null) {
			if (RuleBook.couldFireIfHadTime(enemy, closestThatWeCanSee, a_moveAndVisibility) == true) {
				enemy.stopMoving();
			}
		}
	}

	public void decideWhatToDo(List<Soldier> soldiers, IMoveAndVisibility a_moveAndVisibility,	ICharacterListener a_clistener, Enemy enemy) {
		
		Soldier closestThatWeCanSee = getClosestSoldierThatWeCanSee(enemy, soldiers, a_moveAndVisibility);
		
		if (closestThatWeCanSee != null) {
			if (RuleBook.couldFireIfHadTime(enemy, closestThatWeCanSee, a_moveAndVisibility) == true) {
			
				if (RuleBook.canFireAt(enemy, closestThatWeCanSee, a_moveAndVisibility) == true) {
					enemy.fireAt(closestThatWeCanSee, a_moveAndVisibility, a_clistener);
				} else {
					enemy.m_timeUnits--;
					//hide would be better?
				}
				
			} else {
				enemy.setDestination(closestThatWeCanSee.getPosition(), a_moveAndVisibility, enemy.getRange(), false);
				a_clistener.enemyAILog("set destination");
			}
		} else {
			
			Soldier closestThatWeHaveSeen = enemy.getClosestSoldierSpotted(soldiers);
			
			if (closestThatWeHaveSeen != null) {
				enemy.setDestination(closestThatWeHaveSeen.getPosition(), a_moveAndVisibility, 1, false);
				a_clistener.enemyAILog("going for the once visible");
			} else {
				enemy.doWatch();
				a_clistener.enemyAILog("no visible soldiers, watch");
			}
		}
	}

	

	private Soldier getClosestSoldierThatWeCanSee(Enemy enemy, List<Soldier> soldiers, IMoveAndVisibility a_moveAndVisibility) {
		Soldier closest = null;
		
		float closestDistance = Float.MAX_VALUE;
		for (Soldier s : soldiers) {
			if (a_moveAndVisibility.lineOfSight(enemy, s)) 
			{
				float distance  = s.getPosition().sub(enemy.getPosition()).length();
				if (distance < closestDistance) {
					closestDistance = distance;
					closest = s;
				}
			}
		}
				
		return closest;
	}

}
