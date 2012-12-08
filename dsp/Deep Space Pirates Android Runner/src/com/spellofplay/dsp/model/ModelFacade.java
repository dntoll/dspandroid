package com.spellofplay.dsp.model;

import com.spellofplay.dsp.model.inner.CharacterCollection;
import com.spellofplay.dsp.model.inner.Enemy;
import com.spellofplay.dsp.model.inner.Game;
import com.spellofplay.dsp.model.inner.RuleBook;
import com.spellofplay.dsp.model.inner.Soldier;


public class ModelFacade implements IModel, IEventTarget {
	
	Game m_game = new Game();

	@Override
	public String getGameTitle() {
		return "Deep Space Pirates ";
	}

	@Override
	public TileType getTile(int x, int y) {
		return m_game.getTile(x,y);
	}

	@Override
	public boolean isSoldierTime() {
		
		return m_game.getAliveSoldiers().isTime();
		
	}

	@Override
	public boolean isEnemyTime() {
		if (isSoldierTime())
			return false;
		
		return m_game.getAliveEnemies().isTime();
	}

	@Override
	public void updatePlayers(ICharacterListener clistener) {
		m_game.movePlayers(clistener);
	}

	@Override
	public void updateEnemies(ICharacterListener clistener) {
		m_game.updateEnemies(clistener);
	}

	@Override
	public void doMoveTo(ICharacter selectedSoldier, ModelPosition destination) {
		m_game.doMoveTo(selectedSoldier, destination);
	}

	@Override
	public CharacterIterable getAliveSoldiers() {
		return m_game.getAliveSoldiers().getSafeIterator();
	}

	@Override
	public CharacterIterable getAliveEnemies() {
		return m_game.getAliveEnemies().getSafeIterator();
	}

	@Override
	public CharacterIterable getDeadEnemies() {
		return m_game.getDeadEnemies().getSafeIterator();
	}

	@Override
	public void startNewGame(int a_level) {
		m_game.startLevel(a_level);
		
	}
	
	@Override
	public void startNewEnemyRound() {
		m_game.startNewEnemyRound();	
	}
	
	@Override
	public void startNewSoldierRound() {
		m_game.startNewSoldierRound();	
	}
		
	@Override
	public boolean enemyHasWon() {
		return m_game.getAliveSoldiers().size() == 0;
	}

	@Override
	public boolean playerHasWon() {
		return m_game.getAliveEnemies().size() == 0 && 
			   m_game.getAliveSoldiers().size() > 0;
	}

	@Override
	public boolean fireAt(ICharacter selectedSoldier, ICharacter fireTarget, ICharacterListener listener) {
		return selectedSoldier.fireAt(fireTarget, m_game, listener);
	}

	@Override
	public boolean canShoot(ICharacter soldier, ICharacter enemy) {
		return RuleBook.canFireAt(soldier, enemy, m_game);
	}
	
	@Override
	public boolean canSeeMapPosition(ICharacter soldier, ModelPosition modelPosition) {
		
		
		ModelPosition soldierPos = soldier.getPosition();
		ModelPosition targetPosition = modelPosition;
		
		return m_game.lineOfSight(soldierPos, targetPosition );
	}
	
	@Override
	public boolean canSee(ICharacter soldier, ICharacter enemy) {
		return canSeeMapPosition(soldier, enemy.getPosition());
	}

	@Override
	public CharacterIterable canSee(ICharacter enemy) {
		CharacterCollection<Soldier> aliveSoldiers = m_game.getAliveSoldiers();
		return aliveSoldiers.thatCanSee(m_game, enemy).getSafeIterator();
	}
	
	@Override
	public ICharacter getClosestEnemyThatWeCanShoot(ICharacter selectedSoldier) {
		CharacterCollection<Enemy> aliveEnemies = m_game.getAliveEnemies();
		
		CharacterCollection<Enemy> enemiesWeCanShoot = aliveEnemies.canBeShotBy(selectedSoldier, m_game);
		
		return enemiesWeCanShoot.getClosest(selectedSoldier.getPosition());
	}
	
	@Override
	public CharacterIterable couldShootIfHadTime(ICharacter enemy) {
		CharacterCollection<Soldier> aliveSoldiers = m_game.getAliveSoldiers();
		
		return aliveSoldiers.couldShootIfHadTime(enemy, m_game).getSafeIterator();
	}

	@Override
	public void doWatch(ICharacter selectedSoldier) {
		selectedSoldier.doWatch();
	}


	@Override
	public IMoveAndVisibility getMovePossible() {
		return m_game;
	}

	@Override
	public boolean canMove(ModelPosition clickOnLevelPosition) {
		return m_game.canMove(clickOnLevelPosition);
	}

	


	


	


	
}
