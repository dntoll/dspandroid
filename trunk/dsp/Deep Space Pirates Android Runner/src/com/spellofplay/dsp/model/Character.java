package com.spellofplay.dsp.model;

public abstract class Character   {
	private ModelPosition m_position;
	
	public AStar m_pathFinder;
	
	protected int m_maxTimeUnits = 3;
	protected int m_watchTimeUnits = 0;
	protected int m_timeUnits = m_maxTimeUnits;
	protected int m_hitpoints = 3;

	public Character(ModelPosition startPosition, int a_maxTimeUnits) {
		m_position = startPosition;
		m_maxTimeUnits = m_timeUnits = a_maxTimeUnits;
	}
	
	public void doWatch() {
		m_watchTimeUnits = m_timeUnits;
		m_timeUnits = 0;
	}
	
	public void reset(ModelPosition startLocation) {
		m_position = startLocation;
	}
	
	public ModelPosition getPosition() {
		return m_position;
	}

	public float getRadius() {
		return 0.5f;
	}
	
	public int getFireCost() {
		return 3;
	}
	
	public int getDamage() {
		return 1;
	}
	
	public int getHitpoints() {
		return m_hitpoints;
	}
	
	public int getTimeUnits() {
		return m_timeUnits;
	}
	public int getWatchTimeUnits() {
		return m_watchTimeUnits;
	}

	public void startNewRound() {
		m_timeUnits = m_maxTimeUnits;
		m_pathFinder = null;
		m_watchTimeUnits = 0;
	}
	
	public boolean hasWatch() {
		return m_watchTimeUnits >= getFireCost();
	}
	
	public void setDestination(ModelPosition destination, IMoveAndVisibility a_map, float a_distance, boolean a_checkPathThroughOthers) {
		m_pathFinder = new AStar(a_map);
		
		
		m_pathFinder.InitSearch(m_position, destination, a_distance > 0.0f, a_distance, a_checkPathThroughOthers);
	}

	/**
	 * 
	 * @param multiListener 
	 * @param check
	 * @return true if everything is ok but false if a search or move failed
	 */
	public void move(ICharacterListener clistener, MultiMovementListeners multiListener, IMoveAndVisibility moveAndVisibility) {
		
		if (m_pathFinder != null && m_timeUnits > 0) {
			if (m_pathFinder.isSearchDone()) {
				MoveToNextPosition(clistener, multiListener, moveAndVisibility);
			} 
		}
	}
	
	public void search() {
		if (m_pathFinder != null)
			m_pathFinder.Update(100);
	}

	private void MoveToNextPosition(ICharacterListener clistener, MultiMovementListeners multiListener, IMoveAndVisibility moveAndVisibility) {
		
		ModelPosition pos = m_pathFinder.m_path.get(0);
		m_pathFinder.m_path.remove(0);
		m_position = pos;
		m_timeUnits--;
		
		if (m_pathFinder.m_path.size() == 0 || m_timeUnits <= 0) {
			m_pathFinder = null;
		}
		clistener.moveTo(this);
		multiListener.moveTo(this, moveAndVisibility, clistener);
	}
	
	public boolean fireAt(Character fireTarget, IMoveAndVisibility moveAndVisibility, ICharacterListener a_listener) {
				
		if (RuleBook.canFireAt(this, fireTarget, moveAndVisibility) == false) {
			a_listener.cannotFireAt(this, fireTarget);
			return false;
		}
			
		
		m_timeUnits -= getFireCost();
		
		//Determine success
		if (RuleBook.DetermineFireSuccess(this, fireTarget, moveAndVisibility.targetHasCover(this, fireTarget))) {
			fireTarget.m_hitpoints -= getDamage();
			
			
			a_listener.fireAt(this, fireTarget, true);
			return true;
		}
		a_listener.fireAt(this, fireTarget, false);
		
		
		
		return false;
		
	}

	public abstract float getFireSkill();

	public abstract float getDodgeSkill();

	public float distance(Character other) {
		return m_position.sub(other.getPosition()).length();
	}

	public abstract float getRange();

	

	public void watchMovement(Character mover,
			IMoveAndVisibility moveAndVisibility, ICharacterListener a_listener) {
		
		//a_listener.enemyAILog("watches Movement ");
		
		if (RuleBook.canFireAt(this, mover, moveAndVisibility) == true) {
			fireAt(mover, moveAndVisibility, a_listener);
			//a_listener.enemyAILog("on watch fire ");
		}
	}

	public boolean hasTimeToFire() {
		return m_timeUnits + m_watchTimeUnits >= getFireCost(); 
	}

	
}
