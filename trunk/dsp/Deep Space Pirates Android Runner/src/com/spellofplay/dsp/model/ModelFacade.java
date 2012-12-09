package com.spellofplay.dsp.model;

import com.spellofplay.dsp.model.inner.CharacterCollection;
import com.spellofplay.dsp.model.inner.Enemy;
import com.spellofplay.dsp.model.inner.Game;
import com.spellofplay.dsp.model.inner.RuleBook;
import com.spellofplay.dsp.model.inner.Soldier;


public class ModelFacade implements IModel, IEventTarget {
	
	private Game game = new Game();

	@Override
	public String getGameTitle() {
		return "Deep Space Pirates ";
	}

	@Override
	public TileType getTile(int x, int y) {
		return game.getTile(x,y);
	}

	@Override
	public boolean isSoldierTime() {
		return game.getAliveSoldiers().isTime();
	}

	@Override
	public boolean isEnemyTime() {
		if (isSoldierTime())
			return false;
		return game.getAliveEnemies().isTime();
	}

	@Override
	public CharacterIterable getAliveSoldiers() {
		return game.getAliveSoldiers().getSafeIterator();
	}

	@Override
	public CharacterIterable getAliveEnemies() {
		return game.getAliveEnemies().getSafeIterator();
	}

	@Override
	public CharacterIterable getDeadEnemies() {
		return game.getDeadEnemies().getSafeIterator();
	}

	@Override
	public boolean enemyHasWon() {
		return game.getAliveSoldiers().size() == 0;
	}

	@Override
	public boolean playerHasWon() {
		return game.getAliveEnemies().size() == 0 && 
			   game.getAliveSoldiers().size() > 0;
	}

	@Override
	public boolean canShoot(ICharacter soldier, ICharacter enemy) {
		return RuleBook.canFireAt(soldier, enemy, game);
	}
	
	@Override
	public boolean canSeeMapPosition(ICharacter soldier, ModelPosition modelPosition) {
		ModelPosition soldierPos = soldier.getPosition();
		ModelPosition targetPosition = modelPosition;
		
		return game.lineOfSight(soldierPos, targetPosition );
	}
	
	@Override
	public CharacterIterable canSee(ICharacter enemy) {
		CharacterCollection<Soldier> aliveSoldiers = game.getAliveSoldiers();
		return aliveSoldiers.thatCanSee(game, enemy).getSafeIterator();
	}
	
	@Override
	public ICharacter getClosestEnemyThatWeCanShoot(ICharacter selectedSoldier) {
		CharacterCollection<Enemy> aliveEnemies = game.getAliveEnemies();
		CharacterCollection<Enemy> enemiesWeCanShoot = aliveEnemies.canBeShotBy(selectedSoldier, game);
		
		return (ICharacter) enemiesWeCanShoot.getClosest(selectedSoldier.getPosition());
	}
	
	@Override
	public CharacterIterable couldShootIfHadTime(ICharacter enemy) {
		CharacterCollection<Soldier> aliveSoldiers = game.getAliveSoldiers();
		
		return aliveSoldiers.couldShootIfHadTime(enemy, game).getSafeIterator();
	}

	@Override
	public IMoveAndVisibility getMovePossible() {
		return game;
	}

	@Override
	public boolean canMove(ModelPosition clickOnLevelPosition) {
		return game.canMove(clickOnLevelPosition);
	}
	
	@Override
	public void doWatch(ICharacter selectedSoldier) {
		Soldier selected  = (Soldier)selectedSoldier;
		selected.doWatch();
	}
	
	@Override
	public boolean fireAt(ICharacter selectedSoldier, ICharacter fireTarget, ICharacterListener listener) {
		Soldier selected = (Soldier)selectedSoldier;
		return selected.fireAt(fireTarget, game, listener);
	}
	
	@Override
	public void updatePlayers(ICharacterListener clistener) {
		game.movePlayers(clistener);
	}

	@Override
	public void updateEnemies(ICharacterListener clistener) {
		game.updateEnemies(clistener);
	}

	@Override
	public void doMoveTo(ICharacter selectedSoldier, ModelPosition destination) {
		game.doMoveTo(selectedSoldier, destination);
	}
	
	@Override
	public void startNewGame(int a_level) {
		game.startLevel(a_level);
	}
	
	@Override
	public void startNewEnemyRound() {
		game.startNewEnemyRound();	
	}
	
	@Override
	public void startNewSoldierRound() {
		game.startNewSoldierRound();	
	}

	@Override
	public void open(ICharacter selectedSoldier) {
		game.open(selectedSoldier.getPosition());
	}

	@Override
	public boolean hasDoorCloseToIt(ICharacter selectedSoldier) {
		return game.hasDoorCloseToIt(selectedSoldier.getPosition());
	}
}