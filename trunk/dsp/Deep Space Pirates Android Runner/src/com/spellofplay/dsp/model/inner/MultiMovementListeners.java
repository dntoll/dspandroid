package com.spellofplay.dsp.model.inner;

import java.util.ArrayList;
import java.util.List;

import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.ICharacterListener;
import com.spellofplay.dsp.model.IMoveAndVisibility;


//TODO maybe this is also a charactercollection?
public class MultiMovementListeners {

	private List<ICharacter> listeners = new ArrayList<ICharacter>();
	void addListener(ICharacter character) {
		listeners.add(character);
	}

	void moveTo(ICharacter mover, IMoveAndVisibility moveAndVisibility, ICharacterListener a_listener) {
		for (ICharacter watcher : listeners) {
			com.spellofplay.dsp.model.inner.Character watch = (com.spellofplay.dsp.model.inner.Character)watcher;
			watch.watchMovement(mover, moveAndVisibility, a_listener);
		}
	}
}
