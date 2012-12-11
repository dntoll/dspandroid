package com.spellofplay.dsp.model;

import java.util.ArrayList;
import java.util.List;


//TODO maybe this is also a charactercollection?
public class MultiMovementListeners {

	private List<ICharacter> listeners = new ArrayList<ICharacter>();
	public void addListener(ICharacter character) {
		listeners.add(character);
	}

	public void moveTo(ICharacter mover, IMoveAndVisibility moveAndVisibility, ICharacterListener a_listener) {
		for (ICharacter watcher : listeners) {
			com.spellofplay.dsp.model.inner.Character watch = (com.spellofplay.dsp.model.inner.Character)watcher;
			watch.watchMovement(mover, moveAndVisibility, a_listener);
		}
	}
}
