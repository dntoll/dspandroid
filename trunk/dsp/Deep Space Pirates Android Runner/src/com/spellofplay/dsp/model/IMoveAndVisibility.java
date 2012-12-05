package com.spellofplay.dsp.model;

public interface IMoveAndVisibility {

	boolean isMovePossible(ModelPosition pos, boolean a_canMoveThroughObstacles);

	

	boolean lineOfSight(Character cha1, Character cha2);
	boolean hasClearSight(Character cha1, Character cha2);
	boolean lineOfSight(ModelPosition cha1, ModelPosition cha2);



	boolean targetHasCover(Character attacker, Character target);



	boolean hasClearSight(ModelPosition from, ModelPosition to);
	
}
