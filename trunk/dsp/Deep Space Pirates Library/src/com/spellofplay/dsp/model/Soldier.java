package com.spellofplay.dsp.model;

import com.spellofplay.dsp.model.AStar.SearchResult;

public class Soldier {

	ModelPosition m_position;
	public AStar m_pathFinder;
	int m_timeUnits = 10;

	public Soldier(ModelPosition startPosition) {
		m_position = startPosition;
	}

	public ModelPosition getPosition() {
		return m_position;
	}

	public float getRadius() {
		return 0.5f;
	}

	public void reset(ModelPosition startLocation) {
		m_position = startLocation;
		
	}

	public void setDestination(ModelPosition destination, Level a_map) {
		m_pathFinder = new AStar(a_map);
		m_pathFinder.InitSearch(m_position, destination, false, 1.0f);
		
	}

	public boolean update() {
		
		if (m_pathFinder != null && m_timeUnits > 0) {
			SearchResult result = m_pathFinder.Update(100);
			
			if (result == SearchResult.SearchSucceded) {
				if (m_pathFinder.m_path.size() > 0) {
					ModelPosition pos = m_pathFinder.m_path.get(0);
					m_pathFinder.m_path.remove(0);
					
					m_position = pos;
					m_timeUnits--;
				} else {
					m_pathFinder = null;
				}
			} else if (result == SearchResult.SearchNotDone || result == SearchResult.SearchNotStarted) {
				//do nothing
			} else if (result == SearchResult.SearchFailedNoPath) {
				//do nothing
				m_pathFinder = null;
			}
		}
		return m_timeUnits <= 0;
	}

	public int getTimeUnits() {
		return m_timeUnits;
	}

	public void startNewRound() {
		m_timeUnits = 10;
		m_pathFinder = null;
	}

}
