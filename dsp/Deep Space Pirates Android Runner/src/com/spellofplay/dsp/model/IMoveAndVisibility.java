package com.spellofplay.dsp.model;

import com.spellofplay.dsp.model.inner.MultiMovementListeners;

public interface IMoveAndVisibility {
	boolean lineOfSight(ICharacter cha1, ICharacter cha2);
	boolean lineOfSight(ModelPosition cha1, ModelPosition cha2);
	
	boolean hasClearSight(ICharacter character, ICharacter fireTarget);
	boolean hasClearSight(ModelPosition from, ModelPosition to);

	boolean targetHasCover(ICharacter attacker, ICharacter target);
	
	boolean isMovePossible(ModelPosition pos);
	
	void moveCharacter(ICharacterListener clistener, MultiMovementListeners multiListener, ICharacter character);
}
