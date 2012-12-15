package com.spellofplay.dsp.model;


public interface ISkill {

	int getValue();
	boolean canImprove(Experience stash);
}
