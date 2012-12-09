package com.spellofplay.dsp.model;


public interface IModel {

	public abstract String getGameTitle();

	public abstract TileType getTile(int x, int y);

	public abstract boolean isSoldierTime();

	public abstract boolean isEnemyTime();

	public abstract CharacterIterable getAliveSoldiers();

	public abstract CharacterIterable getAliveEnemies();

	public abstract CharacterIterable getDeadEnemies();

	public abstract boolean enemyHasWon();

	public abstract boolean playerHasWon();

	public abstract boolean canShoot(ICharacter soldier, ICharacter enemy);

	public abstract boolean canSeeMapPosition(ICharacter soldier, ModelPosition modelPosition);

	public abstract CharacterIterable canSee(ICharacter enemy);

	public abstract ICharacter getClosestEnemyThatWeCanShoot(ICharacter selectedSoldier);

	public abstract IMoveAndVisibility getMovePossible();

	public abstract CharacterIterable couldShootIfHadTime(ICharacter enemy);

	public abstract boolean canMove(ModelPosition clickOnLevelPosition);

	public abstract boolean hasDoorCloseToIt(ICharacter m_selectedSoldier);

}