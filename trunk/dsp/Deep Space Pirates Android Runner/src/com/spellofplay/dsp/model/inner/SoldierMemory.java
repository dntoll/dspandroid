package com.spellofplay.dsp.model.inner;

import java.util.ArrayList;
import com.spellofplay.dsp.model.IMoveAndVisibility;

class SoldierMemory {

	private CharacterCollection<Enemy> previouslyObservedEnemies = new CharacterCollection<Enemy>(new ArrayList<Enemy>());
	
	boolean seeNewEnemies(CharacterCollection<Soldier> soldiers,	CharacterCollection<Enemy> enemies, IMoveAndVisibility visibility) {
		CharacterCollection<Enemy> observedEnemies = enemies.selectThoseThatCanSeenBy(soldiers, visibility);
		if (previouslyObservedEnemies.containsAll(observedEnemies)) {
			return false;
		}
		return true;
	}

	void updateSights(CharacterCollection<Soldier> soldiers,	CharacterCollection<Enemy> enemies, IMoveAndVisibility visibility) {
		
		CharacterCollection<Enemy> observedEnemies = enemies.selectThoseThatCanSeenBy(soldiers, visibility);
		previouslyObservedEnemies.addAll(observedEnemies);
	}

}
