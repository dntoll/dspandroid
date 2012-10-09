package com.spellofplay.dsp.model;

import java.util.ArrayList;
import java.util.List;

import com.spellofplay.NotImplementedException;

public class Game {

	public static final int MAX_SOLDIERS = 4;
	Soldier[] m_soldiers = new Soldier[MAX_SOLDIERS];
	Level m_level = new Level();
	
	public Game() {
		m_soldiers[0] = new Soldier(new ModelPosition(5,5));
	}
	
	public List<Soldier> getAliveSoldiers() {
		
		List<Soldier> ret = new ArrayList<Soldier>();
		for (Soldier s : m_soldiers) {
			if (s != null) {
				ret.add(s);
			}
		}
		
		return ret;
	}

	public void startLevel(int a_level) {
		
		m_level.loadLevel(a_level);
		for (int i = 0; i < MAX_SOLDIERS; i++) {
			if (m_soldiers[i] != null) {
				m_soldiers[i].reset(m_level.getStartLocation(i));
			}
		}
		
	}

}
