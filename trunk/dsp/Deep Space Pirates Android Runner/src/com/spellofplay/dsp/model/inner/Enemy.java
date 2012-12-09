package com.spellofplay.dsp.model.inner;


import java.util.HashMap;
import java.util.Map;

import com.spellofplay.dsp.model.IMoveAndVisibility;
import com.spellofplay.dsp.model.ModelPosition;

public class Enemy extends Character {

	private Map<Soldier, ModelPosition> m_soldiersLastPositions = new HashMap<Soldier, ModelPosition>();
	
	Enemy(ModelPosition startPosition) {
		super(startPosition, 5);
		hitPoints = 1;

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
	
	public void updateSights(CharacterCollection<Soldier> soldiers, IMoveAndVisibility moveAndVisibility) { 
		updateSights(this, soldiers, moveAndVisibility);
	}
	
	private void updateSights(Enemy observer, CharacterCollection<Soldier> soldiers, IMoveAndVisibility a_moveAndVisibility) { 
		CharacterCollection<Soldier> soldiersThatEnemyCanSee = soldiers.thatCanSee(a_moveAndVisibility, observer);
		
		for (Soldier soldier : soldiersThatEnemyCanSee) {
			m_soldiersLastPositions.put(soldier, soldier.getPosition() );
		}
	}
	
	public Soldier getClosestSoldierSpotted() {
		float distance = Float.MAX_VALUE;
		Soldier closest = null;
		
		for (Soldier soldier : m_soldiersLastPositions.keySet()) {
			if (soldier.getHitPoints() > 0) {
				float dist = soldier.distance(this);
				if (dist < distance) {
					distance = dist;
					closest = soldier;
				}
			}
		}
		return closest;
	}
}
