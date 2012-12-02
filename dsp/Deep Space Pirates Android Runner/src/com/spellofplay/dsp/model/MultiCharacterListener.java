package com.spellofplay.dsp.model;

import java.util.ArrayList;
import java.util.List;


public class MultiCharacterListener implements ICharacterListener{

	List<ICharacterListener> m_listeners = new ArrayList<ICharacterListener>();
	
	@Override
	public void moveTo(Character character) {
		for (ICharacterListener l : m_listeners) {
			l.moveTo(character);
		}
	}

	@Override
	public void fireAt(Character attacker, Character fireTarget, boolean didHit) {
		for (ICharacterListener l : m_listeners) {
			l.fireAt(attacker, fireTarget, didHit);
		}
	}

	public void addListener(ICharacterListener a_view) {
		m_listeners.add(a_view);
		
	}

	@Override
	public void cannotFireAt(Character character, Character fireTarget) {
		for (ICharacterListener l : m_listeners) {
			l.cannotFireAt(character, fireTarget);
		}
	}

	@Override
	public void enemyAILog(String string) {
		for (ICharacterListener l : m_listeners) {
			l.enemyAILog(string);
		}
	}

}
