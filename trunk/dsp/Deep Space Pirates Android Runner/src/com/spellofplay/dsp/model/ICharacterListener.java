package com.spellofplay.dsp.model;

public interface ICharacterListener {

	
	void fireAt(ICharacter attacker, ICharacter fireTarget, boolean didHit);

	void cannotFireAt(ICharacter character, ICharacter fireTarget);

	void enemyAILog(String string, ICharacter enemy);

	void moveTo(ICharacter character);

	void soldierSpotsNewEnemy();

	void enemySpotsNewSoldier();

}
