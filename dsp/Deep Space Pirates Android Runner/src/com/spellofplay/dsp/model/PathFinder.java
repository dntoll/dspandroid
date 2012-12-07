package com.spellofplay.dsp.model;

public class PathFinder {
	
	protected AStar m_pathFinder;
	
	public void stopAllSearches() {
		m_pathFinder = null;
	}
	
	public boolean isDoingSomething() {
		return m_pathFinder != null;
	}

	public boolean isSearching() {
		return m_pathFinder != null && m_pathFinder.isSearching();
	}
	
	public void search() {
		if (m_pathFinder != null)
			m_pathFinder.Update(100);
	}

	public boolean isMoving() {
		return m_pathFinder != null && m_pathFinder.m_path.size() > 0;
	}

	public boolean didSearchFail() {
		return m_pathFinder != null && m_pathFinder.didSearchFail();
	}

	public ModelPosition getNextPosition() {
		ModelPosition pos = m_pathFinder.m_path.get(0);
		m_pathFinder.m_path.remove(0);
		if (m_pathFinder.m_path.size() == 0) {
			m_pathFinder = null;
		}
		
		return pos;
	}

	public boolean isSearchDone() {
		if (m_pathFinder != null)
			return m_pathFinder.isSearchDone();
		
		return false;
	}

	public void setDestination(IMoveAndVisibility a_map,
			ModelPosition m_position, ModelPosition destination, boolean b,
			float a_distance, boolean a_checkPathThroughOthers) {
		m_pathFinder = new AStar(a_map);
		m_pathFinder.InitSearch(m_position, destination, a_distance > 0.0f, a_distance, a_checkPathThroughOthers);
	}
}
