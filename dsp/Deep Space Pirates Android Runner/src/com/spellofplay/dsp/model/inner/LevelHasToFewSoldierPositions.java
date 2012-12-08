package com.spellofplay.dsp.model.inner;

class LevelHasToFewSoldierPositions extends Exception {
	private static final long serialVersionUID = -5890604369664821722L;

	LevelHasToFewSoldierPositions(String string) {
		super(string);
	}
}
