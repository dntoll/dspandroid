package com.spellofplay.dsp.model;

import com.spellofplay.dsp.model.AStar.SearchResult;

public abstract class Character {
	private ModelPosition m_position;
	
	public AStar m_pathFinder;
	
	protected int m_maxTimeUnits = 3;
	protected int m_timeUnits = m_maxTimeUnits;
	protected int m_hitpoints = 3;

	public Character(ModelPosition startPosition, int a_maxTimeUnits) {
		m_position = startPosition;
		m_maxTimeUnits = m_timeUnits = a_maxTimeUnits;
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

	public void startNewRound() {
		m_timeUnits = m_maxTimeUnits;
		m_pathFinder = null;
	}
	
	public void setDestination(ModelPosition destination, IMoveAndVisibility a_map, float a_distance, boolean a_checkPathThroughOthers) {
		m_pathFinder = new AStar(a_map);
		
		
		m_pathFinder.InitSearch(m_position, destination, a_distance > 0.0f, a_distance, a_checkPathThroughOthers);
	}

	/**
	 * 
	 * @param check
	 * @return true if everything is ok but false if a search or move failed
	 */
	public boolean update(IMoveAndVisibility check, ICharacterListener clistener) {
		
		if (m_pathFinder != null && m_timeUnits > 0) {
			SearchResult result = m_pathFinder.Update(100);
			
			if (result == SearchResult.SearchSucceded) {
				
				if (m_pathFinder.m_path.size() > 0) {
					ModelPosition pos = m_pathFinder.m_path.get(0);
					m_pathFinder.m_path.remove(0);
					
					//kan vi verkligen gå igenom någon annan?
					if (check.isMovePossible(pos, false)) {
						m_position = pos;
						m_timeUnits--; 
						clistener.moveTo(this);
					} else {
						m_pathFinder = null; //this should in theory never happen...
						return false;
					}
				} else {
					m_pathFinder = null;
				}
			} else if (result == SearchResult.SearchNotDone || result == SearchResult.SearchNotStarted) {
				//do nothing
			} else if (result == SearchResult.SearchFailedNoPath) {
				//do nothing
				m_pathFinder = null;
				return false;
			}
		}
		
		if (m_timeUnits <= 0) {
			m_pathFinder = null;
		}
		
		return true;
	}
	
	public boolean fireAt(Character fireTarget, IMoveAndVisibility moveAndVisibility) {
				
		if (RuleBook.canFireAt(this, fireTarget, moveAndVisibility) == false)
			return false;
			
		
		m_timeUnits -= getFireCost();
		
		//Determine success
		if (RuleBook.DetermineFireSuccess(this, fireTarget)) {
			fireTarget.m_hitpoints -= getDamage();
			return true;
		}
		
		
		
		return false;
		
	}

	public abstract float getFireSkill();

	public abstract float getDodgeSkill();
}
