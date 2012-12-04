package com.spellofplay.dsp.model;

import java.util.ArrayList;
import java.util.List;


//TODO maybe this is also a charactercollection?
public class MultiMovementListeners {

	List<Character> listeners = new ArrayList<Character>();
	public void addListener(Character character) {
		listeners.add(character);
	}

	public void moveTo(Character mover, IMoveAndVisibility moveAndVisibility, ICharacterListener a_listener) {
		for (Character watcher : listeners) {
			watcher.watchMovement(mover, moveAndVisibility, a_listener);
		}
		
	}


}
