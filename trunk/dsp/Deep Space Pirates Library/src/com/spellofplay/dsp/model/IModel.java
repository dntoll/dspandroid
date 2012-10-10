package com.spellofplay.dsp.model;

import java.util.List;

import android.graphics.Point;

public interface IModel {

	public abstract String getGameTitle();

	public abstract Level getLevel();

	public abstract boolean hasUnfinishedActions();

	public abstract boolean isEnemyTime();

	public abstract void updatePlayers();

	public abstract void updateEnemies();

	public abstract void doMoveTo(Soldier selectedSoldier, com.spellofplay.dsp.model.ModelPosition destination);

	public abstract List<Soldier> getAliveSoldiers();

	public abstract void startNewGame(int a_level);

	public abstract void startNewRound();

	public abstract boolean enemyHasWon();

	public abstract boolean playerHasWon();

}