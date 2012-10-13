package com.spellofplay.dsp.model;

import com.spellofplay.dsp.model.AStar.SearchResult;

public class Character {
	ModelPosition m_position;
	public AStar m_pathFinder;
	
	int m_maxTimeUnits = 3;
	int m_timeUnits = m_maxTimeUnits;
	int m_hitpoints = 10;

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
	
	public int getTimeUnits() {
		return m_timeUnits;
	}

	public void startNewRound() {
		m_timeUnits = m_maxTimeUnits;
		m_pathFinder = null;
	}
	
	public void setDestination(ModelPosition destination, IIsMovePossible a_map, float a_distance) {
		m_pathFinder = new AStar(a_map);
		
		
		m_pathFinder.InitSearch(m_position, destination, a_distance > 0.0f, a_distance);
	}

	/**
	 * 
	 * @param check
	 * @return true if everything is ok but false if a search or move failed
	 */
	public boolean update(IIsMovePossible check) {
		
		if (m_pathFinder != null && m_timeUnits > 0) {
			SearchResult result = m_pathFinder.Update(100);
			
			if (result == SearchResult.SearchSucceded) {
				if (m_pathFinder.m_path.size() > 0) {
					ModelPosition pos = m_pathFinder.m_path.get(0);
					m_pathFinder.m_path.remove(0);
					
					if (check.isMovePossible(pos)) {
						m_position = pos;
						m_timeUnits--; 
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
}
