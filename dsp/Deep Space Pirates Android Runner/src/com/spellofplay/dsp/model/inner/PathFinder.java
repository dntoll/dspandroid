package com.spellofplay.dsp.model.inner;

import com.spellofplay.dsp.model.IMoveAndVisibility;
import com.spellofplay.dsp.model.ModelPosition;

class PathFinder {
	
	private AStar m_pathFinder;
	
	void stopAllSearches() {
		m_pathFinder = null;
	}
	
	public boolean isDoingSomething() {
		return m_pathFinder != null;
	}

	public boolean isSearching() {
		return m_pathFinder != null && m_pathFinder.isSearching();
	}
	
	void search() {
		if (m_pathFinder != null)
			m_pathFinder.Update(100);
	}

	public boolean isMoving() {
		return m_pathFinder != null && m_pathFinder.path.size() > 0;
	}

	boolean didSearchFail() {
		return m_pathFinder != null && m_pathFinder.didSearchFail();
	}

	public ModelPosition getNextPosition() {
		ModelPosition pos = m_pathFinder.path.get(0);
		m_pathFinder.path.remove(0);
		if (m_pathFinder.path.size() == 0) {
			m_pathFinder = null;
		}
		
		return pos;
	}

	public boolean isSearchDone() {
		if (m_pathFinder != null)
			return m_pathFinder.isSearchDone();
		
		return false;
	}

	void setDestination(IMoveAndVisibility a_map,
			ModelPosition m_position, ModelPosition destination, boolean b,
			float a_distance) {
		m_pathFinder = new AStar(a_map);
		m_pathFinder.InitSearch(m_position, destination, a_distance > 0.0f, a_distance);
	}
}
