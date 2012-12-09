package com.spellofplay.dsp.controller;

import java.util.ArrayList;
import java.util.List;
import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.ICharacterListener;


class MultiCharacterListener implements ICharacterListener{

	private List<ICharacterListener> listeners = new ArrayList<ICharacterListener>();
	
	@Override
	public void moveTo(ICharacter character) {
		for (ICharacterListener l : listeners) {
			l.moveTo(character);
		}
	}

	@Override
	public void fireAt(ICharacter attacker, ICharacter fireTarget, boolean didHit) {
		for (ICharacterListener l : listeners) {
			l.fireAt(attacker, fireTarget, didHit);
		}
	}

	void addListener(ICharacterListener a_view) {
		listeners.add(a_view);
		
	}

	@Override
	public void cannotFireAt(ICharacter character, ICharacter fireTarget) {
		for (ICharacterListener l : listeners) {
			l.cannotFireAt(character, fireTarget);
		}
	}


	@Override
	public void enemyAILog(String string, ICharacter enemy) {
		for (ICharacterListener l : listeners) {
			l.enemyAILog(string, enemy);
		}
	}

}
