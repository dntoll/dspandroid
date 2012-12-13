package com.spellofplay.dsp.model.inner;


import java.util.HashMap;
import java.util.Map;

import com.spellofplay.dsp.model.IMoveAndVisibility;
import com.spellofplay.dsp.model.ModelPosition;

class Enemy extends Character {

	private Map<Soldier, ModelPosition> m_soldiersLastPositions = new HashMap<Soldier, ModelPosition>();
	
	
	private static int[] values = {4,4,4,4};
	Enemy(ModelPosition startPosition) {
		super(startPosition, new SkillSet(values));
		hitPoints = 2;

	}
	
	@Override
	public float getRange() {
		return 12.0f;
	}
	
	
	void updateSights(CharacterCollection<Soldier> soldiers, CharacterCollection<Enemy> friends, IMoveAndVisibility a_moveAndVisibility) { 
		CharacterCollection<Soldier> soldiersThatEnemyCanSee = soldiers.thatCanSee(a_moveAndVisibility, this);
		CharacterCollection<Enemy> friendsThatCanSeeMe = friends.thatCanSee(a_moveAndVisibility, this);
		
		
		for (Soldier soldier : soldiersThatEnemyCanSee) {
			spot(soldier);
		
			for (Enemy friend : friendsThatCanSeeMe) {
				friend.spot(soldier);
			}
		}
	}

	private void spot(Soldier soldier) {
		m_soldiersLastPositions.put(soldier, soldier.getPosition() );
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
