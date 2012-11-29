package com.spellofplay.dsp.model;

import java.util.List;

public class EnemyAI {

	public void think(List<Enemy> enemies, List<Soldier> soldiers, IMoveAndVisibility a_moveAndVisibility, ICharacterListener a_clistener) {
		
		
		int index = 0;
		for (Enemy e : enemies) {
			index++;
			//Is doing something
			if (e.isDoingSomething()) {
				//if search or move failed remove timeunits...
				if (e.update(a_moveAndVisibility, a_clistener) == false) {
					
					e.removeTimeUnit();
				}
				break;
			} else if (e.getTimeUnits() > 0) {
				//attack closest soldier
				Soldier closest = getClosest(e.getPosition(), soldiers);
				
				
				float distance = e.getPosition().sub(closest.getPosition()).length();
				
				if (RuleBook.canFireAt(e, closest, a_moveAndVisibility) == false) {
					e.setDestination(closest.getPosition(), a_moveAndVisibility, 1.0f, true);
				} else {
					e.fireAt(closest, a_moveAndVisibility, a_clistener);
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
