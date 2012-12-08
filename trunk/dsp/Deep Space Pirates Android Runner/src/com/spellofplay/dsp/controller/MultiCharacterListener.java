package com.spellofplay.dsp.controller;

import java.util.ArrayList;
import java.util.List;
import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.ICharacterListener;


public class MultiCharacterListener implements ICharacterListener{

	List<ICharacterListener> m_listeners = new ArrayList<ICharacterListener>();
	
	@Override
	public void moveTo(ICharacter character) {
		for (ICharacterListener l : m_listeners) {
			l.moveTo(character);
		}
	}

	@Override
	public void fireAt(ICharacter attacker, ICharacter fireTarget, boolean didHit) {
		for (ICharacterListener l : m_listeners) {
			l.fireAt(attacker, fireTarget, didHit);
		}
	}

	public void addListener(ICharacterListener a_view) {
		m_listeners.add(a_view);
		
	}

	@Override
	public void cannotFireAt(ICharacter character, ICharacter fireTarget) {
		for (ICharacterListener l : m_listeners) {
			l.cannotFireAt(character, fireTarget);
		}
	}


	@Override
	public void enemyAILog(String string, ICharacter enemy) {
		for (ICharacterListener l : m_listeners) {
			l.enemyAILog(string, enemy);
		}
	}

}
