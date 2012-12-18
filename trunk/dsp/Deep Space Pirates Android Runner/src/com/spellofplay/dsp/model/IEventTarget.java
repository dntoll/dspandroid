package com.spellofplay.dsp.model;

import com.spellofplay.dsp.model.ISkillSet.SkillType;

public interface IEventTarget {

	void updatePlayers(ICharacterListener clistener);
	void updateEnemies(ICharacterListener clistener);

	void doMoveTo(ICharacter selectedSoldier, ModelPosition destination);

	void startNewGame();
	void startNewEnemyRound();
	void startNewSoldierRound();

	void fireAt(ICharacter selectedSoldier, ICharacter fireTarget, ICharacterListener listener);
	void doWatch(ICharacter selectedSoldier);
	void open(ICharacter selectedSoldier);
	void newLevel();
	void spendExperience(ICharacter soldier, SkillType skillType);
	void throwGrenade(ICharacter selectedSoldier, ModelPosition destination, ICharacterListener listener);

}