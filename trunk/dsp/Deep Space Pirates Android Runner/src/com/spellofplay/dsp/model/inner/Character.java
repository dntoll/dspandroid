package com.spellofplay.dsp.model.inner;

import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.ICharacterListener;
import com.spellofplay.dsp.model.IMoveAndVisibility;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.MultiMovementListeners;

public abstract class Character extends ICharacter  {
	private ModelPosition m_position = new ModelPosition();
	
	private PathFinder m_pathFinder = new PathFinder();
	
	private int m_maxTimeUnits = 3;
	private int m_watchTimeUnits = 0;
	protected int m_timeUnits = m_maxTimeUnits;
	protected int m_hitpoints = 5;

	Character(ModelPosition startPosition, int a_maxTimeUnits) {
		m_position.m_x = startPosition.m_x;
		m_position.m_y = startPosition.m_y;
		m_maxTimeUnits = m_timeUnits = a_maxTimeUnits;
	}
	
	public void doWatch() {
		m_watchTimeUnits = m_timeUnits;
		m_timeUnits = 0;
	}
	
	void reset(ModelPosition startLocation) {
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
	
	@Override
	public int getHitPoints() {
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
		m_watchTimeUnits = 0;
		m_pathFinder.stopAllSearches();
	}
	
	public boolean hasWatch() {
		return m_watchTimeUnits >= getFireCost();
	}
	
	void setDestination(ModelPosition destination, IMoveAndVisibility a_map, float a_distance, boolean a_checkPathThroughOthers) {
		
		m_pathFinder.setDestination(a_map, m_position, destination, a_distance > 0.0f, a_distance, a_checkPathThroughOthers);
		
	}

	/**
	 * 
	 * @param multiListener 
	 * @param check
	 * @return true if everything is ok but false if a search or move failed
	 */
	void move(ICharacterListener clistener, MultiMovementListeners multiListener, IMoveAndVisibility moveAndVisibility) {
		
		if (m_timeUnits > 0) {
			if (m_pathFinder.isSearchDone()) {
				MoveToNextPosition(clistener, multiListener, moveAndVisibility);
			} 
		}
	}
	
	

	private void MoveToNextPosition(ICharacterListener clistener, MultiMovementListeners multiListener, IMoveAndVisibility moveAndVisibility) {
		
		ModelPosition pos = m_pathFinder.getNextPosition();
		
		if ( m_timeUnits <= 0) {
			return;
		}
		m_position = pos;
		m_timeUnits--;
		
		
		clistener.moveTo(this);
		multiListener.moveTo(this, moveAndVisibility, clistener);
	}
		
	public boolean fireAt(ICharacter fireTarget, IMoveAndVisibility moveAndVisibility, ICharacterListener a_listener) {
				
		if (RuleBook.canFireAt(this, fireTarget, moveAndVisibility) == false) {
			a_listener.cannotFireAt(this, fireTarget);
			return false;
		}
		m_timeUnits -= getFireCost();
		
		if (RuleBook.DetermineFireSuccess(this, fireTarget, moveAndVisibility.targetHasCover(this, fireTarget))) {
			Character target = (Character)fireTarget;
			target.m_hitpoints -= getDamage();
			a_listener.fireAt(this, fireTarget, true);
			return true;
		} else {
			a_listener.fireAt(this, fireTarget, false);
			return false;
		}
		
	}

	public float distance(ICharacter other) {
		return m_position.sub(other.getPosition()).length();
	}

	public void watchMovement(ICharacter mover,
			IMoveAndVisibility moveAndVisibility, ICharacterListener a_listener) {
		
		if (RuleBook.canFireAt(this, mover, moveAndVisibility) == true) {
			fireAt(mover, moveAndVisibility, a_listener);
		}
	}

	public boolean hasTimeToFire() {
		return m_timeUnits + m_watchTimeUnits >= getFireCost(); 
	}

	public PathFinder getPathFinder() {
		return m_pathFinder;
	}

	public void stopAllMovement() {
		getPathFinder().stopAllSearches();
	}
	
}
