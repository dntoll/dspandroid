package com.spellofplay.dsp.model.inner;

import com.spellofplay.dsp.model.ICharacterListener;
import com.spellofplay.dsp.model.IMoveAndVisibility;
import com.spellofplay.dsp.model.MultiMovementListeners;

public class EnemyAI {

	

	public void think(CharacterCollection<Enemy> enemies, CharacterCollection<Soldier> soldiers, IMoveAndVisibility a_moveAndVisibility, ICharacterListener a_clistener, MultiMovementListeners movementListeners) {
		
		
		for (Enemy enemy : enemies) {
			
			PathFinder pathFinder = enemy.getPathFinder();
			
			if (enemy.getTimeUnits() > 0) {
				if (pathFinder.isSearching() ) {
					pathFinder.search();
					a_clistener.enemyAILog("is searching", enemy);
				} else if (pathFinder.isMoving()) {
					enemy.move(a_clistener, movementListeners, a_moveAndVisibility);
					a_clistener.enemyAILog("is moving", enemy);
					
					stopIfSoldierInSight(soldiers, a_moveAndVisibility, enemy);
					
				} else if (pathFinder.didSearchFail()) {
					enemy.doWatch();
					a_clistener.enemyAILog("failed search do Watch", enemy);
				} else {
					decideWhatToDo(soldiers, a_moveAndVisibility, a_clistener, enemy);
				}
				break;
			}
		}
		
	}

	public void stopIfSoldierInSight(CharacterCollection<Soldier> soldiers,
			IMoveAndVisibility a_moveAndVisibility, Enemy enemy) {
		Soldier closestThatWeCanSee = getClosestSoldierThatWeCanSee(enemy, soldiers, a_moveAndVisibility);
		if (closestThatWeCanSee != null) {
			if (RuleBook.couldFireIfHadTime(enemy, closestThatWeCanSee, a_moveAndVisibility) == true) {
				enemy.getPathFinder().stopAllSearches();
			}
		}
	}

	public void decideWhatToDo(CharacterCollection<Soldier> soldiers, IMoveAndVisibility a_moveAndVisibility,	ICharacterListener a_clistener, Enemy enemy) {
		
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
				a_clistener.enemyAILog("set destination", enemy);
			}
		} else {
			
			Soldier closestThatWeHaveSeen = enemy.getClosestSoldierSpotted();
			
			if (closestThatWeHaveSeen != null) {
				enemy.setDestination(closestThatWeHaveSeen.getPosition(), a_moveAndVisibility, 1, false);
				a_clistener.enemyAILog("going for the once visible", enemy);
			} else {
				enemy.doWatch();
				a_clistener.enemyAILog("no visible soldiers, watch", enemy);
			}
		}
	}

	

	private Soldier getClosestSoldierThatWeCanSee(Enemy enemy, CharacterCollection<Soldier> soldiers, IMoveAndVisibility a_moveAndVisibility) {
		CharacterCollection<Soldier> soldiersThatEnemyCanSee = soldiers.thatCanSee(a_moveAndVisibility, enemy);
		return soldiersThatEnemyCanSee.getClosest(enemy.getPosition());
		
	}

}
