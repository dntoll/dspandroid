package com.spellofplay.dsp.model;


public class ModelFacade {
	
	Game m_game = new Game();
	
	
	
	public String getGameTitle() {
		return "Deep Space Pirates ";
	}

	
	public Level getLevel() {

		return m_game.m_level;
	}
	public boolean isSoldierTime() {
		
		return m_game.getAliveSoldiers().isTime();
		
	}

	public boolean isEnemyTime() {
		if (isSoldierTime())
			return false;
		
		return m_game.getAliveEnemies().isTime();
	}


	public void updatePlayers(ICharacterListener clistener) {
		m_game.movePlayers(clistener);
	}
	
	public void updateEnemies(ICharacterListener clistener) {
		m_game.updateEnemies(clistener);
	}


	public void doMoveTo(Soldier selectedSoldier, ModelPosition destination) {
		m_game.doMoveTo(selectedSoldier, destination);
	}


	public CharacterCollection<Soldier> getAliveSoldiers() {
		return m_game.getAliveSoldiers();
	}
	
	public CharacterCollection<Enemy> getAliveEnemies() {
		return m_game.getAliveEnemies();
	}
	public CharacterCollection<Enemy> getDeadEnemies() {
		return m_game.getDeadEnemies();
	}


	public void startNewGame(int a_level) {
		m_game.startLevel(a_level);
		
	}
	
	public void startNewEnemyRound() {
		m_game.startNewEnemyRound();	
	}
	
	public void startNewSoldierRound() {
		m_game.startNewSoldierRound();	
	}
		
	
	public boolean enemyHasWon() {
		return m_game.getAliveSoldiers().size() == 0;
	}

	public boolean playerHasWon() {
		return m_game.getAliveEnemies().size() == 0 && 
			   m_game.getAliveSoldiers().size() > 0;
	}


	public boolean fireAt(Soldier selectedSoldier, Enemy fireTarget, ICharacterListener listener) {
		return selectedSoldier.fireAt(fireTarget, m_game, listener);
	}

	public boolean canShoot(Soldier soldier, Enemy enemy) {
		return RuleBook.canFireAt(soldier, enemy, m_game);
	}
	
	public boolean canSeeMapPosition(Soldier soldier, ModelPosition modelPosition) {
		
		
		ModelPosition soldierPos = soldier.getPosition();
		ModelPosition targetPosition = modelPosition;
		
		return m_game.m_level.lineOfSight(soldierPos, targetPosition );
	}
	
	public boolean canSee(Soldier soldier, Enemy enemy) {
		return canSeeMapPosition(soldier, enemy.getPosition());
	}

	public CharacterCollection<Soldier> canSee(Enemy enemy) {
		CharacterCollection<Soldier> aliveSoldiers = m_game.getAliveSoldiers();
		return aliveSoldiers.thatCanSee(m_game, enemy);
	}
	
	public Enemy getClosestEnemyThatWeCanShoot(Soldier selectedSoldier) {
		CharacterCollection<Enemy> aliveEnemies = m_game.getAliveEnemies();
		
		CharacterCollection<Enemy> enemiesWeCanShoot = aliveEnemies.canBeShotBy(selectedSoldier, m_game);
		
		return enemiesWeCanShoot.getClosest(selectedSoldier.getPosition());
	}
	
	


	public void doWatch(Soldier selectedSoldier) {
		selectedSoldier.doWatch();
	}


	public IMoveAndVisibility getMovePossible() {
		return m_game;
	}


	


	


	
}
