package com.spellofplay.dsp.model.inner;

import com.spellofplay.dsp.model.Experience;
import com.spellofplay.dsp.model.ISkill;

class Skill implements ISkill{
	private int value;
	
	Skill(int startValue) {
		value = startValue;
	}

	public int getValue() {
		return value;
	}
	public boolean canImprove(Experience stash) {
		return stash.experience >= value;
	}
	
	void improve(Experience stash) {
		if (canImprove(stash)) {
			stash.experience -= value;
			value += 1;
		}
	}
	
	public boolean equals(Skill other) {
		return value == other.value;
	}

	
}
