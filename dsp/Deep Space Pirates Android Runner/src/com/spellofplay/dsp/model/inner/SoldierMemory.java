package com.spellofplay.dsp.model.inner;

import java.util.ArrayList;
import java.util.List;

import com.spellofplay.dsp.model.IMoveAndVisibility;

public class SoldierMemory {

	CharacterCollection<Enemy> previouslyObservedEnemies = new CharacterCollection<Enemy>(new ArrayList<Enemy>());
	
	public boolean seeNewEnemies(CharacterCollection<Soldier> soldiers,	CharacterCollection<Enemy> enemies, IMoveAndVisibility visibility) {
		CharacterCollection<Enemy> observedEnemies = enemies.selectThoseThatCanSeenBy(soldiers, visibility);
		if (previouslyObservedEnemies.containsAll(observedEnemies)) {
			return false;
		}
		return true;
	}

	public void updateSights(CharacterCollection<Soldier> soldiers,	CharacterCollection<Enemy> enemies, IMoveAndVisibility visibility) {
		
		CharacterCollection<Enemy> observedEnemies = enemies.selectThoseThatCanSeenBy(soldiers, visibility);
		previouslyObservedEnemies.addAll(observedEnemies);
	}

}
