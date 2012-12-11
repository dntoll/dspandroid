package com.spellofplay.dsp.model;

public interface IEventTarget {

	public abstract void updatePlayers(ICharacterListener clistener);

	public abstract void updateEnemies(ICharacterListener clistener);

	public abstract void doMoveTo(ICharacter selectedSoldier, ModelPosition destination);

	public abstract void startNewGame();

	public abstract void startNewEnemyRound();

	public abstract void startNewSoldierRound();

	public abstract boolean fireAt(ICharacter selectedSoldier, ICharacter fireTarget, ICharacterListener listener);

	public abstract void doWatch(ICharacter selectedSoldier);

	public abstract void open(ICharacter selectedSoldier);

	public abstract void newLevel();

	public abstract void addTimeUnits(ICharacter soldier);

	public abstract void addShootSkill(ICharacter soldier);

	public abstract void addDodgeSkill(ICharacter soldier);

}