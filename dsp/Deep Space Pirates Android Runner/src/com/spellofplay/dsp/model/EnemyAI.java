package com.spellofplay.dsp.model;

import java.util.List;

public class EnemyAI {

	public void think(List<Enemy> enemies, List<Soldier> soldiers, IMoveAndVisibility a_checker, ICharacterListener clistener) {
		
		
		int index = 0;
		for (Enemy e : enemies) {
			index++;
			//Is doing something
			if (e.isDoingSomething()) {
				//if search or move failed remove timeunits...
				if (e.update(a_checker, clistener) == false) {
					
					e.removeTimeUnit();
				}
				break;
			} else if (e.getTimeUnits() > 0) {
				//attack closest soldier
				Soldier closest = getClosest(e.getPosition(), soldiers);
				
				
				float distance = e.getPosition().sub(closest.getPosition()).length();
				
				if (distance > 1.0f) {
					e.setDestination(closest.getPosition(), a_checker, 1.0f, true);
				} else {
					if (e.fireAt(closest, a_checker) == false) {
						e.removeTimeUnit();
					}
				}
				break; //en i taget
			}
		}
		
	}

	private Soldier getClosest(ModelPosition a_position, List<Soldier> soldiers) {
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
