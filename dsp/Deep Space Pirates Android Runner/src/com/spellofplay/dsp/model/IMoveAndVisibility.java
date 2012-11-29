package com.spellofplay.dsp.model;

public interface IMoveAndVisibility {

	boolean isMovePossible(ModelPosition pos, boolean a_canMoveThroughObstacles);

	

	boolean lineOfSight(Character cha1, Character cha2);



	boolean targetHasCover(Character attacker, Character target);
}
