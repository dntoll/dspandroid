package com.spellofplay.dsp.model;

import com.spellofplay.dsp.model.AStar.SearchResult;

public class Enemy extends Character{

	public Enemy(ModelPosition startPosition) {
		super(startPosition, 5);

	}

	public boolean isDoingSomething() {
		
		return m_pathFinder != null;
	}

	

	public void removeTimeUnit() {
		m_timeUnits--;
		
	}
	
	public float getFireSkill() {
		return 0.7f;
	}

	public float getDodgeSkill() {
		return 0.3f;
	}
	
	@Override
	public float getRange() {
		return 3.0f;
	}

	public boolean isSearching() {
		return m_pathFinder != null && m_pathFinder.isSearching();
		
	}

	public boolean isMoving() {
		return m_pathFinder != null && m_pathFinder.m_path.size() > 0;
	}

	public boolean didSearchFail() {
		return m_pathFinder != null && m_pathFinder.didSearchFail();
	}

	

	

	
}
