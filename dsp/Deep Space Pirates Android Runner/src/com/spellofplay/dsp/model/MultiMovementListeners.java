package com.spellofplay.dsp.model;

import java.util.ArrayList;
import java.util.List;

import com.spellofplay.dsp.model.inner.Character;


//TODO maybe this is also a charactercollection?
public class MultiMovementListeners {

	List<ICharacter> listeners = new ArrayList<ICharacter>();
	public void addListener(ICharacter character) {
		listeners.add(character);
	}

	public void moveTo(Character mover, IMoveAndVisibility moveAndVisibility, ICharacterListener a_listener) {
		for (ICharacter watcher : listeners) {
			watcher.watchMovement(mover, moveAndVisibility, a_listener);
		}
	}
}
