package com.spellofplay.dsp.model;

public interface ICharacterListener {

	void moveTo(Character character);

	void fireAt(Character attacker, Character fireTarget, boolean didHit);

	void cannotFireAt(Character character, Character fireTarget);

	void enemyAILog(String string);

}
