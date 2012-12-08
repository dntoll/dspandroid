package com.spellofplay.dsp.model;

public interface IMoveAndVisibility {

	boolean isMovePossible(ModelPosition pos, boolean a_canMoveThroughObstacles);
	boolean lineOfSight(ICharacter cha1, ICharacter cha2);
	boolean hasClearSight(ICharacter character, ICharacter fireTarget);
	boolean lineOfSight(ModelPosition cha1, ModelPosition cha2);
	boolean targetHasCover(ICharacter attacker, ICharacter target);
	boolean hasClearSight(ModelPosition from, ModelPosition to);
	
}
